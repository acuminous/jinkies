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
							<div class="content widget add fake">
								<div class="name"><span class="text">Upload content...</span></div>
							</div>								
							
							<div class="content widget edit prototype">
								<input type="hidden" name="restId" value="content/123" />
								<div class="delete circular-button" title="Delete Content"><span class="text">&times;</span></div>
								<div class="play circular-button" title="Preview Content"><a target="x-audio/mpeg" href="/api/content/123/data"><r:img uri="/images/play.png" alt="Play"/></a></div>
								<div class="title primary"><span class="text" title="Shaggy from Scooby Doo saying Zoinks">Zoinks</span></div>
								<div class="type"><span class="text">x-audio/mpeg</span></div>						
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