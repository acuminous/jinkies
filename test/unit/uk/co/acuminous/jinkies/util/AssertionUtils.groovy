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
