<a id="jinkies"/>Jinkies
=======

Jinkies is an application for monitoring remote CI servers and notifying you
of important build events (i.e. breaks and fixes). Rather than send emails 
which may go unnoticed, Jinkies notifications are intended to be attention 
grabbing. This version of Jinkies supports mp3 and wav files. In future we 
hope to support video, text to speech, lava lamps and even USB rocket 
launchers are planned.

One of the nice features of Jinkies is that it allows you to "theme" build 
jobs. In this way you can assign a theme (e.g. "The Simpsons" or 
"Scooby Doo" etc) to groups of related jobs, and play appropriate content 
from those themes when an event occurs.

Non CI related events can be posted to Jinkies over HTTP, so it's also
possible to use it to notify you of infrastructure related events for 
example.

<a id="systemRequirements"/>System Requirements
-------------------
You will need a recent version of Java (1.6 should do) and a css3 / HTML5 compliant browser.
Jenkins (and possibly Hudson) are currently the only supported CI servers. You'll also 
need a computer capable of playing sound, on which to install Jinkies.

<a id="installation"/>Installation
------------
TODO

<a id="monitoringCiJobs"/>Monitoring CI Jobs
------------------
You can add Jenkins jobs one by one, or add every job on a specific server. Either way
the process is the same...

1. Open the jobs page
2. Click "Add Jenkins jobs..."
3. Enter a url (e.g. build.acuminous.meh:8080 or build.acuminous.meh:8080/job/jinkies)
4. Optionally specify a theme
5. Click "OK"

Jinkies is configured with some default content, so as soon add jobs you should start 
receiving notifications. 

<a id="creatingThemes"/>Creating Themes & Uploading Theme Content
-----------------------------------------
The Jinkies default content is pretty dull. We'd love to have shipped it with our 
Scooby Doo, Star Wars and Tron sound samples, but were too worried about copyright 
infringment. To compensate we've tried to make it easy to add your own...

1. Open the themes page
2. Click "Upload content..."
3. Decide whether you want to upload content from your computer, or from the interwebs*
4. Select a file or enter a URL
5. Optionally add a description
6. Enter the themes the content is associated with
7. Tick "Success" or "Failure", or specify a custom event.
8. Click OK

Now whenever a job with this theme raises a matching event, this content becomes 
eligible to use in the notification.

