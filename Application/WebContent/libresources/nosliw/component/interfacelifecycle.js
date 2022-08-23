//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_buildInterface;
	var node_getInterface;
	var node_eventUtility;
	var node_getObjectName;
	var node_getObjectType;
	var node_requestUtility;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSimple;
	var node_createStateMachine;
	var node_SMCommandInfo;
	var createStateMachineDef;
	var node_SMTransitInfo;
	var node_StateTransitPath;
	var node_requestServiceProcessor;

//*******************************************   Start Node Definition  ************************************** 	

var INTERFACENAME = "componentLifecycle";

/**
 * INIT : 
 * 		this is the init state of statemachine, so component should be initilized before enter this state.
 * 		there is no callback for entering this state 
 * 		during initialization, all the one time task should be done here 
 * ACTIVE : component is working
 * SUSPENDED: component working paused
 * DEAD: component dead
 */


/*
 * utility functions to build lifecycle object for component
 */
var node_makeObjectWithComponentLifecycle = function(componentCoreComplex){
	return node_buildInterface(componentCoreComplex, INTERFACENAME, loc_createComponentLifecycle(componentCoreComplex));
};
	
var node_getComponentLifecycleInterface = function(baseObject){
	return node_getInterface(baseObject, INTERFACENAME);
};

var node_createLifeCycleEntity = function(id){
	
	var loc_id = id;
	
	var loc_lifecycleEntity;
	var loc_children = {};
	
	var loc_out = {
		
		getId : function(){    return loc_id;    },	
		
		setComponentCore : function(componentCore){ 
			loc_lifecycleEntity = loc_createComponentLifecycle(componentCore);
		},
		
		getChildren : function(){   return loc_children;   },
		getAllDescendants : function(){  
			var out = [];
			_.each(loc_children, function(child){
				out.push(child);
				var childDescendants = child.getAllDescendants();
				out = out.concat(childDescendants);
			});
			return out;
		},
		
		createChild : function(id){
			out = node_createLifeCycleEntity(id);
			loc_children[id] = out;
			return out;
		},
		
		getLifeCycleRequest : function(transitName, handlers, request){
			
			
			
			var all = [];
			
			_.each(loc_children, function(child){
				all.push(child);
				var children = child.getChildren();
			});
		}
	};
	
	return loc_out;
};

var loc_stateMachineDef = node_createStateMachineDef(
	[
		new node_SMTransitInfo(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT),
		new node_SMTransitInfo(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE),
		new node_SMTransitInfo(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD),
		new node_SMTransitInfo(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED),
		new node_SMTransitInfo(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT),
		new node_SMTransitInfo(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE),
	],
	
	[
		new node_SMCommandInfo(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_COMMAND_ACTIVATE, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE]),
		new node_SMCommandInfo(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_COMMAND_DEACTIVATE, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT]),
		new node_SMCommandInfo(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_COMMAND_SUSPEND, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED]),
		new node_SMCommandInfo(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_COMMAND_RESUME, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE]),
	
		new node_SMCommandInfo(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_COMMAND_DESTROY, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD]),
		new node_SMCommandInfo(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_COMMAND_DESTROY, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD]),
		new node_SMCommandInfo(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_COMMAND_DESTROY, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD]),
		
		new node_SMCommandInfo(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_COMMAND_RESTART, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE]),
		new node_SMCommandInfo(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_COMMAND_RESTART, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE]),
	]
);


