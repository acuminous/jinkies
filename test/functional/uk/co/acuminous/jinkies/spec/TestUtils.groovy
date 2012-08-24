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
