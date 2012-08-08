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
