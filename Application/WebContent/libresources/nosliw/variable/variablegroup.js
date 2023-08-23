//get/create package
var packageObj = library.getChildPackage("context");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_createRequestEventGroupHandler;
//*******************************************   Start Node Definition  ************************************** 	

var node_createVariableGroup = function(variablesArray, handler, thisContext){

	//event handler
	var loc_handler = handler;
	//variables
	var loc_variables = {};
	
	var loc_requestEventGroupHandler = undefined;
	
	var loc_addElement = function(variable, key){
		loc_requestEventGroupHandler.addElement(variable.getDataChangeEventObject(), key+"");
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(variablesArray, handler, thisContext){
		loc_requestEventGroupHandler = node_createRequestEventGroupHandler(loc_handler, thisContext);
		
		for(var i in variablesArray){
			loc_addElement(variablesArray[i], i);
		}
	};	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		loc_requestEventGroupHandler.destroy(requestInfo);
		loc_handler = undefined;
	};

	var loc_out = {

		addVariable : function(variable, key){	loc_addElement(variable, key);		},
		
		triggerEvent : function(requestInfo){   loc_requestEventGroupHandler.triggerEvent(requestInfo);  },
		
		destroy : function(requestInfo){	node_getLifecycleInterface(loc_out).destroy(requestInfo);	},
	};
	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	node_getLifecycleInterface(loc_out).init(variablesArray, handler, thisContext);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.event.createRequestEventGroupHandler", function(){node_createRequestEventGroupHandler = this.getData();});

//Register Node by Name
packageObj.createChildNode("createVariableGroup", node_createVariableGroup); 

})(packageObj);
