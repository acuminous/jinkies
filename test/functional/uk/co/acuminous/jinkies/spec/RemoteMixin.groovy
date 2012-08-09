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
package uk.co.acuminous.jinkies.spec

import grails.plugin.remotecontrol.RemoteControl
import uk.co.acuminous.jinkies.test.Nuke


class RemoteMixin {

	RemoteControl remote = new RemoteControl(useNullIfResultWasUnserializable: true)
	
	void nuke() {
		remote {
			new Nuke().detonate()
		}
	}
	
	void data(Closure fixture) {
		remote.exec({ ctx.fixtureLoader }, fixture)
	}
	
	def remote(Closure closure) {
		remote.exec(closure)
	}
	
	static foo = {
		println "WTF"
	}
	
}