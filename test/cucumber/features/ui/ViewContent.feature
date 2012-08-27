Feature: View content via the Web UI 

	Scenario: View all content
		Given that there are 10 mp3s with the Scooby Doo theme
		And 5 mp3s with the Star Wars theme
		When a user opens the themes page
		Then display 10 Scooby Doo mp3s
		And 5 Star Wars mp3s
		
        
    Scenario: User filters by theme
        Given that there are 10 mp3s with the Scooby Doo theme
        And 5 mp3s with the Star Wars theme
        When a user opens the themes page
        And applies the "Star Wars" filter     
        Then display 5 Star Wars mp3s
        And 0 Scooby Doo mp3s		