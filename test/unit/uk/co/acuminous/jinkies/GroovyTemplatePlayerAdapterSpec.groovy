package uk.co.acuminous.jinkies

import uk.co.acuminous.jinkies.content.Content
import uk.co.acuminous.jinkies.content.ContentPlayer
import uk.co.acuminous.jinkies.player.GroovyTemplatePlayerAdapter;
import grails.plugin.spock.UnitSpec

class GroovyTemplatePlayerAdapterSpec extends UnitSpec {

	ContentPlayer player
	GroovyTemplatePlayerAdapter adapter
	
	def setup() {
		player = Mock(ContentPlayer)
		adapter = new GroovyTemplatePlayerAdapter(player: player)
	}
	
	def "Renders template"() {
		
		given:
			Content content = new Content(bytes: '1 + 1 = ${1 + 1}'.bytes)
			
		when:
			adapter.play(content, [:])
			
		then:		
			1 * player.play({ Content transformedContent ->
				new String(transformedContent.bytes) == '1 + 1 = 2'
			}, _)		
	}	
	
	def "Binds event"() {
		
		given:
			Map event = [ msg: 'Hello World' ]
			Content content = new Content(bytes: '$msg'.bytes)
			
		when:
			adapter.play(content, event)
			
		then:
			1 * player.play({ Content transformedContent ->
				new String(transformedContent.bytes) == 'Hello World'
			}, _)
	}
		
	def "Uses placeholders for missing bind variables"() {
		
		given:
			Content content = new Content(bytes: '$a $b $c'.bytes)
			
		when:
			adapter.play(content, [:])
			
		then:
			1 * player.play({ Content transformedContent ->
				new String(transformedContent.bytes) == '. var 1 . var 2 . var 3'
			}, _)
	}
		
	def "Reports errors"() {
		
		given:
			Content content = new Content(title: 'xyz', bytes: '${1 / 0}'.bytes)
			
		when:
			adapter.play(content, [:])
			
		then:
			1 * player.play({ Content transformedContent ->
				println new String(transformedContent.bytes)
				new String(transformedContent.bytes) == 'Error in tem-plate for content xyz. Error message is: Division by zero'
			}, _)
	}

	def "Does not modify original content entity"() {
		
		given:
			Content content = new Content(bytes: '1 + 1 = ${1 + 1}'.bytes)
			
		when:
			adapter.play(content, [:])
			
		then:
			content.bytes == '1 + 1 = ${1 + 1}'.bytes
	}
		
	def "Delegates to player"() {
		
		given:
			Content content = new Content()
		
		when:		
			adapter.isSupported(content)
			
		then:
			1 * player.isSupported(content)
			
		when:
			adapter.getContentTypes()
		
		then:
			1 * player.getContentTypes()
	}
	
}
