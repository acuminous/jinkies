<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Jinkies"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">		
	    <r:require module="core"/>		
		<g:layoutHead/>
        <r:layoutResources />
	</head>
	<body id="${pageProperty(name: 'body.id')}" class="${pageProperty(name: 'body.class')} ${grails.util.Environment.current.name.toLowerCase()}">
		<div id="spinner"><span class="text">Loading...</span></div>	
		<g:layoutBody/>
		
<!--[IE]>		
		<div class="container_12">	
			<div class="grid_12">		
				<div id="ie-apology">
					<span class="text">We only use Safari & Firefox at Jinkies HQ so you might find Jinkies a little buggy in internet explorer.</br> 
					Jinkies is open source and we'll happily accept github <a href="http://www.github.com/acuminous/jinkies">pull requests</a> if you are willing to fix problems you do find.</span>
				</div>
			</div>
		</div>
<![endif]-->
				
		<g:if env="development">
			<g:render template="/ui/fragments/360grid" />
		</g:if>		
        <r:layoutResources />
	</body>
</html>