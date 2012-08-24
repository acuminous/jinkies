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
package uk.co.acuminous.jinkies.player


import org.apache.commons.collections.map.DefaultedMap

import uk.co.acuminous.jinkies.content.Content;
import uk.co.acuminous.jinkies.content.ContentPlayer

import groovy.text.SimpleTemplateEngine
import groovy.text.Template

import groovy.util.logging.Slf4j

@Slf4j
class GroovyTemplatePlayerAdapter {

	@Delegate ContentPlayer player
	
	void play(Content content, Map context) {
		
		String source = new String(content.bytes, 'utf-8')
				
		String text = ''
		
		try {			
			SimpleTemplateEngine engine = new SimpleTemplateEngine()
			DefaultedMap bindings = new DefaultedMap(context, new DefaultWordGenerator())			
			def template = engine.createTemplate(source).make(bindings)			
			text = template.toString()
		} catch (Exception e) {
			log.error('Error rendering template', e)
			text = "Error in tem-plate for content ${content.title}. Error message is: ${e.message}"
		}
		
		Content fake = [bytes: text.bytes] as Content
		
		player.play(fake, context)
	}
	
}

class DefaultWordGenerator {
	
	int tokenIndex = 0
	
	def propertyMissing(String name) {
		this
	}
	
	def methodMissing(String name, args) {
		this
	}	
	
	String toString() {
		tokenIndex++
		". var $tokenIndex"
	}
	
}