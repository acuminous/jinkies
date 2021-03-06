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

import java.util.List

import org.springframework.validation.Errors

import uk.co.acuminous.jinkies.ci.Job;
import uk.co.acuminous.jinkies.jenkins.JenkinsMonitor
import grails.converters.JSON
import static javax.servlet.http.HttpServletResponse.*
import uk.co.acuminous.jinkies.util.JinkiesErrorRenderer

// Mixins are currently working properly, therefore extending (yuck)
class JenkinsController extends JinkiesErrorRenderer {

	JenkinsMonitor jenkinsMonitor
	JenkinsServer jenkinsServer
	
	def list(JobCommand cmd) {
		report(cmd.errors) || renderJobs()
	}

	private void renderJobs() {
		try {
			List<Job> jobs = jenkinsServer.getJobs(params.url)
			
			List<Job> results = jobs.collect knownAndNewJobs
			
			results.sort byJobUrl
			
			render results as JSON
	
		} catch (Exception e) {		
			log.warn("Error retrieving jobs from ${params.url}: ${e.message}")			
			renderJsonError 'job.url.unreachable', SC_INTERNAL_SERVER_ERROR
		}
	}
	
	def knownAndNewJobs = { Job job ->
		Job.findByUrlIlike(job.url) ?: job
	}
	
	def byJobUrl = { Job job1, Job job2 ->
		job1.url.toUpperCase() <=> job2.url.toUpperCase()		
	}	
}

class JobCommand {
	String url
	
	static constraints = {
		url nullable: false, blank: false
	}
}
