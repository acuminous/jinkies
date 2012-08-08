package uk.co.acuminous.jinkies.util

import org.apache.http.conn.routing.HttpRoutePlanner
import org.apache.http.impl.conn.ProxySelectorRoutePlanner

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.RESTClient

class HttpClientsFactory implements Serializable {

	def authConfig
	
	HTTPBuilder getHttpBuilder(Map params = [:]) {
		HTTPBuilder httpBuilder = new HTTPBuilder(params)
		
		enableProxyConfiguration(httpBuilder)

		return httpBuilder
	}
	
	RESTClient getRestClient(String uri) {
		
		RESTClient restClient = new RESTClient(uri)
		
		enableProxyConfiguration(restClient)	
		
		restClient
	}
	
	private void enableProxyConfiguration(HTTPBuilder httpBuilder) {
		HttpRoutePlanner routePlanner = new ProxySelectorRoutePlanner(
			httpBuilder.client.connectionManager.schemeRegistry,
			ProxySelector.default
		)
		httpBuilder.client.routePlanner = routePlanner		
	}
	
}
