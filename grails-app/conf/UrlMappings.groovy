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
class UrlMappings {

	static mappings = {
		
		"/api/$controller"(parseRequest: true) {
			action = [GET:"list", POST:"create"]
			constraints {
				controller(in: ['content', 'tag', 'job', 'event'])
			}
		}
			
		"/api/$controller/$id"(parseRequest: true) {
			action = [GET:"show", DELETE:"delete", PUT:"update"]
			constraints {
				id(matches:/\d+/)
				controller(in: ['content', 'tag', 'job', 'event'])
			}
		}
		
		"/api/content/$id/data" {
			controller = 'content'
			action = [GET:'download', POST:'upload'] // 'PUT' doesn't seem to work for file uploads
		}
		
		"/api/jenkins"(controller: 'Jenkins') {
			action = [GET: "list"]
		}

		
		"/$controller/$title" {
			action = [GET:"index"]
			constraints {
				controller(in: ['audio', 'video',])
			}
		}
		
		"/" {
			controller = 'UserInterface'
		}
		
		"/$action" {
			controller = 'UserInterface'
			constraints {
				action(in: ['jobs', 'content', 'schedule'])
			} 
		}
		
		"/admin/liquibase/$action?"(controller: "liquibase")
		"/admin/manage/$action?"(controller: "adminManage")		

		"/$controller/$action?/$id?" {}
								
		"500"(view:'/error')
		"404"(view:'/not-found')
	}
}
