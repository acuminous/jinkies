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
import betamax.Recorder
import uk.co.acuminous.jinkies.ci.*
import uk.co.acuminous.jinkies.channel.*
import uk.co.acuminous.jinkies.content.*
import uk.co.acuminous.jinkies.event.*
import uk.co.acuminous.jinkies.jenkins.*
import uk.co.acuminous.jinkies.liquibase.MigrationRunner;
import uk.co.acuminous.jinkies.spring.*;
import uk.co.acuminous.jinkies.util.HttpClientsFactory
import uk.co.acuminous.jinkies.player.*

// Please note: You cannot have multiple anonymous inner bean of the same class with a collection property 
// See http://jira.grails.org/browse/GRAILS-8830
beans = {	
			
/******************************************************************************
 * Infrastructure 
 *****************************************************************************/
	
	def betamaxConfig = application.config.betamax
	if (betamaxConfig?.enabled) {
		recorder(Recorder) {
			proxyPort = betamaxConfig.proxyPort
		}
	}
		
	def migrationsConfig = application.config.migrations	
	if (migrationsConfig?.enabled) {
		liquibase(MigrationRunner) { bean ->
			bean.initMethod = 'run'
			dataSource = ref('dataSource')
			dropAll = migrationsConfig.dropAll
		}
	}
	
/******************************************************************************
 * Managed Beans
 *****************************************************************************/   

	httpClientsFactory(HttpClientsFactory) {
		authConfig = application.config.http.auth
	}
	
	jenkinsServer(JenkinsServer) {
		httpClientsFactory = ref('httpClientsFactory')
	}
	
	contentService(ContentServiceFactoryBean)

	eventHistory(SimpleEventHistory)
	
	expiringEventHistory(ExpiringEventHistory) {
		underlyingEventHistory = ref('eventHistory')
		maxAgeInMillis = 60 * 60 * 1000				
	}

	errorHistoryFilter(EventHistoryReadFilter) {
		underlyingEventHistory = ref('expiringEventHistory')
		allowedEvents = ['Error']
	}
	
	successHistoryFilter(EventHistoryReadFilter) {
		underlyingEventHistory = ref('eventHistory')
		allowedEvents = ['Success']
	}

/******************************************************************************
 * Players
 *****************************************************************************/
	
	mp3Player(Mp3Player) { }
	
	speechSynthesizer(GroovyTemplatePlayerAdapter) {
		player = { SpeechSynthesizer player ->
		}
	}

/******************************************************************************
 * Channels
 *****************************************************************************/
   
	audioChannel(ContentChannel) {
		name = 'audio'
		players = [
			ref('mp3Player')
		]
	}
	
	speechChannel(ContentChannel) {
		name = 'speech'
		players = [
			ref('speechSynthesizer')
		]
	}
	
	def testChannelConfig = application.config.jinkies.testChannel	
	testChannel(TestChannel) {
		name = 'test'
		enabled = testChannelConfig.enabled
	}
			
/******************************************************************************
 * Workflow
 *****************************************************************************/
   	
	// Manually posted events enter here.
	'uk.co.acuminous.jinkies.event.EventController'(EventController) { bean ->
		bean.scope = 'prototype'
		bean.autowire = 'byName'
		eventHandler = ref('eventHistoryUpdater')
	}
	
	// Jenkins build events enter here.
	jenkinsMonitor(JenkinsMonitor) {
		server = ref('jenkinsServer')
		errorHandler = ref('consecutiveErrorFilter')
		eventHandler = ref('repeatedBuildFilter')
	}
	
	consecutiveErrorFilter(ConsecutiveEventFilter) {
		eventHistory = ref('errorHistoryFilter')
		nextHandler = ref('eventHistoryUpdater')
	}
	
	repeatedBuildFilter(RepeatedBuildFilter) {
		nextHandler = ref('jenkinsBuildRetriever')
	}
		
	jenkinsBuildRetriever(JenkinsBuildRetriever) {
		server = ref('jenkinsServer')
		nextHandler = ref('buildTagCollator')		
	}

	buildTagCollator(BuildTagCollator) {
		nextHandler = ref('consecutiveSuccessFilter')
	}
		
	consecutiveSuccessFilter(ConsecutiveEventFilter) {
		eventHistory = ref('successHistoryFilter')
		nextHandler = ref('eventHistoryUpdater')
	}
	
	eventHistoryUpdater(EventHistoryUpdater) {
		eventHistory = ref('expiringEventHistory')
		nextHandler = ref('channelIterator')		
	}
	
	// The channel iterator repeats the remainder of the workflow for each eligible channel
	channelIterator(ChannelIteratorFactoryBean) {			
		nextHandler = ref('fallbackThemeApplier')			
	}
	
	// >>>>> Repeat per channel >>>>>
	
		fallbackThemeApplier(FallbackThemeApplier) {
			nextHandler = ref('contentProposer')
		}

		contentProposer(ContentProposer) {
			contentService = ref('contentService')
			nextHandler = ref('contentSelector')
		}
			
		contentSelector(RandomContentSelector) {
			nextHandler = ref('audioSwitch')
		}				
		
		audioSwitch(ChannelSwitch) {
			channel = ref('audioChannel')
			nextHandler = ref('speechSwitch')
		}
		
		speechSwitch(ChannelSwitch) {
			channel = ref('speechChannel')
			nextHandler = ref('testSwitch')
		}
					
		testSwitch(ChannelSwitch) {
			channel = ref('testChannel')
			nextHandler = ref('terminator')
		}
			
		terminator(EventTerminator)
	
	// <<<<< Repeat per channel <<<<<
}
