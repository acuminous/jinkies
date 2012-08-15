package uk.co.acuminous.jinkies.spec

import groovyx.net.http.RESTClient
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*

class RestClientMixin {

	def getRestClient(String baseUrl) {
		RESTClient client = new RESTClient(baseUrl)
		client.defaultRequestContentType = URLENC
		client.handler.failure = { def resp, def reader ->
			resp.data = reader
			resp
		}
		client
	}
	
}
