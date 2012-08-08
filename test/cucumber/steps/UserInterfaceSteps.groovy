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
package steps

import pages.*
import modules.*

this.metaClass.mixin(cucumber.runtime.groovy.EN)

Map pages = [
	jobs: JobsPage
]

When (~'a user opens the (.*) page') { String pageName ->
	destination = pages[pageName]
	to destination
	at destination
}

Then (~'take the user back to the (.*) page') { String pageName ->
		
	waitFor { !page.dialog.displayed }
	
	destination = pages[pageName]
	at destination
}

Then (~'inform the user that "(.*)"') { String message ->	
	assert Alert.message == message 
}
