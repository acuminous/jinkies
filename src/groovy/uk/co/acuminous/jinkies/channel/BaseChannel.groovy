package uk.co.acuminous.jinkies.channel

import java.util.List;

abstract class BaseChannel implements Channel {

	String name
		
	@Override	
	List<String> getContentTypes() {
		[]		
	}
	
	
	@Override
	String toString() {
		"Channel[name=$name,contentTypes=${contentTypes}]"
	}
	
}
