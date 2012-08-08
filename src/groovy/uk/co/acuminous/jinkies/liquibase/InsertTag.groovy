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
