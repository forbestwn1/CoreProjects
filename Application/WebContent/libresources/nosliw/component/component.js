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

//*******************************************   Start Node Definition  ************************************** 	

var INTERFACENAME = "componentLifecycle";
	
/*
 * utility functions to build lifecycle object
 */
var node_makeObjectWithComponentLifecycle = function(baseObject, lifecycleCallback, thisContext){
	return node_buildInterface(baseObject, INTERFACENAME, loc_createComponentLifecycle(thisContext==undefined?baseObject:thisContext, lifecycleCallback));
};
	
var node_getComponentLifecycleInterface = function(baseObject){
	return node_getInterface(baseObject, INTERFACENAME);
};

var loc_createComponentLifecycle = function(thisContext, lifecycleCallback){
	//this context for lifycycle callback method
	var loc_thisContext = thisContext;
	
	var loc_baseObject;
	
	//life cycle call back including all call back method
	var loc_lifecycleCallback = lifecycleCallback==undefined? {}:lifecycleCallback;
	
	var loc_stateMachine = node_createStateMachine(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_START, loc_thisContext);

	var loc_createStateTransit = function(from, to){
		loc_stateMachine.addStateInfo(from, to, 
			function(){
				var fun = loc_lifecycleCallback[from+"_"+to];
				if(fun!=undefined)   return fun.apply(loc_thisContext, arguments);
				else  return true;
			}, 
			function(){
				var fun = loc_lifecycleCallback["_"+from+"_"+to];
				if(fun!=undefined)   return fun.apply(loc_thisContext, arguments);
				else  return true;
			});
	};

	var loc_init = function(){
		loc_createStateTransit(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_START, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE);
		loc_createStateTransit(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_START, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD);
		loc_createStateTransit(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED);
		loc_createStateTransit(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_START);
		loc_createStateTransit(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE);
	};
	
	var loc_out = {
			
		active : function(){	loc_stateMachine.startTransit(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, arguments);	},

		suspend : function(){	loc_stateMachine.startTransit(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED, arguments);	},

		destroy : function(){	loc_stateMachine.startTransit(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD, arguments);	},
		
		resume : function(){	loc_stateMachine.startTransit(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, arguments);	},
		
		transitSuccess : function(request){   loc_successTransit(request);	},
		transitFail : function(request){   loc_failTransit(request);	},

		getComponentStatus : function(){		return loc_stateMachine.getCurrentState();		},

		registerEventListener : function(listener, handler){	loc_stateMachine.registerEventListener(listener, handler, thisContext);	},
		unregisterEventListener : function(listener){  loc_stateMachine.unregisterEventListener(listener);  },

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
nosliw.registerSetNodeDataEvent("common.statemachine.createStateMachine", function(){node_createStateMachine = this.getData();	});

//Register Node by Name
packageObj.createChildNode("makeObjectWithComponentLifecycle", node_makeObjectWithComponentLifecycle); 
packageObj.createChildNode("getComponentLifecycleInterface", node_getComponentLifecycleInterface); 

})(packageObj);
