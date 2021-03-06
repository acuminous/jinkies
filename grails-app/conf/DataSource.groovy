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
