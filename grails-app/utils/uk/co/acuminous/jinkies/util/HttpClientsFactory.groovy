/* 
 * Copyright 2012 Acuminous Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.acuminous.jinkies.util

import org.apache.http.conn.routing.HttpRoutePlanner
import org.apache.http.impl.conn.ProxySelectorRoutePlanner

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.RESTClient

class HttpClientsFactory implements Serializable {

	def authConfig

	HTTPBuilder getHttpBuilder() {
		HTTPBuilder httpBuilder = new HTTPBuilder()
		
		enableProxyConfiguration(httpBuilder)

		return httpBuilder
	}
	
	RESTClient getRestClient(String url) {			
	
		RESTClient restClient = new RESTClient(url)
		
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
