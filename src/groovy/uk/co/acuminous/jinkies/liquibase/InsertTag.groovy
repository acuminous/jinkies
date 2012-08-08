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
package uk.co.acuminous.jinkies.liquibase

import uk.co.acuminous.jinkies.util.UriBuilder
import liquibase.FileOpener
import liquibase.database.Database
import liquibase.database.sql.InsertStatement
import liquibase.database.sql.SqlStatement
import liquibase.exception.CustomChangeException
import liquibase.exception.UnsupportedChangeException

class InsertTag extends BaseSqlChange {

	String names
	String type
		
	@Override
	public SqlStatement[] generateStatements(Database db) throws UnsupportedChangeException, CustomChangeException {
		
		List<SqlStatement> statements = []		
		
		names.split(',').each { String name ->
			statements << createInsertStatement(name.trim())
		}
		
		statements
	}
	
	private SqlStatement createInsertStatement(String name) {
		InsertStatement stmt = new InsertStatement('JINKIES', 'TAG')
		stmt.addColumnValue('NAME', name)
		stmt.addColumnValue('TYPE', type)
		stmt.addColumnValue('URI', (type + '/' + new UriBuilder().toUri(name)))
		stmt.addColumnValue('VERSION', 0)
		stmt
	}

}
