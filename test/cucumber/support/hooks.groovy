package support

import geb.binding.BindingUpdater
import geb.Browser

this.metaClass.mixin (cucumber.runtime.groovy.Hooks)
this.metaClass.mixin(uk.co.acuminous.jinkies.spec.RemoteMixin)

def bindingUpdater

Before () {
	nuke()	
	bindingUpdater = new BindingUpdater (binding, new Browser ())
	bindingUpdater.initialize ()
}

After () {
	// bindingUpdater.remove()
}