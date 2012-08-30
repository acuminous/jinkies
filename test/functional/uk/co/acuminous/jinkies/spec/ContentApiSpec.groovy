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
package uk.co.acuminous.jinkies.spec

import javax.activation.MimetypesFileTypeMap

import groovyx.net.http.*

import spock.lang.*
import uk.co.acuminous.jinkies.*
import uk.co.acuminous.jinkies.content.*
import static groovyx.net.http.Method.*

import grails.converters.*
import org.codehaus.groovy.grails.web.json.*

import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.ByteArrayBody


@Mixin(RemoteUtils)
@Mixin(TestUtils)
@Mixin(RemoteBetamaxRecorder)

class ContentApiSpec extends Specification {

	RESTClient client
	MimetypesFileTypeMap mimeTypes
		
	def setup() {
		nuke()
		client = getRestClient(baseUrl)	
			
		mimeTypes = new MimetypesFileTypeMap()
		mimeTypes.addMimeTypes 'audio/mp3 mp3'
	}
	
	def "Creates content with file"() {
		
		given:		
			Map params = [uploadMethod: 'file', title: 'Zoinks', filename: 'zoinks.mp3', description: 'Shaggy saying Zoinks']
			
		when:
			def response = client.post(path: '/api/content', body: params)
			
		then:
			response.status == 200
			
			Content content = remote {
				Content.findByTitle params.title
			}
			
			content != null
			content.title == params.title
			content.description == params.description
			content.filename == params.filename
	}
		
	def "Creates content from url"() {
		
		given:
			Map params = [uploadMethod: 'url', title: 'Zoinks', url: 'http://www.noiseaddicts.com/samples/3726.mp3', description: 'Shaggy saying Zoinks']
			
		when:
			def response = withRemoteTape('noiseaddicts') {
				client.post(path: '/api/content', body: params)
			}
			
		then:
			response.status == 200
			
			Content content = remote {
				Content.findByTitle params.title
			}
			
			content != null
			content.title == params.title
			content.description == params.description
			content.url == params.url
	}
	
	def "Creates content from text"() {
		
		given:
			Map params = [uploadMethod: 'text', text: 'Zoinks! Project-X has failed', title: 'Zoinks', description: 'Zoinks Text']
			
		when:
			def response = client.post(path: '/api/content', body: params)
			
		then:
			response.status == 200
			
			Content content = remote {
				Content.findByTitle params.title
			}
			
			content != null
			content.title == params.title
			content.description == params.description
			new String(content.bytes) == params.text
			content.type == 'text/plain'
	}

	def "Creates content with new themes"() {
		
		given:
			Map params = [uploadMethod: 'file', title: 'Zoinks', filename: 'anything', theme: ['Scooby Doo', 'Cartoons']]
			
		when:
			def response = client.post(path: '/api/content', body: params)
			
		then:
			response.status == 200		
		
			Content content = remote {
				Content content = Content.findByTitle params.title
				content.themes.each { it.toString() /* workaround hibernate proxy issue */ }
				content
			}
			
			content != null
			content.themes.size() == 2			
			content.themes.collect { it.name }.containsAll(params.theme)
	}
		
	def "Creating content reuses existing themes"() {
		
		given:
			Tag theme = remote {
				Tag theme = new Tag('Scooby Doo', TagType.theme).save(flush:true)				
			}
			Map params = [uploadMethod: 'file', title: 'Zoinks', filename: 'anything', theme: [theme.name]]			
		
		when:
			def response = client.post(path: '/api/content', body: params)
			
		then:
			response.status == 200
		
			remote { Tag.count() } == 1
	}
	
	
	def "Creates content with new events"() {
		
		given:
			Map params = [uploadMethod: 'file', title: 'Zoinks', filename: 'anything', event: ['Stand Down']]
			
		when:
			def response = client.post(path: '/api/content', body: params)
			
		then:
			response.status == 200 
			
			Content content = remote {
				Content content = Content.findByTitle params.title
				content.events.each { it.toString() /* workaround hibernate proxy issue */ }
				content
			}
			
			content != null
			content.events.collect { it.name }.containsAll(params.event)			
	}
		
	def "Creating content reuses existing events"() {
		
		given:
			Tag event = remote {
				Tag event = new Tag('Failure', TagType.event).save(flush:true)
			}
			Map params = [uploadMethod: 'file', title: 'Zoinks', filename: 'zoinks.mp3', event: [event.name]]
		
		when:
			def response = client.post(path: '/api/content', body: params)
			
		then:
			response.status == 200
			remote { Tag.count() } == 1
	}
	
