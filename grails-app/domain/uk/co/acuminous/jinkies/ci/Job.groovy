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
package uk.co.acuminous.jinkies.ci

import uk.co.acuminous.jinkies.util.CommonValidators;
import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.event.Event


class Job implements Serializable {
		
	static final long serialVersionUID = 1L
	
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
		channels validator: CommonValidators.containsDuplicates
	}
	
	static hasMany = [channels:String]
	static fetchMode = [channels: 'eager', theme: 'eager']
	
	static mapping = {
		channels joinTable:[name:'job_channel', key:'job_id', column:'channel', type:'text']
	}
	
	String getResourceId() {
		id ? "job/$id" : null
	}
	
	def afterDelete() {
		Event.withNewSession {
			Event.findAllBySourceId(resourceId)*.delete()
		}
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
