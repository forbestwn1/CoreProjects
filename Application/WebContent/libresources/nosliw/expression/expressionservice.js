//get/create package
var packageObj = library.getChildPackage("expressionservice");    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

	
var loc_executeExpression = function(expression, variables, handlers){

	var executeExpressionRequest = createServiceRequestInfoSequence();
	
	//convert variables
	var variablesData = varsData;
	var convertVarsRequest = loc_buildConvertVarsTask(expression.variables, epression.varConverters, {
		success : function(varsData, requestInfo){
			variablesData = varsData;
		}
	});
	executeExpressionRequest.addRequet(convertVarsRequest);
	
	//execute operand
	var executeOperandRequest = loc_buildExecuteOperandRequest(expression.operand, variablesData, {
		success : function(operandResult, requestInfo){
			
		}
	});
	executeExpressionRequest.addRequet(executeOperandRequest);
	
	//execute task
	nosliw.getRequestServiceManager().processRequest(executeExpressionRequest, false);
};
	
//convert 
var loc_buildConvertVarsTask = function(varsData, varsConverter, handlers){
	//convert variables
	var contertedVars = {};
	var varConvertRequest = createServiceRequestInfoSequenceSet();
	var varConverters = expression.varConverters;
	_.each(varsData, function(varData, varName, list){
		var request = loc_buildConvertTask(varData, varsConverter[varName], {
			success : function(convertedVarData, requestInfo){
				contertedVars[varName] = convertedVarData;
			}
		});
		varConvertRequest.add(request);
	}, this);
	
};

//convert individual var
var loc_buildConvertTask = function(data, varConverter, handlers){
	var dataType = data.dataType;
	var converter = varConverter[dataType];
	
	if(converter==undefined){
		
	}
	else{
		var out = createServiceRequestInfoService(null, handlers);
		
		var converterIds;
		var resourceTask = resourceManager.createGetResourcesTask(converterIds, {
			success : function(resources, requestInfo){
				var input = data;
				for(var i in converterIds){
					var converterId = converterIds[i];
					var resource = resources[converterId];
					input = resource.data[input];
				}
				return input;
			}
		});
		
		out.setDependentService(resourceTask);
		return out;
	}
	
	return resourceManager.createGetResourcesTask();
};

var loc_buildExecuteOperandRequest = function(operand, variables, handlers){
	var out;
	var operandType = operand.type;
	switch(operandType){
	case CONSTANT:
		out = createServiceRequestInfoService(undefined, loc_calConstant, handlers);
		break;
	case VARIABLE: 
		out = createServiceRequestInfoService(undefined, loc_calVariable, handlers);
	    break;
	case OPERATION:
		out = loc_buildExecuteOperationOperandRequest(operand, variables, handlers);
		break;
	case REFERENCE:
		out = expressionService.buildExecuteExpressionRequest(operand.expression, variables, handlers);
		break;
	}
};

var loc_buildExecuteOperationOperandRequest = function(operationOperand, variables, handlers, requestInfo){
	var out = createServiceRequestInfoSequence();

	//cal all parms data and base data
	var parmsOperand = operationOperand.parms;
	var baseOperand = operationOperand.baseOperand;
	
	var parmsData = {};
	var baseData;
	
	var parmsOperandRequest = createServiceRequestInfoSequenceSet(undefined, {
		success : function(data, requestInfo){
			
		}
	});
	_.each(parmsOperand, function(parmOperand, parmName, list){
		var parmOperandRequest = loc_buildExecuteOperandRequest(parmOperand, variables, {
			success : function(result, requestInfo){
				parmsData[parmName] = result;
			}
		});
		parmsOperandRequest.add(parmOperandRequest);
	}, this);
	
	var baseOperandRequest = loc_buildExecuteOperandRequest(baseOperand, variables, {
		success : function(result, requestInfo){
			baseData = result;
		}
	});
	parmsOperandRequest.add(baseOperandRequest);
	out.addRequest(parmsOperandRequest);
	
	
	//figure out operation and converter
	var operationDiscoveryRequest = resourceManager.createOperationDiscoveryRequest(operationName, parms, baseData, {
		success : function(operationDiscovery){
			operationOperand.dataTypeId = operationDiscovery.dataTypeId;
			for(var parmName in operationOperand.parmNames){
				var parmConverters = operationOperand.parmsConverters[parmName];
				for(var dataTypeId in parmVerters){
					operationOperand.parmConverters[parmName][dataTypeId] = parmVerters[dataTypeId];
				}
			}
		}
	}, requestInfo);
	out.addRequest(operationDiscoveryRequest);
	
	//prepare parm and base data
	var operationParms = {};
	var operationBaseData;
	
	var parmsConvertRequest = createServiceRequestInfoSequenceSet(undefined, {
		success : function(data, requestInfo){
			
		}
	});
	_.each(parmsData, function(parmData, parmName, list){
		var parmConvertRequest = loc_buildConvertTask(parmData, operationOperand.parmConverters[parmName], {
			success : function(parmData, requestInfo){
				operationParms[parmName] = parmData;
			}
		});
		parmsConvertRequest.add(parmOperandRequest);
	}, this);
	out.addRequest(parmsConvertRequest);
	
	//cal operation
	var operationRequest = loc_buildExecuteOperationRequest(operationOperand.dataTypeId, operationOperand.operation, operationParms, operationBaseData);
	out.addRequest(operationRequest);
	
	return out;
}

var loc_buildExecuteOperationRequest = function(dataTypeId, operation, parms, requestInfo){
	var operationId;
	var resourceIds;
	var resourceTask = resourceManager.createGetResourcesTask(resourceIds, {
		success : function(resources, requestInfo){
			var operation = resources[operationId];
			var result = operation.operate(parms);
			return result
		}
	});
};	

var loc_calConstant = function(requestInfo){
	return operand.data;
}

var loc_calVariable = function(requestInfo){
	return operand.data;
}




	
	
	
var loc_buildService = function(expression, variables){
	
};	
	
var loc_buildExpressionExecuteRequest = function(expression, variables, handlers){
	createServiceRequestInfoSequence(loc_buildService(service, variables), handlers, requester_parent)	
		
};
		
/**
 * 
 */
var node_createExpressionService = function(){
		
	var loc_out = {
			
		execute : function(expression, variables, requestInfo){
			var operand = expression[NOSLIWATCOMMONTRIBUTECONSTANT.EXPRESSION_OPERAND];
			var variablesInfo = expression[NOSLIWATCOMMONTRIBUTECONSTANT.EXPRESSION_VARIABLES];
			
			
		},
	};
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createExpressionService", node_createExpressionService); 

	var module = {
		start : function(packageObj){
		}
	};
	nosliw.registerModule(module, packageObj);
})(packageObj);
