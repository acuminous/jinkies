package uk.co.acuminous.jinkies.content

import uk.co.acuminous.jinkies.util.UriBuilder

class Tag implements Serializable {

	String name
	TagType type
	String uri
	
	static constraints = {
		uri(unique: true)
	}
	
	static hasMany = [Content]	
	
	Tag(String name, TagType type) {
		this.name = name
		this.type = type
		this.uri = Tag.generateUri(name, type)
	}

	static Tag findThemeByName(String name) {
		String uri = Tag.generateUri(name, TagType.theme)
		Tag.findByUri(uri)
	}
	
	static Tag findEventTypeByName(String name) {
		String uri = Tag.generateUri(name, TagType.event)
		Tag.findByUri(uri)
	}
				
	boolean equals(Object other) {
		this.is(other) || (
			other != null &&
			this.class == other.class &&
			this.uri == other.uri
		)
	}
	
	int hashCode() {
		uri.hashCode()
	}
	
	String toString() {
		"Tag[id=$id,uri=$uri,name=$name]"
	}
	
	static String generateUri(String name, TagType type) {
		type.name() + '/' + new UriBuilder().toUri(name)
	}
	
}
