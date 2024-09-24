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

	var loc_getResultValuePortNameByResultName = function(resultName){
		return node_COMMONCONSTANT.VALUEPORT_NAME_INTERACT_RESULT + node_COMMONCONSTANT.SEPERATOR_PREFIX + resultName;
	};

	var loc_getRequestValuesFromValuePort = function(taskInteractiveRequest, valuePortContainer, valuePortGroup, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var getParmsRequest = node_createServiceRequestInfoSet(undefined, {
			success : function(request, result){
				return result.getResults();
			}
		});
		
		_.each(taskInteractiveRequest[node_COMMONATRIBUTECONSTANT.INTERACTIVEREQUEST_PARM], function(parm){
			var parmName = parm[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME];
			getParmsRequest.addRequest(parmName, node_utilityNamedVariable.getValuePortValueRequest(valuePortContainer, valuePortGroup, node_COMMONCONSTANT.VALUEPORT_TYPE_INTERACTIVE_REQUEST, parmName));
		});
		
		out.addRequest(getParmsRequest);
		return out;
	};

	var loc_out = {
		
		getTaskRequestValuesFromValuePort : function(taskInteractiveRequest, valuePortContainer, handlers, request){
			return loc_getRequestValuesFromValuePort(taskInteractiveRequest, valuePortContainer, node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_INTERACTIVETASK, handlers, request);
		},
	
		getExpressionRequestValuesFromValuePort : function(taskInteractiveRequest, valuePortContainer, handlers, request){
			return loc_getRequestValuesFromValuePort(taskInteractiveRequest, valuePortContainer, node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_INTERACTIVEEXPRESSION, handlers, request);
		},
	
		setTaskResultToValuePort : function(taskResult, taskResultDef, valuePortContainer, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var resultName = taskResult.resultName;
			var resultValue = taskResult.resultValue;
			var outputValue = {};
			_.each(taskResultDef[resultName][node_COMMONATRIBUTECONSTANT.INTERACTIVERESULTTASK_OUTPUT], function(output, i){
				var outputName = output[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME];
				outputValue[outputName] = resultValue[outputName]; 
			});
			
			out.addRequest(node_utilityNamedVariable.setValuesPortValueRequest(valuePortContainer, node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_INTERACTIVETASK, loc_getResultValuePortNameByResultName(resultName), outputValue));
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
