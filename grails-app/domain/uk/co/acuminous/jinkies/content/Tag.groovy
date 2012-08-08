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
