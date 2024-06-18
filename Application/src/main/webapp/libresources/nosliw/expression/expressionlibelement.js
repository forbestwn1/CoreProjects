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
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createDataExpressionElementInLibrary = function(expressionData){
	
	var loc_input = {};
	var loc_result = {};

	var loc_expressionData = expressionData;
	
	var loc_initRequestValue = function(interactiveEntityDef, parmsValue){
		var parmDefs = interactiveEntityDef[node_COMMONATRIBUTECONSTANT.INTERACTIVE_REQUEST];
		_.each(parmDefs, function(parmDef, i){
			var defaultValue = parmDef[node_COMMONATRIBUTECONSTANT.REQUESTPARMININTERACTIVE_DEFAULTVALUE];
			if(defaultValue!=undefined){
				parmsValue[parmDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME]] = defaultValue;
			}
		});
	};

	var loc_getExecuteRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);      
		var expressionItem = loc_expressionData[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYEXPRESSIONDATASINGLE_EXPRESSION];
		var variablesContainer = loc_expressionData[node_COMMONATRIBUTECONSTANT.DATAEXPRESSIONUNIT_VARIABLEINFOS];
		var withValuePortInterface = {
			getValuePort : function(valuePortGroup, valuePortName){
				return loc_getValuePort(valuePortGroup, valuePortName);
			}
		};
		out.addRequest(node_expressionUtility.getExecuteDataExpressionRequest(expressionItem, variablesContainer, withValuePortInterface, undefined, undefined, {
			success : function(request, result){
				loc_result.result = result;
				return result;
			}
		}));
		return out;
	};
	
	var loc_setValueRequest = function(target, setValueInfos, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);      
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			_.each(setValueInfos, function(setValueInfo, i){
				target[setValueInfo.elementId.getRootName()] = setValueInfo.value; 
			});
		}));
		return out;
	};
	
	var loc_getValueRequest = function(source, elmentId, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);      
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			return source[elmentId.getRootName()];
		}));
		return out;
	};
	
	var loc_requestValuePort = {
		getValueRequest : function(elmentId, handlers, request){ 
			return loc_getValueRequest(loc_input, elmentId, handlers, request);
        },

		setValuesRequest : function(setValueInfos, handlers, request){
			return loc_setValueRequest(loc_input, setValueInfos, handlers, request);
		},
	};

	var loc_resultValuePort = {
		getValueRequest : function(elmentId, handlers, request){ 
			return loc_getValueRequest(loc_result, elmentId, handlers, request);
		},

		setValuesRequest : function(setValueInfos, handlers, request){
			return loc_setValueRequest(loc_result, setValueInfos, handlers, request);
		},
	};

	var loc_getValuePort = function(valuePortGroup, valuePortName){
		if(valuePortName==node_COMMONCONSTANT.VALUEPORT_NAME_INTERACT_REQUEST){
			return loc_requestValuePort;
		}
		else{
			return loc_resultValuePort;
		}
	};
	
	var loc_out = {
		
		getInitRequest : function(handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				loc_initRequestValue(loc_expressionData, loc_input);
			}, handlers, request);
		},
		
		getExecuteRequest : function(handlers, request){
			return loc_getExecuteRequest(handlers, request);
		},

		getExternalValuePort : function(valuePortGroup, valuePortName){
			return loc_getValuePort(valuePortGroup, valuePortName);
		},
		
		getInternalValuePort : function(valuePortGroup, valuePortName){
			return loc_getValuePort(valuePortGroup, valuePortName);
		},
	};
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

//Register Node by Name
packageObj.createChildNode("createDataExpressionElementInLibrary", node_createDataExpressionElementInLibrary); 

})(packageObj);
