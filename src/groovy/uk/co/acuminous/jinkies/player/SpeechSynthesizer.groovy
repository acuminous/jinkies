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
