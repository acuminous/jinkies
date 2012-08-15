package uk.co.acuminous.jinkies.spec

import betamax.Recorder
import uk.co.acuminous.jinkies.util.SpringApplicationContext
import grails.plugin.remotecontrol.RemoteControl;

class RemoteBetamaxMixin {

	RemoteControl remote = new RemoteControl(useNullIfResultWasUnserializable: true)
	
	
	def withRemoteTape(String tapeName, Closure doStuff) {		
		withRemoteTape(tapeName, [:], doStuff)		
	}
	
	def withRemoteTape(String tapeName, Map args, Closure doStuff) {		
		
		remote startProxy.curry(tapeName, args) 
		
		def result
		
		try {
			result = doStuff()
		} finally {
			remote stopProxy
		}
		
		result		
	} 
	
	def startProxy = { String tapeName, Map args ->
		Recorder recorder = SpringApplicationContext.getBean('recorder')
		recorder.startProxy(tapeName, args)
	}
	
	def stopProxy = { 
		Recorder recorder = SpringApplicationContext.getBean('recorder')
		recorder.stopProxy()
	}
	
}