	def "Creating content responds with json representation"() {
		
		given:
			Map params = [uploadMethod: 'file', title: 'Zoinks', filename: 'zoinks.mp3', description: 'Shaggy saying Zoinks', theme: ['Scooby Doo', 'Cartoon'], event: ['Failure', 'Error']]
			
		when:
			def response = client.post(path: '/api/content', body: params)
			def result = response.data
			
		then:
			response.status == 200
			
			def id = remote {
				Content.findByTitle(params.title).id
			}
		
			result.resourceId == "content/$id"
			result.title == params.title
			result.description == params.description
			
			result.themes.collect { it.name }.containsAll(params.theme)
			result.events.collect { it.name }.containsAll(params.event)
	}
	
	@Unroll('Validation errors on create result in 400(title=#title,filename=#filename)')
	def "Validation errors on create result in 400"() {
		
		given:
			Map params = [title: title, filename: filename]
			
		expect:
			client.post(path: '/api/content', body: params).status == 400

		where:
			title   | filename
			null	| 'valid'
			''		| 'valid'
			'valid' | null
			'valid' | ''
	}
		
	def "Creates content data"() {
		
		given:
			String filename = 'zoinks.mp3'
			byte[] bytes = 'some data'.bytes
			def id = remote {
				Content content = Content.build(title: 'Zoinks', filename: filename)
				content.id
			}
			
		when:
			// Attempting to do everything in an expect block results in a weird bug 
			// where the remote plugin name gets used as the uri path !!!
			def status = postData("/api/content/$id/data".toString(), filename, bytes).status

		then:		
			status == 204	
			remote { Content content = Content.get(id) }.bytes == bytes		
	}
	
	def "Attempting to add content data to unknown content results in 404"() {
		
		given:
			byte[] bytes = 'some data'.bytes
			
		expect:
			postData("/api/content/999/data", 'blah.mp3', bytes).status == 404
	}	
	
	def "Attempting to upload unsupported content returns 400"() {
		
		given:
			String filename = 'zoinks.js'
			byte[] bytes = 'some data'.bytes
			def id = remote {
				Content content = Content.build(title: 'Zoinks', filename: filename)
				content.id
			}
			
		when:
			def response = postData("/api/content/$id/data".toString(), filename, bytes)

		then:		
			response.status == 400
			response.data[0] == "Content type 'application/octet-stream' is unsupported."	

	}
	
	def "Attempting to upload unsupported content after a create deletes the incomplete record"() {
		
		given:
			String filename = 'zoinks.js'
			byte[] bytes = 'some data'.bytes
			def id = remote {
				Content content = Content.build(title: 'Zoinks', filename: filename)
				content.id
			}
			
		when:
			def response = postData("/api/content/$id/data".toString(), filename, bytes)

		then:
			response.status == 400
			
			Content content = remote {
				Content.get(id)
			}			
			assert content == null
	}
	
	def "Attempting to upload unsupported content after an update reverts the record"() {
		
		given:		
			String originalFilename = 'zoinks.mp3'
			String originalContentType = 'audio/mp3'
			byte[] originalBytes = 'original data'.bytes
			def id = remote {
				Content content = Content.build(title: 'Zoinks', filename: originalFilename, bytes: originalBytes, type: originalContentType)
				content.id
			}
			
		when:
			def response = postData("/api/content/$id/data".toString(),  'zoinks.js', 'updated data'.bytes)

		then:
			response.status == 400
			
			Content content = remote {
				Content.get(id)
			}
			
			assert content.bytes == originalBytes
			assert content.type == originalContentType
			assert content.filename == originalFilename
	}
	
	def "Updates content"() {
		
		given:
			def id = remote {
				Content content = new Content(title: 'Zoinks', description: 'Shaggy saying Zoinks', bytes: 'some'.bytes, type: 'some/type')
				assert content.save(flush:true) : content.errors
				content.id				
			}
			Map params = [uploadMethod: 'file', title: 'The Force Will Be With You Always', filename: 'theforce.mp3', description: 'Mystic words from Ben Kenobi', theme: ['Star Wars']]
			
			
		when:
			def response = client.put(path: "/api/content/$id", body: params)
			
		then:
			response.status == 200 
			
			Content content = remote {
				Content content = Content.findByTitle params.title
				content.themes.each { it.toString() /* workaround hibernate proxy issue */ }
				content
			}
			
			content.title == params.title
			content.description == params.description
			content.filename == 'theforce.mp3'
			content.themes.size() == 1
			content.themes.collect { it.name }.containsAll(params.theme)
	}
	
