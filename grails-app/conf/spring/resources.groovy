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
import uk.co.acuminous.jinkies.util.SpringApplicationContext
import uk.co.acuminous.jinkies.player.*

beans = {	
	
	springApplicationContext(SpringApplicationContext)
	
	def migrations = application.config.migrations	
	if (migrations?.enabled) {
		liquibase(MigrationRunner) { bean ->
			bean.initMethod = 'run'
			dataSource = ref('dataSource')
			dropAll = migrations.dropAll
		}
	}
	
	def betamax = application.config.betamax
	if (betamax?.enabled) {
		recorder(Recorder) {	
			proxyPort = betamax.proxyPort		
		}
	}
	
	httpClientsFactory(HttpClientsFactory) { 
		authConfig = application.config.http.auth	
	}
	
	contentService(ContentServiceFactoryBean) {
	}
	
	eventHistory(SimpleEventHistory) {		
	}
	
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

	jenkinsServer(JenkinsServer) {
		httpClientsFactory = ref('httpClientsFactory')
	}
	
	// Manually posted events enter here.
	'uk.co.acuminous.jinkies.event.EventController'(EventController) { bean ->
		bean.scope = 'prototype'
		bean.autowire = 'byName'
		eventHistory = ref('eventHistory')
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
	
	fallbackThemeApplier(FallbackThemeApplier) {
		nextHandler = ref('contentProposer')
	}
	
	contentProposer(ContentProposer) {
		contentService = ref('contentService')
		nextHandler = ref('noCandidateContentFilter')
	}
		
	noCandidateContentFilter(NoCandidateContentFilter) {
		nextHandler = ref('contentSelector')
	}
		
	contentSelector(RandomContentSelector) {
		nextHandler = ref('audioSwitch')
	}
				
	audioSwitch(ChannelSwitch) {
		channel = { Channel audio ->
			name = 'audio'
			players = [
				ref('mp3Player'),
			]
		}
		nextHandler = ref('terminator')
	}
		
	mp3Player(Mp3Player) {
	}

	terminator(EventTerminator) {		
	}
	
	
}
