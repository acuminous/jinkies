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


class Build {
	
	String url
	Integer number	
	String result
	Long timestamp
	Job job	

	boolean isSuccess() {
		result == 'success'
	}
			
	@Override
	boolean equals(Object other) {
		this.is(other) || (
			other != null &&
			other.class == this.class &&
			other.url == this.url
		)
	}
	
	@Override
	int hashCode() {
		url.hashCode()
	}
	
	@Override
	String toString() {
		"Build[url=$url,result=$result,timestamp=$timestamp]"
	}
}
