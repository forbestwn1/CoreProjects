	nosliw.registerNodeEvent("runtime", "active", function(eventName, nodeName){
		
		nosliw.runtime.getUIResourceService().executeCreateUIResourceViewRequest("Example1", 
				{
					success : function(requestInfo, uiResourceView){
						nosliw.logging.info(JSON.stringify(uiResourceView));
						
						$('#testDiv').attr('id').append(uiResourceView.getViews().children());

					}
				}
		);
	});