\* We like [http://www.rosswalker.co.uk/movie_sounds/](http://www.rosswalker.co.uk/movie_sounds/)

<a id="supportedFormats"/>Supported File Formats

Audio
Jinkies can play mp3s and some types of wav file. The standard java sound system limits
wav playback to files that use PCM compression and a sample rate of 11025Hz or less. We're 
working to find a solution for this, but it's a difficult problem to solve. For now we 
recommend converting your wav files to mp3 using [MediaIO](http://media.io)

Other
Jinkies doesn't currently support any non-audio content. We'd like to add video and text
(for text-to-speech), but haven't got their yet. 

<a id="proxyConfiguration"/>Proxy Configuration
-------------------
You can configure Jinkies to use a proxy server for HTTP and HTTPS traffic by following the 
standard [Java Networking and Proxies](http://docs.oracle.com/javase/6/docs/technotes/guides/net/proxies.html) configuration instructions

<a id="httpAuthentication"/>HTTP Authentication
-------------------
If your CI server requires authentication, then we recommend creating a 
read-only "jinkies" user on the CI server for this purpose. Currently the only
way to specify a username / password Jinkies currently is to embed them 
in the job URL, e.g. https://bob:secret@build.yourcompany.com

This is just about OK if your CI server uses HTTPS (because the URL 
will be encrypted), but weak if it's fronted by Apache running HTTPS 
(because your password will be in clear text between Apache and the app 
server), and totally insecure if you're not using HTTPS and could easily
lead to your server being p'owned if it's publicly accessible.
(hint: Don't make your CI server publicly accessible without HTTPS!). 

If this is unacceptable in your environment, let us know and we'll consider   
an improved solution. If you can't wait then place to start is 
uk.co.acuminous.jinkies.HttpClientsFactory.groovy. We look forward to your 
pull request.

<a id="httpsConfiguration"/>HTTPS/SSL Configuration
-----------------------
If you're attempting to download content or connect to a build server over HTTPS 
and the remote server is using a self signed certificate you'll need to tell the 
JVM running Jinkies to trust it by downloading the certificate and adding it to 
the [cacerts](http://docs.oracle.com/javase/6/docs/technotes/tools/solaris/keytool.html#cacerts) file using the java [keytool](http://docs.oracle.com/javase/6/docs/technotes/tools/solaris/keytool.html) program.

<a id="externalConfiguration"/>External Configuration
----------------------
Jinkies is a [Grails](http://www.grails.org) application. As such it has a main 
configuration file called [Config.groovy](http://www.github.com/acuminous/jinkies/grails-app/conf/Config.groovy) and some environment based 
overrides such as [development.groovy](http://www.github.com/acuminous/jinkies/grails-app/conf/environment/development.groovy) and [production.groovy](http://www.github.com/acuminous/jinkies/grails-app/conf/environment/development.groovy).

Jinkies will also attempt apply overrides from an external config file if such
a file exists. By default Jinkies will look for /etc/jinkies/config.groovy, but
you can change this by adding a -Djinkies.config=/path/to/configfile at startup.

<a id="pollFrequency"/>Changing The CI Server Poll Frequency
-------------------------------------
Jinkies is configured to poll all jobs jobs every 15 seconds. To change this
setup an [External Configuration](#externalConfiguration) file, then paste in the
contents of [QuartzConfig](http://www.github.com/acuminous/jinkies/grails-app/conf/QuartzConfig.groovy)

	import org.quartz.impl.triggers.CronTriggerImpl
	import grails.plugin.quartz2.ClosureJob
	import groovyx.net.http.HTTPBuilder
	import org.quartz.impl.triggers.SimpleTriggerImpl
	
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

Change the "repeatInterval" attribute to the desired number of milliseconds

<a id="schedulingNotifications"/>Scheduling Notifications
------------------------
We use Jinkies to tell everyone it's time for the daily stand-up. Someday we hope 
to build a nice UI to do this, but right now you need a bit of HTTP and a text editor.
Scheduling a notification is very similar to changing the [CI Server Poll Frequency](#pollFrequency) [First setup an External Configuration](externalConfiguration) file, then paste in the 
following...

	import org.quartz.impl.triggers.CronTriggerImpl 
	import grails.plugin.quartz2.ClosureJob
	import uk.co.acuminous.jinkies.util.HttpClientsFactory

	grails.plugin.quartz2.jobSetup.projectXStandup = { quartzScheduler, ctx ->
	
		def job = ClosureJob.createJob({ jobCtx , appCtx->
			Map params = [target: 'projectX', theme: 'Scooby Doo', event: 'Stand-Up', channel: ['audio']]
			new HttpClientsFactory().getHttpBuilder('http://localhost:8080/api/event').post(body: params)
		}
	
		def trigger = new CronTriggerImpl(
			name: 'ProjectX Stand-up Trigger',
			cronExpression: '0 30 9 ? * MON-FRI'
		)
	
		quartzScheduler.scheduleJob(job, trigger)
	}

Then go about substituting 'projectX' with your project name, 'Scooby Doo' with your theme etc.
You can find more information about cron expressions [here](http://quartz-scheduler.org/api/2.0.0/org/quartz/CronExpression.html) 

Developer Notes
---------------
Many of the functional tests currently won't work in your environment.
This is because they expect a Jenkins server at http://build.acuminous.meh:8080 
to be running and configured with various jobs. At some point we'll get round to 
either creating a controller with stub responses or try running Rob Fletcher's 
[Betamax](http://freeside.co/betamax/) library between Jinkies and the build server. 
If you want to contribute to Jinkies and our laziness is causing inconvenience, 
let us know and I'll expedite this.