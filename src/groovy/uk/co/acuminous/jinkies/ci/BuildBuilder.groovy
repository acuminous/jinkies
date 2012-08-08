package uk.co.acuminous.jinkies.ci

import uk.co.acuminous.jinkies.ci.Build;


class BuildBuilder {
	
	Build build(Map data = [:]) {
		
		data.job = data.job ?: new Job(displayName: 'Jinkies', url: '.../job/Jinkies/')
		data.number = data.number ?: 2
		data.url = data.url ?: data.job.url + "${data.number}/"
		data.result = data.result ?: 'SUCCESS'
		data.timestamp = data.timestamp ?: System.currentTimeMillis()
		return new Build(data)
	} 
	
}
