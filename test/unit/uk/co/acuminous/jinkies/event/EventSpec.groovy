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
package uk.co.acuminous.jinkies.event

import spock.lang.Unroll
import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType
import grails.plugin.spock.UnitSpec
import grails.test.mixin.Mock
import static uk.co.acuminous.jinkies.util.AssertionUtils.*;

@Mock([Event, Tag])
class EventSpec extends UnitSpec {

	@Unroll("Event #field has #constraint constraint")
	def "Event fields with constraints"() {
		
		given:
			Event event = new Event((field): value)
			
		expect:
			hasConstraint(event, field, constraint)
			
		where:
			field         | value | constraint
			'uuid'		  | null  | 'nullable'
			'uuid'        | ''    | 'blank'
			'sourceId'    | null  | 'nullable'	
			'sourceId'    | ''    | 'blank'
			'type'        | null  | 'nullable'
			'timestamp'   | null  | 'nullable'
	}
	
	def "Event fields are unique"() {
		
		given:
			Tag success = new Tag('Success', TagType.event).save()
			Event event1 = new Event(uuid: '1', sourceId: 'foo/bar', type: success, timestamp: System.currentTimeMillis()).save()
					
		when:
			Event event2 = new Event(uuid: '1', sourceId: 'foo/bar', type: success, timestamp: System.currentTimeMillis())
			
		then:
			hasConstraint(event2, 'uuid', 'unique')
			

	}
	
}
