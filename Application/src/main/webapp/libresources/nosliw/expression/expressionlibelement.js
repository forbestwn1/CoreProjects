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
	var node_createValuePortValueFlat;
	var node_interactiveUtility;
	var node_buildWithValuePort;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createDataExpressionElementInLibrary = function(expressionDataEleInLibrary, interactiveValuePortsGroup){
	
	var loc_expressionDataEleInLibrary = expressionDataEleInLibrary;
	
	var loc_interactiveValuePortsGroup = interactiveValuePortsGroup;
	
	var loc_interactiveValuePorts =  node_createInteractiveValuePortsExpression();
	
	var loc_init = function(){
		var requestDef = loc_expressionDataEleInLibrary[node_COMMONATRIBUTECONSTANT.WITHINTERACTIVE_INTERACTIVE][node_COMMONATRIBUTECONSTANT.INTERACTIVE_REQUEST];
		
		loc_interactiveValuePorts.init(node_interactiveUtility.getInteractiveRequestInitValue(requestDef));
	};
	
	var loc_getExecuteRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);      
		var dataExpression = loc_expressionDataEleInLibrary[node_COMMONATRIBUTECONSTANT.ELEMENTINLIBRARYDATAEXPRESSION_EXPRESSION];
		var withValuePortInterface = node_buildWithValuePort({
			getValuePort : function(valuePortGroup, valuePortName){
				return loc_interactiveValuePorts.getValuePort(valuePortName);
			}
		});
		out.addRequest(node_expressionUtility.getExecuteDataExpressionRequest(dataExpression, node_buildWithValuePort(loc_interactiveValuePortsGroup.getWithValuePort()), undefined, {
			success : function(request, result){
				return loc_interactiveValuePortsGroup.getSetResultValueRequest(result);
//				loc_interactiveValuePorts.setResultValue(result);
//				return result;
			}
		}));
		return out;
	};
	
	var loc_out = {
		
		getInitRequest : function(handlers, request){
		},
		
		getExecuteRequest : function(handlers, request){
			return loc_getExecuteRequest(handlers, request);
		},

		getExternalValuePort : function(valuePortGroup, valuePortName){
			return loc_interactiveValuePorts.getValuePort(valuePortName);
		},
		
		getInternalValuePort : function(valuePortGroup, valuePortName){
			return loc_interactiveValuePorts.getValuePort(valuePortName);
		},
	};
	
	loc_init();
	return loc_out;
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
nosliw.registerSetNodeDataEvent("valueport.createValuePortValueFlat", function(){	node_createValuePortValueFlat = this.getData();	});
nosliw.registerSetNodeDataEvent("task.interactiveUtility", function(){	node_interactiveUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("task.createInteractiveValuePortsExpression", function(){	node_createInteractiveValuePortsExpression = this.getData();	});
nosliw.registerSetNodeDataEvent("valueport.buildWithValuePort", function(){	node_buildWithValuePort = this.getData();	});


//Register Node by Name
packageObj.createChildNode("createDataExpressionElementInLibrary", node_createDataExpressionElementInLibrary); 

})(packageObj);
