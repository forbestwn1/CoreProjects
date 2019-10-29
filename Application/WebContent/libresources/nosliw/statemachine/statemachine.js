//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_createEventObject;
	var node_requestUtility;
	var node_requestServiceProcessor;
	var node_getObjectType;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createServiceRequestInfoCommon;
	var node_ServiceRequestExecuteInfo;
	var node_buildServiceProvider;
	var node_RequestResult;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createStateMachineTask = function(nexts, stateMachine){

	var loc_stateMachine = stateMachine;

	var loc_nexts = [];
	loc_nexts.push(loc_stateMachine.getCurrentState());
	_.each(nexts, function(next, i){loc_nexts.push(next);});
	
	var loc_eventObj = node_createEventObject();
	
	var loc_currentNext = 0;

	var loc_processNext = function(request){
		loc_currentNext++;
		if(loc_currentNext>=loc_nexts.length){
			//finish
			loc_stateMachine.prv_finishTask();
			loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION, undefined, request);
		}
		else{
			var listener = loc_stateMachine.prv_registerEventListener(undefined, function(eventName, eventData, request){
				loc_stateMachine.prv_unregisterEventListener(listener);
				if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION){
					loc_processNext(request);
				}
				else if (eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION || eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION){
					loc_currentNext = loc_currentNext - 2;
					node_requestServiceProcessor.processRequest(loc_getRollBackRequest({
						success : function(request){
							loc_stateMachine.prv_finishTask();
							//finish
							loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION, eventData, request);
						}
					}, request));
				}
			});
			loc_stateMachine.prv_startTransit(loc_nexts[loc_currentNext], request);
		}
	};
	
	var loc_getRollBackRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request); 
		while(loc_currentNext>=0){
			out.addRequest(loc_stateMachine.prv_getRollBackRequest(loc_nexts[loc_currentNext], request));
			loc_currentNext--;
		};
		return out;
	};
	
	var loc_trigueEvent = function(eventName, eventData, request){	loc_eventObj.triggerEvent(eventName, eventData, request);	};
	
	var loc_out = {
		
		process : function(request){	
			var request = loc_out.getRequestInfo(request);
			loc_processNext(request);
			return request;
		},
		
		getProcessRequest : function(handlers, request){
			var request = loc_out.getRequestInfo(request);
			var out = node_createServiceRequestInfoCommon(undefined, handlers, request);
			out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(request){
				var listener = loc_out.registerEventListener(undefined, function(eventName, eventData, eventRequest){
					if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION){
						out.successFinish();
					}
					else if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION){
						if(eventData.type==node_CONSTANT.REQUEST_FINISHTYPE_ERROR) 	out.errorFinish(eventData.data);
						else if(eventData.type==node_CONSTANT.REQUEST_FINISHTYPE_EXCEPTION) 	out.exceptionFinish(eventData.data);
						else out.errorFinish();
					}
					loc_out.unregisterEventListener(listener);
				});

				loc_processNext(request);
			}));
			return out;
		},
		
		transitSuccess : function(request){   loc_stateMachine.prv_transitSuccess(request);	},
		transitFail : function(request){   loc_stateMachine.prv_transitFail(request);	},

		registerEventListener : function(listener, handler, thisContext){	return loc_eventObj.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventObj.unregister(listener); },
	};
	
	loc_out = node_buildServiceProvider(loc_out, "stateMachineTask");

	return loc_out;
};

