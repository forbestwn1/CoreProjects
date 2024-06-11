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
	var loc_referencedRuntime = {};
	
	var loc_input = {};
	var loc_result = {};
	
	var loc_facade = node_createTaskInterface({
		getExecuteRequest : function(taskInput, handlers, request){
			var expressionItem = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYEXPRESSIONDATASINGLE_EXPRESSION);
			return node_expressionUtility.getExecuteDataExpressionRequest(expressionItem, loc_input, undefined, undefined, handlers, request)
		},
	});
	
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
			return loc_resultValuePort[valuePortName];
		}
	};

	var loc_out = {

		getExternalValuePort : function(valuePortGroup, valuePortName){
			return loc_getValuePort(valuePortGroup, valuePortName);
		},
		
		getInternalValuePort : function(valuePortGroup, valuePortName){
			return loc_getValuePort(valuePortGroup, valuePortName);
		},
		
		
		
		getEntityInitRequest1 : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var refAttrNames = loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYEXPRESSIONDATA_ATTRIBUTESREFERENCE);
			
			_.each(refAttrNames, function(attrName, i){
				out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createAttributeRequest(attrName, undefined, {
					success : function(request, childNode){
						loc_referencedRuntime[attrName] = childNode.getChildValue();
					}
				}));
			});
			return out;
		}
	};
	
	return node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_EXPRESSION, loc_facade);
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
packageObj.createChildNode("createDataExpressionLibraryElementPlugin", node_createDataExpressionLibraryElementPlugin); 

})(packageObj);
