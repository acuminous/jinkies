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

import java.util.List;

import uk.co.acuminous.jinkies.content.Content
import uk.co.acuminous.jinkies.content.ContentPlayer
import javax.sound.sampled.AudioInputStream
import marytts.LocalMaryInterface
import marytts.MaryInterface
import marytts.util.data.audio.AudioPlayer

class SpeechSynthesizer implements ContentPlayer {

	List<String> contentTypes = [
		'text/plain'
	]
	
	@Override	
	boolean isSupported(Content content) {
		contentTypes.find { it == content.type }
	}

	@Override
	synchronized void play(Content content, Map context) {

		String text = new String(content.bytes, 'utf-8')
		
		MaryInterface synthesizer = new LocalMaryInterface()
		Set<String> voices = synthesizer.availableVoices		
		synthesizer.setVoice(voices.iterator().next())
		
		AudioInputStream audio = synthesizer.generateAudio(text);
		AudioPlayer player = new AudioPlayer(audio);
		player.start();
		player.join();
	}

}
