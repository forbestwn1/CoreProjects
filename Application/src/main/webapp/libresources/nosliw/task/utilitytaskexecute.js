//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_basicUtility;
	var node_getObjectType;
	var node_ResourceId;
	var node_resourceUtility;
	var node_createConfigure;
	var node_getEntityTreeNodeInterface;
	var node_namingConvensionUtility;
	var node_complexEntityUtility;
	var node_getApplicationInterface;
	var node_getEntityObjectInterface;
	var node_getBasicEntityObjectInterface;

//*******************************************   Start Node Definition  ************************************** 	

var node_taskExecuteUtility = function(){

  var loc_getTaskCoreFromTaskEntityCore = function(taskEntityCore){
		var taskEntityCore = node_complexEntityUtility.getCoreEntity(taskEntityCore);
		var taskCoreFacade = node_getApplicationInterface(taskEntityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK);
		var taskCore = taskCoreFacade.getTaskCore();
		return taskCore;
  };

  var loc_getTaskAdapter = function(entityCore, adapterName){
		var adapters = node_getEntityTreeNodeInterface(entityCore).getAdapters();
		var taskAdapter;
		if(adapterName==undefined){
			//if adapter name not provided, then find adapter for task
			for(var name in adapters){
				var adapter = adapters[name];
				var adapterDef = node_getBasicEntityObjectInterface(adapter).getEntityDefinition();
				var adapterBrickType = adapterDef.getBrickType()[node_COMMONATRIBUTECONSTANT.IDBRICKTYPE_BRICKTYPE]; 
				if(adapterBrickType==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONFORTASK||adapterBrickType==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONFOREXPRESSION){
					taskAdapter = adapter;
				}
			}
		}
		else{
			taskAdapter = adapters[adapterName];
		}
		return taskAdapter;
  };
  
  var loc_getExecuteTaskRequest = function(taskCore, taskSetup, onInitTaskRequest, onFinishTaskRequest, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		if(onInitTaskRequest==undefined){
			onInitTaskRequest = function(handlers, request){
				return node_createServiceRequestInfoSimple(undefined, function(){}, handlers, request);
			}
		}

		if(onFinishTaskRequest==undefined){
			onFinishTaskRequest = function(taskResult, handlers, request){
				return node_createServiceRequestInfoSimple(undefined, function(){}, handlers, request);
			}
		}
		
		//task init
		taskCore.addTaskSetup(taskSetup);
		out.addRequest(taskCore.getTaskInitRequest());

		out.addRequest(onInitTaskRequest({
			success : function(request){
				return taskCore.getTaskExecuteRequest({
					success : function(request, taskResult){
						return onFinishTaskRequest(taskResult, {
							success : function(request){
								return taskResult;
							}
						});
					}
				});
			}
		}));
		return out;		
  };
  
  var loc_out = {
	  
	registerTaskLifecycleEventListener : function(taskEntityCore, listenerEventObj, handler, thisContext){
		var taskCore = loc_getTaskCoreFromTaskEntityCore(taskEntityCore); 
        taskCore.registerLifecycleEventListener(listenerEventObj, handler, thisContext);
	},
	  
	getExecuteEntityTaskRequest : function(entityCore, taskSetup, onInitTaskRequest, onFinishTaskRequest, handlers, request){
		return loc_getExecuteTaskRequest(loc_getTaskCoreFromTaskEntityCore(entityCore), taskSetup, onInitTaskRequest, onFinishTaskRequest, handlers, request);
	},

	getExecuteEntityTaskWithAdapterRequest : function(entityCore, adapterName, taskSetup, handlers, request){
		var taskAdapter = loc_getTaskAdapter(entityCore, adapterName);
		
		if(taskAdapter!=undefined){
			return taskAdapter.getExecuteTaskRequest(entityCore, taskSetup, handlers, request);
		}
		else{
			return this.getExecuteEntityTaskRequest(entityCore, taskSetup, undefined, undefined, handlers, request);
		}
	},

	getExecuteWrapperedTaskRequest : function(wrapperCore, taskSetup, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		var taskFactory = node_getApplicationInterface(wrapperCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_FACTORY);
		out.addRequest(taskFactory.getCreateEntityRequest({
			success : function(request, entityCore){
				var taskCore = loc_getTaskCoreFromTaskEntityCore(entityCore); 
				taskCore.addTaskSetup(taskSetup);
				return loc_getExecuteTaskRequest(taskCore, taskSetup);
			}
		}));

		return out;		
	},
	
	getExecuteWrapperedTaskWithAdapterRequest : function(wrapperCore, adapterName, taskSetup, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		var taskFactory = node_getApplicationInterface(wrapperCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_FACTORY);
		out.addRequest(taskFactory.getCreateEntityRequest({
			success : function(request, entityCore){
				var taskCore = loc_getTaskCoreFromTaskEntityCore(entityCore); 
				taskCore.addTaskSetup(taskSetup);
				return loc_out.getExecuteEntityTaskWithAdapterRequest(entityCore, adapterName);
			}
		}));

		return out;		
	},

  };

  return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){	node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.getApplicationInterface", function(){node_getApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getBasicEntityObjectInterface", function(){node_getBasicEntityObjectInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("taskExecuteUtility", node_taskExecuteUtility); 

})(packageObj);
