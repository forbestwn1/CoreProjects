/**
 * 
 */
{
	type : "loop",
	
	processor : "com.nosliw.data.core.process.activity.HAPLoopActivityProcessor",
	
	definition : "com.nosliw.data.core.process.activity.HAPLoopActivityDefinition",
	
	script : {
		
		javascript : function(nosliw, env){
			var loc_env = env;
			var loc_expressionService = nosliw.runtime.getExpressionService(); 
			var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
			var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
			var node_createServiceRequestInfoService = nosliw.getNodeData("request.request.createServiceRequestInfoService");
			var node_DependentServiceRequestInfo = nosliw.getNodeData("request.request.entity.DependentServiceRequestInfo");
			var node_IOTaskResult = nosliw.getNodeData("iovalue.entity.IOTaskResult");
			var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
			var node_objectOperationUtility = nosliw.getNodeData("common.utility.objectOperationUtility");
			var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
			var node_dataOperationUtility = nosliw.getNodeData("common.utility.dataOperationUtility");
			var node_createIODataSet = nosliw.getNodeData("iovalue.entity.createIODataSet");
			
			var loc_out = {
				
				getExecuteActivityRequest : function(activity, input, env, handlers, request){
					var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteLoopActivity", undefined), handlers, request);
					
					//container data
					var dataPath = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_CONTAINERDATAPATH];
					var rootData = node_objectOperationUtility.getObjectAttributeByPath(input, dataPath[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_ROOTPATH]);
					out.addRequest(node_dataOperationUtility.getChildValueRequest(rootData, dataPath[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_PATH], {
						success : function(request, containerData){
							//loop through container data
							return node_dataOperationUtility.getGetElementsRequest(containerData, {
								success : function(request, elements){
									var stepRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteEmbededProcessActivity"));
									for(var i in elements){
										var element = elements[i];
										
										var extraData = {};
										extraData[activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_ELEMENTNAME]] = element.value;
										extraData[activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_INDEXNAME]] = i;
										var extraDataContext = {
											public : extraData
										};
										var extraDataSetIO = node_createIODataSet(extraDataContext);
										stepRequest.addRequest(env.getExecuteProcessRequest(activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_STEP], node_createIODataSet(input), extraDataSetIO, {
											success : function(request, processResult){
												return processResult;
											}
										}));
									}
									return stepRequest;
								}
							});
						}
					}));
					
					return out;
				}
			};
			return loc_out;
		}
	} 
}
