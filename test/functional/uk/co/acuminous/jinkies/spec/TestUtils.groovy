package uk.co.acuminous.jinkies.spec

import groovyx.net.http.RESTClient
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*


class TestUtils {

	static RESTClient getRestClient(String baseUrl) {
		RESTClient client = new RESTClient(baseUrl)
		client.defaultRequestContentType = URLENC
		client.handler.failure = { def resp, def reader ->
			resp.data = reader
			resp
		}
		client
	}
	
	static String getBaseUrl() {
		String port = System.getProperty('grails.server.port.http') ?: '8080'
		"http://localhost:$port"
	}
	
}
