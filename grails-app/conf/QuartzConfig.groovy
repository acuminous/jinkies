import org.quartz.impl.triggers.CronTriggerImpl
import grails.plugin.quartz2.ClosureJob
import org.quartz.impl.triggers.SimpleTriggerImpl
import uk.co.acuminous.jinkies.util.HttpClientsFactory

grails.plugin.quartz2.jobSetup.jenkinsMonitor = { quartzScheduler, ctx ->

	def job = ClosureJob.createJob([concurrentExectionDisallowed: true]) { jobCtx , appCtx->
		appCtx.jenkinsMonitor.check()
	}

	def trigger = new SimpleTriggerImpl(
		name: 'Jenkins Monitor Trigger',
		startTime: new Date(),
		repeatInterval: 15000,
		repeatCount: -1
	)

	quartzScheduler.scheduleJob(job, trigger)
}

/* Uncomment and modify to schedule a daily stand-up notification*/
grails.plugin.quartz2.jobSetup.projectXStandup = { quartzScheduler, ctx ->
	
	def job = ClosureJob.createJob({ jobCtx , appCtx->
		Map params = [target: 'projectX', theme: 'Scooby Doo', event: 'Stand-Up', channel: ['audio']]
		new HttpClientsFactory().getHttpBuilder('http://localhost:8080/api/event').post(body: params)
	})

	def trigger = new CronTriggerImpl(
		name: 'ProjectX Stand-up Trigger',
		cronExpression: '0 30 9 ? * MON-FRI' // 09:30 Monday - Friday
	)

	quartzScheduler.scheduleJob(job, trigger)
}