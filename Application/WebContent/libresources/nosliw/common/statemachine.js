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

//*******************************************   Start Node Definition  ************************************** 	

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

var node_createStateMachine = function(state, thisContext){

	var loc_allStates = {};
	var loc_thisContext = thisContext;
	var loc_currentState = state;
	var loc_inTransit = undefined;
	
	var loc_eventObj = node_createEventObject();
	
	var loc_trigueEvent = function(eventName, eventData, request){
		loc_eventObj.triggerEvent(eventName, eventData, request);
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
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ProcessLifecycleResult", {}), undefined, request);
			out.addRequest(result, {
				success : function(request){	loc_successTransit(request);		},
				error : function(request){		loc_failTransit(request);			},
				exception : function(request){	loc_failTransit(request);			}
			});
			node_requestServiceProcessor.processRequest(out);
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
		var reverseCallBack = loc_allStates[inTransit.from].nextStates[inTransit.to].reverseCallBack;
		if(reverseCallBack!=undefined)	reverseCallBack.apply(loc_thisContext, request);
		loc_inTransit = undefined;
		loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION, inTransit, request);
	};
	
	var loc_out = {
			
		getCurrentState : function(){	return loc_currentState;	},

		addState : function(stateInfo){   loc_allStates[stateInfo.name] = stateInfo;    },
		addStateInfo : function(name, next, callBack, reverseCallBack){
			var stateInfo = loc_allStates[name];
			if(stateInfo==undefined){
				stateInfo = new node_StateInfo(name);
				loc_out.addState(stateInfo);
			}
			stateInfo.addNextState(next, callBack, reverseCallBack);
		},
		
		startTransit : function(next, args){
			//if in the same state, then just do nothing
			if(next == loc_out.getCurrentState())  return;
			//if in transit, do nothing
			if(loc_inTransit!=undefined)  return;
			
			var request = node_requestUtility.getRequestInfoFromFunctionArguments(args);	
			var nextStateInfo = loc_allStates[loc_out.getCurrentState()].nextStates[next];
			
			loc_inTransit = {
				from : this.getCurrentState(),
				to : next
			};
			var callBack = nextStateInfo.callBack;
			var initResult = true;      
			if(callBack!=undefined)	initResult = callBack.apply(loc_thisContext, args);
			
			return loc_processStatuesResult(initResult, request);
		},
		
		transitSuccess : function(request){   loc_successTransit(request);	},
		transitFail : function(request){   loc_failTransit(request);	},
		
		registerEventListener : function(listener, handler, thisContext){	return loc_eventObj.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventObj.unregister(listener); },
		
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

//Register Node by Name
packageObj.createChildNode("NextStateInfo", node_NextStateInfo); 
packageObj.createChildNode("StateInfo", node_StateInfo);
packageObj.createChildNode("createStateMachine", node_createStateMachine); 

})(packageObj);
