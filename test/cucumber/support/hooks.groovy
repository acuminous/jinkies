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
package support

import geb.binding.BindingUpdater
import geb.Browser

this.metaClass.mixin (cucumber.runtime.groovy.Hooks)
this.metaClass.mixin(uk.co.acuminous.jinkies.spec.RemoteUtils)
import fixtures.HttpStub
import uk.co.acuminous.jinkies.spec.RemoteUtils;
import uk.co.acuminous.jinkies.test.Nuke

def bindingUpdater

Before () {
	new Nuke().detonate()	
	bindingUpdater = new BindingUpdater (binding, new Browser ())
	bindingUpdater.initialize ()
	
	HttpStub.enable()
	
}

After () {
	bindingUpdater.remove()
	HttpStub.disable()
}