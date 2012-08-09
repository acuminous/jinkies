Jinkies
===
Jinkies is an application for monitoring remote CI servers and notifying you
of important build events (i.e. breaks and fixes). Rather than send emails 
which may go unnoticed, Jinkies notifications are intended to be attention 
grabbing. This version of Jinkies supports mp3 and wav files. In future we 
hope to support video, text to speech, lava lamps, firework displays and 
Mars landings.

One of the nice features of Jinkies is that it allows you to "theme" build 
jobs. In this way you can assign a theme (e.g. "The Simpsons" or 
"Scooby Doo" etc) to groups of related jobs, and play appropriate content 
from those themes when an event occurs.

Non CI related events can be posted to Jinkies over HTTP, so it's also
possible to use it to notify you of infrastructure related events such as 
backup failures or downed servers.

## <a id="quickStart"></a>Quick Start
1. Check the [system requirements](#system-requirements)
2. [Install Jinkies](#installation)
3. [Add one or more build jobs](#monitoring-ci-jobs)

## <a id="systemRequirements"></a>System Requirements
You will need a recent version of Java (1.6 should do) and a css3 / HTML5 compliant browser.
Jenkins (and possibly Hudson) are currently the only supported CI servers. You'll also 
need a computer capable of playing sound, on which to install Jinkies.

## <a id="installation"></a>Installation

### Option 1 - Executable War File (easy)
1. Download the executable Jinkies [binaries](http://www.jinkies.co.uk/binaries/executable/jinkies.war)
2. Run 'java -jar jinkies.war' (defaults to port 8080. Specify -Dport=NNNN to change)
3. Give it a few moments to start then test it by opening [http://localhost:8080](http://localhost:8080)

### Option 2 - Deployable War File (requires a Java application server)
1. Download the deployable Jinkies [binaries](http://www.jinkies.co.uk/binaries/deployable/jinkies.war)
2. Deploy it to your app server
3. Give it a few moments to start then navigate to wherever you deployed it

## <a id="monitoringCiJobs"></a>Monitoring CI Jobs
You can add Jenkins jobs one by one, or add every job on a specific server. Either way
the process is the same...

1. Open the jobs page (e.g. http://jinkies-host:8080/jobs)
2. Click "Add Jenkins jobs..."
3. Enter a url (e.g. build.acuminous.meh:8080 or build.acuminous.meh:8080/job/jinkies)
4. Optionally specify a theme
5. Click "OK"

Jinkies is configured with some default content, so as soon add jobs you should start 
hearing notifications. 

## <a id="creatingThemes"></a>Creating Themes & Uploading Theme Content
The Jinkies default content is pretty dull. We'd love to have shipped it with our 
Scooby Doo, Star Wars and Tron sound samples, but were too worried about copyright 
infringment. To compensate we've tried to make it easy to add your own...

1. Open the themes page (e.g. http://jinkies-host:8080/themes)
2. Click "Upload content..."
3. Decide whether you want to upload content from your computer, or from the interwebs*
4. Select the file or enter the URL
5. Optionally add a description
6. Enter themes the content is associated with (e.g. 'Scooby Doo')
7. Tick "Success" or "Failure", or specify a custom event.
8. Click OK

Now whenever a job with this theme raises a matching event, this content becomes 
eligible for use. The actual content used in the notification is selected by random 
from all eligible content.

If instead of creating your own themes, you would like to add more content to the default theme, 
use 'Fallback' for the theme name

\* We like [http://www.rosswalker.co.uk/movie_sounds/](http://www.rosswalker.co.uk/movie_sounds/) 
although the wav files form this site need [some love](#audio) before they will work.

## <a id="supportedFormats"></a>Supported File Formats
### <a id="audio"></a>Audio
Jinkies can play mp3s and some types of wav file. The standard java sound system limits
wav playback to files that use PCM compression and a sample rate of 11,025Hz or less. We're 
working to find a solution for this, but it's a difficult problem to solve. For now we 
recommend converting your wav files to mp3 using [MediaIO](http://media.io)

### Other
Jinkies doesn't currently support any non-audio content. We'd like to add video and text
(for text-to-speech), but haven't got there yet. 

## <a id="proxyConfiguration"></a>Proxy Configuration
You can configure Jinkies to use a proxy server for HTTP and HTTPS traffic by following the 
standard [Java Networking and Proxies](http://docs.oracle.com/javase/6/docs/technotes/guides/net/proxies.html) configuration instructions.

## <a id="httpAuthentication"></a>HTTP Authentication
If your CI server requires authentication, then we recommend creating a 
read-only "jinkies" user on the CI server for this purpose. Currently the only
way to specify a username / password Jinkies currently is to embed them 
in the job URL, e.g. https://bob:secret@build\.yourcompany\.com

This is just about OK if your CI server uses HTTPS (because the URL 
will be encrypted), but weak if it's fronted by Apache running HTTPS 
(because your password will be in clear text between Apache and the app 
server), and totally insecure if you're not using HTTPS and could easily
lead to your server being p'owned if it's publicly accessible.
(hint: Don't make your CI server publicly accessible without HTTPS!). 

If this is unacceptable in your environment, let us know and we'll consider an 
improved solution. If you can't wait then place to start is uk.co.acuminous.jinkies.HttpClientsFactory.groovy. 
We look forward to your [pull request](https://help.github.com/articles/using-pull-requests/).

## <a id="httpsConfiguration"></a>HTTPS/SSL Configuration
If you're attempting to download content or connect to a build server over HTTPS 
and the remote server is using a self signed certificate you'll need to tell the 
JVM running Jinkies to trust it by downloading the certificate and adding it to 
the [cacerts](http://docs.oracle.com/javase/6/docs/technotes/tools/solaris/keytool.html#cacerts) file using the java [keytool](http://docs.oracle.com/javase/6/docs/technotes/tools/solaris/keytool.html) program.

## <a id="externalConfiguration"></a>External Configuration
Jinkies is a [Grails](http://www.grails.org) application. As such it has a main 
configuration file called [Config.groovy](./grails-app/conf/Config.groovy) and some environment based 
overrides such as [development.groovy](./grails-app/conf/environment/development.groovy) and [production.groovy](./grails-app/conf/environment/development.groovy).

Jinkies will also attempt apply overrides from an external config file if such
a file exists. By default Jinkies will look for /etc/jinkies/config.groovy, but
you can change this by adding a -Djinkies.config=/path/to/config-file at startup.

Further information about Grails configuration can be found [here](http://grails.org/doc/latest/guide/conf.html).

##<a id="pollFrequency"></a>Changing The CI Server Poll Frequency
Jinkies is configured to poll all jobs jobs every 15 seconds. To change this
setup an [External Configuration](#external-configuration) file, then paste in the
contents of [QuartzConfig](./grails-app/conf/QuartzConfig.groovy) (see below).

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

Change the "repeatInterval" attribute from 15000 to the desired number of milliseconds to change the poll frequency.

## <a id="schedulingNotifications"></a>Scheduling Notifications
We use Jinkies to tell everyone it's time for the daily stand-up. Someday we hope 
to build a nice UI to do this, but right now you need a bit of HTTP and a text editor.
Scheduling a notification first setup an [External Configuration](#external-configuration) file, then paste in the 
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

Substitute 'projectX' with your project name, 'Scooby Doo' with your theme etc and give the trigger a valid cron expression 
that represents when your Stand-Up event will fire. You can find more information about cron expressions [here](http://quartz-scheduler.org/api/2.0.0/org/quartz/CronExpression.html) 

## Custom Events
If you want to use Jinkies to report other events, you need to POST a request to /api/event with the following parameters...

* target [e.g. Floor 2]
* event [e.g. Sandwich Trolley - there must be at least one piece of content associated with this event type.]
* theme [e.g. Yogi Bear]
* channel [e.g. audio]

## <a id="developerNotes"></a>Developer Notes
Many of the functional tests currently won't work in your environment.
This is because they expect a Jenkins server at http://build.acuminous.meh:8080 
to be running and configured with various jobs. At some point we'll get round to 
either creating a controller with stub responses or try running Rob Fletcher's 
[Betamax](http://freeside.co/betamax/) library between Jinkies and our build server. 
If you want to contribute to Jinkies and our laziness is causing inconvenience, 
let us know and I'll expedite this.

STS complains about compilation errors in Spock tests that use the @build annotation
immediately after a clean. Making a superficial change to the test causes it to be
rebuild and subsequently work. We're gradually phasing out the build-test-data plugin 
because of this and other problems.

Finally we hope you enjoy using Jinkies. Thanks for listening...

The Jinkies development team.