	def "Updating content reuses existing themes"() {
		
		given:		
			def id = remote {
				Tag theme = new Tag('Scooby Doo', TagType.theme).save(flush:true)
				Content content = new Content(title: 'Zoinks', description: 'Shaggy saying Zoinks', themes: [theme])
				content.filename = 'zoinks.mp3'
				assert content.save(flush: true) : content.errors
				content.id
			}
			
			Map params = [uploadMethod: 'file', title: 'The Force Will Be With You Always', filename: 'theforce.mp3', description: 'Mystic words from Ben Kenobi', theme: ['Scooby Doo']]
			
			
		when:
			def response = client.put(path: "/api/content/$id", body: params)
			
		then:
			response.status == 200
			remote { Tag.count() } == 1

	}
	
	
	def "Updating content responds with json representation"() {
		
		given:
			def id = remote {
				Tag theme = Tag.build(name: 'Scooby Doo', uri: 'scooby-doo')
				Content content = new Content(title: 'Zoinks', description: 'Shaggy saying Zoinks', bytes: 'some'.bytes, type: 'some/type', themes: [theme])
				assert content.save(flush:true) : content.errors
				content.id
			}
			Map params = [uploadMethod: 'file', title: 'Eject', filename: 'file', description: 'Wedge saying Eject', theme: ['Star Wars']]
			
		when:
			def response = client.put(path: "/api/content/$id", body: params)
			def result = response.data
						
		then:
			response.status == 200	
		
			result.resourceId == "content/$id"
			result.title == params.title
			result.description == params.description
			result.themes.size() == 1
			result.themes[0].name == params.theme[0]
	}
	
	@Unroll('Validation errors on update result in 400(title=#title)')
	def "Validation errors on udate result in 400"() {
		
		given:
			Map params = [title: title]
			
		expect:
			client.post(path: '/api/content', body: params).status == 400

		where:
			title << [null, '']
	}
	
	def "Provides a json representation of content"() {
		
		given:
			Content content = remote {
				Tag theme = new Tag('Scooby Doo', TagType.theme).save()
				Tag event = new Tag('Failure', TagType.event).save()
				Content content = new Content(title: 'Zoinks', description: 'Shaggy saying Zoinks', filename: 'zoinks.mp3', bytes: 'Zoinks'.bytes, type: 'audio/mp3')
				content.addToThemes(theme)
				content.addToEvents(event)
				content.save(flush:true)
			}
		
		when:
			def response = client.get(path: "/api/content/$content.id")
			def result = response.data
			
		then:
			response.status == 200

			result.resourceId == "content/$content.id"
			result.title == content.title
			result.description == content.description
			result.type == content.type
			result.dataHashCode != null 
			result.dataResourceId == "content/$content.id/data"
			result.themes.size() == 1
			result.themes[0].name == content.themes[0].name
			result.events.size() == 1
			result.events[0].name == content.events[0].name
	}
	
	def "Responds with content data"() {
		
		given:		
			byte[] bytes = 'some data'.bytes	
			def id = remote {
				Content.build(bytes: bytes, type: 'foo/bar').id
			}
					
		expect:
			client.get(path: "/api/content/$id/data").data.bytes == bytes
	}
		
	def "Specifies correct content type for data"() {
		
		given:
			String contentType = 'audio/mpeg'
			def id = remote {
				Content.build(type: contentType, bytes: 'some data'.bytes).id
			}
			
		expect:
			client.get(path: "/api/content/$id/data").contentType == contentType
	}
		
	@Unroll("Sends 404 when content not found #path")
	def "Sends 404 when content not found"() {
			
		expect:
			client.get(path: path).status == 404
			
		where:
			path << ['api/content/999', 'api/content/999/data']
	}
	
	def "Lists all content in alphabetic order"() {
		
		given:
			data {
				it.load('scoobydoo', 'starwars')
			}		
			  
		when:
			def results = client.get(path: "/api/content").data
			
		then:
			results.size() == 5
			results[0].title == 'A day long remembered'
			results[4].title == 'The force will be with you always'
	}		
	
	def "List can return no results"() {
		
		when:
			def results = client.get(path: "/api/content").data
			
		then:
			results.size() == 0
	}
		
	def "Deletes content"() {		
		given:
			def id = remote {
				Content.build(title: 'Zoinks', filename: 'zoinks.mp3').id				
			}
			
		expect:
			client.delete(path: "/api/content/$id").status == 204
	}
	
	def "Deleting content that doesn't exist is tollerated"() {
			
		expect:
			client.delete(path: "/api/content/999").status == 204
	}

	
	private HttpResponseDecorator postData(String path, String filename, byte[] bytes) {
		client.request(POST) { request ->
			request.entity = getMultipartEntity('file', filename, bytes)
			uri.path = path
		}
	}
	
	private MultipartEntity getMultipartEntity(String paramName, String filename, byte[] bytes) {		
		String mimeType = mimeTypes.getContentType(filename)
		
		ByteArrayBody body = new ByteArrayBody(bytes, mimeType, filename)
		MultipartEntity entity = new MultipartEntity()
		entity.addPart(paramName, body)
		entity
	}
			

	
}