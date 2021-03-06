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
