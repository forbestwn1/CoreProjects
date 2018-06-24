//get/create package
var packageObj = library.getChildPackage("variable");    

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
var node_uiDataOperationServiceUtility;
var node_createVariable;
var node_getHandleEachElementRequest;

//*******************************************   Start Node Definition  **************************************
//input model:
//	variable
//	variable wrapper + path
//	same as variable input
var node_createVariableWrapper = function(data1, data2, adapterInfo){
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(data1, data2, adapterInfo){
		var entityType = node_getObjectType(data1);

		if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE && node_basicUtility.isStringEmpty(data2) && adapterInfo==undefined){
			loc_out.prv_variable = data1;
		}
		else{
			if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLEWRAPPER)	data1 = data1.prv_getVariable();
			loc_out.prv_variable = node_createVariable(data1, data2, adapterInfo);
		}
		
		//use variable when created
		loc_out.prv_variable.use();
		
		//event source for event that communicate operation information with outsiders
		loc_out.prv_dataOperationEventObject = node_createEventObject();

		//receive event from variable and trigue new same event
		//the purpose of re-trigue the new event is for release the resources after this variable wrapper is released
		loc_out.prv_variable.registerDataOperationEventListener(loc_out.prv_dataOperationEventObject, function(event, eventData, request){loc_out.prv_dataOperationEventObject.triggerEvent(event, eventData, request);}, loc_out);
	};	

	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){
		//take care of release event 
		loc_out.prv_dataOperationEventObject.clearup();
		loc_out.prv_dataOperationEventObject=undefined;
		//release variable
		loc_out.prv_variable.release();
		loc_out.prv_variable=undefined;
	};	
	
	var loc_out = {
		
		prv_getVariable : function(){	return this.prv_variable;	},
			
		createChildVariable : function(path, adapterInfo){	
//			return node_createVariableWrapper(this.prv_variable.createChildVariable(path).variable, undefined, adapterInfo);
			return node_createVariableWrapper(this, path, adapterInfo);
		}, 
		
		release : function(requestInfo){	node_getLifecycleInterface(loc_out).destroy(requestInfo);	},
		
		getDataOperationRequest : function(operationService, handlers, request){	return this.prv_variable.getDataOperationRequest(operationService, handlers, request);	},
		
		registerDataOperationEventListener : function(listenerEventObj, handler, thisContext){return this.prv_dataOperationEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);},
		unregisterDataOperationEventListener : function(listenerEventObj){return this.prv_dataOperationEventObject.unregister(listenerEventObj);},
		getDataOperationEventObject : function(){   return this.prv_dataOperationEventObject;   },
		
	};
	
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLEWRAPPER);
	loc_out = node_makeObjectWithId(loc_out, nosliw.generateId());

	node_getLifecycleInterface(loc_out).init(data1, data2, adapterInfo);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createVariable", function(){node_createVariable = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.orderedcontainer.getHandleEachElementRequest", function(){node_getHandleEachElementRequest = this.getData();});

//Register Node by Name
packageObj.createChildNode("createVariableWrapper", node_createVariableWrapper); 

})(packageObj);
