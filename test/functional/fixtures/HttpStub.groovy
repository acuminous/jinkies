package fixtures

import uk.co.acuminous.jinkies.spec.RemoteBetamaxRecorder

class HttpStub {

	public static enable() {
		println "Enabling remote http"
		RemoteBetamaxRecorder recorder = new RemoteBetamaxRecorder()
		recorder.start('http-stub', [:])
	}
	
	public static disable() {
		println "Disabling remote http"
		RemoteBetamaxRecorder recorder = new RemoteBetamaxRecorder()
		recorder.stop()
	}
}
