package uk.co.acuminous.jinkies.ci

class JobBuilder {
	
	static Job build(Map data = [:]) {
		data.displayName = data.get('displayName', 'Jinkies')
		data.type = data.get('type', 'jenkins')
		data.url = data.get('url', '.../job/Jinkies/')
		data.theme = data.get('theme', null)
		data.channels = data.get('channels', ['audio'])
		
		return new Job(data)
	} 
	
}
