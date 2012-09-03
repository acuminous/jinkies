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

import spock.lang.Specification
import spock.lang.Unroll
import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType
import static uk.co.acuminous.jinkies.util.AssertionUtils.*;
import grails.buildtestdata.mixin.Build

@Build(Job)
class JobSpec extends Specification {

	@Unroll("Job #field has #constraint constraint")
	def "Job fields with constraints"() {
		
		given:
			Job job = new Job((field): value)
			
		expect:
			hasConstraint(job, field, constraint)
			
		where:
			field         | value | constraint
			'displayName' | null  | 'nullable'
			'displayName' | ''    | 'blank'
			'url'         | null  | 'nullable'	
			'url'		  | ''    | 'blank'
			'type'        | null  | 'nullable'
			'type'   	  | ''    | 'blank'
	}
	
	@Unroll("Job #field does not have #constraint constraint")
	def "Job fields without constraints"() {
		
		given:
			Job job = new Job((field): value)
			
		expect:
			withoutConstraint(job, field, constraint)
			
		where:
			field             | value | constraint
			'theme'           | null  | 'nullable'
	}
		
	@Unroll("Job #field is unique")
	def "Job fields are unique"() {
		
		given:
			Job job1 = Job.build((field): value)
			Job job2 = new Job((field): value)
					
		expect:
			hasConstraint(job2, field, 'unique')
			
		where:
			field   | value
			'url'   | 'jinkies'
	}
	
	@Unroll("Renders Job as String [#displayName, #url, #theme, #channels]")
	def "Renders Job as String"() {
		
		given:
			Tag tag = new Tag('Scooby Doo', TagType.theme)
			Job job = Job.build(displayName: displayName, url: url, theme: tag)
			
		expect:
			job.toString() == "Job[id=1,displayName=$displayName,url=$url,theme=Scooby Doo]"
			
		where:
			displayName | url
			'Jinkies'   | '.../Jinkes/'
			'Julez'     | '.../Julez/' 
	}
	
	def "Jobs cannot have duplicate channels"() {
		
		given:
			Job job = Job.build(channels: ['a'])
			
		when:
			job.addToChannels('a')
			
		then:
			hasConstraint(job, 'channels', 'duplicate')		
		
	}
	
	@Unroll("Comparing #url1 against #url2")
	def "Job equality is based on url"() {
		expect:
			def actual = new Job(url: url1) == new Job(url: url2)
			actual == expected
			
		where:
			url1      | url2      | expected
			'a'       | 'a'	      | true
			'A'       | 'a'       | true
			'a'       | 'A'       | true
			'a'       | 'b'       | false
			'a'       | null      | false
			null      | 'a'       | false		
	}
}
