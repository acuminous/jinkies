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

import betamax.Recorder
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
		Recorder recorder = ctx.getBean('recorder')
		recorder.startProxy(tape, args)
	}
	
	def stopProxy = { 
		Recorder recorder = ctx.getBean('recorder')
		recorder.stopProxy()
	}
}
