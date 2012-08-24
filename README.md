Jinkies
===
Jinkies is an application for monitoring remote CI servers and notifying you
of important build events (i.e. breaks and fixes). Rather than send emails 
which may go unnoticed, Jinkies notifications are intended to be attention 
grabbing. This version of Jinkies only supports mp3 files. In future we 
hope to support other audio content as well as video, text to speech, lava lamps, 
firework displays and Mars landings.

One of the nice features of Jinkies is that it allows you to "theme" build 
jobs. In this way you can assign a theme (e.g. "The Simpsons" or 
"Scooby Doo" etc) to groups of related jobs, and play appropriate content 
from those themes when an event occurs.

Non CI related events can be posted to Jinkies over HTTP, so it's also
possible to use it to notify you of infrastructure related events such as 
backup failures or downed servers.

## Danger Will Robinson!!!
Installing Jinkies on a publicly accessible server is a **VERY** bad idea. Doing so 
would allow anyone to upload and execute programs on that machine. 

## <a id="quickStart"></a>Quick Start
1. Check the [system requirements](#system-requirements)
2. [Install Jinkies](#installation)
3. [Add one or more build jobs](#monitoring-ci-jobs)

## <a id="systemRequirements"></a>System Requirements
You will need a recent version of Java (1.6 should do) and a CSS3 / HTML5 compliant browser.
Jenkins (and possibly Hudson) are currently the only supported CI servers. You'll also 
need a computer capable of playing sound, on which to install Jinkies.

## <a id="installation"></a>Installation

### Option 1 - Executable War with default settings
1. Download the [executable binaries](http://www.jinkies.co.uk/binaries/executable/jinkies.war)
2. Run 'java -jar jinkies.war'
3. Give it a few moments to start then test it by opening [http://localhost:8080](http://localhost:8080)

### Option 2 - Executable War with custom port
1. Download the [executable binaries](http://www.jinkies.co.uk/binaries/executable/jinkies.war)
2. Create an [external configuration](#external-configuration) file and add the line 

    grails.serverURL = 'http://localhost:NNNN'
    
3. Run 'java -Dport=NNNN -jar jinkies.war'
3. Give it a few moments to start then test it by opening [http://localhost:NNNN](http://localhost:NNNN) 

### Option 3 - Deploying to an Application Server under the root context and port 8080
1. Download the [deployable binaries](http://www.jinkies.co.uk/binaries/deployable/jinkies.war)
2. Deploy the war file to your app server
3. Give it a few moments to start then test it by opening [http://localhost:8080](http://localhost:8080)

### Option 4 - Deploying to an Application Server using a custom context and/or different port
1. Download the [deployable binaries](http://www.jinkies.co.uk/binaries/deployable/jinkies.war)
2. Create an [external configuration](#external-configuration) file and add the line

    grails.serverURL = 'http://localhost:NNNN/context'
    
3. Deploy the war file to your app server
4. Give it a few moments to start then test it by opening [http://localhost:NNN/context](http://localhost:NNNN)

### Troubleshooting

* The first time you start Jinkies, it will take a little time to initialise it's database, please be patient. 

* The default java memory settings are quite stingy. If you get memory related exceptions try increasing the JVM memory via the -Xmx and -XX:MaxPermSize arguments.


## <a id="monitoringCiJobs"></a>Monitoring CI Jobs
You can add Jenkins jobs one by one, or add every job on a specific server. Either way
the process is the same...

1. Open the jobs page (e.g. http://jinkies-host:8080/jobs)
2. Click "Add Jenkins jobs..."
3. Enter a url (e.g. build.acuminous.meh:8080 or build.acuminous.meh:8080/job/jinkies)
4. Optionally specify a theme
5. Click "OK"

Jinkies is configured with some default content (thanks to <a href="http://www.freesound.org">freesound.org</a>), so you should start hearing notifications a few 
seconds after adding your first job. 

## <a id="creatingThemes"></a>Creating Themes & Uploading Theme Content
The Jinkies default content is pretty limited. We'd love to have shipped it with our 
Scooby Doo, Star Wars and Tron sound samples, but were too worried about copyright 
infringment. To compensate we've tried to make it easy to add your own...

1. Open the themes page (e.g. http://jinkies-host:8080/themes)
2. Click "Add content..."
3. Say where the content should be sourced from - your computer, the interwebs* or text input 
4. Select the file, enter the URL or type in the text
5. Optionally add a description
6. Enter themes the content is associated with (e.g. 'Scooby Doo')
7. Tick "Success" or "Failure", or specify a [custom event](#customEvent) type.
8. Click OK

Now whenever a job with this theme raises a matching event, this content becomes 
eligible for use. The actual content used in the notification is selected by random 
from all eligible content.

If instead of creating your own themes, you would like to add more content to the default theme, 
use 'Fallback' for the theme name

\* We like [http://www.rosswalker.co.uk/movie_sounds/](http://www.rosswalker.co.uk/movie_sounds/) 
although the wav files from this site need [some love](#mp3) before they will work.

## <a id="contentTypes"></a>Content Types
### <a id="mp3"></a>MP3
Right now Jinkies can only play mp3s. We're investigating reliable ways to play other file formats (wav, ogg & flac), 
but when it comes to java development we're much more familiar with the boring server side stuff, so it might be 
some time. For now we recommend converting your files to mp3 using [MediaIO](http://media.io) who
have a much better idea what they're doing.

### <a id="textToSpeech"></a>Text To Speech
We've taught Jinkies to read. If you upload a plain text file or type in the text directly, Jinkies will treat it as 
audio content and read it aloud. You can even reference variables and include any other valid <a href="http://groovy.codehaus.org/Groovy+Templates">Groovy Template</a> syntax.
For example:

    Build ${build.number} of project ${job.displayName} resulted in ${build.result}
    
The following variables are available when a success or failure event occur

 * build.number
 * build.project
 * build.result
 
 If an error occurs while checking a remote build server then 
 
 * job.displayName
 * type
 
 Or for [custom events](#customEvents)
 
 * target
 * type
 
 When previewing the text-to-speech function, the variables won't exist so Jinkies substitutes "var 1", "var 2", etc.  

### Other
Jinkies doesn't currently support any non-audio content. We're keen to add video and <a href="http://www.aviosys.com/9255.html">IP9255</a> support, 
but haven't got there yet. 

## <a id="proxyConfiguration"></a>Proxy Configuration
You can configure Jinkies to use a proxy server for HTTP and HTTPS traffic by following the 
standard [Java Networking and Proxies](http://docs.oracle.com/javase/6/docs/technotes/guides/net/proxies.html) configuration instructions.

## <a id="httpAuthentication"></a>HTTP Authentication
If your CI server requires authentication, then we recommend creating a 
read-only "jinkies" user on the CI server for this purpose. Currently the only
way to tell Jinkies to use a username & password is to embed them 
in the job URL, e.g. https://bob:secret@build\.yourcompany\.com

This is just about OK if your CI server uses HTTPS (because the URL 
will be encrypted), but weak if it's fronted by Apache running HTTPS 
(because your password will be in clear text between Apache and the app 
server), but totally insecure if you're not using HTTPS and could easily
lead to your server being pwnd if it's publicly accessible.
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
setup an [external configuration](#external-configuration) file, paste in the
contents of [QuartzConfig](./grails-app/conf/QuartzConfig.groovy) (see below),
and set the 'repeatInterval' to the desired number of milliseconds.

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

## <a id="schedulingNotifications"></a>Scheduling Notifications
We use Jinkies to tell everyone it's time for the daily stand-up. Someday we hope 
to build a nice UI to do this, but right now you need a bit of HTTP and a text editor.
To scheduling a notification, first setup an [external configuration](#external-configuration) file, then 
paste in the    following...

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

## <a id="customEvents">Custom Events</a>
If you want to use Jinkies to report other events, you need to POST a request to /api/event with the following parameters...

<table>
    <thead>
        <tr><th>Parameter Name</th><th>Purpose</th><th>Mandatory</th><th>Example</th></tr>     
    </thead>
    <tbody>
        <tr><td>target</td><td>Your identifier for the originator / owner / subject of this event</td><td>Yes</td><td>Floor 2</td></tr>
        <tr><td>event</td><td>The event type (set to any value you want)</td><td>Yes</td><td>Sandwich Trolley</td></tr>
        <tr><td>theme</td><td>Associate a theme with this event to help select appropriate content</td><td>No</td><td>Yogi Bear</td></tr>
        <tr><td>channel</td><td>Specify which channels the notification should be sent to</td><td>Yes</td><td>audio</td></tr>
        <tr><td>content</td><td>Instead of relying on a theme you can specify the content you would like to play. You will have find the "restId" of desired content by viewing the HTML on the content page.</td><td>No</td><td>content/123</td></tr>
    </tbody>
</table>

There must be at least one piece of content for the given event and theme (or 'Fallback' theme if you didn't specify one).

## <a id="developerNotes"></a>Developer Notes
STS complains about compilation errors in Spock tests that use the @Build annotation
immediately after a clean. Making a superficial change to the test causes it to be
rebuild and subsequently work. We're gradually phasing out the build-test-data plugin 
because of this and other problems.


## And Finally...

We hope you enjoy using Jinkies. Please do provide <a href="https://github.com/acuminous/jinkies/issues">feedback<a/> (especially the negative kind). 

The Jinkies development team.

