package uk.co.acuminous.jinkies.util

import grails.test.mixin.support.GrailsUnitTestMixin

class AssertionUtils {

	public static boolean hasConstraint(def object, String field, String error) {
		assert !object.validate()
		assert object.hasErrors()
		assert object.errors[field]
		assert object.errors[field].code == error
		return true
	}
	
	public static boolean withoutConstraint(def object, String field, String error) {
		object.validate()
		assert object.errors[field]?.code != error
		return true
	}
	
}
