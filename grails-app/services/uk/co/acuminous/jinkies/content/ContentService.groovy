package uk.co.acuminous.jinkies.content

import org.springframework.transaction.annotation.Transactional

@Transactional
class ContentService {

	List<ContentPlayer> players
	
	List<Content> findAllEligibleContent(Tag theme, Tag eventType, List<String> contentTypes) {
								
		Content.createCriteria().listDistinct {
			themes {
				idEq theme?.id
			}
			events {
				idEq eventType?.id
			}			
			inList 'type', contentTypes
			
			order 'title', 'asc'
		}	
	}
	
	boolean isSupported(Content content) {
		players.find { it.isSupported content }
	}
}
