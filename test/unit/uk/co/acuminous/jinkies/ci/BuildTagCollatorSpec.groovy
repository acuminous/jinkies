package uk.co.acuminous.jinkies.ci

import spock.lang.*
import uk.co.acuminous.jinkies.event.EventHandler
import uk.co.acuminous.jinkies.content.*

import grails.buildtestdata.mixin.Build as BuildMixin

@BuildMixin(Tag)
class BuildTagCollatorSpec extends Specification {

	EventHandler nextHandler = Mock(EventHandler)
	BuildTagCollator collator = new BuildTagCollator(nextHandler: nextHandler)
	
	def "Coerces build attributes into the event"() {
		
		given:
			Tag eventType = new Tag('Success', TagType.event).save(flush:true) 
			Tag theme = new Tag('Scooby Doo', TagType.theme).save(flush:true)
			 
			Job job = new Job()
			job.theme = theme
			job.channels = ['audio', 'www']
			
			Build build = new BuildBuilder().build(job: job, result: eventType.name)
						
			Map event = [build: build]
		when:
			collator.handle(event)
			
		then:
			event.theme == theme
			event.type == eventType
			event.channels == job.channels
			1 * nextHandler.handle(event)			
	}
}
