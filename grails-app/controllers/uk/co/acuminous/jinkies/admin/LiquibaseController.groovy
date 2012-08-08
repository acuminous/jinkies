package uk.co.acuminous.jinkies.admin

import liquibase.database.*
import liquibase.diff.*
import java.sql.Connection

class LiquibaseController {

	def dataSource
	
	def index() {
		Connection connection = dataSource.connection
		
		DatabaseFactory factory = DatabaseFactory.getInstance()
		Database database = factory.findCorrectDatabaseImplementation(connection)
		database.setDefaultSchemaName('jinkies')
		
		Diff diff = new Diff(database, 'jinkies')
		DiffResult diffResult = diff.compare()
		diffResult.printChangeLog(new PrintStream(response.outputStream), database)
				
		response.contentType = 'text/xml'
		response.outputStream.flush()
	}
	
}
