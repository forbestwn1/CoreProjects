//get/create package
var packageObj = library.getChildPackage("statemachine");    

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
	
//*******************************************   Start Node Definition  ************************************** 	

var node_CommandInfo = function(name, froms, nexts){
	this.name = name;
	this.froms = froms;
	this.nexts = nexts;
};
	
var node_NextStateInfo = function(name, callBack, reverseCallBack){
	this.name = name;
	this.callBack = callBack;
	this.reverseCallBack = reverseCallBack;
};	
	
var node_StateInfo = function(name, nextStates){
	this.name = name;
	this.nextStates = nextStates;
	if(this.nextStates==undefined)  this.nextStates = {};
};

node_StateInfo.prototype = {
	addNextState : function(name, callBack, reverseCallBack){
		this.nextStates[name] = new node_NextStateInfo(name, callBack, reverseCallBack);
	}
};

var node_TransitInfo = function(from, to){
	this.from = from;
	this.to = to;
};

var node_createStateMachineTask = function(nexts, stateMachine){

	var loc_stateMachine = stateMachine;
	var loc_from = loc_stateMachine.getCurrentState();
	var loc_nexts = nexts;
	
	var loc_eventObj = node_createEventObject();
	
	var loc_currentNext = -1;

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
					loc_processNext();
				}
				else if (eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION || eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION){
					loc_currentNext = loc_currentNext - 2;
					loc_rollBack(request);
					loc_stateMachine.prv_finishTask();
					//finish
					loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION, undefined, request);
				}
			});
			loc_stateMachine.prv_startTransit(nexts[loc_currentNext]);
		}
	};
	
	var loc_rollBack = function(request){
		while(loc_currentNext>=0){
			loc_stateMachine.prv_rollBack(loc_nexts[loc_currentNext], request);
			loc_currentNext--;
		};
	};
	
	var loc_trigueEvent = function(eventName, eventData, request){	loc_eventObj.triggerEvent(eventName, eventData, request);	};
	
	var loc_out = {
		
		process : function(request){	
			loc_processNext(request);
			return loc_out;
		},
		
		getProcessRequest : function(handlers, request){
			var out = node_createServiceRequestInfoCommon(undefined, handlers, request);
			out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(request){
				var listener = loc_out.registerEventListener(undefined, function(eventName, eventData, request){
					if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION){
						out.executeSuccessHandler();
					}
					else if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION){
						out.executeErrorHandler();
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
	return loc_out;
};

var node_createStateMachineDef = function(){

	var loc_states = {};
	
	var loc_commands = {};

	var loc_nextCommandsByState;

	var loc_addState = function(stateInfo){   loc_states[stateInfo.name] = stateInfo;    };
	
	var loc_getStateInfo = function(state){
		var stateInfo = loc_states[state];
		if(stateInfo==undefined){
			stateInfo = new node_StateInfo(state);
			loc_addState(stateInfo);
		}
		return stateInfo;
	};

	var loc_getAllStates = function(){
		var out = [];
		_.each(loc_states, function(state, name){   out.push(name);   });
		return out;
	};

	var loc_getNextCandidateStates = function(state){
		var out = [];
		_.each(loc_getStateInfo(state).nextStates, function(state, name){   out.push(name);   });
		return out;
	};
	

	var loc_buildNextCommandsByState = function(){
		loc_nextCommandsByState = {};
		_.each(loc_getAllStates(), function(state, i){
			var commands = [];
			var candidates = loc_getNextCandidateStates(state);
			_.each(loc_commands, function(commandInfo, command){
				if(candidates.includes(commandInfo.nexts[0])){
					if(commandInfo.froms==undefined || commandInfo.froms.includes(state)){
						commands.push(command);
					}
				}
			});
			loc_nextCommandsByState[state] = commands;
		});
	};
	
	var loc_out = {

		getStateInfo : function(state){		return loc_getStateInfo(state);	},	
			
		addStateInfo : function(transitInfo, callBack, reverseCallBack){
			var stateInfo = loc_getStateInfo(transitInfo.from);
			stateInfo.addNextState(transitInfo.to, callBack, reverseCallBack);
		},

		addCommand : function(commandInfo){		loc_commands[commandInfo.name] = commandInfo;		},
		
		getCandidateCommands : function(state){
			if(loc_nextCommandsByState==undefined){
				loc_buildNextCommandsByState();
			}
			return loc_nextCommandsByState[state];
		},
		
		getCandidateTransits : function(state){	return loc_getNextCandidateStates(state);	}, 
		
		getAllStates : function(){     
			var out = [];
			_.each(loc_states, function(stateInfo, stateName){ out.push(stateName);  });
			return out;
		},
		
		getAllCommands : function(){   
			var out = [];
			_.each(loc_commands, function(commandInfo, commandName){ out.push(commandName);  });
			return out;
		},
		getCommandInfo : function(command){		return loc_commands[command];	}
		
	};
	
	return loc_out;
};

var node_createStateMachine = function(stateDef, initState, thisContext){

	var loc_stateDef = stateDef;
	
	var loc_thisContext = thisContext;

	var loc_eventObj = node_createEventObject();

	var loc_currentTask;
	var loc_currentState = initState;
	var loc_inTransit = undefined;
	
	var loc_trigueEvent = function(eventName, eventData, request){	loc_eventObj.triggerEvent(eventName, eventData, request);	};

	var loc_startTransit = function(next, request){
		//if in the same state, then just do nothing
		if(next == loc_out.getCurrentState()){
			loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION, next+"|Samestate", request);
			return;
		}
		//if in transit, do nothing
		if(loc_inTransit!=undefined){
			loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION, next+"|InTransiting", request);
			return;
		}
		
		var nextStateInfo = loc_stateDef.getStateInfo(loc_out.getCurrentState()).nextStates[next];
		if(nextStateInfo==undefined){
			loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION, next+"|Notvalidtransit", request);
			return;
		}
		
		loc_inTransit = new node_TransitInfo(loc_currentState, next); 
		var callBack = nextStateInfo.callBack;
		var initResult = true;      
		if(callBack!=undefined)	initResult = callBack.call(loc_thisContext, request);
		
		return loc_processStatuesResult(initResult, request);
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
			loc_failTransit(request);
			return;
		}
		else if(node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==entityType){
			//if return request, then build wrapper request

			result.addPostProcessor({
				success : function(request){	loc_successTransit(request);		},
				error : function(request){		loc_failTransit(request);			},
				exception : function(request){	loc_failTransit(request);			}
			});
			node_requestServiceProcessor.processRequest(result);
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
		loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION, inTransit, request);
	};

	var loc_failTransit = function(request){
		var inTransit = loc_inTransit;
		loc_rollBack(inTransit, request);
		loc_inTransit = undefined;
		loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION, inTransit, request);
	};
	
	var loc_rollBack = function(transitInfo, request){
		var reverseCallBack = loc_stateDef.getStateInfo(inTransit.from).nextStates[inTransit.to].reverseCallBack;
		if(reverseCallBack!=undefined)	reverseCallBack.apply(loc_thisContext, request);
		loc_currentState = transitInfo.from;
	};
	
	var loc_out = {
			
		prv_startTransit : function(next, request){  loc_startTransit(next, request);    },	
		prv_rollBack : function(next, request){  loc_rollBack(new node_TransitInfo(next, loc_currentState), request);    },

		prv_transitSuccess : function(request){   loc_successTransit(request);	},
		prv_transitFail : function(request){   loc_failTransit(request);	},

		prv_registerEventListener : function(listener, handler, thisContext){	return loc_eventObj.registerListener(undefined, listener, handler, thisContext); },
		prv_unregisterEventListener : function(listener){	return loc_eventObj.unregister(listener); },

		prv_finishTask : function(){  loc_currentTask = undefined;  },

		getCurrentState : function(){	return loc_currentState;	},
		getAllStates : function(){  return loc_stateDef.getAllStates();   },
		
		getAllCommands : function(){  return loc_stateDef.getAllCommands();  },
		getNextStateCandidates : function(){  return loc_stateDef.getCandidateTransits(loc_out.getCurrentState());   },
		getCommandCandidates : function(){   return loc_stateDef.getCandidateCommands(loc_out.getCurrentState());   },
		
		newTask : function(nexts){
			if(loc_currentTask!=undefined)  return undefined;
			if(typeof nexts === 'string' || nexts instanceof String){
				//command
				nexts = loc_stateDef.getCommandInfo(nexts).nexts;
			}
			loc_currentTask = node_createStateMachineTask(nexts, loc_out);
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

//Register Node by Name
packageObj.createChildNode("TransitInfo", node_TransitInfo); 
packageObj.createChildNode("CommandInfo", node_CommandInfo); 
packageObj.createChildNode("NextStateInfo", node_NextStateInfo); 
packageObj.createChildNode("StateInfo", node_StateInfo);
packageObj.createChildNode("createStateMachine", node_createStateMachine); 
packageObj.createChildNode("createStateMachineDef", node_createStateMachineDef); 

})(packageObj);
