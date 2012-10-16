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

import uk.co.acuminous.jinkies.ci.Build;


class BuildBuilder {
	
	Build build(Map data = [:]) {
		
		data.job = data.job ?: new Job(displayName: 'Jinkies', url: '.../job/Jinkies/')
		data.number = data.number ?: 2
		data.url = data.url ?: data.job.url + "${data.number}/"
		data.result = data.result ?: 'SUCCESS'
		data.timestamp = data.timestamp ?: System.currentTimeMillis()
		data.uuid = data.uuid ?: "${data.url}_2012-09-06_08-21-54" 
		
		return new Build(data)
	} 
	
}
