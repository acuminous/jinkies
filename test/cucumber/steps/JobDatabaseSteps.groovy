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
package steps

import uk.co.acuminous.jinkies.ci.Job
import fixtures.RemoteJobRepository
import modules.JobWidget
import pages.JobsPage
import modules.JobDialog

this.metaClass.mixin(cucumber.runtime.groovy.EN)

RemoteJobRepository jobRepository = new RemoteJobRepository()

Given (~'that a job called (.*) does not exist') { String displayName ->
	assert Job.findByDisplayName(displayName) == null
}

Given (~'that there are (.*) (?:jobs|successful jobs)') { int number ->		
	jobs = []
	number.times { 
		jobs << jobRepository.buildRandomJob()
	}
}

Given (~'(.*) failing jobs') { int number ->
	jobs = []
	number.times {
		jobs << jobRepository.buildRandomJob()
		jobs.each {
			jobRepository.fail it
		}
	}
}

Given (~'that a (.*) job called (.*) is hosted at (.*)') {  String serverType, String jobName, String serverUrl ->
	job = jobRepository.buildJob([
		displayName: jobName, 
		type: serverType.toLowerCase(), 
		url: serverUrl + '/job/' + jobName,
		channels: []
	])
}

Given (~'a (.*) job called (.*)') {  String serverType, String jobName ->
	job = jobRepository.buildJob([
		displayName: jobName,
		type: serverType.toLowerCase(),
		url: '.../job/' + jobName
	])
}

Given (~'that (.*?) (?:also reports|reports|only reports) build events via the (.*) channel') { String jobName, String channel ->
	job = jobRepository.addChannel(jobName, channel)
}

Given (~'that (.*) has a (.*) theme') { String jobName, String theme ->
	job = jobRepository.setTheme(jobName, theme)
}


Then (~'create (.*) jobs in the database') { Integer n ->

	waitFor {
		jobs = Job.findAllByUrlIlike("%$job.url%")
		jobs.size() == n
	}
	
	jobs.each { databaseJob ->
		verifyDatabaseJob(job)
	}
}

Then (~'create the job in the database') { ->

	Job databaseJob
	waitFor {
		databaseJob = Job.findByDisplayName(job.displayName) 
	}	
	verifyDatabaseJob(databaseJob)

}

Then (~'do not create the job in the database') { ->
	Thread.sleep(500)
	Job databaseJob = Job.findByDisplayName(job.displayName)
	assert databaseJob == null
}

Then (~'delete (.*) from the database') { String jobName ->
	waitFor {
		Job databaseJob = Job.findByDisplayName(job.displayName)
		databaseJob == null
	}
}

Then (~'update the job in the database') { ->
	Job databaseJob = Job.findByDisplayName(job.displayName)
	
	verifyDatabaseJob(job)

}

Then (~'do not update the job in the database') { ->
	Job databaseJob = Job.findByDisplayName(job.displayName)
	
	verifyDatabaseJob(job)
}

verifyDatabaseJob = { Job databaseJob ->
	assert databaseJob != null
	if (job.displayName) {
		// When adding a whole server we wont have set an expected display name
		assert databaseJob.displayName == job.displayName
	}	
	assert databaseJob.url == job.url
	assert databaseJob.type == job.type
	assert databaseJob.theme == job.theme
	assert databaseJob.channels == job.channels
}