//lifecycle interface
var loc_createComponentLifecycle = function(componentCoreComplex){
	
	var loc_componentCoreComplex = componentCoreComplex;
	
	//state
	var loc_componentState = node_createComponentState(loc_componentCoreComplex.getState(), 
		function(handlers, request){
			return loc_componentCoreComplex.getGetStateDataRequest(handlers, request);
		},
		function(stateData, handlers, request){
			return loc_componentCoreComplex.getRestoreStateDataRequest(stateData, handlers, request);
		}
	);

	var loc_stateMachine;

	//init statemachine for component
	var loc_init = function(){

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
		
		
		//call back when start a statemachine task
		var taskCallback = {
			startTask : function(){		
				loc_componentState.initDataForRollBack();
			},
			endTask : function(){		
				loc_componentState.clearDataFroRollBack();
			}
		};

		//build statemachine, start with init status
		var stateMachine = node_createStateMachine(loc_stateMachineDef, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, taskCallback, loc_componentCoreComplex);
		//register callback
		//state transit callback method name follow naming conversion: from_to  or  _from_to for reverse 
		_.each(stateMachine.getStateDefinition().getAllTransits(), function(transit, i){	
			var from = transit.from;
			var to = transit.to;
			stateMachine.registerTransitCallBack(transit, 
				function(){
					var fun = lifecycleCallback[transit.from+"_"+transit.to];
					if(fun!=undefined)   return fun.apply(loc_thisContext, arguments);
					else  return true;
				}, 
				function(){
					var fun = lifecycleCallback["_"+transit.from+"_"+transit.to];
					if(fun!=undefined)   return fun.apply(loc_thisContext, arguments);
					else  return true;
				});
		});
		
		
		loc_stateMachine = node_createStateMachineWrapper(stateMachine);
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
	
	//call back method for reverse lifecycle change
	var loc_getReverseLiefCycleCallBackRequestRequest = function(lifecycleName, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("loc_getReverse"+lifecycleName+"LifeCycleCallBackRequestRequest", {}), handlers, request);
		//roll back data from state
		out.addRequest(loc_getRestoreStateDataForRollBackRequest());
		//clear backup state
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){  loc_clearBackupState(request)  }));
		//execute complex lifecycle call back
		out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(lifecycleName));
		return out;
	};

	var loc_getSaveStateDataForRollBackRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getSaveStateDataForRollBack", {}), handlers, request);
		out.addRequest(loc_componentState.getSaveStateDataForRollBackRequest());
		return out;
	};
	
	var loc_getRestoreStateDataForRollBackRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getRestoreStateDataForRollBack", {}), handlers, request);
		out.addRequest(loc_componentState.getRestoreStateDataForRollBackRequest());
		return out;
	};
	
	var loc_getBackupStateRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getBackupStateRequest", {}), handlers, request);
		out.addRequest(loc_componentState.getBackupStateRequest(handlers, request));
		return out;
	};

	var loc_getRestoreStateRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getRestoreStateRequest", {}), handlers, request);
		out.addRequest(loc_componentState.getRestoreStateRequest(handlers, request));
		return out;
	};
	
	var loc_clearBackupState = function(request){
		loc_backupState.clear(request);
	};

	var loc_out = {

		transit : function(commandName, request){
			var task = loc_stateMachine.newTask(commandName);
			if(task!=undefined)  	return task.process(request);
		},
		getTransitRequest : function(commandName, handlers, request){
			var task = loc_stateMachine.newTask(commandName);
			if(task!=undefined)  	return task.getProcessRequest(handlers, request);
		},
		executeTransitRequest : function(commandName, handlers, request){
			var request = loc_out.getTransitRequest(commandName, handlers, request);
			node_requestServiceProcessor.processRequest(request);
		},
			
		getStateMachine : function(){  return loc_stateMachine;   },
		getComponentStatus : function(){		return loc_stateMachine.getCurrentState();		},
		isActive : function(){  return this.getComponentStatus()==node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE;    },
		
		registerEventListener : function(listener, handler, thisContext){	return loc_stateMachine.prv_registerEventListener(listener, handler, thisContext);	},
		unregisterEventListener : function(listener){  loc_stateMachine.prv_unregisterEventListener(listener);  },

		bindBaseObject : function(baseObject){		loc_baseObject = baseObject;	}
	};

	loc_init();
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithname.getObjectName", function(){node_getObjectName = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("statemachine.createStateMachine", function(){node_createStateMachine = this.getData();	});
nosliw.registerSetNodeDataEvent("statemachine.SMCommandInfo", function(){node_SMCommandInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("statemachine.createStateMachineDef", function(){node_createStateMachineDef = this.getData();	});
nosliw.registerSetNodeDataEvent("statemachine.SM TransitInfo", function(){node_SMTransitInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("statemachine.StateTransitPath", function(){node_StateTransitPath = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});


//Register Node by Name
packageObj.createChildNode("makeObjectWithComponentLifecycle", node_makeObjectWithComponentLifecycle); 
packageObj.createChildNode("getComponentLifecycleInterface", node_getComponentLifecycleInterface); 

})(packageObj);
