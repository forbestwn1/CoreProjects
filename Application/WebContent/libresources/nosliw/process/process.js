//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_taskUtility;
	var node_IOTaskResult;
	var node_createDataAssociation;
	var node_createIODataSet;
	var node_getLifecycleInterface;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_requestServiceProcessor;
	
//*******************************************   Start Node Definition  **************************************
//normal activity output (next activity + context)
var loc_NormalActivityOutput = function(next, context){
	this.next = next;
	this.context = context;
};

//end activity output (result name + context)
var loc_EndActivityOutput = function(resultName, context){
	this.resultName = resultName;
	this.context = context;
};

	
var node_createProcess = function(processDef, envObj){
	var loc_processDef = processDef;
	var loc_envObj = envObj;
	var loc_processContextIO;
	var loc_activityPlugins = [];
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(processDef, envObj){
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
	};	
	
	var loc_createContextIORequest = function(input, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var valueType = node_getObjectType(input);
		if(valueType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_EXTERNALMAPPING){
			out.addRequest(node_createDataAssociation(input.dataIO, input.dataAssociationDef).getExecuteRequest({
				success : function(request, mappingedIO){
					return mappingedIO.getGetDataSetValueRequest({
						success : function(request, mappingedDataSet){
							loc_processContextIO = node_createIODataSet(loc_processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_INITSCRIPT](mappingedDataSet));
						}
					});
				}
			}));
		}
		else{
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(requestInfo){	
				loc_processContextIO = node_createIODataSet(loc_processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_INITSCRIPT](input));
			}));
		}
		return out;
	};
	
	var loc_getActivityPluginRequest = function(pluginName, handlers, request){
		var service = new node_ServiceInfo("getActivityPlugin", {"pluginName":pluginName})
		var plugin = loc_activityPlugins[pluginName];
		if(plugin!=null){
			return node_createServiceRequestInfoSimple(service, function(requestInfo){	return plugin;}, handlers, request);
		}
		else{
			var out = node_createServiceRequestInfoSequence(service, handlers, request);
			out.addRequest(nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([pluginName], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_ACTIVITYPLUGIN, {
				success : function(request, resourceData){
					var plugin = resourceData[pluginName][node_COMMONATRIBUTECONSTANT.PLUGINACTIVITY_SCRIPT](nosliw, loc_envObj);
					loc_activityPlugins[pluginName] = plugin;
					return plugin;
				}
			}));
			return out;
		}
	};
	
	var loc_getExecuteNormalActivityRequest = function(normalActivity, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteNormalActivity", {"activity":normalActivity}), handlers, request);
		
		out.addRequest(node_taskUtility.getExecuteTaskRequest(
				loc_processContextIO, 
				undefined,
				normalActivity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_INPUT], 
				function(input, handlers, request){
					var executeActivityPluginRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteActivityPlugin", {}), handlers, request);
					//get activity plugin 
					executeActivityPluginRequest.addRequest(loc_getActivityPluginRequest(normalActivity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_TYPE], {
						success : function(requestInfo, activityPlugin){
							//execute activity plugin
							return activityPlugin.getExecuteActivityRequest(normalActivity, input, loc_envObj, {
								success : function(requestInfo, activityResult){  //get activity results (result name + result value map)
									return activityResult;
								}
							});
						}
					}));
					return executeActivityPluginRequest;
				}, 
				function(resultName){
					return normalActivity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_RESULT][resultName][node_COMMONATRIBUTECONSTANT.EXECUTABLERESULTACTIVITYNORMAL_DATAASSOCIATION]; 
				},
				loc_processContextIO,
				{
					success : function(request, taskResult){
						var activityResultConfig = normalActivity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_RESULT][taskResult.resultName];
						return new loc_NormalActivityOutput(activityResultConfig[node_COMMONATRIBUTECONSTANT.EXECUTABLERESULTACTIVITYNORMAL_FLOW][node_COMMONATRIBUTECONSTANT.DEFINITIONSEQUENCEFLOW_TARGET]);
					}
				}));
		return out;
	};

	var loc_getExecuteActivitySequenceRequest = function(activityId, activities, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteActivity", {"activityId":activityId}), handlers, request);
		var activitExecuteRequest;
		var activity = activities[activityId];
		var activityType = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_TYPE];
		if(activityType==node_COMMONCONSTANT.ACTIVITY_TYPE_START){
			activitExecuteRequest = node_createServiceRequestInfoSimple(new node_ServiceInfo("ExecuteStartActivity", {"activity":activity}), 
					function(requestInfo){
						var nextActivityId = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLERESULTACTIVITYNORMAL_FLOW][node_COMMONATRIBUTECONSTANT.DEFINITIONSEQUENCEFLOW_TARGET];
						return loc_getExecuteActivitySequenceRequest(nextActivityId, activities); 
					});
		}
		else if(activityType==node_COMMONCONSTANT.ACTIVITY_TYPE_END){
			activitExecuteRequest = node_createServiceRequestInfoSimple(new node_ServiceInfo("ExecuteEndActivity", {"activity":activity}), 
					function(requestInfo){
						return new loc_EndActivityOutput(activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_RESULTNAME]);
					}, 
					handlers, request);
		}
		else{
			activitExecuteRequest = loc_getExecuteNormalActivityRequest(activity, {
				success : function(requestInfo, normalActivityOutput){
					return loc_getExecuteActivitySequenceRequest(normalActivityOutput.next, activities, normalActivityOutput.context);
				}
			}, request);
		}
		out.addRequest(activitExecuteRequest);
		return out;
	};

	var loc_getExecuteProcessRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteProcess", {}), handlers, request);

		var startActivityId = loc_processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_STARTACTIVITYID];
		var activities = loc_processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_ACTIVITY];
		out.addRequest(loc_getExecuteActivitySequenceRequest(startActivityId, activities, {
			success : function(requestInfo, endActivityOutput){
				var dataAssociation = node_createDataAssociation(node_createIODataSet(endActivityOutput.context), loc_processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_RESULT][endActivityOutput.resultName], loc_processContextIO);
				return dataAssociation.getExecuteRequest(						
					{
						success : function(requestInfo, processOutputIODataSet){
							return new node_ProcessResult(endActivityOutput.resultName, processOutputIODataSet);
						}
					});
			}
		}));
		return out;
	};

	var loc_getExecuteEmbededProcessRequest = function(embededProcess, input, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteEmbededProcess", {"embededProcess":embededProcess, "input":input}), handlers, request);
		out.addRequest(loc_getExecuteProcessRequest(embededProcess, input, {
			success : function(requestInfo, processResult){
				var backToGlobal = embededProcess[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_BACKTOGLOBAL][processResult.resultName];
				if(backToGlobal!=null){
					return node_ioTaskUtility.getExecuteDataAssociationRequest(processResult.value, backToGlobal, {
						success : function(request, globalData){
							return new node_ProcessResult(processResult.resultName, globalData.getData());
						}
					});
				}
				else return new node_ProcessResult(processResult.resultName);
			}
		}));
		return out;
	};

	var loc_out = {

		getExecuteProcessRequest : function(input, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_createContextIORequest(input));
			out.addRequest(loc_getExecuteProcessRequest({
				success : function(request, processResult){
					return new node_IOTaskResult(processResult.resultName, loc_processContextIO);
				}
			}));
			return out;
		},	

		executeProcessRequest : function(input, handlers, request){
			var requestInfo = this.getExecuteProcessRequest(input, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},	
	};
	
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_PROCESS);

	node_getLifecycleInterface(loc_out).init(processDef, envObj);

	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityResult", function(){node_IOTaskResult = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.taskUtility", function(){node_taskUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.IOTaskResult", function(){node_IOTaskResult = this.getData();});

//Register Node by Name
packageObj.createChildNode("createProcess", node_createProcess); 

})(packageObj);
