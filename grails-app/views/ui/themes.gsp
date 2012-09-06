<html>
	<head>
			<meta name="layout" content="main"/>
			<title>Themes</title>	
			<r:require module="content"/>					
	</head>
	<body id="content-page" class="comic busy">
		<div class="container_12">
			<div class="page">	
				<div class="grid_4">
					<g:render template="/ui/fragments/logo" />		
				</div>					
				<div class="grid_4">
					<g:render template="/ui/fragments/menu-item" model="[name: 'jobs', title: 'Jobs...']" />
				</div>									
				<div class="grid_4">
					<g:render template="/ui/fragments/menu-item" model="[name: 'themes', title: 'Themes...', selected: true]" />
				</div>
				<div class="clear"></div>
						
				<div class="grid_12">
					<div id="content-panel" class="main panel">
					
						<div id="contentContainer" class="widgetContainer">
						
                            <h1 class="title">
                                Showing 
                                <span id="filter-name" data-all="all themes">all themes</span>
                                <r:img id="filter" uri="/images/next-filter.png" alt="Next Filter" />
                            </h1>                            
						
							<div class="content widget add fake">
								<div class="name"><span class="text">Add content...</span></div>
							</div>								
														
							<div class="content widget edit prototype">
								<input type="hidden" name="resourceId" value="content/123" />
								<div class="delete circular-button" title="Delete Content"><span class="text">&times;</span></div>
								<div class="play circular-button" title="Play Content"><r:img uri="/images/play.png" alt="Play"/></div>
								<div class="title primary"><span class="text" title="Shaggy from Scooby Doo saying Zoinks">Zoinks</span></div>						
                                <div class="theme"><span class="text">Scooby Doo</span></div>								
							</div>
						
						</div>								
										
						<div class="clear"></div>				
						
					</div>					
				</div>
				<div class="clear"></div>
				<g:render template="/ui/fragments/footer" />
			</div>
		</div>
		<g:render template="/ui/fragments/content-dialog" />
	</body>
</html>
