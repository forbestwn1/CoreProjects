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
	var node_requestServiceProcessor;
	var node_getComponentLifecycleInterface;
	var node_createComponentManagementInterfaceDelegateObject;
	var node_componentUtility;
	var node_createLifecycleTask;

//*******************************************   Start Node Definition  ************************************** 	
//interface for manage component by debug tool
	
var node_getComponentManagementInterface = function(baseObject){	return node_getInterface(baseObject, node_CONSTANT.INTERFACE_COMPONENTENTITYMANAGE);	};

//component's generic interface
// command, inputIO, lifecycle, 
var node_makeObjectWithComponentManagementInterface = function(thisContext, rawObject){

	//for method's this
	var loc_thisContext = thisContext;

	//object that define behavior
	var loc_agentObj = node_getComponentInterface(rawObject);
	
	//IODataSet for component context
//	var loc_componentContextIODataSet = loc_agentObj.getContextIODataSet();
	
	var loc_out = {

		getLifecycleState : function(){
			return loc_agentObj.getLifecycleEntity().getStateMachine().getCurrentState();
		},
		
		createLifecycleTask : function(next, request){
			return node_createLifecycleTask(next, loc_agentObj, request);
		},
		
		//execute command on component
		getExecuteCommandRequest : function(command, parms, handlers, request){
			return loc_agentObj.getExecuteCommandRequest==undefined?undefined:loc_agentObj.getExecuteCommandRequest(command, parms, handlers, request);
		},

		getContextIODataSet :  function(){  return loc_agentObj.getContextIODataSet==undefined?undefined:loc_agentObj.getContextIODataSet();  },

		//component's context data set value
		getContextDataSetValueRequest : function(handlers, request){		return loc_out.getContextIODataSet==undefined?undefined:loc_out.getContextIODataSet().getGetDataSetValueRequest(handlers, request);		},
		
		//listener to context data change event
		registerContextDataChangeEventListener : function(listener, handler){	
			return loc_out.getContextIODataSet==undefined?undefined:loc_out.getContextIODataSet().registerEventListener(listener, 
				function(eventName, eventData, request){
					var dataRequest = loc_out.getContextIODataSet().getGetDataSetValueRequest({
						success : function(request, dataSet){
							handler(eventName, dataSet);
						}
					}, request);
					node_requestServiceProcessor.processRequest(dataRequest);
				}, loc_thisContext);	
		},
		unregisterContextDataChangeEventListener : function(listener){  loc_out.getContextIODataSet==undefined?undefined:loc_out.getContextIODataSet().unregisterEventListener(listener);  },
		
		registerEventListener : function(listener, handler){	return loc_agentObj.registerEventListener==undefined?undefined:loc_agentObj.registerEventListener(listener, handler, loc_thisContext);	},
		unregisterEventListener : function(listener){  loc_agentObj.unregisterEventListener==undefined?undefined:loc_agentObj.unregisterEventListener(listener);  },

		registerValueChangeEventListener : function(listener, handler){	return loc_agentObj.registerValueChangeEventListener==undefined?undefined:loc_agentObj.registerValueChangeEventListener(listener, handler, loc_thisContext);	},
		unregisterValueChangeEventListener : function(listener){  loc_agentObj.unregisterValueChangeEventListener==undefined?undefined:loc_agentObj.unregisterValueChangeEventListener(listener);  },
	};

	return node_buildInterface(rawObject, node_CONSTANT.INTERFACE_COMPONENTENTITYMANAGE, loc_out);

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
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentManagementInterfaceDelegateObject", function(){node_createComponentManagementInterfaceDelegateObject = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.createLifecycleTask", function(){node_createLifecycleTask = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("makeObjectWithComponentManagementInterface", node_makeObjectWithComponentManagementInterface); 
packageObj.createChildNode("getComponentManagementInterface", node_getComponentManagementInterface); 

})(packageObj);
