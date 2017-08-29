	nosliw.registerNodeEvent("runtime", "active", function(eventName, nodeName){
		
		nosliw.logging.info(nodeName + "    " + eventName );
/*
		var resourceUtility = nosliw.getNodeData("resource.utility");
		
		nosliw.runtime.getResourceService().executeGetResourcesRequest([resourceUtility.createOperationResourceId("test.string;1.0.0", "subString")], 
				{
					success : function(request, resources){
						nosliw.logging.info(JSON.stringify(resources));
					}
				}, undefined);
*/		
		
		//discovery resources
/*		
		nosliw.runtime.getGateway().requestDiscoverResources(
				[resourceUtility.createOperationResourceId("test.string;1.0.0", "subString")], 
				{
					success : function(request, resourceInfos){
						console.log(JSON.stringify(resourceInfos));

						nosliw.runtime.getGateway().requestLoadResources(resourceInfos, {
							success : function(request, resources){
								
							}
						});
					}
				}
		);
		*/

		/*
		var expressionRequest = {
			suite : "expression6",
			expressionName : "main",
			
		};
		
		nosliw.runtime.getGateway().getExpressions(
				[expressionRequest], 
				{
					success : function(request, expressionResponses){
						var expressionResponse = expressionResponses[0];
						nosliw.runtime.getExpressionService().executeExecuteExpressionRequest(expressionResponse.expression, expressionResponse.variablesValue, 
							{
								success : function(requestInfo, result){
									nosliw.logging.info(JSON.stringify(result));
								}	
							}, undefined);
						
					}
				}
		);
		*/
		
	});
