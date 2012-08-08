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

import uk.co.acuminous.jinkies.content.Tag;
import uk.co.acuminous.jinkies.util.UriBuilder;

class TagService {

	List<Tag> findOrCreateThemes(List<String> tagNames) {			
		List<Tag> results = tagNames.collect { String name ->
			findOrCreateTag(name, TagType.theme)
		}
		results.unique()
	}
	
	List<Tag> findOrCreateEvents(List<String> tagNames) {
		List<Tag> results = tagNames.collect { String name ->
			findOrCreateTag(name, TagType.event)
		}
		results.unique()
	}
	
	Tag findOrCreateTag(String name, TagType tagType) {	
		Tag tag = new Tag(name, tagType)
		Tag.findByUri(tag.uri) ?: tag.save(flush:true, failOnError:true)
	}
	
}
