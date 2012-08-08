Feature: Deletes a job via the Web UI
	
	Scenario: Successfully deletes a monitored job
		Given that a Jenkins job called Julez is hosted at http://localhost:8080
		When a user opens the jobs page
		And deletes Julez
		Then delete Julez from the database
		And remove Julez's details from display
