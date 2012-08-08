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
