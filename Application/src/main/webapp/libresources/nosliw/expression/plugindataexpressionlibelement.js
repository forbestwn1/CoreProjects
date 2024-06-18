//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildComponentInterface;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_ResourceId;
	var node_resourceUtility;
	var node_componentUtility;
	var node_createServiceRequestInfoSimple;
	var node_expressionUtility;
	var node_makeObjectWithApplicationInterface;
    var node_createTaskContainerInterface;
	var node_createTaskInterface;
	var node_createDataExpressionElementInLibrary;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createDataExpressionLibraryElementPlugin = function(){
	
	var loc_out = {

		getCreateEntityCoreRequest : function(complexEntityDef, valueContextId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createDataExpressionLibraryElementComponentCore(complexEntityDef, valueContextId, bundleCore, configure);
			}, handlers, request);
		},
	};

	return loc_out;
};

var loc_createDataExpressionLibraryElementComponentCore = function(complexEntityDef, valueContextId, bundleCore, configure){

	var loc_complexEntityDef = complexEntityDef;
	var loc_expressionData = node_createDataExpressionElementInLibrary(loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKDATAEXPRESSIONELEMENTINLIBRARY_VALUE));
	
	var loc_envInterface = {};

	var loc_facade = node_createTaskInterface({
		getInitRequest : function(handlers, request){
			return loc_expressionData.getInitRequest(handlers, request);
		},
		
		getExecuteRequest : function(handlers, request){
			return loc_expressionData.getExecuteRequest(handlers, request);
		},
	});
	
	var loc_out = {

		getExternalValuePort : function(valuePortGroup, valuePortName){
			return loc_expressionData.getExternalValuePort(valuePortGroup, valuePortName);
		},
		
		getInternalValuePort : function(valuePortGroup, valuePortName){
			return loc_expressionData.getInternalValuePort(valuePortGroup, valuePortName);
		},
		
		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},

	};
	
	return node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_EXPRESSION, node_createTaskInterface(loc_facade));
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithApplicationInterface", function(){node_makeObjectWithApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("task.createTaskContainerInterface", function(){	node_createTaskContainerInterface = this.getData();	});
nosliw.registerSetNodeDataEvent("task.createTaskInterface", function(){	node_createTaskInterface = this.getData();	});
nosliw.registerSetNodeDataEvent("expression.createDataExpressionElementInLibrary", function(){node_createDataExpressionElementInLibrary = this.getData();});


//Register Node by Name
packageObj.createChildNode("createDataExpressionLibraryElementPlugin", node_createDataExpressionLibraryElementPlugin); 

})(packageObj);
