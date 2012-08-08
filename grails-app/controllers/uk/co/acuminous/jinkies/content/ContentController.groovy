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
		
		if (!reportMissing(content)) {
			
			def file = request.getFile('file')		
			content.bytes = file.inputStream.bytes
			content.type = file.contentType
	
			if (!reportUnsupported(content)) {
				content.save(flush:true)
				report(content.errors) || render(status: SC_NO_CONTENT)
			}			
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
		
		// It's very lame to set no cache headers. Need to add either the hashcode or filesize to the get url 
		response.setHeader 'Cache-Control','no-cache'
		response.setHeader 'Pragma','no-cache'
		response.setDateHeader 'Expires', 0
		response.setDateHeader 'Last-Modified', 0
		
		response.contentType = content.type
		response.contentLength = content.bytes.size()
		response.outputStream << content.bytes
		response.outputStream.flush()
	}
	
	private boolean reportUnsupported(Content content) {
		boolean result = false
		if (!contentService.isSupported(content)) {
			String code = (content.type =~ /wav$/) ? 'content.type.unsupported.wav' : 'content.type.unsupported'			
			content.errors.reject(code, [content.type] as Object[], 'Content type {0} is unsupported')
			content.delete(flush:true)
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