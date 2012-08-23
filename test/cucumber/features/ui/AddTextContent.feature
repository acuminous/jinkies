Feature: Add text content via the Web UI 

    Scenario: Add text content
        When a user opens the themes page
        And decides to add some content 
        And sets the text to "Who broke the feckin build"
        And the title to "Build Failure"
        And adds a theme of Father Ted
        And an event type of failure
        And proceeds
        Then create the content in the database
        And take the user back to the themes page
        And display the content's details