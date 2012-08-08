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

import groovy.util.logging.Slf4j;
import liquibase.FileOpener
import liquibase.database.Database
import liquibase.database.sql.InsertStatement
import liquibase.database.sql.SqlStatement
import liquibase.exception.CustomChangeException
import liquibase.exception.UnsupportedChangeException

@Slf4j
class InsertJob extends BaseSqlChange {

	String displayName
	String url
	String type	
	String theme
	
	@Override
	public SqlStatement[] generateStatements(Database db) throws UnsupportedChangeException, CustomChangeException {
		InsertStatement stmt= new InsertStatement('JINKIES', 'JOB')
		stmt.addColumnValue('DISPLAY_NAME', displayName)
		stmt.addColumnValue('URL', url)
		stmt.addColumnValue('TYPE', type)
		if (theme) {
			stmt.addColumnValue('THEME', theme)
		}
		stmt.addColumnValue('VERSION', 0)		
		[stmt]
	}
	
	String getData() {
		fileOpener.getResourceAsStream(path).bytes.encodeHex()
	}

}
