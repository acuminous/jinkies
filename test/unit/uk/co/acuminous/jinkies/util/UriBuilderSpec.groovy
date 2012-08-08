package uk.co.acuminous.jinkies.util

import grails.plugin.spock.UnitSpec

class UriBuilderSpec extends UnitSpec {

	def "Normalizes text"() {
		
		expect:
			normalized ==  new UriBuilder().toUri(text)		
		
		where:
			normalized | text
			'blah' | ' Blah '
			'bl-ah' | '__Bl__ah__'
			'blah' | '--Blah--'
			"this-used-to-have-whitespace" | " this used_to\t\n have  whitespace  "
			"wtf" | "WTF" // lower case
			"wtf" | "WTF!!!!!?????" // punctuation
			"wtf123" | "wtf123"  // numbers
			"omg-wtf-bbq" | "-omg-wtf-  bbq" // dashes
			"funki" | "funk\u00ef" // umlauts and other non-ascii crud
			"a3456-and-7890-b" | "a!\"3\u00A34\$5^6&7*8(9)0-_+=@':;~#?/>.<,\u00AC`b" // any non-alphanumerics stripped
			"a-3456-and-7890-b" | "a     !\"3\u00A34\$5^6&7*8(9)0-_+=@':;~#?/>.<,\u00AC`-    b" // any non-alphanumerics stripped
			'xx' | '%XX' // invalid url escape sequence
			'st-pauls' | 'St. Paul\'s'
			'stpauls' | 'St.Paul\'s'
			'elephant-and-castle' | 'Elephant & Castle'
			'elephant-and-castle' | 'Elephant&Castle'
			'south-west-trains' | 'South West Trains'
			'build573706-and-tokenec-4ak501405j7136402-and-timestamp2008-05-13t090041z-and-correlationid19b7e22185f-and-acksuccess-and-version32-and-a-b' | 'BUILD=573706&TOKEN=EC%2d4AK501405J7136402&TIMESTAMP=2008%2d05%2d13T09%3a00%3a41Z&CORRELATIONID=19b7e22185f&ACK=Success&VERSION=3%2e2&a%20b'; //some funny url encoded stuff

			// unusual accents
			'bouvetoya' | 'Bouvet\u00f8ya'
			'bouvetoya' | 'Bouvet\u00d8ya'
			'garcon' | 'Gar\u00e7on'
			'el-nino' | 'El Ni\u00f1o'
		
			// ligatures / old english
			'thing' | "\u00deing"
			'thing' | "\u00feing"
			'aegis' | "\u00c6gis"
			'aegis' | "\u00e6gis"
			'dh' | "\u00d0"
			'dh' | "\u00F0"
			'sz' | "\u00DF"

			// combining diacritic accents
			'cliche' | "Clich\u0301e"
	}
	
}
