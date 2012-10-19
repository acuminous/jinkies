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
package uk.co.acuminous.jinkies.jenkins

import groovy.util.logging.Slf4j
import groovyx.net.http.HTTPBuilder
import org.apache.http.impl.conn.ProxySelectorRoutePlanner

import uk.co.acuminous.jinkies.ci.Build
import uk.co.acuminous.jinkies.ci.Job
import uk.co.acuminous.jinkies.util.HttpClientsFactory

import static groovyx.net.http.ContentType.JSON

@Slf4j
class JenkinsServer {
	
	HttpClientsFactory httpClientsFactory

	List<Job> getJobs(String url) {
		
		log.debug("Requesting jobs from $url")
		
		List<Job> results = []
							 	
		HTTPBuilder httpBuilder = httpClientsFactory.getHttpBuilder()
		
		httpBuilder.get(uri: jsonApi(url), contentType: JSON) { resp, json ->
			List jobs = json.jobs ?: [ json ]
			jobs.each { job ->
				results << new Job(displayName: job.name, url: job.url, type: 'jenkins')
			}
		}
		
		return results
	}
	
	Build getLatestBuild(Job job) {
		log.debug("Retrieving latest build for $job.url")
		
		Build build
		
		HTTPBuilder httpBuilder = httpClientsFactory.getHttpBuilder()
		String jobUrl = jsonApi job.url
		httpBuilder.get(uri: jobUrl, contentType: JSON) { resp, json ->
			if (json.builds) {
				build = new Build(job: job)
				build.url = json.builds[0].url
				build.number = json.builds[0].number
			}
		}
		
		if (build) {
			String buildUrl = jsonApi build.url
			httpBuilder.get(uri: buildUrl, contentType: JSON) { resp, json ->
				build.result = json.building ? 'BUILDING' : json.result
				build.timestamp = json.timestamp
				build.uuid = build.url + '_' + json.id
			}
		}
				
		return build
	}
	
	String jsonApi(String url) {
		(url.endsWith('/') ? "$url" : "$url/") + 'api/json'
	}
}
