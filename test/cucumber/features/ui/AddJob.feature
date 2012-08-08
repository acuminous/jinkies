Feature: Adds a job via the Web UI
	
	Scenario: Successfully add all jobs from the specified CI server
		When a user opens the jobs page
		And adds a new Jenkins server
		And sets the server url to http://build.acuminous.meh:8080
		And the theme to Star Wars
		And enables the audio channel	
		And proceeds
		Then create 6 jobs in the database
		And take the user back to the jobs page
		And display 6 jobs

	Scenario: Successfully add a single job from the specified CI server
		When a user opens the jobs page
		And adds a new Jenkins job called Jinkies
		And sets the server url to http://build.acuminous.meh:8080/job/Jinkies/
		And the theme to Star Wars
		And enables the audio channel	
		And proceeds
		Then create the job in the database
		And take the user back to the jobs page
		And display the job's details
		
	Scenario: Fail to add a job when the CI Server url is missing
		When a user opens the jobs page
		And adds a new Jenkins job called Julez
		But neglects to specify the server url
		And attempts to proceed
		Then do not create the job in the database
		And inform the user that "A URL is required."
		
	Scenario: Fail to add a job when the CI Server is unreachable
		When a user opens the jobs page
		And adds a new Jenkins job called Julez
		And sets the server url to http://duff.acuminous.meh:8080
		And attempts to proceed
		And inform the user that "The URL returned an invalid response or could not be reached."

	Scenario: Fail to add a job when the CI server job does not exist
		When a user opens the jobs page
		And adds a new Jenkins job called Julez
		And sets the server url to http://build.acuminous.meh:8080/duff
		And attempts to proceed
		Then do not create the job in the database
		And inform the user that "The URL returned an invalid response or could not be reached."
		
	Scenario: Cancel adding a job
		When a user opens the jobs page
		And adds a new Jenkins job called Julez
		And sets the server url to http://build.acuminous.meh:8080
		But cancels
		Then do not create the job in the database	
		And take the user back to the jobs page		
