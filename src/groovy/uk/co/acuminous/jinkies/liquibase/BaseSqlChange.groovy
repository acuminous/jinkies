package uk.co.acuminous.jinkies.liquibase

import liquibase.FileOpener
import liquibase.change.custom.CustomSqlChange
import liquibase.database.Database
import liquibase.database.sql.InsertStatement
import liquibase.database.sql.RawSqlStatement
import liquibase.database.sql.SqlStatement
import liquibase.exception.CustomChangeException
import liquibase.exception.InvalidChangeDefinitionException
import liquibase.exception.SetupException
import liquibase.exception.UnsupportedChangeException

abstract class BaseSqlChange implements CustomSqlChange {

	FileOpener fileOpener
	
	@Override
	void setUp() throws SetupException {
	}
	
	@Override
	String getConfirmationMessage() {
		'OK'
	}
		
	@Override
	void validate(Database db) throws InvalidChangeDefinitionException {
	}

	
}
