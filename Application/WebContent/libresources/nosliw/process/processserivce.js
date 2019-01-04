//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
//*******************************************   Start Node Definition  ************************************** 	

var node_createProcessService = function(){

	var loc_generateInput = function(context, inputMapping, handlers){
		
	};
	
	var loc_generateOutput = function(out, outputMapping, handlers){
		
	}
	
	var loc_getExecuteNormalActivityRequest = function(activityId, activities, context, handlers, requester_parent){
		var activity = activities[activityId];
		var executeActivityRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteActivity", {"":expression, "variables":variables}), handlers, requestInfo);
		executeActivityRequest.addReqeust(loc_generateInput(context, activity.input, {
			success : function(input){
				resourceService.getResource(activity.type, {
					success : function(activityPlugin){
						return activityPlugin.script.getExecuteRequest(input, {
							success : function(out){
								return loc_generateOutput(out, activity.outputMapping, {
									success : function(){
										return outputContext;
									}
								});
							}
						});
					}
				});
			}
		}));
		
	};

	var loc_getExecuteEndActivityRequest = function(activityId, activities, context, handlers, requester_parent){
		
	};

	var loc_getExecuteStartActivityRequest = function(activityId, activities, context, handlers, requester_parent){
		
	};

	var processSuccess = function(nextActivityId, activities){
		if(isNormal){
			return loc_getExecuteNormalActivityRequest(aId, activities, {
				success : function(nextActivityId){
					return processSuccess(nextActivityId, activities);
				}
			});
		}
		else if(end){
			return loc_getExecuteEndActivityRequest(aId, activities, {
				success : function(output){
					
				}
			});
		}
	};
	
	var loc_getExecuteProcessResourceRequest = function(process, input, handlers, requester_parent){
		//process init
		
		
		//execute activity
		var executeFlowRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteFlow", {"":expression, "variables":variables}), handlers, requestInfo);
		var startActivityId = process[id];
		var activities = process[activities];
		var startActivityRequest = loc_getStartExecuteActivityRequest(startActivityId, activities, {
			success : function(nextActivityId){
				return processSuccess(nextActivityId, activities);
			}
		});
		executeFlowRequest.addRequest(startActivityRequest);
		
		//generate output
		
	};
	
	var loc_out = {

		getExecuteProcessResourceRequest : function(id, input, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoService(new node_ServiceInfo("ExecuteProcessResource", {"id":id, "input":input}), handlers, requestInfo)

			var getProcessRequest = nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([id], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_PROCESS, {});
			
			out.setDependentService(new node_DependentServiceRequestInfo(getProcessRequest, {
				success : function(requestInfo, processes){
					var process = processes[id];
					return loc_getExecuteProcessResourceRequest(process, input, handlers, requester_parent);
				}
			}));
			return out;
		},
			
		executeProcessResourceRequest : function(id, input, handlers, requester_parent){
			var requestInfo = this.getExecuteProcessResourceRequest(id, input, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
			
	};

	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

//Register Node by Name
packageObj.createChildNode("createProcessService", node_createProcessService); 

})(packageObj);
