<html>
	<head>
			<meta name="layout" content="main"/>
			<title>Jobs</title>	
			<r:require module="jobs"/>					
	</head>
	<body id="jobs-page" class="comic busy">
		<div class="container_12">
	
			<div class="page">
			
				<div class="grid_4">
					<g:render template="/ui/fragments/logo" />
				</div>
				<div class="grid_4">
					<g:render template="/ui/fragments/menu-item" model="[name: 'jobs', title: 'Jobs...', selected: true]" />
				</div>							
				<div class="grid_4">
					<g:render template="/ui/fragments/menu-item" model="[name: 'themes', title: 'Themes...']" />
				</div>				
				<div class="clear"></div>
						
				<div class="grid_12">
					<div id="jobs-panel" class="main panel">
				
						<div id="jobs" class="widgetContainer">
						
                            <h1 class="title">
                                <r:img id="filter" uri="/images/next-filter.png" alt="Next Filter" />
                                Showing 
                                <span id="filter-name" data-all="all jobs" data-failure="failed jobs" data-error="jobs that caused errors">all jobs</span>
                                <r:img id="refresh" uri="/images/refresh.png" alt="Refresh" />
                            </h1>						
						
							<div class="job widget add fake">							
								<div class="name"><span class="text">Add Jenkins jobs...</span></div>
							</div>															
							
							<div class="job widget edit prototype">
								<input type="hidden" name="resourceId" value="job/123" />
								<div class="spacer circular-button"><span class="text">&nbsp;</span></div>								
								<div class="delete circular-button" title="Delete Job"><span class="text">&times;</span></div>
								<div class="audio channel" title="Audio"><r:img uri="/images/quaver.png" alt="Audio channel enabled"/></div>
								<div class="speech channel" title="Text To Speech"><r:img uri="/images/lips.png" alt="Text To Speech channel enabled"/></div>
								<div class="video channel" title="Video"><r:img uri="/images/video.png" alt="Video channel enabled"/></div>
								<div class="name primary"><span class="text">Jinkies</span></div>
								<div class="theme"><span class="text">Raw Hide</span></div>						
							</div>
						
						</div>					
										
						<div class="clear"></div>				
						
					</div>					
				</div>
				<div class="clear"></div>
				<g:render template="/ui/fragments/footer" />
			</div>
		</div>
		<g:render template="/ui/fragments/job-dialog" />
	</body>
</html>
