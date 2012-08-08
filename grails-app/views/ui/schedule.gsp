<html>
	<head>
			<meta name="layout" content="main"/>
			<title>Jobs</title>	
			<r:require module="jobs"/>					
	</head>
	<body id="schedule-page" class="comic">
		<div class="container_12">
	
			<div class="page">
			
				<div class="grid_4">
					<div id="main-logo-container">		
						<r:img uri="/images/jinkies-logo.png" />
					</div>
					&nbsp;		
				</div>			
				<div class="grid_4">
					<g:render template="/ui/fragments/menu-item" model="[name: 'jobs', title: 'Jobs...']" />
				</div>
				<div class="grid_4">
					<g:render template="/ui/fragments/menu-item" model="[name: 'content', title: 'Content...']" />
				</div>
				<div class="clear"></div>															
						
				<div class="grid_12">
					<div id="schedule-panel" class="panel">
						<h1 class="title">Schedule</h1>
						<br/>
						<div id="schedule">
							Scheduled notifications will be available soon.						
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