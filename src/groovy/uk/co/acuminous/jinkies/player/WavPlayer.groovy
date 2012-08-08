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

import groovy.util.logging.Slf4j;

import java.util.List;

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.DataLine
import javax.sound.sampled.SourceDataLine

import uk.co.acuminous.jinkies.content.Content;
import uk.co.acuminous.jinkies.content.ContentPlayer;

@Slf4j
class WavPlayer implements ContentPlayer {
	
	List<String> contentTypes = ['audio/wav', 'audio/x-wav']
	
	boolean isSupported(Content content) {
		// The Java Sound System only plays a limited subset of wav files and
		// throws an exception for those that it doesn't support
		boolean result = false
		try {
			InputStream inputStream = new ByteArrayInputStream(content.bytes)
			AudioFormat af = AudioSystem.getAudioFileFormat(inputStream)		
		} catch (Exception e) {
			// meh
		}
		return result
	}
	
	
	@Override
	synchronized void play(Content content) {
		
		if (contentTypes.contains(content.type)) {

			try {
				InputStream inputStream = new ByteArrayInputStream(content.bytes)
				AudioInputStream ais = AudioSystem.getAudioInputStream(inputStream)
	
				Clip clip = AudioSystem.clip
				clip.open(ais)
				clip.start()				
			} catch (Exception e) {
				log.error("Error playing $content.title", e)
			}
		}
	}
}