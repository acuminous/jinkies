package uk.co.acuminous.jinkies.spec

class TestUtils {

	static getBaseUrl() {
		String port = System.getProperty('grails.server.port.http') ?: '8080'
		"http://localhost:$port"
	}
	
}
