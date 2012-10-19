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
import grails.util.Environment

String environmentName = Environment.current.name.toLowerCase()
println "Using ${environmentName} configuration"

grails.config.locations = [

	// Configuration for grails run-app
	"file:grails-app/environments/${environmentName}.groovy",
		
	// Configuration for deployed war
	"classpath:${environmentName}.groovy",

	// Quartz Scheduler Config
	QuartzConfig,
	
	// External configuration options (useful for overrides)
	System.properties['jinkies.config'] ? "file:${System.properties['jinkies.config']}" : "file:/etc/jinkies/config.groovy"
]

grails.project.groupId = appName
grails.mime.file.extensions = true
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']


// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"

// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true

// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true

// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// enable query caching by default
grails.hibernate.cache.queries = true

grails.app.context = '/'

grails.gorm.failOnError=false

grails.logging.jul.usebridge = true

grails.plugin.quartz2.autoStartup = false


log4j = {

	appenders {

		console name: 'stdout'
		'null' name: 'file'
		
		environments {
			
			production {
				rollingFile name: 'file', file: "jinkies.log", maxFileSize:'1MB', layout: pattern(conversionPattern: '%d{ISO8601} [%t] %p %c %m%n')
				'null' name: 'stacktrace'
				'null' name: 'stdout'
			}
		}
		
	}

	root {
		warn 'stdout', 'file'
	}

	error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
		   'org.codehaus.groovy.grails.web.pages', //  GSP
		   'org.codehaus.groovy.grails.web.sitemesh', //  layouts
		   'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
		   'org.codehaus.groovy.grails.web.mapping', // URL mapping
		   'org.codehaus.groovy.grails.commons', // core / classloading
		   'org.codehaus.groovy.grails.plugins', // plugins
		   'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
		   'org.springframework',
		   'org.hibernate',
		   'net.sf.ehcache.hibernate'
		
	environments {
	   development {
		   debug 'uk.co.acuminous.jinkies'
	   }   		
	}	   
}

migrations.enabled = false
migrations.dropAll = false

grails.resources.debug = false
grails.resources.modules = {
	core {
		dependsOn 'jquery'
		dependsOn '960'
		dependsOn 'modal'
		dependsOn 'classy'		
		resource url:'/css/error.less', attrs:[rel: "stylesheet/less", type:'css']	
		resource url:'/css/comic.less', attrs:[rel: "stylesheet/less", type:'css']
		resource url:'/css/dialog.less', attrs:[rel: "stylesheet/less", type:'css']
		resource url:'/js/common/baseline.js', attrs:[type:'js']
		resource url:'/js/common/errors.js', attrs:[type:'js']
		resource url:'/js/common/panel.js', attrs:[type:'js']
		resource url:'/js/common/dialog.js', attrs:[type:'js']
		resource url:'/js/common/widget.js', attrs:[type:'js']
	}
	'960' {
		resource url:'/css/960.css'
	}
	modal {
		resource url:'/js/simplemodal/jquery.simplemodal-1.4.2.js', attrs:[type:'js']
	}
	fileUpload {
		resource url:'/js/fileupload/jquery.ui.widget.js', attrs:[type:'js']
		resource url:'/js/fileupload/jquery.iframe-transport.js', attrs:[type:'js']
		resource url:'/js/fileupload/jquery.fileupload.js', attrs:[type:'js']
	}
	classy {
		resource url:'/js/classy/classy-1.4.js', attrs:[type:'js']
	}
	splash {
		dependsOn 'core'
		resource url:'/css/splash.less', attrs:[rel: "stylesheet/less", type:'css']
	}
	about {
		dependsOn 'core'
		resource url:'/css/about.less', attrs:[rel: "stylesheet/less", type:'css']
	}
	jobs {
		dependsOn 'core'
		resource url:'/js/job/job-init.js', attrs:[type:'js']
		resource url:'/js/job/job-status-checker.js', attrs:[type:'js']
		resource url:'/js/job/job-panel.js', attrs:[type:'js']
		resource url:'/js/job/job-api.js', attrs:[type:'js']
		resource url:'/js/job/job-widget.js', attrs:[type:'js']
		resource url:'/js/job/job-dialog.js', attrs:[type:'js']
	}
	content {
		dependsOn 'core'
		dependsOn 'classy'
		dependsOn 'fileUpload'
		resource url:'/js/content/content-init.js', attrs:[type:'js']
		resource url:'/js/content/content-panel.js', attrs:[type:'js']
		resource url:'/js/content/content-api.js', attrs:[type:'js']
		resource url:'/js/content/content-widget.js', attrs:[type:'js']
		resource url:'/js/content/content-dialog.js', attrs:[type:'js']		
	}
}

jinkies.splash.enabled = false
jinkies.testChannel.enabled = false