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

//*******************************************   Start Node Definition  ************************************** 	

var INTERFACENAME = "componentInterface";

var node_makeObjectWithComponentInterface = function(baseObject, interfaceObj, thisContext){
	return node_buildInterface(baseObject, INTERFACENAME, loc_createComponentInterfaceObj(thisContext==undefined?baseObject:thisContext, baseObject, interfaceObj));
};
	
var node_getComponentInterface = function(baseObject){
	return node_getInterface(baseObject, INTERFACENAME);
};

var loc_createComponentInterfaceObj = function(thisContext, baseObj, interfaceObj){

	var loc_thisContext = thisContext;

	var loc_interfaceObj = interfaceObj;
	
	var loc_baseObj = baseObj;
	
	var loc_idDataSet = loc_interfaceObj.prv_getIODataSet();
	
	var loc_init = function(){
		
	};

	var loc_dataChangeEventProcessor = function(eventName, eventData, request){
		loc_idDataSet.getGetDataSetValueRequest({
			success : function(request, dataSet){
				
			}
		}, request);
	};
	
	var loc_out = {

		getExecuteCommandRequest : function(command, parms, handlers, request){
			var lifycyclePrefix = 'lifecycle.';
			if(command.startsWith(lifycyclePrefix)){
				var lifecycle = node_getComponentLifecycleInterface(loc_baseObj);
				return lifecycle.getTransitRequest(command.substring(lifycyclePrefix.length), handlers, request);
			}
			else{
				return loc_interfaceObj.prv_getExecuteCommandRequest(command, parms, handlers, request);
			}
		},

		registerDataChangeEventListener : function(listener, handler){	
			return loc_idDataSet.registerEventListener(listener, 
				function(eventName, eventData, request){
					var dataRequest = loc_idDataSet.getGetDataSetValueRequest({
						success : function(request, dataSet){
							handler(eventName, dataSet);
						}
					}, request);
					node_requestServiceProcessor.processRequest(dataRequest);
				}, loc_baseObj);	
		},

		getContextDataSetRequest : function(handlers, request){
			return loc_idDataSet.getGetDataSetValueRequest(handlers, request);
		},
		
		getIOContext : function(){  return loc_idDataSet;   },
		
		unregisterDataChangeEventListener : function(listener){  loc_idDataSet.unregisterEventListener(listener);  },
		
		registerEventListener : function(listener, handler){	return loc_interfaceObj.prv_registerEventListener(listener, handler, loc_thisContext);	},
		unregisterEventListener : function(listener){  loc_interfaceObj.prv_unregisterEventListener(listener);  },

		registerValueChangeEventListener : function(listener, handler){	return loc_interfaceObj.prv_registerValueChangeEventListener(listener, handler, loc_thisContext);	},
		unregisterValueChangeEventListener : function(listener){  loc_interfaceObj.prv_unregisterValueChangeEventListener(listener);  },

		registerValueChangeEventListener : function(listener, handler){	return loc_interfaceObj.prv_registerValueChangeEventListener(listener, handler, loc_thisContext);	},
		unregisterValueChangeEventListener : function(listener){  loc_interfaceObj.prv_unregisterValueChangeEventListener(listener);  },
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
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});


//Register Node by Name
packageObj.createChildNode("makeObjectWithComponentInterface", node_makeObjectWithComponentInterface); 
packageObj.createChildNode("getComponentInterface", node_getComponentInterface); 

})(packageObj);
