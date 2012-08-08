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
