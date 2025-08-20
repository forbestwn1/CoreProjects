//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_CONSTANT;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_taskUtility;
	var node_createIODataSet;
	var node_getLifecycleInterface;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_requestServiceProcessor;
	var node_IOTaskInfo;
	var node_ExecutableResult;
	var node_getObjectType;
	
//*******************************************   Start Node Definition  **************************************
var loc_moduleName = "activity";	

//process has contextIO as its status
var node_createActivity = function(activityDef, inputData, envObj){
	var loc_activityDef = activityDef;
	//environment obj for activity to interact with external world
	var loc_envObj = envObj;
	//activity plugin
	var loc_activityPlugin = undefined;
	//io data for activity status
	var loc_actvityStatusIO;
	
	//input data type, value or data set io, different data type have different output 
	var loc_inputDataType;
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(activityDef, inputData, envObj){
		loc_inputDataType = node_getObjectType(inputData);
		
		loc_actvityStatusIO = node_createIODataSet(inputData);

		if(loc_envObj.getSyncOutRequest==undefined){
			var envObj = {
					//add method for sync data from internal process context to external process context
					getSyncOutRequest : function(internalValue, handlers, request){
						return node_createServiceRequestInfoSimple(undefined, function(request){}, handlers, request);
					}
				};
			loc_envObj = _.extend(envObj, loc_envObj);
		}
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
	};	
	
	var loc_getExecuteActivityRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteNormalActivity", {}), handlers, request);
		
		out.addRequest(node_taskUtility.getExecuteTaskRequest(
				loc_actvityStatusIO, 
				undefined,
				loc_activityDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_INPUTMAPPING],    //input data association
				new node_IOTaskInfo(function(inputValue, handlers, request){
					var executeActivityPluginRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteActivityPlugin", {}), handlers, request);
					//get activity plugin 
					executeActivityPluginRequest.addRequest(loc_getActivityPluginRequest(loc_activityDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_TYPE], {
						success : function(requestInfo, activityPlugin){
							//execute activity plugin
							return activityPlugin.getExecuteActivityRequest(loc_activityDef, inputValue, loc_envObj, {
								success : function(requestInfo, activityResult){  //get activity results (result name + result value map)
									return activityResult;
								}
							});
						}
					}));
					return executeActivityPluginRequest;
				}, "ACTIVITYTASK_"+loc_activityDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_ID]), 
				function(resultName){   //output data association function for result by name
					return loc_activityDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_RESULT][resultName][node_COMMONATRIBUTECONSTANT.EXECUTABLERESULTACTIVITY_DATAASSOCIATION]; 
				},
				loc_actvityStatusIO,
				{
					success : function(request, taskResult){
						//build normal activity output
						return taskResult;
					}
				}));
		
		return out;
		
	};
	
	//load plugin resource
	var loc_getActivityPluginRequest = function(pluginName, handlers, request){
		var service = new node_ServiceInfo("getActivityPlugin", {"pluginName":pluginName})
		if(loc_activityPlugin!=null){
			return node_createServiceRequestInfoSimple(service, function(requestInfo){	return loc_activityPlugin;}, handlers, request);
		}
		else{
			var out = node_createServiceRequestInfoSequence(service, handlers, request);
			out.addRequest(nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([pluginName], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_ACTIVITYPLUGIN, {
				success : function(request, resourceData){
					loc_activityPlugin = resourceData[pluginName][node_COMMONATRIBUTECONSTANT.PLUGINACTIVITY_SCRIPT](nosliw, loc_envObj);
					return loc_activityPlugin;
				}
			}));
			return out;
		}
	};
	
	var loc_out = {
		
		getExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_getExecuteActivityRequest({
				success : function(request, activityTaskResult){
					if(loc_inputDataType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_IODATASET){
						//if input data is io data set, out put is io data set as well
						return new node_ExecutableResult(activityTaskResult.resultName, loc_actvityStatusIO);
					}
					else{
						//otherwise, input data is value, then output is value
						return loc_actvityStatusIO.getGetDataValueRequest(undefined, {
							success : function(request, value){
								return new node_ExecutableResult(activityTaskResult.resultName, value);
							}
						});
						
					}
				}
			}));
			return out;
		},

		executeRequest : function(handlers, request){
			var requestInfo = this.getExecuteRequest(handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		}
			
	};
	
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_ACTIVITY);

	node_getLifecycleInterface(loc_out).init(activityDef, inputData, envObj);

	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.taskUtility", function(){node_taskUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.IOTaskInfo", function(){node_IOTaskInfo = this.getData();});
nosliw.registerSetNodeDataEvent("task.entity.ExecutableResult", function(){node_ExecutableResult = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createActivity", node_createActivity); 

})(packageObj);
