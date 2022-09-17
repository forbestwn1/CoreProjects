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
	var node_createServiceRequestInfoError;
	var node_createStateMachine;
	var node_createStateMachineDef;
	var node_SMCommandInfo;
	var node_SMTransitDefinition;
	var node_StateTransitPath;
	var node_requestServiceProcessor;
	var node_createComponentState;
	var node_createStateMachineTask;

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
	return node_buildInterface(componentCoreComplex, INTERFACENAME, loc_createComponentLifecycleEntity(componentCoreComplex));
};
	
var node_getComponentLifecycleInterface = function(baseObject){
	return node_getInterface(baseObject, INTERFACENAME);
};


var node_createLifecycleTask = function(next, rootComponent, request){
	var lifecycleEntity = rootComponent.getLifecycleEntity();

	var currentStatus = lifecycleEntity.getStateMachine().getCurrentState();
	//interprect next to array nexts
	var nexts = node_getStateMachineDefinition().processNext(currentStatus, next);
	//process init-active 
	if(currentStatus==node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT && nexts[0]==node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE){
		var backupStateData = rootComponent.getBackupState().getStateValue(request);
		if(backupStateData!=undefined){
			nexts = [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED].concat(nexts);
		}
	}
	
	var descendants = lifecycleEntity.getAllDescendants();
	return node_createStateMachineTask(nexts, descendants, {
		transitSuccess : function(transit){
			if(!(transit.from==node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE&&transit.to==node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED)){
				rootComponent.getBackupState().clear();
			}
		},
		transitFail : function(transit){
			
		},
		transitRollback : function(transit){
			
		},
	});
};


var node_getStateMachineDefinition = function(){
	var loc_stateMachineDef = node_createStateMachineDef(
			[
				new node_SMTransitDefinition(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT),
				new node_SMTransitDefinition(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED, false),
				new node_SMTransitDefinition(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE),
				new node_SMTransitDefinition(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD),
				new node_SMTransitDefinition(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED),
				new node_SMTransitDefinition(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT),
				new node_SMTransitDefinition(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE),
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
	return loc_stateMachineDef;
};


//lifecycle runtime context in component tree
var node_createLifeCycleRuntimeContext = function(id){
	
	var loc_id = id;
	
	var loc_lifecycleEntity;
	var loc_dumyStateMachine;
	var loc_children = {};
	
	var loc_getDumyStateMachine = function(){
		if(loc_dumyStateMachine==undefined)		loc_dumyStateMachine = node_createStateMachine(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, undefined, loc_id);
		return loc_dumyStateMachine;
	};
	
	var loc_out = {
		
		prv_getAllDescendants : function(outArray){ 
			//only with lifecycle entity
			outArray.push(this);
			_.each(loc_children, function(child){
				child.prv_getAllDescendants(outArray);
			});
		},
			
		setComponentCore : function(componentCore){  loc_lifecycleEntity = loc_createComponentLifecycleEntity(componentCore, loc_id);	},
		
		getId : function(){    return loc_id;    },	
		
		getStateMachine : function(){
			if(loc_lifecycleEntity!=undefined)		return loc_lifecycleEntity.getStateMachine();
			else		return loc_getDumyStateMachine();
		},
		
		getTaskCallBack : function(){  return loc_lifecycleEntity==undefined?undefined:loc_lifecycleEntity.getTaskCallBack();  },
		
		getChildren : function(){   return loc_children;   },
		createChild : function(id){
			out = node_createLifeCycleRuntimeContext(id);
			loc_children[id] = out;
			return out;
		},

		getAllDescendants : function(){
			var out = [];
			this.prv_getAllDescendants(out);
			return out;
		},
	};
	
	return loc_out;
};

//lifecycle entity 
var loc_createComponentLifecycleEntity = function(componentCoreComplex, id){
	
	var loc_componentCoreComplex = componentCoreComplex;

	var loc_stateMachine;

	//state
	var loc_componentState = node_createComponentState(loc_componentCoreComplex.getBackupState(), 
		function(handlers, request){
			return loc_componentCoreComplex.getGetStateDataRequest==undefined?undefined:loc_componentCoreComplex.getGetStateDataRequest(handlers, request);
		},
		function(stateData, handlers, request){
			return loc_componentCoreComplex.getRestoreStateDataRequest==undefined?undefined:loc_componentCoreComplex.getRestoreStateDataRequest(stateData, handlers, request);
		}
	);

	//init statemachine for component
	var loc_init = function(componentCoreComplex, id){

		//component lifecycle call back methods
		var lifecycleCallback = {};

		lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESTORE] = function(request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("loc_getActivateLiefCycleCallBackRequestRequest", {}), undefined, request);
			//prepare roll back data
			out.addRequest(loc_getSaveStateDataForRollBackRequest());
			//restore back up data
			out.addRequest(loc_getRestoreStateRequest());
			//clear back up data
//			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){  loc_clearBackupState(request);  }));
			//component job
			out.addRequest(loc_getComponentLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESTORE));
			return out;
		};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND_REVERSE] = function(request){	return loc_getReverseLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESOURCE_REVERSE, undefined, request);	};	

		lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND] = function(request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("loc_getSuspendLiefCycleCallBackRequestRequest", {}), undefined, request);
			//prepare roll back data
			out.addRequest(loc_getSaveStateDataForRollBackRequest());
			//save back up data
			out.addRequest(loc_getBackupStateRequest());
			//component job
			out.addRequest(loc_getComponentLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND));
			return out;
		};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND_REVERSE] = function(request){	return loc_getReverseLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND_REVERSE, undefined, request);	};	
		
		lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE] = function(request){  return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE, undefined, request);  }
		lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE_REVERSE] = function(request){	return loc_getReverseLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE_REVERSE, undefined, request);	};	

		lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE]= function(request){	return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE, undefined, request); };
		lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE_REVERSE] = function(request){	return loc_getReverseLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE_REVERSE, undefined, request);	};	
		
		lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME] = function(request){	return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME, undefined, request);	};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME_REVERSE] = function(request){	return loc_getReverseLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME_REVERSE, undefined, request);	};	
		
		lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY] = function(request){	return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY, undefined, request);	};


		//build statemachine, start with init status
		loc_stateMachine = node_createStateMachine(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, loc_componentCoreComplex, id);
		//register callback
		//state transit callback method name follow naming conversion: from_to  or  _from_to for reverse 
		_.each(node_getStateMachineDefinition().getAllTransits(), function(transit, i){	
			var from = transit.from;
			var to = transit.to;
			loc_stateMachine.registerTransitCallBack(transit, 
				function(){
					var fun = lifecycleCallback[transit.from+"_"+transit.to];
					if(fun!=undefined)   return fun.apply(loc_componentCoreComplex, arguments);
					else  return true;
				}, 
				function(){
					var fun = lifecycleCallback["_"+transit.from+"_"+transit.to];
					if(fun!=undefined)   return fun.apply(loc_componentCoreComplex, arguments);
					else  return true;
				});
		});
		
	};

	var loc_getComponentLifeCycleRequest = function(lifecycleName, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			var componentLifecycleResule = loc_componentCoreComplex.getLifeCycleRequest(lifecycleName);
			var entityType = node_getObjectType(componentLifecycleResule);
			if(node_CONSTANT.TYPEDOBJECT_TYPE_ERROR==entityType){
				return node_createServiceRequestInfoError(componentLifecycleResule);
			}
			else if(node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==entityType){
				return componentLifecycleResule;
			}
		}));
		
		return out;
	};
	
	//call back method for normal lifecycle change
	var loc_getNormalLiefCycleCallBackRequestRequest = function(lifecycleName, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("loc_getNormal"+lifecycleName+"LifeCycleCallBackRequestRequest", {}), handlers, request);
		//prepare roll back data
		out.addRequest(loc_getSaveStateDataForRollBackRequest());
		//clear backup state
