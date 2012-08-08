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
