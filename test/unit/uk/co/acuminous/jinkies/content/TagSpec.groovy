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
package uk.co.acuminous.jinkies.content


import spock.lang.Specification
import spock.lang.Unroll
import uk.co.acuminous.jinkies.content.Tag;
import grails.buildtestdata.mixin.Build
import static uk.co.acuminous.jinkies.util.AssertionUtils.*;

@Build(Tag)
class TagSpec extends Specification {

	@Unroll("Tag #field has #constraint constraint")
	def "Tag fields are mandatory"() {
				
		given:
			Tag tag = new Tag((field): value)
			
		expect:
			hasConstraint(tag, field, constraint)
			
		where:
			field   | value | constraint
			'uri'   | null  | 'nullable'
			'name'  | null  | 'nullable'
			'type'  | null  | 'nullable'
	}
	
	@Unroll("Tag #field is unique")
	def "Tag fields are unique"() {
		
		given:
			Tag tag1 = Tag.build((field): value)
			Tag tag2 = new Tag((field): value)
					
		expect:
			hasConstraint(tag2, field, 'unique')
			
		where:
			field   | value    
			'uri'   | 'jinkies'
	}
	
	@Unroll("Renders Tag as String [#uri, #name]")
	def "Renders Tag as String"() {
		
		given:
			Tag tag = Tag.build(uri: uri, name: name)
			
		expect:
			tag.toString() == "Tag[id=1,uri=$uri,name=$name]"
			
		where:
			uri               | name
			'theme/scoobydoo' | 'Scooby Doo'
			'event/success'   | 'Success'
	}
	
	@Unroll("Generates uri for #name and #type")
	def "Renders uri for tag"() {
		
		
		expect:
			Tag.generateUri(name, type) == uri
			
		where:
			name         | type          | uri
			'Success'    | TagType.event | 'event/success'
			'Scooby Doo' | TagType.theme | 'theme/scooby-doo'
			
	}
	
	
}
