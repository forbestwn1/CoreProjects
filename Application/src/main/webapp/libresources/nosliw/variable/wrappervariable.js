//get/create package
var packageObj = library.getChildPackage("wrapper");    

(function(packageObj){
//get used node
var node_ServiceInfo;
var node_CONSTANT;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithId;
var node_eventUtility;
var node_requestUtility;
var node_wrapperFactory;
var node_basicUtility;
var node_createEventObject;
var node_createServiceRequestInfoSequence;
var node_valueInVarOperationServiceUtility;
var node_getHandleEachElementRequest;
var node_requestServiceProcessor;

//*******************************************   Start Node Definition  **************************************
//input model:
//	variable
//	variable wrapper + path
//	same as variable input
var node_createVariableWrapper = function(data1, data2, adapterInfo, info){
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(data1, data2, adapterInfo, info){

		//every variable has a id, it is for debuging purpose
		loc_out.prv_id = nosliw.runtime.getIdService().generateId();

		//event source for event that communicate operation information with outsiders
		loc_out.prv_dataOperationEventObject = node_createEventObject();
		loc_out.prv_dataChangeEventObject = node_createEventObject();

		var entityType = node_getObjectType(data1);
		if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_EMPTY){
			return;
		}
		
		if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE && node_basicUtility.isStringEmpty(data2) && adapterInfo==undefined){
			loc_setVariable(data1);
		}
		else{
			if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLEWRAPPER)	data1 = data1.prv_getVariable();
			loc_setVariable(nosliw.runtime.getVariableManager().createVariable(data1, data2, adapterInfo, info));
		}
	};	

	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){
		//take care of release event 
		loc_out.prv_dataOperationEventObject.clearup();
		loc_out.prv_dataChangeEventObject.clearup();
		delete loc_out.prv_dataOperationEventObject;
		delete loc_out.prv_dataChangeEventObject;
		//release variable
		loc_out.prv_variable.release();
		delete loc_out.prv_variable;
	};	
	
	var loc_setVariable = function(variable){
		loc_out.prv_variable = variable;
		
		//use variable when created
		loc_out.prv_variable.use();
		
		//receive event from variable and trigue new same event
		//the purpose of re-trigue the new event is for release the resources after this variable wrapper is released
		loc_out.prv_variable.registerDataOperationEventListener(loc_out.prv_dataOperationEventObject, function(event, eventData, request){
			if(loc_out.prv_dataOperationEventObject!=undefined) loc_out.prv_dataOperationEventObject.triggerEvent(event, eventData, request);
		}, loc_out);
		loc_out.prv_variable.registerDataChangeEventListener(loc_out.prv_dataChangeEventObject, function(event, eventData, request){
			if(loc_out.prv_dataChangeEventObject!=undefined) loc_out.prv_dataChangeEventObject.triggerEvent(event, eventData, request);
		}, loc_out);
	};
	
	var loc_out = {
		
		prv_getVariable : function(){	return this.prv_variable;	},

        getVariableId : function(){   return this.prv_variable.getVariableId();   },

		setVariable : function(variable){	loc_setVariable(variable);  	},
		getVariable : function(){	return this.prv_variable;	},
		
		createChildVariable : function(path, adapterInfo, info){	
			return node_createVariableWrapper(this, path, adapterInfo, info);
		}, 
		
		release : function(requestInfo){	node_getLifecycleInterface(loc_out).destroy(requestInfo);	},
		
		isEmpty : function(){  return this.prv_variable.prv_isWrapperExists();    },
		
		getDataOperationRequest : function(operationService, handlers, request){	return this.prv_variable.getDataOperationRequest(operationService, handlers, request);	},
		executeDataOperationRequest : function(operationService, handlers, request){
			var requestInfo = this.getDataOperationRequest(operationService, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},

		getGetValueRequest : function(handlers, request){   return this.prv_variable.getGetValueRequest(handlers, request);  },

		registerDataOperationEventListener : function(listenerEventObj, handler, thisContext){return this.prv_dataOperationEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);},
		registerDataChangeEventListener : function(listenerEventObj, handler, thisContext){return this.prv_dataChangeEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);},
		unregisterDataOperationEventListener : function(listenerEventObj){return this.prv_dataOperationEventObject.unregister(listenerEventObj);},
		unregisterDataChangeEventListener : function(listenerEventObj){return this.prv_dataChangeEventObject.unregister(listenerEventObj);},
		getDataOperationEventObject : function(){   return this.prv_dataOperationEventObject;   },
		getDataChangeEventObject : function(){   return this.prv_dataChangeEventObject;   },
		
	};
	
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLEWRAPPER);
	loc_out = node_makeObjectWithId(loc_out, nosliw.generateId());

	node_getLifecycleInterface(loc_out).init(data1, data2, adapterInfo, info);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.valueInVarOperationServiceUtility", function(){node_valueInVarOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.orderedcontainer.getHandleEachElementRequest", function(){node_getHandleEachElementRequest = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});

//Register Node by Name
packageObj.createChildNode("createVariableWrapper", node_createVariableWrapper); 

})(packageObj);
