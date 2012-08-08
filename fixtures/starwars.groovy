import uk.co.acuminous.jinkies.content.*

include 'build-events'


fixture {
	starWars(Tag, 'Star Wars', TagType.theme)
	vader1(Content, title: 'I have you now', type: 'audio/wav', bytes: 'I have you now'.bytes) {
		themes = [ ref('starWars')]
		events = [ ref('failure')]
	}
	
	vader2(Content, title: 'A day long remembered', type: 'audio/mpeg', bytes: 'This will be a day long remembered'.bytes) {
		themes = [ ref('starWars')]
		events = [ ref('failure')]
	}
	
	kenobi1(Content, title: 'The force will be with you always', type: 'audio/mpeg', bytes: 'audio/mpeg'.bytes) {
		themes = [ ref('starWars')]
		events = [ ref('success')]
	}
}