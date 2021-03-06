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
package uk.co.acuminous.jinkies.channel

import java.util.List;

import uk.co.acuminous.jinkies.content.Content
import uk.co.acuminous.jinkies.content.ContentPlayer;

class ContentChannel extends BaseChannel {

	List<ContentPlayer> players = []
	
	@Override
	public void handle(Map event) {
		
		log.debug "Received event: $event"
				
		if (event.selectedContent) {							
			players.each { ContentPlayer player ->	
				Content content = event.selectedContent
				if (player.isSupported(content)) {							
					player.play(content, event)
				}
			}
		}
	}
	
	@Override	
	List<String> getContentTypes() {
		Set<String> results = new LinkedHashSet()
		
		players.each { ContentPlayer player ->
			results += player.contentTypes
		}
		
		results as List
	}
}
