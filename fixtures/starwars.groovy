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
import uk.co.acuminous.jinkies.content.*

include 'build-events'


fixture {
	starWars(Tag, 'Star Wars', TagType.theme)
	vader1(Content, title: 'I have you now', type: 'audio/wav', bytes: 'I have you now'.bytes) {
		themes = [ ref('starWars')]
		events = [ ref('failure')]
	}
	
	vader2(Content, title: 'A day long remembered', type: 'audio/mpeg', bytes: 'This will be a day long remembered'.bytes) {
		themes = [ ref('starWars')]
		events = [ ref('failure')]
	}
	
	kenobi1(Content, title: 'The force will be with you always', type: 'audio/mpeg', bytes: 'audio/mpeg'.bytes) {
		themes = [ ref('starWars')]
		events = [ ref('success')]
	}
}