package uk.co.acuminous.jinkies.ci

import spock.lang.Specification
import spock.lang.Unroll
import uk.co.acuminous.jinkies.content.Tag
import uk.co.acuminous.jinkies.content.TagType
import static uk.co.acuminous.jinkies.util.AssertionUtils.*;
import grails.buildtestdata.mixin.Build

@Build(Job)
class JobSpec extends Specification {

	@Unroll("Job #field has #constraint constraint")
	def "Job fields with constraints"() {
		
		given:
			Job job = new Job((field): value)
			
		expect:
			hasConstraint(job, field, constraint)
			
		where:
			field         | value | constraint
			'displayName' | null  | 'nullable'
			'displayName' | ''    | 'blank'
			'url'         | null  | 'nullable'	
			'url'		  | ''    | 'blank'
			'type'        | null  | 'nullable'
			'type'   	  | ''    | 'blank'
	}
	
	@Unroll("Job #field does not have #constraint constraint")
	def "Job fields without constraints"() {
		
		given:
			Job job = new Job((field): value)
			
		expect:
			withoutConstraint(job, field, constraint)
			
		where:
			field             | value | constraint
			'theme'           | null  | 'nullable'
	}
	
	
	@Unroll("Job #field is unique")
	def "Job fields are unique"() {
		
		given:
			Job job1 = Job.build((field): value)
			Job job2 = new Job((field): value)
					
		expect:
			hasConstraint(job2, field, 'unique')
			
		where:
			field   | value
			'url'   | 'jinkies'
	}
	
	@Unroll("Renders Job as String [#displayName, #url, #theme, #channels]")
	def "Renders Job as String"() {
		
		given:
			Tag tag = new Tag('Scooby Doo', TagType.theme)
			Job job = Job.build(displayName: displayName, url: url, theme: tag)
			
		expect:
			job.toString() == "Job[id=1,displayName=$displayName,url=$url,theme=Scooby Doo]"
			
		where:
			displayName | url
			'Jinkies'   | '.../Jinkes/'
			'Julez'     | '.../Julez/' 
	}
	
	def "Jobs cannot have duplicate channels"() {
		
		given:
			Job job = Job.build(channels: ['a'])
			
		when:
			job.addToChannels('a')
			
		then:
			hasConstraint(job, 'channels', 'duplicate')		
		
	}
	
	@Unroll("Comparing #url1 against #url2")
	def "Job equality is based on url"() {
		expect:
			def actual = new Job(url: url1) == new Job(url: url2)
			actual == expected
			
		where:
			url1      | url2      | expected
			'a'       | 'a'	      | true
			'A'       | 'a'       | true
			'a'       | 'A'       | true
			'a'       | 'b'       | false
			'a'       | null      | false
			null      | 'a'       | false		
	}
}