//		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){  loc_clearBackupState(request)  }));
		//execute complex lifecycle call back
		out.addRequest(loc_getComponentLifeCycleRequest(lifecycleName));
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
		out.addRequest(loc_getComponentLifeCycleRequest(lifecycleName));
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
		out.addRequest(loc_componentState.getBackupStateRequest());
		return out;
	};

	var loc_getRestoreStateRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getRestoreStateRequest", {}), handlers, request);
		out.addRequest(loc_componentState.getRestoreStateRequest());
		return out;
	};
	
	var loc_clearBackupState = function(request){
		loc_componentCoreComplex.getBackupState().clear(request);
	};

	var loc_out = {

		getStateMachine : function(){  return loc_stateMachine;   },

		getTaskCallBack : function(){  
			//call back when start a statemachine task
			return {
				startTask : function(){		
					loc_componentState.initDataForRollBack();
				},
				endTask : function(){		
					loc_componentState.clearDataFroRollBack();
				}
			};
		},
		
		
//		transit : function(commandName, request){
//			var task = loc_stateMachine.newTask(commandName);
//			if(task!=undefined)  	return task.process(request);
//		},
//		getTransitRequest : function(commandName, handlers, request){
//			var task = loc_stateMachine.newTask(commandName);
//			if(task!=undefined)  	return task.getProcessRequest(handlers, request);
//		},
//		executeTransitRequest : function(commandName, handlers, request){
//			var request = loc_out.getTransitRequest(commandName, handlers, request);
//			node_requestServiceProcessor.processRequest(request);
//		},
//		 	
//		getComponentStatus : function(){		return loc_stateMachine.getCurrentState();		},
//		isActive : function(){  return this.getComponentStatus()==node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE;    },
//		
//		registerEventListener : function(listener, handler, thisContext){	return loc_stateMachine.prv_registerEventListener(listener, handler, thisContext);	},
//		unregisterEventListener : function(listener){  loc_stateMachine.prv_unregisterEventListener(listener);  },
//
//		bindBaseObject : function(baseObject){		loc_baseObject = baseObject;	}
	};

	loc_init(componentCoreComplex, id);
	
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoError", function(){node_createServiceRequestInfoError = this.getData();});
nosliw.registerSetNodeDataEvent("statemachine.createStateMachine", function(){node_createStateMachine = this.getData();	});
nosliw.registerSetNodeDataEvent("statemachine.SMCommandInfo", function(){node_SMCommandInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("statemachine.createStateMachineDef", function(){node_createStateMachineDef = this.getData();	});
nosliw.registerSetNodeDataEvent("statemachine.SMTransitDefinition", function(){node_SMTransitDefinition = this.getData();	});
nosliw.registerSetNodeDataEvent("statemachine.StateTransitPath", function(){node_StateTransitPath = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentState", function(){node_createComponentState = this.getData();});
nosliw.registerSetNodeDataEvent("statemachine.createStateMachineTask", function(){node_createStateMachineTask = this.getData();});

//Register Node by Name
packageObj.createChildNode("makeObjectWithComponentLifecycle", node_makeObjectWithComponentLifecycle); 
packageObj.createChildNode("getComponentLifecycleInterface", node_getComponentLifecycleInterface); 
packageObj.createChildNode("createLifeCycleRuntimeContext", node_createLifeCycleRuntimeContext); 
packageObj.createChildNode("getStateMachineDefinition", node_getStateMachineDefinition); 
packageObj.createChildNode("createLifecycleTask", node_createLifecycleTask); 


})(packageObj);
