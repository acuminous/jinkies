		<div id="job-dialog" class="dialog">
			<div class="container_12">
				<div class="page">
					<div class="panel">
						<div class="grid_8">									
							<h1 class="title hideInUpdateMode">Add Jenkins Jobs</h1>
							<h1 class="title hideInCreateMode">Update <span id="displayNameInHeading">Job</span></h1>
						</div>
						<div class="clear"></div>
						<form>
							<div class="form-row">
								<div class="grid_2">
									<label class="heading" for="serverUrl">URL</label>
								</div>
								<div class="grid_6">
									<g:textField id="serverUrl" name="serverUrl" class="hideInUpdateMode" placeholder="e.g. http://jenkins.yourdomain.com"/>
									<a href="/job-url" class="hideInCreateMode"><span class="text">job name</span></a>									
								</div>
								<div class="clear"></div>
							</div>
							
							<div class="form-tip hideInUpdateMode">
								<div class="grid_8">
									<div class="tip"><span class="text">Enter the url of a Jenkins server or individual job</span></div>								
								</div>	
								<div class="clear"></div>
							</div>
							
							<div class="form-row">							
								<div class="grid_2">
									<label class="heading" for="theme">Theme</label>
								</div>
								<div class="grid_6">
									<g:textField id="theme" name="theme" placeholder="e.g. Scooby Doo"/>
								</div>		
								<div class="clear"></div>
							</div>

							<div class="form-tip">
								<div class="grid_8">
									<div class="tip"><span class="text">Set the theme to be used for notifications</span></div>								
								</div>
								<div class="clear"></div>							
							</div>
							
							<div class="form-row">
								<div class="grid_2">
									<span class="heading">Notification</span> 
								</div>
								<div class="grid_6">
									<input id="audio-checkbox" type="checkbox" class="channel" name="audio" />
									<label class="checkbox" for="audio">Audio</label>									
									<g:hiddenField id="audio" name="channel" value="audio" />
									
                                    <input id="speech-checkbox" type="checkbox" class="channel" name="speech" />
                                    <label class="checkbox" for="speech">Speech</label>                                   
                                    <g:hiddenField id="speech" name="channel" />									
								</div>	
								<div class="clear"></div>
							</div>	
							
							<div class="form-tip">
								<div class="grid_8">
									<div class="tip"><span class="text">Build events will be reported via the selected channels</span></div>								
								</div>
								<div class="clear"></div>							
							</div>													
										
							<div class="controls form-row">																															
								<div class="grid_3">
									<div class="button-container">
										<input type="button" class="cancel" />
									</div>												
								</div>												
								<div class="grid_2">
									&nbsp;
								</div>
								<div class="grid_3">
									<div class="button-container">
										<input type="button" class="ok" />
									</div>						
								</div>					
								<div class="clear"></div>
							</div>												
						</form>	
					</div>
				</div>
			</div>
		</div>		