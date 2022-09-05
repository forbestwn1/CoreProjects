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
	var node_requestServiceProcessor;
	var node_SMTransitInfo;
	
//*******************************************   Start Node Definition  ************************************** 	

//task handle transaction ( multiple transition steps )
//  handle success
//  handle roll back when fail
//  trigue event when task finish
var node_createStateMachineTask = function(nexts, stateMachineWrappers){

	var loc_stateMachineWrappers = stateMachineWrappers;

	var loc_nexts = nexts;
	_.each(nexts, function(next, i){loc_nexts.push(next);});
	
	var loc_eventObj = node_createEventObject();
	
	//next result
	var loc_results = [];
	var loc_failData = [];
	var loc_finalResult = true;
	
	var loc_currentNext = 0;

	//process one next
	var loc_processNext = function(request){
		loc_currentNext++;
		if(loc_currentNext>=loc_nexts.length){
			//finish successfully
			loc_finishTask();
			loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION, undefined, request);
		}
		else{
			loc_results = [];
			loc_failData = [];
			loc_finalResult = true;
			_.each(loc_stateMachineWrappers, function(wrapper, i){
				var stateMachine = wrapper.getStateMachine();
				var stateMachineId = wrapper.getId();
				
				var listener = stateMachine.registerEventListener(undefined, function(eventName, eventData, request){
					stateMachine.unregisterEventListener(listener);
					if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION){
						//one state machine transit success 
						loc_results.push(true);
					}
					else if (eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION || eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION){
						//one state machine transit fail 
						loc_results.push(eventData);
						loc_finalResult = false;
					}
					
					if(loc_results.length==loc_stateMachineWrappers.length){
						//when all statemachine done, 
						if(loc_finalResult==true){
							//all success, move to next transit
							loc_processNext(request);
						}
						else{
							//failure happened, roll back
							loc_currentNext = loc_currentNext - 2;
							node_requestServiceProcessor.processRequest(loc_getRollBackRequest({
								success : function(request){
									//finish fail
									loc_finishTask();
									loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION, eventData, request);
								}
							}, request));
						}
					}
				});
				stateMachine.startTransit(loc_nexts[loc_currentNext], request);
			});
		}
	};

	//roll back task
	var loc_getRollBackRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request); 
		while(loc_currentNext>=0){
			_.each(loc_stateMachineWrappers, function(wapper, i){
				var stateMachine = wrapper.getStateMachine();
				var stateMachineId = wapper.getId();
				out.addRequest(stateMachine.prv_getRollBackRequest(loc_nexts[loc_currentNext], request));
			});
			loc_currentNext--;
		};
		return out;
	};
	
	var loc_trigueEvent = function(eventName, eventData, request){	loc_eventObj.triggerEvent(eventName, eventData, request);	};

	var loc_startTask = function(){
		_.each(loc_stateMachineWrappers, function(wrapper, i){
			var taskCallBack = wrapper.getTaskCallBack();
			if(taskCallBack!=undefined)  taskCallBack.startTask();
		});
	};
	
	var loc_finishTask = function(){
		_.each(loc_stateMachineWrappers, function(wrapper){
			var taskCallBack = wrapper.getTaskCallBack();
			if(taskCallBack!=undefined)  taskCallBack.endTask();
		});
	};
	
	var loc_out = {
			
		process : function(request){
			loc_startTask();
			var request = loc_out.getRequestInfo(request);
			loc_processNext(request);
			return request;
		},
		
		getProcessRequest : function(handlers, request){
			loc_startTask();
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
		
		executeProcessRequest : function(handlers, request){
			node_requestServiceProcessor.processRequest(this.getProcessRequest(handlers, request));
		},
		
		registerEventListener : function(listener, handler, thisContext){	return loc_eventObj.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventObj.unregister(listener); },
	};
	
	loc_out = node_buildServiceProvider(loc_out, "stateMachineTask");

	return loc_out;
};

