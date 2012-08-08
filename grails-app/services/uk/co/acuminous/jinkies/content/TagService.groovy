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
