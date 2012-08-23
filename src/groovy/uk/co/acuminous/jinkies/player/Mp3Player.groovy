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
package uk.co.acuminous.jinkies.player


import groovy.util.logging.Slf4j
import javazoom.jl.player.Player as JLayerPlayer
import uk.co.acuminous.jinkies.content.Content
import uk.co.acuminous.jinkies.content.ContentPlayer


@Slf4j
class Mp3Player implements ContentPlayer {
	
	List<String> contentTypes = [
		'audio/mpeg',
		'audio/x-mpeg', 
		'audio/mp3',
		'audio/x-mp3', 
		'audio/mpeg3', 
		'audio/x-mpeg3',
		'audio/mpg',
		'audio/x-mpg',
		'audio/x-mpegaudio'
	]
	
	@Override	
	boolean isSupported(Content content) {
		contentTypes.find { it == content.type }
	}
	
	@Override
	synchronized void play(Content content, Map context) {
				
		if (contentTypes.contains(content.type)) {
			
			InputStream inputStream = new ByteArrayInputStream(content.bytes)
			JLayerPlayer jLayerPlayer = new JLayerPlayer(inputStream)
			
			try {
				jLayerPlayer.play()
			} catch (Exception e) {
				log.error("Error playing $content.title", e)
			} finally {
				jLayerPlayer.close()				
			}

		}
	}
	
}
