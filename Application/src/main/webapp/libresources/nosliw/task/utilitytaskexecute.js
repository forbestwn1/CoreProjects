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
	var node_createServiceRequestInfoCommon;

//*******************************************   Start Node Definition  ************************************** 	

var node_taskExecuteUtility = function(){

  var loc_discoverInteractiveAdapters = function(entityCore, adapterNames, isExplicit){
	  var out = [];
		var adapters = node_getEntityTreeNodeInterface(entityCore).getAdapters();
		
		if(adapterNames!=undefined&&adapterNames.length>0){
			_.each(adapterNames, function(adapterName, i){
				out.push(adapters[adapterName]);
			});
		}
		else{
    		if(isExplicit==false||isExplicit==undefined){
                //implied adapter			
    			for(var name in adapters){
	    			var adapter = adapters[name];
		    		var adapterDef = node_getBasicEntityObjectInterface(adapter).getEntityDefinition();
			    	var adapterBrickType = adapterDef.getBrickType()[node_COMMONATRIBUTECONSTANT.IDBRICKTYPE_BRICKTYPE]; 
				    if(adapterBrickType==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONFORTASK||adapterBrickType==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONFOREXPRESSION){
					    out.push(adapter);
				    }
			    }
	    	}
		}
	  return out;
  };

  var loc_getInteractiveEntityCoreRequest = function(interactiveBrickPackage, handlers, request){
      var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

      var coreEntityRef = interactiveBrickPackage.getCoreEntityReference();
      var baseCoreEntity = coreEntityRef.getBaseCoreEntity();
      var remainingPath = coreEntityRef.getRemainingPath();
      if(remainingPath!=undefined&&remainingPath==node_COMMONATRIBUTECONSTANT.BLOCKTASKWRAPPER_TASK){
    	  var interactiveFactory = node_getApplicationInterface(baseCoreEntity, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_FACTORY);
          out.addRequest(interactiveFactory.getCreateEntityRequest());
	  }
	  else{
          out.addRequest(getInteractiveEntityCoreRequest.addRequest(node_createServiceRequestInfoSimple(undefined, function(){
			  return baseCoreEntity;
		  })));
	  }
	  return out;
  };


  var loc_buildInteractiveInfoRequest = function(interactiveBrickPackage, interactiveInfo, handlers, request){

        if(interactiveInfo==undefined){
			interactiveInfo = {
				adapters : []
			};
		}

		//check base first
		var baseEntityCorePackage = interactiveBrickPackage.getBaseCoreEntityPackage();
		if(baseEntityCorePackage!=undefined){
			var adapters = loc_discoverInteractiveAdapters(interactiveBrickPackage.getCoreEntityReference().getBaseCoreEntity(), interactiveBrickPackage.getAdapterInfo().getAdapterNames(), interactiveBrickPackage.getAdapterInfo().getIsAdapterExplicit());
			_.each(adapters, function(adapter){
				interactiveInfo.adapters.push({
					adapter : adapter,
					baseEntityCore : interactiveBrickPackage.getCoreEntityReference().getBaseCoreEntity()
				});
			});
			return loc_buildInteractiveInfoRequest(baseEntityCorePackage, interactiveInfo, handlers, request);
		}
		else{
            var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_getInteractiveEntityCoreRequest(interactiveBrickPackage, {
				success : function(request, interactiveCoreEntity){
					interactiveInfo.coreEntity = interactiveCoreEntity;
         			var adapters = loc_discoverInteractiveAdapters(interactiveCoreEntity, interactiveBrickPackage.getAdapterInfo().getAdapterNames(), interactiveBrickPackage.getAdapterInfo().getIsAdapterExplicit());
        			_.each(adapters, function(adapter){
		    		    interactiveInfo.adapters.push({
        					adapter : adapter,
		        			baseEntityCore : interactiveCoreEntity
				        });
			        });
					return interactiveInfo;
				}
			}));
			return out;
		}
  };

  var loc_getTaskCoreFromTaskEntityCore = function(taskEntityCore){
		var taskEntityCore = node_complexEntityUtility.getCoreBrick(taskEntityCore);
		var taskCoreFacade = node_getApplicationInterface(taskEntityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK);
		var taskCore = taskCoreFacade.getTaskCore();
		return taskCore;
  };

  var loc_getExecuteInteractiveEntityCoreRequest = function(interactiveEntityCore, taskSetup, adapters, handlers, request){
      var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

    	var taskCore = loc_getTaskCoreFromTaskEntityCore(interactiveEntityCore); 
		taskCore.addTaskSetup(taskSetup);
		//init task
		out.addRequest(taskCore.getTaskInitRequest(undefined, {
			success : function(request){
                var executeRequest = node_createServiceRequestInfoSequence();

                _.each(adapters, function(adapterInfo, i){
                    var wrapperAdapter = node_getApplicationInterface(adapterInfo.adapter, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_ADAPTER_WRAPPER);
                   	executeRequest.addRequest(wrapperAdapter.getBeforeRequest(adapterInfo.baseEntityCore));
                });

			    executeRequest.addRequest(taskCore.getTaskExecuteRequest({
               	    success : function(request, result){
                        var aftersRequest = node_createServiceRequestInfoSequence(undefined, {
    				        success : function(request){
						        return result;
					        }
				        });
                        _.each(adapters, function(adapterInfo, i){
                            var wrapperAdapter = node_getApplicationInterface(adapterInfo.adapter, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_ADAPTER_WRAPPER);
                               	aftersRequest.addRequest(wrapperAdapter.getAfterRequest(result, adapterInfo.baseEntityCore));
                        });
                        return aftersRequest;
              		}
           		}));

                return executeRequest;
			}
		}));

	  return out;
  };





  var loc_getTaskAdapter = function(entityCore, adapterName){
		var adapters = node_getEntityTreeNodeInterface(entityCore).getAdapters();
		var taskAdapter;
		if(adapterName==undefined){
			//if adapter name not provided, then find adapter for task
			var adapters = loc_discoverInteractiveAdapters();
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

    getExecuteInteractiveRequest1 : function(interactiveEntityCore, taskSetup, adapters, handlers, request){
		return loc_getExecuteInteractiveRequest(interactiveEntityCore, taskSetup, adapters, handlers, request);
	},

    getExecuteInteractiveBrickPackageRequest : function(interactiveBrickPackage, taskSetup, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		out.addRequest(loc_buildInteractiveInfoRequest(interactiveBrickPackage, undefined, {
			success : function(request, interactiveInfo){
                return loc_getExecuteInteractiveEntityCoreRequest(interactiveInfo.coreEntity, taskSetup, interactiveInfo.adapters);			
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){	node_createServiceRequestInfoCommon = this.getData();	});
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
