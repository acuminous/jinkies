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
	
	boolean isSupported(Content content) {
		contentTypes.find { it == content.type }
	}
	
	@Override
	synchronized void play(Content content) {
		
		
		if (contentTypes.contains(content.type)) {
			
			Thread t = Thread.start {
			
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

			t.join(30000)			
		}
	}
	
}
