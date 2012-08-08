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

class JobBuilder {
	
	static Job build(Map data = [:]) {
		data.displayName = data.get('displayName', 'Jinkies')
		data.type = data.get('type', 'jenkins')
		data.url = data.get('url', '.../job/Jinkies/')
		data.theme = data.get('theme', null)
		data.channels = data.get('channels', ['audio'])
		
		return new Job(data)
	} 
	
}
