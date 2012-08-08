package uk.co.acuminous.jinkies.test

class QuietException extends RuntimeException {
	
	public String getMessage() {
		"This is a test exception. Please ignore."
	}
	
	// Causes propblems with log4j when running stack traces
//	@Override
//	public Throwable fillInStackTrace() {
//		return null
//	}
}
