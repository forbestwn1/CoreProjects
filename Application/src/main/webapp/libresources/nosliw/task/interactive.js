//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_basicUtility;
	var node_getObjectType;
	var node_ResourceId;
	var node_resourceUtility;
	var node_createConfigure;
	var node_getEntityTreeNodeInterface;
	var node_namingConvensionUtility;
	var node_complexEntityUtility;
	var node_getApplicationInterface;
	var node_createValuePortValueFlat;
	var node_createValuePort;

//*******************************************   Start Node Definition  ************************************** 	

var node_interactiveUtility = function(){
		
	var loc_out = {

		getInteractiveRequestInitValue : function(obj){
			var interactive = obj[node_COMMONATRIBUTECONSTANT.WITHINTERACTIVE_INTERACTIVE];
			if(obj==undefined)  interactive = obj;
			var out = {};
			var parmDefs = interactive[node_COMMONATRIBUTECONSTANT.INTERACTIVE_REQUEST][node_COMMONATRIBUTECONSTANT.INTERACTIVEREQUEST_PARM];
			_.each(parmDefs, function(parmDef, i){
				var defaultValue = parmDef[node_COMMONATRIBUTECONSTANT.REQUESTPARMININTERACTIVE_DEFAULTVALUE];
				if(defaultValue!=undefined){
					out[parmDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME]] = defaultValue;
				}
			});
			return out;
		}
		
	};
	
	return loc_out;
}();

var node_createInteractiveExpressionValuePortsGroup = function(valuePortContainerId, varDomain){
	
	var loc_valuePortContainer = varDomain.getValuePortContainer(valuePortContainerId);
	var loc_valuePort = node_createValuePort(valuePortContainerId, varDomain);
	
	var loc_out = {
		
		getSetResultValueRequest : function(resultValue, handlers, request){
			var valueStructureId = loc_valuePortContainer.getValueStructureIdByGroupAndValuePort(node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_INTERACTIVEEXPRESSION, node_COMMONCONSTANT.VALUEPORT_NAME_INTERACT_RESULT);
			return loc_valuePort.getValueRequest(node_createValuePortElementInfo(valueStructureId, node_COMMONCONSTANT.NAME_ROOT_RESULT));
		},
		
		getWithValuePort : function(){
			return {
				getValuePort : function(valuePortGroup, valuePortName){
					return loc_valuePort;
				}
			};
		}
		
	};

	return loc_out;
};

var node_createInteractiveValuePortsExpression = function(){
	
	var loc_requestValuePort = node_createValuePortValueFlat();
	var loc_resultValuePort = node_createValuePortValueFlat();
	
	var loc_out = {
		
		init : function(defaultValueByName){
			loc_requestValuePort.init(defaultValueByName);
		},
		
		setResultValue : function(resultValue){
			loc_resultValuePort.setValue("result", resultValue);
		},
		
		getValuePort : function(valuePortName){
			if(valuePortName==node_COMMONCONSTANT.VALUEPORT_NAME_INTERACT_REQUEST){
				return loc_requestValuePort;
			}
			else{
				return loc_resultValuePort;
			}
		},
	};
	return loc_out;
};


var node_createInteractiveValuePortsTask = function(){
	
	var loc_requestValuePort = node_createValuePortValueFlat();
	var loc_resultValuePorts = {};
	
	var loc_getResultValuePort = function(resultValuePortName){
		var valuePort = loc_resultValuePorts[resultValuePortName];
		if(valuePort==null){
			valuePort = node_createValuePortValueFlat();
			loc_resultValuePorts[resultValuePortName] = valuePort;
		}
		return valuePort;
	};
	
	var loc_getResultValuePortNameByResultName = function(resultName){
		return node_COMMONCONSTANT.VALUEPORT_NAME_INTERACT_RESULT + node_COMMONCONSTANT.SEPERATOR_PREFIX + resultName;
	};
	
	var loc_out = {
		
		init : function(defaultValueByName){
			loc_requestValuePort.init(defaultValueByName);
		},
		
		setResultValue : function(result){
			var valuePort = loc_getResultValuePort(loc_getResultValuePortNameByResultName(result.resultName));
			_.each(result.resultValue, function(value, name){
				valuePort.setValue(name, value);
			});
		},
		
		getValuePort : function(valuePortName){
			if(valuePortName==node_COMMONCONSTANT.VALUEPORT_NAME_INTERACT_REQUEST){
				return loc_requestValuePort;
			}
			else{
				return loc_getResultValuePort(valuePortName);
			}
		},
	};
	return loc_out;
};




//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){	node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.getApplicationInterface", function(){node_getApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.createValuePortValueFlat", function(){	node_createValuePortValueFlat = this.getData();	});
nosliw.registerSetNodeDataEvent("valueport.createValuePort", function(){node_createValuePort = this.getData();});

//Register Node by Name
packageObj.createChildNode("interactiveUtility", node_interactiveUtility); 
packageObj.createChildNode("createInteractiveValuePortsExpression", node_createInteractiveValuePortsExpression); 
packageObj.createChildNode("createInteractiveValuePortsTask", node_createInteractiveValuePortsTask); 
packageObj.createChildNode("createInteractiveExpressionValuePortsGroup", node_createInteractiveExpressionValuePortsGroup); 

})(packageObj);
