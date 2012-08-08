import uk.co.acuminous.jinkies.content.*

include 'build-events'

fixture {
	scoobyDoo(Tag, 'Scooby Doo', TagType.theme)	
	
	velma1(Content, title: 'Jinkies', type: 'audio/mpeg', bytes: 'Jinkies'.bytes) {
		themes = [ ref('scoobyDoo') ]
		events = [ ref('failure') ]
	}
	
	villan1(Content, title: 'Darn Kids', type: 'audio/mpeg', bytes: 'And I would have got away with it if it weren\'t for those darn kids'.bytes) {
		themes = [ ref('scoobyDoo') ]
		events = [ ref('failure') ]
	}
}