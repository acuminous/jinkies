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
grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.project.war.file = "target/${appName.toLowerCase()}.war"

grails.war.resources = { stagingDir ->
	copy(todir:"$stagingDir/WEB-INF/classes") {
		fileset(dir:'grails-app/environments', includes:'**/*')
	}
	copy(todir:"$stagingDir/WEB-INF/classes") {
		fileset(dir:'grails-app/migrations', includes:'**/*')
	}
}

grails.project.dependency.resolution = {
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "error"
    checksums true

    repositories {
        inherits true
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
		
		mavenRepo 'http://plugins.energizedwork.com'
    }
	
	def seleniumVersion = '2.23.1'
	
    dependencies {
		compile 'de.huxhorn.sulky:de.huxhorn.sulky.3rdparty.jlayer:1.0'
		compile('org.codehaus.groovy.modules.http-builder:http-builder:0.5.2') {
			excludes 'xalan'
			excludes 'xml-apis'
			excludes 'groovy'
		}
		test "org.codehaus.geb:geb-spock:0.7.0"
		test ("org.seleniumhq.selenium:selenium-htmlunit-driver:$seleniumVersion") {
			exclude "xml-apis"
		}
		test "org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion"
		test "org.seleniumhq.selenium:selenium-chrome-driver:$seleniumVersion"
		test "org.seleniumhq.selenium:selenium-support:$seleniumVersion"
		test 'com.github.robfletcher:betamax:1.0'
		test 'org.apache.httpcomponents:httpmime:4.1.3'
    }

    plugins {
		build ":tomcat:$grailsVersion"
		
        runtime ":hibernate:$grailsVersion"
        runtime ':jquery:1.7.1'
		runtime ':quartz2:0.2.3'		
				
		runtime ':resources:1.1.6'
		// runtime ':yui-minify-resources:0.1.5' currently broken
		runtime ':zipped-resources:1.0'
		runtime ':cached-resources:1.0'
		runtime ':cache-headers:1.1.5'
		runtime ':lesscss-resources:1.3.0.3'
		
		runtime(':liquibase:1.9.3.6') {
			exclude 'data-source'			
		}
						
		test ':spock:0.6'
		test ':geb:0.7.0'
		test ':fixtures:1.0.7'
		test ':build-test-data:2.0.2'
		test ':remote-control:1.2'
		test ':cucumber:0.6.0'
		test ":code-coverage:1.2.5"
    }
}
