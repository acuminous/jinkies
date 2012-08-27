Feature: View jobs via the Web UI 

	Scenario: View all jobs monitored by Jinkies
		Given that there are 20 jobs
		When a user opens the jobs page
		Then display 20 jobs
		
	Scenario: The job name, url, channels and theme are displayed for each job
		Given that a Jenkins job called Julez is hosted at http://localhost:8080
		And that Julez reports build events via the audio channel
		And that Julez also reports build events via the video channel
		And that Julez has a Star Wars theme		
		When a user opens the jobs page		
		Then display these details
		
    Scenario: User filters by failing jobs
        Given that there are 7 successful jobs
        And 3 failing jobs
        When a user opens the jobs page
        And applies the "failed jobs" filter     
        Then display 3 jobs