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
import liquibase.database.sql.RawSqlStatement
import liquibase.database.sql.SqlStatement
import liquibase.exception.CustomChangeException
import liquibase.exception.UnsupportedChangeException

class AssignChannels extends BaseSqlChange {

	String jobs
	String channels
		
	
	@Override
	public SqlStatement[] generateStatements(Database db) throws UnsupportedChangeException, CustomChangeException {
		List<SqlStatement> statements = []
		
		jobs.split(',').each { String job ->
			channels.split(',').each { String channel ->
				statements << createInsertStatement(job.trim(), channel.trim())
			}
		}
		
		return statements		
	}
	
	def createInsertStatement = { String job, String channel ->
			String sql = """
INSERT INTO JINKIES.JOB_CHANNEL (JOB_ID, CHANNEL, CHANNELS_IDX) VALUES (
   (SELECT JOB.ID FROM JINKIES.JOB WHERE DISPLAY_NAME = '$job'), 
   '$channel',
   (SELECT COUNT(*) FROM JINKIES.JOB_CHANNEL WHERE JOB_ID = (SELECT JOB.ID FROM JINKIES.JOB WHERE DISPLAY_NAME = '$job'))   
)"""
			new RawSqlStatement(sql)
		}

}
