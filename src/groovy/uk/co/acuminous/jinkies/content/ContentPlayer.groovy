package uk.co.acuminous.jinkies.content

interface ContentPlayer {

	boolean isSupported(Content content)
	void play(Content content)
	List<String> getContentTypes()
	
}
