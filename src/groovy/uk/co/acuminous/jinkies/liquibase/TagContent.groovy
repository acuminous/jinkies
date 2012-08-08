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

class TagContent extends BaseSqlChange {

	String content
	String themes	
	String events
	
	@Override
	public SqlStatement[] generateStatements(Database db) throws UnsupportedChangeException, CustomChangeException {
		List<SqlStatement> statements = []
		
		content.split(',').each { String content ->
			themes.split(',').each { String theme ->
				statements << createThemeInsertStatement(content.trim(), theme.trim())
			}
			events.split(',').each { String event ->
				statements << createEventInsertStatement(content.trim(), event.trim())
			}
		}
		
		return statements		
	}
	
	def createThemeInsertStatement = { String content, String theme ->
			String sql = """
INSERT INTO JINKIES.CONTENT_TAG (CONTENT_THEMES_ID, TAG_ID, THEMES_IDX) VALUES (
   (SELECT CONTENT.ID FROM JINKIES.CONTENT WHERE TITLE = '$content'), 
   (SELECT TAG.ID FROM JINKIES.TAG WHERE NAME = '$theme' AND TYPE = 'theme'),
   (SELECT COUNT(*) FROM JINKIES.CONTENT_TAG WHERE CONTENT_THEMES_ID = (SELECT CONTENT.ID FROM JINKIES.CONTENT WHERE TITLE = '$content'))
   
)"""
		new RawSqlStatement(sql)
	}
	
	def createEventInsertStatement = { String content, String event ->
		String sql = """
INSERT INTO JINKIES.CONTENT_TAG (CONTENT_EVENTS_ID, TAG_ID, EVENTS_IDX) VALUES (
(SELECT CONTENT.ID FROM JINKIES.CONTENT WHERE TITLE = '$content'),
(SELECT TAG.ID FROM JINKIES.TAG WHERE NAME = '$event' AND TYPE = 'event'),
(SELECT COUNT(*) FROM JINKIES.CONTENT_TAG WHERE CONTENT_EVENTS_ID = (SELECT CONTENT.ID FROM JINKIES.CONTENT WHERE TITLE = '$content'))

)"""
	new RawSqlStatement(sql)
}

}
