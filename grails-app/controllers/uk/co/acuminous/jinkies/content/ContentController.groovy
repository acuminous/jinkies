/* 
 * Copyright 2012 Acuminous Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.acuminous.jinkies.content

import static javax.servlet.http.HttpServletResponse.*

import java.util.List;

import grails.converters.JSON
import uk.co.acuminous.jinkies.ci.Job
import uk.co.acuminous.jinkies.content.Content
import uk.co.acuminous.jinkies.content.ContentService
import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagService

import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import uk.co.acuminous.jinkies.util.JinkiesErrorRenderer


// Mixins are currently working properly, therefore extending (yuck)
class ContentController extends JinkiesErrorRenderer {
	
	ContentService contentService
	
	def show() {		
		Content content = Content.get(params.id)		
		reportMissing(content) || render(content as JSON)		
	}	
	
	def list() {
		List<Content> content = Content.list(sort: 'title', order: 'asc')
		render content as JSON
	}
	
	def create(ContentCommand cmd) {				
		Content content = new Content()		
		if (!report(cmd.errors) ) {
			save(content, cmd)
			report(content.errors) || render(content as JSON)
		}
	}
	
	def update(ContentCommand cmd) {		
		Content content = Content.get(params.id)		
		if (!reportMissing(content) && !report(cmd.errors)) {
			save(content, cmd)
			report(content.errors) || render(content as JSON)
		} 
	}
		
	def delete() {
		Content.get(params.id)?.delete(flush:true)
		render status: SC_NO_CONTENT
	}
	
	def upload() {
		Content content = Content.get(params.id)
		
		if (!reportMissing(content) && !reportUnsupported(content)) {
			content.save(flush:true)
			report(content.errors) || render(status: SC_NO_CONTENT)			
		}
		
	}
	
	def download() {		
		Content content = Content.get(params.id)		
		reportMissing(content) || serve(content)
	}
		
	private void save(Content content, ContentCommand cmd) {

		// Binding parameters directly from the command
		// causes a StaleObjectException when changing from
		// url to filename or vice-versa.		

		content.title = cmd.title
		content.description = cmd.description
		content.themes = cmd.themes
		content.events = cmd.events
		
		if (cmd.uploadFromUrl()) {
			content.filename = null
			content.url = cmd.url
			content.aquire()
		} else {
			content.url = null
			content.filename = cmd.filename
		}
		
		content.save(flush: true)
	}
	
	private void serve(Content content) {
		response.contentType = content.type
		response.contentLength = content.bytes.size()
		response.outputStream << content.bytes
		response.outputStream.flush()
	}
	
	private boolean reportUnsupported(Content content) {
		
		boolean result = false
		MultipartFile file = request.getFile('file')		
		Content testContent = new Content(bytes: file.inputStream.bytes, type: file.contentType)
		
		if (contentService.isSupported(testContent)) {
			content.bytes = testContent.bytes
			content.type = testContent.type
		} else {
			if (content.bytes == null) {
				content.delete(flush:true)
			}
			
			String code = (testContent.type =~ /wav$/) ? 'content.type.unsupported.wav' : 'content.type.unsupported'
			content.errors.reject(code, [testContent.type] as Object[], 'Content type {0} is unsupported')
			renderJsonError content.errors
			result = true
		}
		result
	}
}

class ContentCommand {
	
	TagService tagService
		
	String title
	String uploadMethod
	String filename
	String url
	String description
	List<String> theme
	List<String> event 

	static constraints = {
		uploadMethod nullable: false		
		filename nullable: true, validator: { String filename, ContentCommand cmd ->
			if (cmd.uploadMethod == 'file' && !filename) {
				'contentCommand.filename.nullable'
			}
		}
		url nullable: true, validator: { String url, ContentCommand cmd ->
			if (cmd.uploadMethod == 'url' && !url) {
				'contentCommand.url.nullable'
			}
		}
	}
	
	boolean uploadFromUrl() {
		uploadMethod == 'url'
	}
	
	List<Tag> getThemes() {
		tagService.findOrCreateThemes(theme) ?: []
	}
	
	List<Tag> getEvents() {
		tagService.findOrCreateEvents(event) ?: []
	}
}