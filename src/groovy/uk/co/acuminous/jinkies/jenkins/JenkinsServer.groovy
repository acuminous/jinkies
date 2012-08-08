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
						
		httpBuilder.get(uri: jsonApi(url), contentType: JSON) { resp, json ->
			List jobs = json.jobs ?: [ json ]
			jobs.each { job ->
				results << new Job(displayName: job.name, url: job.url, type: 'jenkins')
			}
		}
		
		return results
	}
	
	List<Build> getBuildHistory(Job job) {
		
		log.debug("Requesting build history from $job.url")
		
		List<Build> results = []

		httpBuilder.get(uri: jsonApi(job.url), contentType: JSON) { resp, json ->
			json.builds.each { build ->
				results << new Build(job: job, url: build.url, number: build.number)
			}
		}
		
		return results
	}
	
	void populateMissingDetailsIn(Build build) {
		
		log.info("Requesting build details from $build.url")
		
		httpBuilder.get(uri: jsonApi(build.url), contentType: JSON) { resp, json ->
			build.result = json.result
			build.timestamp = json.timestamp
		}
	}
	
	String jsonApi(String url) {
		(url.endsWith('/') ? "$url" : "$url/") + 'api/json'
	}
	
	HTTPBuilder getHttpBuilder() {
		httpClientsFactory.getHttpBuilder()
	}	
}
