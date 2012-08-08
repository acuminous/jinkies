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
class InsertContent extends BaseSqlChange {

	String title
	String type
	String path
	String description
	
	@Override
	public SqlStatement[] generateStatements(Database db) throws UnsupportedChangeException, CustomChangeException {
		InsertStatement stmt= new InsertStatement('JINKIES', 'CONTENT')
		stmt.addColumnValue('TITLE', title)
		stmt.addColumnValue('TYPE', type)
		stmt.addColumnValue('FILENAME', path.split('/')[-1])
		if (description) {
			stmt.addColumnValue('DESCRIPTION', description)
		}
		stmt.addColumnValue('BYTES', data)
		stmt.addColumnValue('VERSION', 0)
		[stmt]
	}
	
	String getData() {
		fileOpener.getResourceAsStream(path).bytes.encodeHex()
	}

}
