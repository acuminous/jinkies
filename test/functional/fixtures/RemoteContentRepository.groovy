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
package fixtures

import uk.co.acuminous.jinkies.content.Content;
import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagService
import uk.co.acuminous.jinkies.content.TagType
import uk.co.acuminous.jinkies.spec.RemoteUtils
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;

class RemoteContentRepository {
	
	TagService tagService = new TagService()

	Content buildRandomContent(String themeName) {
		Tag theme = tagService.findOrCreateTag(themeName, TagType.theme)						
		Content.build(title: randomAlphabetic(10), filename: randomAlphabetic(10), themes: [theme])
	}	
}
