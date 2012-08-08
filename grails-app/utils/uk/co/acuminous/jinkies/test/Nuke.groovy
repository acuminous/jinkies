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
		['CONTENT_TAG', 'TAG', 'CONTENT', 'JOB_CHANNEL', 'JOB'].each {
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
