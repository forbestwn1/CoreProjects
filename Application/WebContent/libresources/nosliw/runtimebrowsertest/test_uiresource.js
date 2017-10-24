	nosliw.registerNodeEvent("runtime", "active", function(eventName, nodeName){
		
		nosliw.runtime.getUIResourceService().executeCreateUIResourceViewRequest("Example1", 
				{
					success : function(requestInfo, uiResourceView){
						nosliw.logging.info(JSON.stringify(uiResource));
						
						$('#test').attr('id').append(eleResourceView.getViews().children());

					}
				}
		);
	});
