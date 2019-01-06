//get/create package
var packageObj = library.getChildPackage("service");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_objectOperationUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createProcessService = function(){

	//activity plugin entity 
	var loc_activityPlugins = {};
	
	var loc_getActivityPluginRequest = function(pluginName, handlers, request){
		var service = new node_ServiceInfo("getActivityPlugin", {"pluginName":pluginName})
		var plugin = loc_activityPlugins[pluginName];
		if(plugin!=null){
			return node_createServiceRequestInfoSimple(service, function(requestInfo){	return plugin;}, handlers, request);
		}
		else{
			var out = node_createServiceRequestInfoSequence(service, handlers, request);
			out.addReqest(nosliw.getResourceService().getGetResourceDataByTypeRequest([pluginName], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_ACTIVITYPLUGIN, {
				success : function(request, resourceData){
					var plugin = resourceData[pluginName](nosliw);
					loc_activityPlugins[pluginName] = plugin;
					return plugin;
				}
			}));
			return out;
		}
	};
	
	var loc_dyanimicValueBuild = function(output, outputPathSegs, input, intpuPathSegs){
		var inputValue = node_objectOperationUtility.getObjectAttributeByPath(input, inputPathSegs);
		node_objectOperationUtility.operateObjectByPathSegs(output, outputPathSegs, node_CONSTANT.WRAPPER_OPERATION_SET, inputValue);
		return ouput;
	};
	
	var loc_getGenerateDataAssociationOutputRequest = function(dataAssociation, input, handlers, request){
		out = node_createServiceRequestInfoSimple(new node_ServiceInfo("generateDataAssociationOutputRequest", {"dataAssociation":dataAssociation, "input":input}), 
			function(requestInfo){
				return dataAssociation[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATIONGROUP_CONVERTFUNCTION](input, loc_dyanimicValueBuild);
			}, 
			handlers, request);
	};
	
	var loc_getExecuteNormalActivityRequest = function(activityId, activities, context, handlers, request){
		var activity = activities[activityId];
		var executeActivityRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteNormalActivity", {"ac":expression, "variables":variables}), handlers, requestInfo);
		//calculate input for activity first
		executeActivityRequest.addReqeust(loc_getGenerateDataAssociationOutputRequest(activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_INPUT], context, {
			success : function(input){
				//get activity plugin 
				return loc_getActivityPluginRequest(activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_TYPE], {
					success : function(activityPlugin){
						//execute activity plugin
						return activityPlugin.getExecuteRequest(activity, input, {
							success : function(activityResult){  //get activity results (result name + result value map)
								//calculate variable output
								var activityResultConfig = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_RESULT][activityResult.resultName];
								var activityResultDataAssociation = activityResultConfig[node_COMMONATRIBUTECONSTANT.EXECUTABLERESULTACTIVITYNORMAL_OUTPUT];
								return loc_getGenerateDataAssociationOutputRequest(activityResultDataAssociation, activityResult.resultValue, {
									success : function(request, activityOutput){
										//build new context
										_.each(activityOutput, function(ele, name){
											context[name] = ele;
										});
										return new node_ActivityOutput(activityResultConfig[node_COMMONATRIBUTECONSTANT.EXECUTABLERESULTACTIVITYNORMAL_FLOW][node_COMMONATRIBUTECONSTANT.DEFINITIONSEQUENCEFLOW_TARGET],
												context);
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
