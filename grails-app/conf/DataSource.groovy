dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
    // loggingSql = true              
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
	default_schema = 'jinkies'
}

environments {
    development {
        dataSource {
            dbCreate = "create-drop"
            url = "jdbc:h2:mem:jinkies;MVCC=TRUE;INIT=CREATE SCHEMA IF NOT EXISTS JINKIES"
        }
    }
    test {
        dataSource {
            dbCreate = "create-drop"
            url = "jdbc:h2:mem:jinkies;MVCC=TRUE;INIT=CREATE SCHEMA IF NOT EXISTS JINKIES"
        }
    }
	migration {
		dataSource {
			url = "jdbc:h2:jinkies;MVCC=TRUE;INIT=CREATE SCHEMA IF NOT EXISTS JINKIES"			
		}
	}
	build {
		dataSource {
			url = "jdbc:h2:jinkies;MVCC=TRUE;INIT=CREATE SCHEMA IF NOT EXISTS JINKIES"
		}
	}
    production {
        dataSource {
            url = "jdbc:h2:jinkies;MVCC=TRUE;INIT=CREATE SCHEMA IF NOT EXISTS JINKIES"
            pooled = true
            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=true
               validationQuery="SELECT 1"
            }
        }
    }
}
