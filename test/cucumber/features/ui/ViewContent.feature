Feature: View content via the Web UI 

	Scenario: View all content
		Given that there are 10 mp3s with the Scooby Doo theme
		And 5 mp3s with the Star Wars theme
		When a user opens the themes page
		Then display 10 Scooby Doo mp3s
		And 5 Star Wars mp3s