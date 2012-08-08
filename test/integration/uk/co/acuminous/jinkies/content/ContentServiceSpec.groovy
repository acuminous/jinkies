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

import grails.plugin.spock.IntegrationSpec
import spock.lang.*

class ContentServiceSpec extends IntegrationSpec {

	def fixtureLoader
	ContentService contentService
	
	def "Lists content in alphabetical order"() {
		
		given:
			fixtureLoader.load('scoobydoo', 'starwars')
			Tag theme = Tag.findByName('Star Wars')
			Tag eventType = Tag.findByName('Failure')
			
		when:
			List<Content> results = contentService.findAllEligibleContent(theme, eventType, ['audio/mpeg', 'audio/wav'])
					
		then:
			results.size() == 2
			results[0].title == 'A day long remembered'
			results[1].title == 'I have you now'		
	}
		
	def "Narrows results by content type"() {
		
		given:
			fixtureLoader.load('scoobydoo', 'starwars')
			Tag theme = Tag.findByName('Star Wars')
			Tag eventType = Tag.findByName('Failure')
			
		when:
			List<Content> results = contentService.findAllEligibleContent(theme, eventType, ['audio/wav'])
		
		then:
			results.size() == 1
			results[0].title == 'I have you now'
	}
	
		
	def "List handles no results"() {
		
		given:
			fixtureLoader.load('scoobydoo', 'starwars')
			Tag theme = Tag.findByName('Scooby Doo')
			Tag eventType = Tag.findByName('Failure')
			
		when:
			List<Content> results = contentService.findAllEligibleContent(theme, eventType, ['audio/wma'])
		
		then:
			results.size() == 0
	}
	
	def "Reports supported content"() {
		
		given:
			Content content = new Content()
		
			ContentPlayer player = Mock(ContentPlayer)
			contentService.players = [player]
			
		when:
			def result = contentService.isSupported(content)
			
		then:
			result == true
			player.isSupported(content) >> true
	}
	
	
	def "Detecting supported content stops as soon as a supporting player is found"() {
		
		given:
			Content content = new Content()
		
			ContentPlayer player1 = Mock(ContentPlayer)
			ContentPlayer player2 = Mock(ContentPlayer)
			contentService.players = [player1, player2]
			
		when:
			def result = contentService.isSupported(content)
			
		then:
			result == true
			player1.isSupported(content) >> false
			player2.isSupported(content) >> true
	}
	
	def "Reports unsupported content"() {
		
		given:
			Content content = new Content()
		
			ContentPlayer player1 = Mock(ContentPlayer)
			ContentPlayer player2 = Mock(ContentPlayer)
			contentService.players = [player1, player2]
			
		when:
			def result = contentService.isSupported(content)
			
		then:
			result == false
			player1.isSupported(content) >> false
			player2.isSupported(content) >> false
	}
}
