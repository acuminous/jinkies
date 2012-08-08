<!doctype html>
<html>
	<head>
		<title>Sorry...</title>
		<meta name="layout" content="main">
	</head>
	<body id="server-error" class="comic error status-500">
		<div class="container_12">	
			<div class="page">			
				<div class="grid_12">
					<div id="error-panel" class="panel">
						<h1 class="title">Something went badly wrong! <g:link uri="/">Click here</a> to recover...</g:link></h1>
						<div class="debug">
							<g:renderException exception="${exception}" />
						</div>										
					</div>
				</div>
				<div class="clear" ></div>												
				<g:render template="/ui/fragments/footer" />				
			</div>
		</div>
	</body>
</html>