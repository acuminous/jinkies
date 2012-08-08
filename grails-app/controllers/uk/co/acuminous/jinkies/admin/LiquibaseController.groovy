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
