package uk.co.acuminous.jinkies.content

import uk.co.acuminous.jinkies.util.HttpClientsFactory
import groovyx.net.http.*
import static groovyx.net.http.Method.GET
import static groovyx.net.http.ContentType.BINARY


class Content implements Serializable {

	String title
	String description
	String filename
	String url
	String type
	byte[] bytes
	List<Tag> themes
	List<Tag> events
	
	HttpClientsFactory httpClientsFactory
		
	static hasMany = [themes: Tag, events: Tag]
	
	static constraints = {
		title blank: false
		description nullable: true	
		filename nullable: true, validator: filenameValidator
			
		url nullable: true		
		type nullable: true, validator: contentTypeValidator 
		bytes nullable: true, maxSize: 1073741824, validator: bytesValidator
	}
	
	static transients = ['httpClientsFactory']
	
	public void aquire() {
		if (url) {
			clearContent()
			aquireContent()
		}
	}
	
	private void clearContent() {
		bytes = null
		type = null
	}
	
	private void aquireContent() {
		try {
			RESTClient client = httpClientsFactory.getRestClient(url)
			def response = client.get(contentType: BINARY)			
			bytes = response.data.bytes
			type = response.contentType
		} catch (Exception e) {
			// Errors are reported via constraint validation
			log.warn('Error aquiring content', e)
		}
	}
	
	String toString() {
		"Content[id=$id,title=$title,type=$type]"
	}
	
	static Closure filenameValidator = { String filename, Content content ->
		if (filename && content.url) {
			'content.filename.url'
		}
	}

	
	static Closure bytesValidator = { byte[] bytes, Content content ->
		if (!(bytes || content.filename || content.url)) {
			'content.bytes.nullable'
		} else if (content.url && !bytes) {
			'content.url.unreachable'
		} else if (!bytes && content.type) {
			'content.bytes.nullable'
		}
	}
	
	static Closure contentTypeValidator = { String type, Content content ->
		if (!type && content.bytes) {
			'content.type.nullable'
		}
	}	
}
