		<div id="content-dialog" class="dialog">
			<div class="container_12">
				<div class="page">
					<div class="panel">
						<div class="grid_8">									
							<h1 class="title hideInUpdateMode">Add Content</h1>
							<h1 class="title hideInCreateMode">Update <span id="titleInHeading">Content</span></h1>							
						</div>
						<div class="clear"></div>
						<form>							
							<div class="form-row">	
								<div class="grid_2">
									<span class="heading">Source</span>
								</div>													
								<div class="grid_6">
									<input id="fileUploadMethod" type="radio" name="uploadMethod" value="file" checked="checked"/>
									<label for="fileUploadMethod">File</label>

									<input id="urlUploadMethod" type="radio" name="uploadMethod" value="url"/>
									<label for="urlUploadMethod">URL</label>
									
                                    <input id="textUploadMethod" type="radio" name="uploadMethod" value="text" checked="checked"/>
                                    <label for="textUploadMethod">Text</label>									
								</div>		
								<div class="clear"></div>
							</div>	
						
							<div class="form-row">							
								<div class="grid_2">
									<label class="heading" for="file">File</label>
								</div>
								<div class="grid_6">
									<div class="fileInput">
										<input id="fileButton" type="button" value="Choose file..." /><span class="text"></span>
										<input type="hidden" id="filename" name="filename" />									
										<input type="file" id="file" name="file" tabIndex="-1" />																				
									</div>
									<div class="clear"></div>
									
								</div>		
								<div class="clear"></div>
							</div>							

                            <div class="form-tip hidden">
                                <div class="grid_8">
                                    <div class="tip"><span class="text">Jinkies only supports mp3s. You can convert other formats with <a class="mediaio" href="http://media.io">media.io</a></span></div>                              
                                </div>
                                <div class="clear"></div>                           
                            </div>
							
							<div class="form-row hidden">
								<div class="grid_2">
									<label class="heading" for="contentUrl">URL</label>
								</div>
								<div class="grid_6">
									<g:textField id="contentUrl" name="contentUrl" placeholder="e.g. http://some-server/samples/scoobydoo.mp3"/>
								</div>
								<div class="clear"></div>
							</div>
							
                            <div class="form-tip hidden">
                                <div class="grid_8">
                                    <div class="tip"><span class="text">Jinkies only supports mp3s. You can convert other formats with <a class="mediaio" href="http://media.io">media.io</a></span></div>                              
                                </div>
                                <div class="clear"></div>                           
                            </div>
                            
                            <div class="form-row hidden">
                                <div class="grid_2">
                                    <label class="heading" for="text">Text</label>
                                </div>
                                <div class="grid_6">
                                    <g:textArea id="text" name="text" placeholder="Build ${"\${build.number}"} of project ${"\${build.job.displayName}"} resulted in ${"\${build.result}"}."></g:textArea>
                                </div>
                                <div class="clear"></div>
                            </div>
                                                        
                            <div class="form-tip hidden">
                                <div class="grid_8">
                                    <div class="tip"><span class="text">See the <a href="https://github.com/acuminous/jinkies/blob/${g.meta(name:'app.version')}/README.md#text-to-speech">documentation</a> for additional syntax</span></div>                              
                                </div>
                                <div class="clear"></div>                           
                            </div>                                                          
						
							<div class="form-row">
								<div class="grid_2">
									<label class="heading" for="title">Title</label>
								</div>
								<div class="grid_6">
									<g:textField id="title" name="title" placeholder="e.g. Scooby Dooby Doo"/>
								</div>
								<div class="clear"></div>
							</div>											
							
							<div class="form-row">							
								<div class="grid_2">
									<label class="heading" for="description">Description</label>
								</div>
								<div class="grid_6">
									<g:textField id="description" name="description" />
								</div>		
								<div class="clear"></div>
							</div>

							<div class="form-row">							
								<div class="grid_2">
									<label class="heading" for="themes">Themes</label>
								</div>
								<div class="grid_6">
									<g:textField id="themes" name="themes" placeholder="e.g. Scooby Doo, Cartoons"/>
								</div>		
								<div class="clear"></div>
							</div>	
							
							<div class="form-tip">
								<div class="grid_8">
									<div class="tip"><span class="text">Jobs with a matching theme may use this content for notifications</span></div>								
								</div>
								<div class="clear"></div>							
							</div>		
							
							<div class="form-row">							
								<div class="grid_2">
									<label class="heading" for="events">Events</label>
								</div>
								<div class="grid_6">
									<input id="successEvent" type="checkbox" name="event" value="Success" />
									<label for="successEvent">Success</label>

									<input id="failureEvent" type="checkbox" name="event" value="Failure" />
									<label for="failureEvent">Failure</label>
																		
									<label id="otherEventsLabel"for="otherEvents">Other</label>									
									<g:textField id="otherEvents" name="events" placeholder="e.g. Error, Stand-Up"/>
								</div>		
								<div class="clear"></div>
							</div>	
							
							<div class="form-tip">
								<div class="grid_8">
									<div class="tip"><span class="text">Specify which events this content is appropriate for</span></div>								
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