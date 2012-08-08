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