var node_createStateMachine = function(initState, thisContext, id){

	var loc_id = id;
	
	//static infor, all the state definition
//	var loc_stateDef = stateDef;
	
	//transit call back
	var loc_transitCallBacks = {};
	
	//this context
	var loc_thisContext = thisContext;

	//event object
	var loc_eventObj = node_createEventObject();

	//current status
	var loc_currentState = initState;

	//if in stranist, it contains from status and to status
	var loc_inTransit = undefined;
	
	//flag whether during transit
	var loc_finishTransit = true;
	
	var loc_trigueEvent = function(eventName, eventData, request){	loc_eventObj.triggerEvent(eventName, eventData, request);	};

	var loc_startTransit = function(next, request){
		//mark as during transit
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
//		if(!loc_stateDef.isTransitValid(loc_out.getCurrentState(), next)){
//			//invalid tranist
//			loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION, next+"|Notvalidtransit", request);
//			loc_finishTransit = true;
//			return;
//		}
		
		//do tranist
		loc_inTransit = new node_SMTransitInfo(loc_currentState, next); 
		
		var callBackInfo = loc_getCallBackInfoForTransit(loc_inTransit.from, loc_inTransit.to)
		var callBack = callBackInfo==undefined?undefined:callBackInfo.callBack;
		var transitResult = true;      
		if(callBack!=undefined)	transitResult = callBack.call(loc_thisContext, request);
		
		//process trainsit result
		loc_processStatuesResult(transitResult, request);
	};
	
	//process transit callback result
	var loc_processStatuesResult = function(result, request){

		if(result==true || result==undefined){
			//success finish
			loc_successTransit(request);
			return;
		}
		if(result==false)   return;           //not finish, wait for finish method get called
		
		var entityType = node_getObjectType(result);
		if(node_CONSTANT.TYPEDOBJECT_TYPE_ERROR==entityType){
			//if result is error, failed
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
			//if result is request, then build wrapper request
			var transitRequest = node_createServiceRequestInfoSequence(undefined, {
				success : function(request){			},
				error : function(request, serviceData){	
					//call back request in error
					node_requestServiceProcessor.processRequest(loc_getRollBackRequest(loc_inTransit), {
						attchedTo : request
					});
//					loc_rollBack(loc_inTransit, request);
//					loc_failTransit(new node_RequestResult(node_CONSTANT.REQUEST_FINISHTYPE_ERROR, serviceData), request);		
				},
				exception : function(request, serviceData){
					//call back request in exception
					node_requestServiceProcessor.processRequest(loc_getRollBackRequest(loc_inTransit), {
						attchedTo : request
					});
//					loc_rollBack(loc_inTransit, request);
//					loc_failTransit(new node_RequestResult(node_CONSTANT.REQUEST_FINISHTYPE_EXCEPTION, serviceData), request);		
				}
			});

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

	//method called when transition is finished failure
	var loc_failTransit = function(request){
		var inTransit = loc_inTransit;
		loc_inTransit = undefined;
		loc_currentState = inTransit.from;
		loc_finishTransit = true;
		loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION, inTransit, request);
	};

	var loc_getCallBackInfoForTransit = function(from, to){
		var byTo = loc_transitCallBacks[from];
		if(byTo==undefined)  return;
		return byTo[to];
	};
	
	//do rollback task
	var loc_getRollBackRequest = function(transitInfo, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var callBackInfo = loc_getCallBackInfoForTransit(transitInfo.from, transitInfo.to);
		var reverseCallBack = callBackInfo==undefined?undefined:callBackInfo.reverseCallBack;
		if(reverseCallBack!=undefined){
			var rollbackResult = reverseCallBack.apply(loc_thisContext, request);
			var entityType = node_getObjectType(rollbackResult);
			if(node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==entityType){
				out.addRequest(rollbackResult);
			}
		}
		return out;
	};

	var loc_out = {
		
		getId : function(){   return loc_id;    },
		
		startTransit : function(next, request){  loc_startTransit(next, request);    },	
		prv_getRollBackRequest : function(next, request){  return loc_getRollBackRequest(new node_SMTransitInfo(next, loc_currentState), request);    },

		//method to finish transit
		prv_transitSuccess : function(request){   loc_successTransit(request);	},
		prv_transitFail : function(request){	loc_processStatuesResult(node_createErrorData(), request);	},

		registerEventListener : function(listener, handler, thisContext){	return loc_eventObj.registerListener(undefined, listener, handler, thisContext, true); },
		unregisterEventListener : function(listener){	return loc_eventObj.unregister(listener); },

		registerTransitCallBack : function(transit, callBack, reverseCallBack){
			var callbacksByTo = loc_transitCallBacks[transit.from];
			if(callbacksByTo==undefined){
				callbacksByTo = {};
				loc_transitCallBacks[transit.from] = callbacksByTo;
			}
			callbacksByTo[transit.to] = {
				callBack : callBack,
				reverseCallBack : reverseCallBack
			};
		},
		
		getCurrentState : function(){	return loc_currentState;	},
		
		
		
		
//		getAllStates : function(){  return loc_stateDef.getAllStates();   },
//		getNextStateCandidates : function(){  return loc_stateDef.getCandidateTransits(loc_out.getCurrentState());   },
//		
//		getAllCommands : function(){  return loc_stateDef.getAllCommands();  },
//		getCommandCandidates : function(){   return loc_stateDef.getCandidateCommands(loc_out.getCurrentState());   },
//
//		newTask : function(next){
//			if(loc_currentTask!=undefined)  return undefined;
//			
//			var nexts = [];
//			if(Array.isArray(next))  nexts = next;
//			else nexts.push(next);
//
//			var transferSequence = [];
//			_.each(nexts, function(next, i){
//				loc_buildTransitSequence(transferSequence, next);
//			});
//
//			loc_currentTask = node_createStateMachineTask(transferSequence, loc_out);
//			if(loc_taskCallback.startTask!=undefined)    loc_taskCallback.startTask();
//			
//			return loc_currentTask;
//		},
//
//		newTask1 : function(nexts){
//			if(loc_currentTask!=undefined)  return undefined;
//			if(typeof nexts === 'string' || nexts instanceof String){
//				//if nexts parm is command string
//				var commandInfo = loc_stateDef.getCommandInfo(nexts, loc_currentState);
//				if(commandInfo==undefined){
//					//nexts parm is target state
//					nexts = loc_stateDef.getNextsByTransitInfo(new node_SMTransitInfo(loc_currentState, nexts));
//				}
//				else{
//					//command
//					nexts = commandInfo.nexts;
//				}
//			}
//			loc_currentTask = node_createStateMachineTask(nexts, loc_out);
//			if(loc_taskCallback.startTask!=undefined)    loc_taskCallback.startTask();
//			
//			return loc_currentTask;
//		},
		
	};
	
	loc_out.id = loc_id;
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
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("statemachine.SMTransitInfo", function(){node_SMTransitInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("createStateMachine", node_createStateMachine); 
packageObj.createChildNode("createStateMachineTask", node_createStateMachineTask); 

})(packageObj);
