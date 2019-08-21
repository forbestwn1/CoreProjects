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
	var node_IOTaskInfo;
	
//*******************************************   Start Node Definition  **************************************
//normal activity output (next activity)
var loc_NormalActivityOutput = function(next){
	this.next = next;
};

//end activity output (result name)
var loc_EndActivityOutput = function(resultName){
	this.resultName = resultName;
};

//process has contextIO as its status
var node_createProcess = function(processDef, envObj){
	var loc_processDef = processDef;
	//environment obj for activity to interact with external world
	var loc_envObj = envObj;
	//context io in process
	var loc_processContextIO;
	//all activity plugin used in this process
	var loc_activityPlugins = {};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(processDef, envObj){
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
	};	
	
	//generate initial context io according to input value
	var loc_createContextIO = function(inputValue){
		loc_processContextIO = node_createIODataSet(loc_processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_INITSCRIPT](inputValue));
	};
	
	//load plugin resource
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
	
	//execute normal activity
	var loc_getExecuteNormalActivityRequest = function(normalActivity, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteNormalActivity", {"activity":normalActivity}), handlers, request);
		
		out.addRequest(node_taskUtility.getExecuteTaskRequest(
				loc_processContextIO, 
				undefined,
				normalActivity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_INPUT],    //input data association
				new node_IOTaskInfo(function(inputValue, handlers, request){
					var executeActivityPluginRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteActivityPlugin", {}), handlers, request);
					//get activity plugin 
					executeActivityPluginRequest.addRequest(loc_getActivityPluginRequest(normalActivity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_TYPE], {
						success : function(requestInfo, activityPlugin){
							//execute activity plugin
							return activityPlugin.getExecuteActivityRequest(normalActivity, inputValue, loc_envObj, {
								success : function(requestInfo, activityResult){  //get activity results (result name + result value map)
									return activityResult;
								}
							});
						}
					}));
					return executeActivityPluginRequest;
				}, "ACTIVITYTASK_"+normalActivity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_ID]), 
				function(resultName){   //output data association function for result by name
					return normalActivity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_RESULT][resultName][node_COMMONATRIBUTECONSTANT.EXECUTABLERESULTACTIVITYNORMAL_DATAASSOCIATION]; 
				},
				loc_processContextIO,
				{
					success : function(request, taskResult){
						//build normal activity output
						var activityResultConfig = normalActivity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_RESULT][taskResult.resultName];
						return new loc_NormalActivityOutput(activityResultConfig[node_COMMONATRIBUTECONSTANT.EXECUTABLERESULTACTIVITYNORMAL_FLOW][node_COMMONATRIBUTECONSTANT.DEFINITIONSEQUENCEFLOW_TARGET]);
					}
				}));
		return out;
	};

	//execute activity in process
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
					return loc_getExecuteActivitySequenceRequest(normalActivityOutput.next, activities);
				}
			}, request);
		}
		out.addRequest(activitExecuteRequest);
		return out;
	};

	//execute process
	var loc_getExecuteProcessRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteProcess", {}), handlers, request);

		var startActivityId = loc_processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_STARTACTIVITYID];
		var activities = loc_processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_ACTIVITY];   //all activities
		out.addRequest(loc_getExecuteActivitySequenceRequest(startActivityId, activities, {
			success : function(requestInfo, endActivityOutput){
				var resultDataAssociationDef = loc_processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_RESULT][endActivityOutput.resultName];
				if(resultDataAssociationDef!=undefined){
					//has output data association for result
					return node_createDataAssociation(
							loc_processContextIO, 
							resultDataAssociationDef, 
							undefined, 
							node_dataAssociationUtility.buildDataAssociationName("EMBEDEDPROCESSRESULT",loc_processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEWRAPPERTASK_TASK][node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_ID], "RESULT", "MAPPEDRESULT")
						)
						.getExecuteRequest(						
							{
								success : function(requestInfo, processOutputIODataSet){
									return processOutputIODataSet.getGetDataValueRequest(undefined, {
										success : function(request, resultValue){
											return new node_ProcessResult(endActivityOutput.resultName, resultValue);
										}
									});
								}
							});
				}
				else{
					return loc_processContextIO.getGetDataValueRequest(undefined, {
						success : function(request, resultValue){
							return new node_ProcessResult(endActivityOutput.resultName, resultValue);
						}
					});
				}
			},
			error : function(requestInfo, data){
				
			},
			exception : function(requestInfo, data){
				
			}
			
		}));
		return out;
	};

	var loc_out = {

		getExecuteProcessRequest : function(inputValue, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			loc_createContextIO(inputValue);
			out.addRequest(loc_getExecuteProcessRequest());
			return out;
		},	

		executeProcessRequest : function(inputValue, handlers, request){
			var requestInfo = this.getExecuteProcessRequest(inputValue, handlers, request);
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
nosliw.registerSetNodeDataEvent("iotask.entity.IOTaskInfo", function(){node_IOTaskInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("createProcess", node_createProcess); 

})(packageObj);
