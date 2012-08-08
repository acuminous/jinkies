package uk.co.acuminous.jinkies.liquibase

import liquibase.FileOpener
import liquibase.Liquibase
import liquibase.database.Database
import liquibase.database.DatabaseFactory
import liquibase.ClassLoaderFileOpener
import liquibase.FileSystemFileOpener
import groovy.util.logging.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection
import java.util.Enumeration;

import javax.sql.DataSource
import org.springframework.core.io.ClassPathResource

@Slf4j
class MigrationRunner {
		
	DataSource dataSource
	String changelog = 'changelog.xml'
	boolean dropAll = false
	
    void run() {
		
        Liquibase liquibase = null
        try {
            Connection connection = dataSource.connection
            
            FileOpener fileOpener = new BeltAndBracesFileOpener()

            Database database = DatabaseFactory.instance.findCorrectDatabaseImplementation(connection)
            database.defaultSchemaName = connection.catalog
			
            liquibase = new Liquibase(changelog, fileOpener, database);
			
			if (dropAll) {
				log.info("Dropping database")
				liquibase.dropAll()
			}
			
			log.info("Updating database")
            liquibase.update(null)
        }
        finally {
            if (liquibase && liquibase.database) {
                liquibase.database.close()
            }
        }
    }
}

class BeltAndBracesFileOpener extends ClassLoaderFileOpener {
	
	def InputStream getResourceAsStream(String filename) {		
		try {
			new ClassPathResourceFileOpener().getResourceAsStream(filename)
		} catch (Exception e) {
			new FileSystemResourceFileOpener().getResourceAsStream(filename)
		}
	}	
}

class FileSystemResourceFileOpener extends FileSystemFileOpener {
	
	def InputStream getResourceAsStream(String filename) {
		File file = new File("grails-app/migrations/", filename)
		return file.exists() ? new FileInputStream(file) : null
	}	
}

class ClassPathResourceFileOpener extends ClassLoaderFileOpener {

    def InputStream getResourceAsStream(String filename) {
		new ClassPathResource(filename).inputStream
    }
}
