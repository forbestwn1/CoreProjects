//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_makeObjectWithComponentLifecycle;
	var node_makeObjectWithComponentManagementInterface;
	var node_getComponentManagementInterface;
	var node_createComponentCoreComplex;
	var node_componentUtility;
	var node_getComponentLifecycleInterface;
	var node_createServiceRequestInfoSimple;
	var node_createEventObject;
	var node_basicUtility;
	var node_requestServiceProcessor;
	var node_createComponentState;
	
//*******************************************   Start Node Definition  ************************************** 	

//runtime is the one that 
//        expose lifecycle and interface inteface
//        manage backup
//        manage roll back
var node_createComponentRuntime = function(componentCore, configure, componentDecorationInfos, rootView, backupState, request){
	
	var loc_componentCoreComplex;
	var loc_rootView = rootView;
	
	var loc_backupState = backupState;
	var loc_componentStates = [];

	var loc_lifeCycleStatus;
	
	var loc_eventListener = node_createEventObject();

	var loc_init = function(componentCore, configure, componentDecorationInfos, rootView, backupState, request){
		loc_componentCoreComplex = node_createComponentCoreComplex(configure, loc_runtimeEnv);
		loc_componentCoreComplex.setCore(componentCore);
		loc_componentCoreComplex.addDecorations(componentDecorationInfos);
		
		loc_componentStates.push(loc_createComplexLayerState(loc_componentCoreComplex.getCore(), "core"));

		_.each(loc_componentCoreComplex.getDecorations(), function(decoration, i){
			loc_componentStates.push(loc_createComplexLayerState(decoration, "dec_"+decoration.getId()));
		});
	};

	var loc_createComplexLayerState = function(layer, id){
		return node_createComponentState(loc_backupState.createChildState(id), 
			function(handlers, request){  
				return layer.getGetStateDataRequest(handlers, request);
			}, 
			function(stateData, handlers, request){
				return layer.getRestoreStateDataRequest(stateData, handlers, request);
			});
	};
	
	var loc_getSaveStateDataForRollBackRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getSaveStateDataForRollBack", {}), handlers, request);
		_.each(loc_componentStates, function(componentState, i){
			out.addRequest(componentState.getSaveStateDataForRollBackRequest());
		});
		return out;
	};
	
	var loc_getRestoreStateDataForRollBackRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getRestoreStateDataForRollBack", {}), handlers, request);
		_.each(loc_componentStates, function(componentState, i){
			out.addRequest(componentState.getRestoreStateDataForRollBackRequest());
		});
		return out;
	};
	
	var loc_getBackupStateRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getBackupStateRequest", {}), handlers, request);
		_.each(loc_componentStates, function(componentState, i){
			out.addRequest(componentState.getBackupStateRequest(handlers, request));
		});
		return out;
	};

	var loc_getRestoreStateRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getRestoreStateRequest", {}), handlers, request);
		_.each(loc_componentStates, function(componentState, i){
			out.addRequest(componentState.getRestoreStateRequest(handlers, request));
		});
		return out;
	};
	
	//call back when start a statemachine task
	var loc_lifecycleTaskCallback = {
		startTask : function(){		
			_.each(loc_componentStates, function(componentState, i){
				componentState.initDataForRollBack();
			});
		},
		endTask : function(){		
			_.each(loc_componentStates, function(componentState, i){
				componentState.clearDataFroRollBack();
			});
		}
	};
	
	//call back method for normal lifecycle change
	var loc_getNormalLiefCycleCallBackRequestRequest = function(lifecycleName, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("loc_getNormal"+lifecycleName+"LifeCycleCallBackRequestRequest", {}), handlers, request);
		//prepare roll back data
		out.addRequest(loc_getSaveStateDataForRollBackRequest());
		//clear backup state
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){  loc_clearBackupState(request)  }));
		//execute complex lifecycle call back
		out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(lifecycleName));
		return out;
	};
	
	//call back method for normal lifecycle change
	var loc_getReverseLiefCycleCallBackRequestRequest = function(lifecycleName, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("loc_getReverse"+lifecycleName+"LifeCycleCallBackRequestRequest", {}), handlers, request);
		//prepare roll back data
		out.addRequest(loc_getRestoreStateDataForRollBackRequest());
		//clear backup state
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){  loc_clearBackupState(request)  }));
		//execute complex lifecycle call back
		out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(lifecycleName));
		return out;
	};
	
	var loc_clearBackupState = function(request){
		loc_backupState.clear(request);
	};
	
	//component lifecycle call back methods
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ActiveComponentRuntime", {}), undefined, request);
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			var backupStateData = loc_backupState.getStateValue(request);
			if(backupStateData!=undefined){
				//have backup state, then do backup only
				//only call lifecycle, not process
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("loc_getActivateLiefCycleCallBackRequestRequest", {}), undefined, request);
				out.addRequest(loc_getRestoreStateRequest());
				out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESTOREBACKUP));
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){  loc_clearBackupState(request);  }));
				return out;
			}
			else{
				//no backup state, then 
				//normal, call both lifecycle and process
				return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE, undefined, request);
			}
		}));
		return out;
	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE]= function(request){	return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE, undefined, request); };

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("loc_getSuspendLiefCycleCallBackRequestRequest", {}), undefined, request);
		//prepare roll back data
		out.addRequest(loc_getSaveStateDataForRollBackRequest());
		out.addRequest(loc_getBackupStateRequest());
		out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND));
		return out;
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME] = function(request){	return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME, undefined, request);	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY] = function(request){	return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY, undefined, request);	};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE_REVERSE] = function(request){	return loc_getReverseLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE_REVERSE, undefined, request);	};	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE_REVERSE] = function(request){	return loc_getReverseLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE_REVERSE, undefined, request);	};	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND_REVERSE] = function(request){	return loc_getReverseLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND_REVERSE, undefined, request);	};	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME_REVERSE] = function(request){	return loc_getReverseLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME_REVERSE, undefined, request);	};	

	var loc_getComponentCore = function(){   return loc_componentCoreComplex.getCore();   };
	var loc_getTaskEnv = function(){   return loc_componentCoreComplex.getInterface();    };

	
	
	
	
	var loc_getContextIODataSet = function(){  return loc_getComponentCore().getContextIODataSet();   };


	//runtime environment provided for component complex
	var loc_runtimeEnv = {
		//process request
		processRequest : function(request){  node_requestServiceProcessor.processRequest(request); },

		getLifecycleStatus : function(){   return loc_lifeCycleStatus;     },
		
		getTaskEnv : function(){    return loc_getTaskEnv();    },
		
		getContextIODataSet : function(){   return loc_getContextIODataSet();   }, 
		
		getVariableGroup : function(){}
	};

	var loc_out = {
		
		getInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("InitUIModuleRuntime", {}), handlers, request);
			out.addRequest(loc_componentCoreComplex.getUpdateViewRequest(loc_rootView, {
				success : function(request, view){
					loc_getComponentCore().setRootView(view);
				}
			}));
			out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT));

			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				loc_lifeCycleStatus = node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT;
			}));
			
			return out;
		},

		getUpdateSystemDataRequest : function(domain, systemData, handlers, request){
			return loc_getComponentCore().getUpdateSystemDataRequest(domain, systemData, handlers, request);
		},
		
		//component management interface 
		getContextIODataSet :  function(){  return loc_getContextIODataSet();  },
		getExecuteCommandRequest : function(command, parms, handlers, request){
			var out = node_componentUtility.getProcessNosliwCommand(loc_out, command, parms, handlers, request);
			if(out==undefined){
				out = loc_componentCoreComplex.getExecuteCommandRequest(command, parms, handlers, request);    
			}
			return out;
		},
		registerEventListener : function(listener, handler, thisContext){  return loc_componentCoreComplex.registerEventListener(listener, handler, thisContext);   },
		unregisterEventListener : function(listener){   return loc_componentCoreComplex.unregisterEventListener(listener);  },
		registerValueChangeEventListener : function(listener, handler, thisContext){   return loc_componentCoreComplex.registerValueChangeEventListener(listener, handler, thisContext);   },
		unregisterValueChangeEventListener : function(listener){   return loc_componentCoreComplex.unregisterValueChangeEventListener(listener);    }
	};
	
	loc_init(componentCore, configure, componentDecorationInfos, rootView, backupState, request);
	
	loc_out = node_makeObjectWithComponentLifecycle(loc_out, lifecycleCallback, loc_lifecycleTaskCallback, loc_out);
	//listen to lifecycle event and update lifecycle status
	node_getComponentLifecycleInterface(loc_out).registerEventListener(loc_eventListener, function(eventName, eventData, request){
		if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION){
			loc_lifeCycleStatus = eventData.to;
		}
	});
	
	loc_out = node_makeObjectWithComponentManagementInterface(loc_out, loc_out, loc_out);

	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentLifecycle", function(){node_makeObjectWithComponentLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentManagementInterface", function(){node_makeObjectWithComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentCoreComplex", function(){node_createComponentCoreComplex = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentState", function(){node_createComponentState = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentRuntime1", node_createComponentRuntime); 

})(packageObj);
