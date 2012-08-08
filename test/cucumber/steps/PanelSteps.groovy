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

When (~'deletes (.*)') { String displayName ->
	page.widget(displayName).delete()
}

When (~'edits (.*)') { String displayName ->
	page.widget(displayName).update()
	waitFor { page.dialog.displayed }
}

Then (~'remove (.*) details from display') { String displayName ->
	waitFor { page.widget(displayName).empty }
}

panelToLoad = {
	page.widgets.size() > 0
}