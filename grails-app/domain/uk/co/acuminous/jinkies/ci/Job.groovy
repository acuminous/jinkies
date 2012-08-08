package uk.co.acuminous.jinkies.ci

import uk.co.acuminous.jinkies.test.ListValidator
import uk.co.acuminous.jinkies.content.Tag


class Job implements Serializable {
		
	String displayName
	String url
	String type	
	Tag theme
	List<String> channels
		
	static constraints = {
		displayName blank:false
		url blank:false, unique:true
		type blank: false
		theme nullable: true
		channels validator: ListValidator.containsDuplicates
	}
	
	static hasMany = [channels:String]
	static fetchMode = [channels: 'eager', theme: 'eager']	
	
	static mapping = {
		channels joinTable:[name:'job_channel', key:'job_id', column:'channel', type:'text']
	}
	
	@Override
	String toString() {
		"Job[id=$id,displayName=$displayName,url=$url,theme=${theme?.name}]"
	}
	
	@Override
	boolean equals(Object other) {
		this.is(other) || (		
			other != null &&		
			other.class == this.class &&
			other.url?.toLowerCase() == this.url?.toLowerCase()
		)
	}
	
	@Override
	int hashCode() {
		url.hashCode()
	}
}
