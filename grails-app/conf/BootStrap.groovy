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

import org.quartz.Scheduler

import uk.co.acuminous.jinkies.content.*
import uk.co.acuminous.jinkies.json.CustomJsonMarshaller
import grails.converters.JSON

import uk.co.acuminous.jinkies.ci.Job
import uk.co.acuminous.jinkies.event.Event
import uk.co.acuminous.jinkies.event.EventService

class BootStrap {
	
	EventService eventService
	Scheduler quartzScheduler
	def grailsApplication
	
    def init = { servletContext ->
		
		JSON.registerObjectMarshaller(Job,CustomJsonMarshaller.jobWithLastEvent.curry(eventService) )
		JSON.registerObjectMarshaller(Content, CustomJsonMarshaller.content)
		JSON.registerObjectMarshaller(Tag, CustomJsonMarshaller.tag)
		JSON.registerObjectMarshaller(Event, CustomJsonMarshaller.event)

		if (grailsApplication.config.grails.plugin.quartz2.immediateStandby == true) {
			quartzScheduler.standby()
		}
		
		println "Jinkies has started successfully and can be accessed on ${grailsApplication.config.grails.serverURL}"
    }
	
    def destroy = {
    }
}
