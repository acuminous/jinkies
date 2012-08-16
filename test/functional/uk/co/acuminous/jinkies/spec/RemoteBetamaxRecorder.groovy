package uk.co.acuminous.jinkies.spec

import betamax.Recorder
import uk.co.acuminous.jinkies.util.SpringApplicationContext
import grails.plugin.remotecontrol.RemoteControl;

class RemoteBetamaxRecorder {

	RemoteControl remote = new RemoteControl(useNullIfResultWasUnserializable: true)
	
	def withRemoteTape(String tapeName, Closure doStuff) {		
		withRemoteTape(tapeName, [:], doStuff)		
	}
	
	def withRemoteTape(String tape, Map args, Closure doStuff) {		
		
		start(tape, args) 
				
		try {
			return doStuff()
		} finally {
			stop()
		}
	} 


	void start(String tape, Map args) {				
		remote startProxy.curry(tape, args)
	}
	
	void stop() {
		remote stopProxy
	}
		
	def startProxy = { String tape, Map args ->
		Recorder recorder = SpringApplicationContext.getBean('recorder')
		recorder.startProxy(tape, args)
	}
	
	def stopProxy = { 
		Recorder recorder = SpringApplicationContext.getBean('recorder')
		recorder.stopProxy()
	}
}