var node_createStateMachine = function(stateDef, initState, taskCallback, thisContext){

	//all the state definition
	var loc_stateDef = stateDef;
	var loc_taskCallback = taskCallback;
	
	var loc_thisContext = thisContext;

	var loc_eventObj = node_createEventObject();

	var loc_currentTask;
	
	var loc_currentState = initState;
	//if in stranist, it contains from status and to status
	var loc_inTransit = undefined;
	var loc_finishTransit = true;
	
	var loc_trigueEvent = function(eventName, eventData, request){	loc_eventObj.triggerEvent(eventName, eventData, request);	};

	var loc_startTransit = function(next, request){
		loc_finishTransit = false;
		
		request = node_createServiceRequestInfoCommon(undefined, undefined, request);

		//if in the same state, then just do nothing
		if(next == loc_out.getCurrentState()){
			loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION, next+"|Samestate", request);
			loc_finishTransit = true;
			return;
		}
		//if in transit, do nothing
		if(loc_inTransit!=undefined){
			loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION, next+"|InTransiting", request);
			loc_finishTransit = true;
			return;
		}
		
		//find next state info
		var nextStateInfo = loc_stateDef.getStateInfo(loc_out.getCurrentState()).nextStates[next];
		if(nextStateInfo==undefined){
			//invalid tranist
			loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION, next+"|Notvalidtransit", request);
			loc_finishTransit = true;
			return;
		}
		
		//do tranist
		loc_inTransit = new node_TransitInfo(loc_currentState, next); 
		var callBack = nextStateInfo.callBack;
		var transitResult = true;      
		if(callBack!=undefined)	transitResult = callBack.call(loc_thisContext, request);
		
		//process trainsit result
		loc_processStatuesResult(transitResult, request);
	};
	
	
	var loc_processStatuesResult = function(result, request){

		if(result==true || result==undefined){
			//success finish
			loc_successTransit(request);
			return;
		}
		if(result==false)   return;           //not finish, wait for finish method get called
		
		var entityType = node_getObjectType(result);
		if(node_CONSTANT.TYPEDOBJECT_TYPE_ERROR==entityType){
			//failed
			node_requestServiceProcessor.processRequest(loc_getRollBackRequest(loc_inTransit, {
				success : function(request){
					loc_failTransit(request);
				}
			}), {
				attchedTo : request
			});
			return;
		}
		else if(node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==entityType){
			var transitRequest = node_createServiceRequestInfoSequence(undefined, {
				success : function(request){			},
				error : function(request, serviceData){	
					node_requestServiceProcessor.processRequest(loc_getRollBackRequest(loc_inTransit), {
						attchedTo : request
					});
//					loc_rollBack(loc_inTransit, request);
//					loc_failTransit(new node_RequestResult(node_CONSTANT.REQUEST_FINISHTYPE_ERROR, serviceData), request);		
				},
				exception : function(request, serviceData){	
					node_requestServiceProcessor.processRequest(loc_getRollBackRequest(loc_inTransit), {
						attchedTo : request
					});
//					loc_rollBack(loc_inTransit, request);
//					loc_failTransit(new node_RequestResult(node_CONSTANT.REQUEST_FINISHTYPE_EXCEPTION, serviceData), request);		
				}
			});

			//if return request, then build wrapper request
			transitRequest.addRequest(result);

			transitRequest.registerEventListener(undefined, function(eventName, eventData){
				if(loc_finishTransit==false){
					if(eventName==node_CONSTANT.REQUEST_EVENT_DONE){
						var trainsitResult = transitRequest.getResult();
						if(trainsitResult.type==node_CONSTANT.REQUEST_FINISHTYPE_SUCCESS){
							loc_successTransit(request);
						}
						else{
							loc_failTransit(request);
						}
					}
				}
			});
			
			node_requestServiceProcessor.processRequest(transitRequest, {
				attchedTo : request
			});
			return;
		}
	};

	/*
	 * method called when transition is finished successfully
	 */
	var loc_successTransit = function(request){
		var inTransit = loc_inTransit;
		loc_inTransit = undefined;
		loc_currentState = inTransit.to;
		loc_finishTransit = true;
		loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION, inTransit, request);
	};

	var loc_failTransit = function(request){
		var inTransit = loc_inTransit;
		loc_inTransit = undefined;
		loc_currentState = inTransit.from;
		loc_finishTransit = true;
		loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION, inTransit, request);
	};

	var loc_getRollBackRequest = function(transitInfo, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var reverseCallBack = loc_stateDef.getStateInfo(transitInfo.from).nextStates[transitInfo.to].reverseCallBack;
		if(reverseCallBack!=undefined){
			var rollbackResult = reverseCallBack.apply(loc_thisContext, request);
			var entityType = node_getObjectType(rollbackResult);
			if(node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==entityType){
				out.addRequest(rollbackResult);
			}
		}
		return out;
	};

	var loc_updateNextSequence = function(transferSequence, next){
		if(typeof next === 'string' || next instanceof String){
			//if nexts parm is command string
			var commandInfo = loc_stateDef.getCommandInfo(next, loc_currentState);
			var nexts = [];
			if(commandInfo==undefined){
				//nexts parm is target state
				nexts = loc_stateDef.getNextsByTransitInfo(new node_TransitInfo(loc_currentState, next));
			}
			else{
				//command
				nexts = commandInfo.nexts;
			}
			_.each(nexts, function(next, i){
				transferSequence.push(next);
			});
		}
	};
	
	var loc_out = {
			
		prv_startTransit : function(next, request){  loc_startTransit(next, request);    },	
		prv_getRollBackRequest : function(next, request){  return loc_getRollBackRequest(new node_TransitInfo(next, loc_currentState), request);    },

		prv_transitSuccess : function(request){   loc_successTransit(request);	},
		prv_transitFail : function(request){	loc_processStatuesResult(node_createErrorData(), request);	},

		prv_registerEventListener : function(listener, handler, thisContext){	return loc_eventObj.registerListener(undefined, listener, handler, thisContext); },
		prv_unregisterEventListener : function(listener){	return loc_eventObj.unregister(listener); },

		prv_finishTask : function(){  
			loc_currentTask = undefined;
			if(loc_taskCallback.endTask!=undefined)    loc_taskCallback.endTask();
		},

		getCurrentState : function(){	return loc_currentState;	},
		getAllStates : function(){  return loc_stateDef.getAllStates();   },
		getNextStateCandidates : function(){  return loc_stateDef.getCandidateTransits(loc_out.getCurrentState());   },
		
		getAllCommands : function(){  return loc_stateDef.getAllCommands();  },
		getCommandCandidates : function(){   return loc_stateDef.getCandidateCommands(loc_out.getCurrentState());   },

		newTask : function(next){
			if(loc_currentTask!=undefined)  return undefined;
			
			var nexts = [];
			if(Array.isArray(next))  nexts = next;
			else nexts.push(next);

			var transferSequence = [];
			_.each(nexts, function(next, i){
				loc_updateNextSequence(transferSequence, next);
			});

			loc_currentTask = node_createStateMachineTask(transferSequence, loc_out);
			if(loc_taskCallback.startTask!=undefined)    loc_taskCallback.startTask();
			
			return loc_currentTask;
		},

		newTask1 : function(nexts){
			if(loc_currentTask!=undefined)  return undefined;
			if(typeof nexts === 'string' || nexts instanceof String){
				//if nexts parm is command string
				var commandInfo = loc_stateDef.getCommandInfo(nexts, loc_currentState);
				if(commandInfo==undefined){
					//nexts parm is target state
					nexts = loc_stateDef.getNextsByTransitInfo(new node_TransitInfo(loc_currentState, nexts));
				}
				else{
					//command
					nexts = commandInfo.nexts;
				}
			}
			loc_currentTask = node_createStateMachineTask(nexts, loc_out);
			if(loc_taskCallback.startTask!=undefined)    loc_taskCallback.startTask();
			
			return loc_currentTask;
		},
		
	};
	return loc_out;
};	
	
//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){	node_createServiceRequestInfoCommon = this.getData();	});
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){	node_ServiceRequestExecuteInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.entity.RequestResult", function(){node_RequestResult = this.getData();});

nosliw.registerSetNodeDataEvent("statemachine.TransitInfo", function(){node_TransitInfo = this.getData();});
nosliw.registerSetNodeDataEvent("statemachine.CommandInfo", function(){node_CommandInfo = this.getData();});
nosliw.registerSetNodeDataEvent("statemachine.NextStateInfo", function(){node_NextStateInfo = this.getData();});
nosliw.registerSetNodeDataEvent("statemachine.StateInfo", function(){node_StateInfo = this.getData();});
nosliw.registerSetNodeDataEvent("statemachine.createStateMachineDef", function(){node_createStateMachineDef = this.getData();}); 

//Register Node by Name
packageObj.createChildNode("createStateMachine", node_createStateMachine); 

})(packageObj);
