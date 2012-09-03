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
package uk.co.acuminous.jinkies.test

import groovy.sql.Sql
import javax.sql.DataSource
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.hibernate.SessionFactory
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes

class Nuke {

	void detonate() {
		sessionFactory.currentSession.flush()
		Sql sql = new Sql(dataSource)
		sql.execute 'UPDATE JINKIES.JOB SET THEME_ID = null'		
		['EVENT', 'CONTENT_TAG', 'TAG', 'CONTENT', 'JOB_CHANNEL', 'JOB'].each {
			sql.execute "DELETE JINKIES.$it".toString()
		}
		sessionFactory.currentSession.clear()
	}

	SessionFactory getSessionFactory() {
		applicationContext.getBean('sessionFactory')
	}
	
	DataSource getDataSource() {
		applicationContext.getBean('dataSource')
	}
	
	ApplicationContext getApplicationContext() {
		ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT);
	}
	
}
