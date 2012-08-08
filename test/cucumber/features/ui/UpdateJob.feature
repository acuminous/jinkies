Feature: Updates a job via the Web UI
	
	Scenario: Successfully update a job on the CI server
		Given that a Jenkins job called Julez is hosted at http://localhost:8080
		And that Julez reports build events via the audio channel
		And that Julez has a Star Wars theme					
		When a user opens the jobs page
		And edits Julez
		And changes the theme from Star Wars to Scooby Doo
		And disables the audio channel
		And proceeds
		Then update the job in the database
		And take the user back to the jobs page
		And display the job's updated details

	Scenario: Cancel updating a job
		Given that a Jenkins job called Julez is hosted at http://localhost:8080
		And that Julez has a Star Wars theme							
		When a user opens the jobs page
		And edits Julez
		And changes the theme from Star Wars to Scooby Doo		
		But cancels
		Then do not update the job in the database		
		And take the user back to the jobs page		
