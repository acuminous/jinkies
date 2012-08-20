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
import uk.co.acuminous.jinkies.util.HttpClientsFactory
import static uk.co.acuminous.jinkies.util.AssertionUtils.*;
import grails.buildtestdata.mixin.Build

import betamax.Betamax
import betamax.Recorder
import org.junit.Rule

@Build(Content)
class ContentSpec extends Specification {

	@Rule Recorder recorder = new Recorder()
	
	@Unroll("Content #field has #constraint constraint")
	def "Content fields are mandatory"() {
		
		given:
			Content content = new Content((field): value)
			
		expect:
			hasConstraint(content, field, constraint)
			
		where:
			field   | value | constraint
			'title' | null  | 'nullable'
			'title' | ''	| 'blank'
	}	
		
	@Unroll("Content #field does not have #constraint constraint")
	def "Content fields without constraints"() {
		
		given:
			Content content = new Content((field): value)
			
		expect:
			withoutConstraint(content, field, constraint)
			
		where:
			field         | value | constraint
			'description' | null  | 'nullable'
			'type'		  | null  | 'nullable'
			'bytes'       | null  | 'nullable'
			'filename'    | null  | 'nullable'
			'url'         | null  | 'nullable'
	}
	
	@Unroll("Content filename #filename and url #url are mutually exclusive")
	def "Content filename and url are mutually exclusive"() {
		
		given:
			Content content = new Content(filename: 'filename', url: 'url')
			
		expect:
			hasConstraint(content, 'filename', 'content.filename.url')
	}
	
	@Unroll("Cannot save content with no bytes #bytes, no filename #filename and no url #url")	
	def "Cannot save content with no bytes, no filename and no url"() {
		
		given:
			Content content = new Content(title: 'foo', bytes: bytes, filename: filename, url: url)
		
		expect:
			!content.validate()
			hasConstraint(content, 'bytes', 'content.bytes.nullable')
			
		where:
			bytes     | filename | url
			null      | null     | null
			null      | ''       | null
			null      | null     | ''
	}
	
	def "Cannot save content with a url but no bytes"() {
		
		given:
			Content content = new Content(title: 'foo', url: 'url')
		
		expect:
			hasConstraint(content, 'bytes', 'content.url.unreachable')
	}	
	
	def "Can save content with a filename but no bytes"() {
		
		given:
			Content content = new Content(title: 'foo', filename: 'filename')
		
		expect:
			withoutConstraint(content, 'bytes', 'content.bytes.required')
	}
		
	def "Cannot save content with a content type but no bytes"() {
		
		given:
			Content content = new Content(title: 'foo', type: 'foo/bar', filename: 'filename')
		
		expect:
			hasConstraint(content, 'bytes', 'content.bytes.nullable')
	}
		
	def "Cannot save content with bytes but no content type"() {
		
		given:
			Content content = new Content(bytes: 'foo'.bytes)
		
		expect:
			hasConstraint(content, 'type', 'content.type.nullable')
	}
	
	@Unroll("Renders Content as String [#title, #type]")
	def "Renders Content as String"() {
		
		given:
			Content content = Content.build(title: title, type: type, filename: 'foo', bytes: 'foo'.bytes)
			
		expect:
			content.toString() == "Content[id=1,title=$title,type=$type]"
			
		where:
			title              | type
			'Jinkies'          | 'audio/mpeg'
			'Scooby Dooby Doo' | 'audio/wav'
	}
		
	@Betamax(tape="Content")	
	def "Aquires content with url"() {
		
		given:
			Content content = new Content()
			content.httpClientsFactory = new HttpClientsFactory()	
			
		when:
			content.setUrl('http://www.rosswalker.co.uk/movie_sounds/sounds_files_20100522_76672091/airplane/surely_a.wav')
			content.aquire()
		
		then:
			content.bytes?.length > 1000
			content.type == 'audio/x-wav'		
	}
	
	
	@Betamax(tape="Content")
	def "Aquires content without a url has no side effects"() {
		
		given:
			Content content = new Content(bytes: 'Jinkies'.bytes)
			
		when:
			content.aquire()
		
		then:
			content.bytes == 'Jinkies'.bytes
	}
	
	
	@Betamax(tape="Content")
	def "Tollerates exceptions aquiring content"() {
		
		given:
			Content content = new Content()
			
		when:
			content.setUrl('http://www.rosswalker.co.uk/movie_sounds/sounds_files_20100522_76672091/airplane/doesnotexist.wav')
		
		then:
			content.bytes == null
			content.type == null
	}
}
