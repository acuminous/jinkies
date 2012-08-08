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
