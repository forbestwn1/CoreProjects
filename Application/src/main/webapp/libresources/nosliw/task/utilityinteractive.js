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
	var node_getEntityObjectInterface;
	var node_getBasicEntityObjectInterface;
	var node_utilityNamedVariable;

//*******************************************   Start Node Definition  ************************************** 	

var node_interactiveUtility = function(){

	var loc_getRequestValuesFromValuePort = function(valuePortContainer, valuePortGroupType, handlers, request){
		
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var getParmsRequest = node_createServiceRequestInfoSet(undefined, {
			success : function(request, result){
				return result.getResults();
			}
		});
		
		var valueStructures = valuePortContainer.getValueStructuresByGroupTypeAndValuePortName(valuePortGroupType, node_COMMONCONSTANT.VALUEPORT_TYPE_INTERACTIVE_REQUEST);
		_.each(valueStructures, function(valueStructure, i){
			var eleNames = valueStructure.getElementsName();
			_.each(eleNames, function(eleName, i){
				var eleVar = valueStructure.getElement(eleName);
				getParmsRequest.addRequest(eleName, eleVar.getGetValueRequest());
			});
		});
		out.addRequest(getParmsRequest);
		return out;
	};

	var loc_out = {
		
		getResultValuePortNameByResultName : function(resultName){
			return node_COMMONCONSTANT.VALUEPORT_NAME_INTERACT_RESULT + node_COMMONCONSTANT.SEPERATOR_PREFIX + resultName;
		},
		
		getTaskRequestValuesFromValuePort : function(valuePortContainer, handlers, request){
			return loc_getRequestValuesFromValuePort(valuePortContainer, node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_INTERACTIVETASK, handlers, request);
		},
	
		getExpressionRequestValuesFromValuePort : function(valuePortContainer, handlers, request){
			return loc_getRequestValuesFromValuePort(valuePortContainer, node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_INTERACTIVEEXPRESSION, handlers, request);
		},
	
		setTaskResultToValuePort : function(taskResult, valuePortContainer, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var resultName = taskResult.resultName;
			var resultValue = taskResult.resultValue;
			
			out.addRequest(node_utilityNamedVariable.setValuesPortValueRequest(valuePortContainer, node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_INTERACTIVETASK, this.getResultValuePortNameByResultName(resultName), resultValue));
			return out;			
		},
		
		setExpressionResultToValuePort : function(expressionResult, valuePortContainer, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(node_utilityNamedVariable.setValuePortValueRequest(
				valuePortContainer, 
				node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_INTERACTIVEEXPRESSION, 
				node_COMMONCONSTANT.VALUEPORT_TYPE_INTERACTIVE_RESULT,
				node_COMMONCONSTANT.NAME_ROOT_RESULT, 
				expressionResult));
			return out;
		}
	};
	
	return loc_out;
}();

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
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.getBasicEntityObjectInterface", function(){node_getBasicEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.utilityNamedVariable", function(){node_utilityNamedVariable = this.getData();});

//Register Node by Name
packageObj.createChildNode("interactiveUtility", node_interactiveUtility); 

})(packageObj);
