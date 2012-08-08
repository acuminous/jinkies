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

import org.springframework.validation.Errors


import grails.converters.JSON
import static javax.servlet.http.HttpServletResponse.*


class JinkiesErrorRenderer {
	
	protected boolean report(Errors errors) {
		if (errors.hasErrors()) {
			renderJsonError errors
			true
		}
	}
	
	protected boolean reportMissing(def bean) {
		if (!bean) {
			response.sendError(SC_NOT_FOUND)
			true
		}
	}
	
	protected renderJsonError(String code, int status = SC_BAD_REQUEST) {
		String message = message(code: code)
		renderJsonError([message], status)
	}
	
	protected void renderJsonError(Errors errors, int status = SC_BAD_REQUEST) {
		List<String> messages = errors.getAllErrors().collect() {
			message(error: it)
		}
		renderJsonError(messages, status)
	}
	
	protected void renderJsonError(List<String> messages, int status = SC_BAD_REQUEST) {
		response.status = status
		render messages as JSON
	}
	
}
