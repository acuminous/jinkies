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