	nosliw.registerNodeEvent("runtime", "active", function(eventName, nodeName){
		
		nosliw.runtime.getUIResourceService().executeGetUIResourceRequest(["Example1"], 
				{
					success : function(requestInfo, uiResource){
						nosliw.logging.info(JSON.stringify(uiResource));
					}
				}
		);
	});
