//get/create package
var packageObj = library.getChildPackage("expressionservice");    

(function(packageObj){
	//get used node
	var node_requestProcessor;
	var node_buildServiceProvider;
	var node_COMMONTRIBUTECONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoService;
//*******************************************   Start Node Definition  ************************************** 	

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

var node_createExpressionService = function(){
	/**
	 * Request for execute expression
	 */
	var loc_getExecuteExpressionRequest = function(expression, variables, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteExpression", {"expression":expression, "variables":variables}), handlers, requestInfo);
		var variablesInfo = expression[node_COMMONTRIBUTECONSTANT.EXPRESSION_VARIABLES];
		
		//convert variables
		var varsMatchRequest = node_createServiceRequestInfoSet();
		_.each(variables, function(varData, varName, list){
			var request = loc_getMatchDataTaskRequest(varData, varsConverter[varName], {}, requestInfo);
			varsMatchRequest.add(varName, request);
		}, this);

		varsMatchRequest.setRequestProcessors({
			success : function(reqInfo, setResult){
				var matchedVars = {};
				var results = setResult.getResults();
				_.each(results, function(result, varName, list){
					matchedVars[varName] = result;
				}, this);
				reqInfo.setData("variables", matchedData);
			}, 
		});
		out.addRequet(varsMatchRequest);
		
		//execute operand
		var executeOperandRequest = loc_buildExecuteOperandRequest(expression[node_COMMONTRIBUTECONSTANT.EXPRESSION_OPERAND], out.getData("variables"), {
			success : function(operandResult, requestInfo){
				return operandResult;
			}
		});
		out.addRequet(executeOperandRequest);
		
		return out;
	};

	//convert individual data to targetCriteria
	var loc_getMatchDataTaskRequest = function(data, converters, targetCriteria, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);

		var dataType = data[node_COMMONTRIBUTECONSTANT.DATA_DATATYPEID];
		var converter = converters[dataType];
		
		if(converter==undefined){
			//if converter does not created, then get it
		}
		else{
			//otherwise, use converter
			var out = node_createServiceRequestInfoService(null, handlers, requestInfo);
			
			var converterId = new node_ResourceId();
			var resourceTask = nosliw.runtime.getResourceService().createGetResourcesTask([converterId], {
				success : function(requestInfo, resources){
					var input = data;
					for(var i in converterIds){
						var converterId = converterIds[i];
						var resource = resources[converterId];
						input = resource.data[input];
					}
					return input;
				}
			}, requestInfo);
			
			out.setDependentService(resourceTask);
			return out;
		}
		
		return resourceManager.createGetResourcesTask();
	};


	
	var loc_out = {
		getExecuteExpressionRequest : function(expression, variables, handlers, requester_parent){
			return loc_getExecuteExpressionRequest(expression, variables, handlers, requester_parent);
		},
			
		executeExecuteExpressionRequest : function(expression, variables, handlers, requestInfo){
			var requestInfo = this.getExecuteExpressionRequest(resourceIds, handlers, requestInfo);
			node_requestServiceProcessor.processRequest(requestInfo, false);
		}
	};
	
	loc_out = node_buildServiceProvider(loc_out, "expressionService");
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createExpressionService", node_createExpressionService); 

	var module = {
		start : function(packageObj){
			node_buildServiceProvider = packageObj.getNodeData("request.buildServiceProvider");
			node_requestProcessor = packageObj.getNodeData("request.requestServiceProcessor");
			node_COMMONTRIBUTECONSTANT = packageObj.getNodeData("constant.COMMONTRIBUTECONSTANT");
			node_createServiceRequestInfoSequence = packageObj.getNodeData("request.request.createServiceRequestInfoSequence");
			node_ServiceInfo = packageObj.getNodeData("common.service.ServiceInfo");
			node_createServiceRequestInfoSet = packageObj.getNodeData("request.request.createServiceRequestInfoSet");
			node_createServiceRequestInfoService = packageObj.getNodeData("request.request.createServiceRequestInfoService");
		}
	};
	nosliw.registerModule(module, packageObj);
})(packageObj);
