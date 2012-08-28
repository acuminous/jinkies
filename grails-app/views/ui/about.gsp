<!doctype html>
<html>
	<head>
		<title>Jinkies - broken builds need love not email</title>
		<meta name="layout" content="main">
        <r:require module="about"/>                  		
	</head>
	<body id="about-page" class="comic">
		<div class="container_12">
			<div class="page">		
				<div class="grid_12">
                    <div id="about-panel" class="main panel">				
                        <h1 class="title">About</h1>
                        <div>
                            <span class="text">
Jinkies is an open source tool for monitoring remote build servers and grabbing your attention when something goes wrong.
Since this is the first release, and we're all about <del>obnoxious</del> <a href="http://en.wikipedia.org/wiki/Agile_software_development">agile</a> 
and <a href="http://en.wikipedia.org/wiki/Lean_software_development">lean</a> software development, we've built a 
<a href="http://en.wikipedia.org/wiki/Minimum_viable_product">minimal viable product</a> that we 
hope to <a href="http://en.wikipedia.org/wiki/Iterative_and_incremental_development">iterate</a> into something wonderful.
<br/>
<br/>
For this reason Jinkies is currently limited to <a href="http://jenkins-ci.org/">Jenkins</a> servers (possibly <a href="http://hudson-ci.org/">Hudson</a> too) 
and audio based notifications in the form of mp3s and text-to-speech. If you would prefer to use other file formats then for now we recommend converting 
them using <a href="media.io">media.io</a> but watch this space for forthcoming improvements, lava lamps and disco lights are on the way!
<br/>
<br/>
Thanks for investigating Jinkies, we hope you enjoy the experience, but if not please vent your frustration <a href="https://github.com/acuminous/jinkies/issues">here</a>.
<br/>
<br/>
The Jinkies development team<br/>
<br/>
                            </span>
                        </div>	                    
	                </div>
                    <div class="clear"></div>
	                
	                <div id="installation-panel" class="main panel">
	   					<h1 class="title">Installation</h1>
                        <div>
                            <span class="text">                           
Jinkies pre-requisits are a Java 6 runtime environment (or higher), a REST compliant web browser and a 
PC that can play sound. After this all you need to do is download and run the 
Jinkies <a href="http://www.jinkies.co.uk/binaries/executable/jinkies.war">executable war</a>. Depending on your PC setup you might be able to do this by double clicking the 
file after download but we recommend starting it by typing:<br/>
<br/>
<code>
    java -jar jinkies.war
</code><br/>
<br/>
This way you can stop Jinkies using CTRL+C instead of finding and killing the process.<br/>
<br/>
The first time Jinkies starts it has to initialise an h2 database which can take a little while. Keep trying <a href="http://localhost:8080">http://localhost:8080</a> 
until you see the jobs page. From there click "Add Jenkins jobs" and supply the URL of your build server or build job. Once you've got this working try uploading your own 
mp3s and specifying a theme for the jobs.<br/>
<br/>
Please see the <a href="https://github.com/acuminous/jinkies/blob/${g.meta(name:'app.version')}/README.md">documentation</a> for other installation options, including 
instructions for changing the port and getting the <a href="http://www.jinkies.co.uk/binaries/deployable/jinkies.war">deployable war</a> running on a Java Application Server.<br/>
<br/>
One more thing - please <em>don't install Jinkies on a publicly accessible server</em>. If you do, anyone will be able to upload and execute programs on your machine.<br/>
<br/>
                            </span>
                        </div>
                    </div>
                    <div class="clear"></div>
                        
                    <div id="help-panel" class="main panel">	                        
	                    <h1 class="title">Getting Help</h1>
                        <div>
                            <span class="text">
If you're having problems with Jinkies or find what looks like a bug, the first thing to do is read the 
<a href="https://github.com/acuminous/jinkies/blob/${g.meta(name:'app.version')}/README.md">documentation</a> and if that doesn't help 
<a href="https://github.com/acuminous/jinkies/issues">report</a> it. If you're a little more adventurous you can 
browse the <a href="https://github.com/acuminous/jinkies">source code</a> and if you're feeling incredibly generous, fix 
the problem for us and submit a <a href="https://github.com/acuminous/jinkies/pulls">pull request</a>. Do this and 
open source karma will be yours.<br/>
<br/>                             
                            </span>
                        </div>                            	                    
                    </div>
                    <div class="clear"></div>
    				    
				</div>
				<div class="clear" ></div>										
				<g:render template="/ui/fragments/footer" />			
			</div>
		</div>
	</body>
</html>