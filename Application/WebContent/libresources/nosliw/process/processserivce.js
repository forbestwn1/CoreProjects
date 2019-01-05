//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
//*******************************************   Start Node Definition  ************************************** 	

ActivityResult = function(resultName, value, context, activities){
	
}	

ActivityOutput = function(next, context, activities){
	
}

ProcessResult = function(resultName, value){
	
}

var node_createProcessService = function(){

	var loc_generateInput = function(activity, context, handlers){
		out = node_createServiceRequestInfoSimple(new node_ServiceInfo("generateInput", {"activity":activity, "context":constants}), 
				function(requestInfo){
					return activity.inputScript(context, utilFun);
				}, 
				handlers, requestInfo);
	};
	
	var loc_generateOutput = function(activity, activityResult, out, outputMapping, handlers){
		
	}
	
	var loc_getExecuteNormalActivityRequest = function(activityId, activities, context, handlers, requester_parent){
		var activity = activities[activityId];
		var executeActivityRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteActivity", {"":expression, "variables":variables}), handlers, requestInfo);
		executeActivityRequest.addReqeust(loc_generateInput(context, activity.input, {
			success : function(input){
				return resourceService.getResourceRequest(activity.type, {
					success : function(activityPlugin){
						return activityPlugin.script.getExecuteRequest(input, {
							success : function(activityResult){
								return loc_generateOutput(out, activity.outputMapping, {
									success : function(activityOutput){
										return loc_getExecuteActivityRequest(activityOutput.next, context, activities);
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
		out = node_createServiceRequestInfoSimple(new node_ServiceInfo("generateInput", {"activity":activity, "context":constants}), 
				function(requestInfo){
					var output = process[result][activity[result]];
					return new ProcessResult(activity[result], output.inputScript(context, utilFun));
				}, 
				handlers, requestInfo);
	};

	var loc_getExecuteStartActivityRequest = function(activityId, activities, context, handlers, requester_parent){
		var startActivity = activities[activityId];
		out = node_createServiceRequestInfoSimple(new node_ServiceInfo("ExecuteStartActivity", {"activity":activity, "context":constants}), 
				function(requestInfo){
					return new ActivityResult(context, activities, startActivity.next);
				}, 
				handlers, requestInfo);
		
	};

	var loc_getExecuteActivityRequest = function(activityId, activities, context, handlers, requester_parent){
		var activity = activities[activityId];
		var activityType = activity[type];
		if(activityType==start){
			return loc_getExecuteActivityRequest(activity.next, activities, context, );
		}
		else if(activityType==normal){
			return loc_getExecuteNormalActivityRequest(activityId, activities, context);
		}
		else if(activityType==end){
			return loc_getExecuteEndActivityRequest(activityId, activities, context);
		}
	};


	
	var loc_getExecuteProcessResourceRequest = function(process, input, handlers, requester_parent){
		//init, build context
		var context = process.init();
		_.each(input, function(value, name){
			context[public][name] = value;
		});
		
		//execute activity
		var executeFlowRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteProcess", {"process":process, "context":context}), {
			success : function(processResult){
				return processResult;
			}
		}, requestInfo);
		var startActivityId = process[startActivityId];
		var activities = process[activities];
		var startActivityRequest = loc_getExecuteActivityRequest(startActivityId, activities, context);
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
