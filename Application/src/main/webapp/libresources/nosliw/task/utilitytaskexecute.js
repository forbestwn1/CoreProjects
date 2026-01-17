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

  var loc_getExecuteInteractiveRequest = function(interactiveEntityCore, taskSetup, adapters, handlers, request){
      var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

	  var interactiveFactory = node_getApplicationInterface(interactiveEntityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_FACTORY);
      if(interactiveFactory!=undefined){
		out.addRequest(interactiveFactory.getCreateEntityRequest());
	  }
	  else{
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(){
			return interactiveEntityCore;
		}));
	  }
	  
	  out.addRequest(node_createServiceRequestInfoCommon(undefined, {
		  success : function(request, entityCore){
				var taskCore = loc_getTaskCoreFromTaskEntityCore(entityCore); 
				taskCore.addTaskSetup(taskSetup);
				//init task
				return taskCore.getTaskInitRequest(undefined, {
					success : function(request){
                        var beforesRequest = node_createServiceRequestInfoSequence(undefined, {
							success : function(request){
                				return taskCore.getTaskExecuteRequest({
				                	success : function(request, result){
                                        var aftersRequest = node_createServiceRequestInfoSequence(undefined, {
											success : function(request){
												return result;
											}
										});
                                        _.each(adapters, function(adapter, i){
                                            var wrapperAdapter = node_getApplicationInterface(adapter, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_ADAPTER_WRAPPER);
                                        	aftersRequest.addRequest(wrapperAdapter.getAfterRequest(result, interactiveEntityCore));
                                        });
                					}
                				});
							}
						});
                        _.each(adapters, function(adapter, i){
                            var wrapperAdapter = node_getApplicationInterface(adapter, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_ADAPTER_WRAPPER);
                        	beforesRequest.addRequest(wrapperAdapter.getBeforeRequest(interactiveEntityCore));
                        });
                        return beforesRequest;
					}
				});
		  }
	  }));
	  
	  return out;
  };


  var loc_getTaskCoreFromTaskEntityCore = function(taskEntityCore){
		var taskEntityCore = node_complexEntityUtility.getCoreBrick(taskEntityCore);
		var taskCoreFacade = node_getApplicationInterface(taskEntityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK);
		var taskCore = taskCoreFacade.getTaskCore();
		return taskCore;
  };

  var loc_discoverTaskAdapters = function(entityCore){
	  var out = [];
		var adapters = node_getEntityTreeNodeInterface(entityCore).getAdapters();
		if(adapterName==undefined){
			//if adapter name not provided, then find adapter for task
			for(var name in adapters){
				var adapter = adapters[name];
				var adapterDef = node_getBasicEntityObjectInterface(adapter).getEntityDefinition();
				var adapterBrickType = adapterDef.getBrickType()[node_COMMONATRIBUTECONSTANT.IDBRICKTYPE_BRICKTYPE]; 
				if(adapterBrickType==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONFORTASK||adapterBrickType==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONFOREXPRESSION){
					out.push(adapter);
				}
			}
		}
	  return out;
  };

  var loc_getTaskAdapter = function(entityCore, adapterName){
		var adapters = node_getEntityTreeNodeInterface(entityCore).getAdapters();
		var taskAdapter;
		if(adapterName==undefined){
			//if adapter name not provided, then find adapter for task
			var adapters = loc_discoverTaskAdapters();
			if(adapters.length==1){
				taskAdapter = adapters[0];
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

    getExecuteInteractiveRequest : function(interactiveEntityCore, taskSetup, adapters, handlers, request){
		return loc_getExecuteInteractiveRequest(interactiveEntityCore, taskSetup, adapters, handlers, request);
	},

    getExecuteInteractiveBrickPackageRequest : function(interactiveBrickPackage, taskSetup, handlers, request){
		
		var adapters = [];
		var interactiveEntityCore = interactiveBrickPackage.getCoreEntity();
		
		_.each(interactiveBrickPackage.getAdapters(), function(adapter){
			adapters.push(adapter);
		});
		
		if(interactiveBrickPackage.getIsAdapterExplicit()==false){
			var a1 = loc_discoverTaskAdapters(interactiveEntityCore);
			if(a1.length==1)   adapters.push(a1[0]);
		}
		
		//child
		var childEntityCorePackage = interactiveBrickPackage.getChildCoreEntityPackage();
		if(childEntityCorePackage!=null){
			interactiveEntityCore = childEntityCorePackage.getCoreEntity();
			
    		if(childEntityCorePackage.getIsAdapterExplicit()==false){
	    		var a2 = loc_discoverTaskAdapters(interactiveEntityCore);
		    	if(a2.length==1)   adapters.push(a2[0]);
		    }
		}
		
		return loc_getExecuteInteractiveRequest(interactiveEntityCore, taskSetup, adapters, handlers, request);
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
