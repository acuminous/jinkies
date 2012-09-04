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
import grails.plugin.quartz2.ClosureJob
import org.apache.log4j.Logger
import org.quartz.impl.triggers.CronTriggerImpl
import org.quartz.impl.triggers.SimpleTriggerImpl

grails.plugin.quartz2.jobSetup.jenkinsMonitor = { quartzScheduler, ctx ->

	def job = ClosureJob.createJob([concurrentExectionDisallowed: true]) { jobCtx , appCtx->
		try {
			appCtx.jenkinsMonitor.check()
		} catch (Throwable t) {
			Logger.getLogger('JenkinsMonitorJob').error('An error while handling Jenkins build events', t)
		}
	}

	def trigger = new SimpleTriggerImpl(
		name: 'Jenkins Monitor Trigger',
		startTime: new Date(),
		repeatInterval: 15000,
		repeatCount: -1
	)

	quartzScheduler.scheduleJob(job, trigger)
}

grails.plugin.quartz2.jobSetup.eventHouskeeper = { quartzScheduler, ctx ->
	
	def job = ClosureJob.createJob([concurrentExectionDisallowed: true]) { jobCtx , appCtx->
		try {
			 appCtx.eventHousekeeper().run()
		} catch (Throwable t) {
			Logger.getLogger('EventHousekeeperJob').error('An error while tidying up old events', t)
		}
	}

	def trigger = new CronTriggerImpl(
		name: 'Event Housekeeper Trigger',
		cronExpression: '0 0 3 * * ?' // 03:00 Daily
	)

	quartzScheduler.scheduleJob(job, trigger)
}
	

/* Uncomment and modify to schedule a daily stand-up notification
grails.plugin.quartz2.jobSetup.projectXStandup = { quartzScheduler, ctx ->
	
	def job = ClosureJob.createJob({ jobCtx , appCtx->
		try {
			Map params = [resourceId: 'project/x', theme: 'Scooby Doo', event: 'Stand-Up', channel: ['audio']]
			new HttpClientsFactory().getHttpBuilder('http://localhost:8080/api/event').post(body: params)
		} catch (Throwable t) {
			Logger.getLogger('ProjectXStandupJob').error('An error while sounding the Project X standup', t)
		}
	})

	def trigger = new CronTriggerImpl(
		name: 'ProjectX Stand-up Trigger',
		cronExpression: '0 30 9 ? * MON-FRI' // 09:30 Monday - Friday
	)

	quartzScheduler.scheduleJob(job, trigger)
}
*/