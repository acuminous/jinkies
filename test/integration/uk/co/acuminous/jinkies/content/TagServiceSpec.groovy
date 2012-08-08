package uk.co.acuminous.jinkies.content

import grails.plugin.spock.IntegrationSpec

class TagServiceSpec extends IntegrationSpec {

	def fixtureLoader
	TagService tagService
	
	def "Retrieves existing themes"() {
		
		given:
			fixtureLoader.load('build-events')
			
		when:
			List<Tag> results = tagService.findOrCreateThemes(['Scooby Doo', 'Star Wars'])
		
		then:		
			results.size() == 2		
			results.findAll { it.id == null }.isEmpty()			
			results[0].uri == 'theme/scooby-doo'
			results[1].uri == 'theme/star-wars'
	}
		
	def "Creates new themes"() {
		
		when:
			List<Tag> results = tagService.findOrCreateThemes(['Apple', 'Banana', 'Cherry'])
		
		then:
			results.size() == 3
			results.findAll { it.id == null }.isEmpty()
			results[0].uri == 'theme/apple'
			results[1].uri == 'theme/banana'
			results[2].uri == 'theme/cherry'
	}
		
	def "Handles a mixture of new and existing themes"() {
		
		given:
			fixtureLoader.load('build-events')
			
		when:
			List<Tag> results = tagService.findOrCreateThemes(['Apple', 'Banana', 'Scooby Doo', 'Star Wars'])
		
		then:
			results.size() == 4
			results[0].uri == 'theme/apple'
			results[1].uri == 'theme/banana'
			results[2].uri == 'theme/scooby-doo'
			results[3].uri == 'theme/star-wars'
	}
	
	
	def "Given themes order is maintained"() {
		
		given:
			fixtureLoader.load('build-events')
			
		when:
			List<Tag> results = tagService.findOrCreateThemes(['Scooby Doo', 'Apple', 'Banana', 'Star Wars'])
		
		then:
			results.size() == 4
			results[0].uri == 'theme/scooby-doo'
			results[1].uri == 'theme/apple'
			results[2].uri == 'theme/banana'
			results[3].uri == 'theme/star-wars'
	}
	
	
	def "Dedupes themes"() {
		
		when:
			List<Tag> results = tagService.findOrCreateThemes(['Apple', 'Banana', 'Apple'])
	
		then:
			results.size() == 2
			results[0].uri == 'theme/apple'
			results[1].uri == 'theme/banana'
	}
	
	def "Searches for existing themes by uri to avoid unique constraint violations"() {
		
		given:
			Tag tag = new Tag('Apple', TagType.theme).save(flush:true)
			
		when:
			List<Tag> results = tagService.findOrCreateThemes(['APPLE'])
		
		then:
			results.size() == 1
			results[0].is(tag)
	}	
	
	def "Retrieves existing event types"() {
		
		given:
			fixtureLoader.load('build-events')
			
		when:
			List<Tag> results = tagService.findOrCreateEvents(['Success', 'Failure'])
		
		then:
			results.size() == 2
			results.findAll { it.id == null }.isEmpty()
			results[0].uri == 'event/success'
			results[1].uri == 'event/failure'
	}
		
	def "Creates new event types"() {
		
		when:
			List<Tag> results = tagService.findOrCreateEvents(['Apple', 'Banana', 'Cherry'])
		
		then:
			results.size() == 3
			results.findAll { it.id == null }.isEmpty()
			results[0].uri == 'event/apple'
			results[1].uri == 'event/banana'
			results[2].uri == 'event/cherry'
	}
	
	def "Dedupes events types"() {
		
		when:
			List<Tag> results = tagService.findOrCreateEvents(['Apple', 'Banana', 'Apple'])
	
		then:
			results.size() == 2
			results[0].uri == 'event/apple'
			results[1].uri == 'event/banana'
	}
}
