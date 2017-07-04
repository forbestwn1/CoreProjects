//get/create package
var packageObj = library.getChildPackage("service");    

(function(packageObj){
	//get used node
	var node_resourceUtility;
	var node_requestServiceProcessor;
	var node_buildServiceProvider;
	var node_COMMONTRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_basicUtility;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoService;
	var node_createServiceRequestInfoSimple;
	var node_OperationContext;
	var node_OperationParm;
	var node_OperationParms;
	var node_DependentServiceRequestInfo;
	var node_expressionUtility;
	var node_namingConvensionUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createExpressionService = function(){
	/**
	 * Request for execute expression
	 */
	var loc_getExecuteExpressionRequest = function(expression, variables, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteExpression", {"expression":expression, "variables":variables}), handlers, requestInfo);
		out.setData("variables", variables);
		var variablesInfo = expression[node_COMMONTRIBUTECONSTANT.EXPRESSION_VARIABLEINFOS];
			
		//if have variables, convert variables
			var varsMatchers = expression[node_COMMONTRIBUTECONSTANT.EXPRESSION_VARIABLESMATCHERS];
			var varsMatchRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("MatcherVariable", {"variables":variables, "variablesMatchers":varsMatchers}), 
					{			
						success : function(reqInfo, setResult){
							var matchedVars = {};
							_.each(variables, function(varData, varName, list){
								var matchedVar = setResult.getResult(varName);
								if(matchedVar==undefined){
									matchedVar = variables[varName];
								}
								matchedVars[varName] = matchedVar;
							}, this);
							out.setData("variables", matchedVars);
							
							//execute operand
							var executeOperandRequest = loc_getExecuteOperandRequest(expression, expression[node_COMMONTRIBUTECONSTANT.EXPRESSION_OPERAND], out.getData("variables"), {
								success : function(requestInfo, operandResult){
									return operandResult;
								}
							}, null);
							return executeOperandRequest;
						}, 
					}, 
					null);
			_.each(variables, function(varData, varName, list){
				var varMatchers = varsMatchers[varName];
				if(varMatchers!=undefined){
					var request = loc_getMatchDataTaskRequest(varData, varMatchers, {}, null);
					varsMatchRequest.addRequest(varName, request);
				}
			}, this);

			out.addRequest(varsMatchRequest);
		
		return out;
	};

	//execute general operand
	var loc_getExecuteOperandRequest = function(expression, operand, variables, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);

		var out;
		var operandType = operand[node_COMMONTRIBUTECONSTANT.OPERAND_TYPE];
		switch(operandType){
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_CONSTANT:
			out = node_createServiceRequestInfoSimple(new node_ServiceInfo("ExecuteConstantOperand", {"operand":operand, "variables":variables}), 
					function(requestInfo){  
						return requestInfo.getService().parms.operand[node_COMMONTRIBUTECONSTANT.OPERAND_DATA];  
					}, 
					handlers, requestInfo);
			break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_VARIABLE: 
			out = node_createServiceRequestInfoSimple(new node_ServiceInfo("ExecuteVariableOperand", {"operand":operand, "variables":variables}), 
					function(requestInfo){  
						var varName = requestInfo.getService().parms.operand[node_COMMONTRIBUTECONSTANT.OPERAND_VARIABLENAME];
						return requestInfo.getService().parms.variables[varName];  
					}, 
					handlers, requestInfo);
		    break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_OPERATION:
			out = loc_getExecuteOperationOperandRequest(expression, operand, variables, handlers, requestInfo);
			break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_REFERENCE:
			out = loc_getExecuteExpressionRequest(expression[node_COMMONTRIBUTECONSTANT.EXPRESSION_REFERENCES][operand[node_COMMONTRIBUTECONSTANT.OPERAND_REFERENCENAME]], variables, handlers, requestInfo);
			break;
		}
		return out;
	};

	//execute operation operand
	var loc_getExecuteOperationOperandRequest = function(expression, operationOperand, variables, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteOperationOperand", {"operationOperand":operationOperand, "variables":variables}), handlers, requestInfo);

		//cal all parms
		var parmsOperand = operationOperand[node_COMMONTRIBUTECONSTANT.OPERAND_PARMS];
		var parmsOperandRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("CalOperationParms", {"parms":parmsOperand}), {
			success : function(requestInfo, setResult){
				var parmsData = setResult.getResults();
				out.setData("parmsData", parmsData);
				
				//match parms and base
				var parmsMatcherRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("MatchOperationParms", {"parmsData":parmsData, "matchers":operationOperand[node_COMMONTRIBUTECONSTANT.OPERAND_MATCHERSPARMS]}), {
					success : function(requestInfo, parmMatchResult){
						var parmsData = out.getData("parmsData");
						var parmMatchedData = parmMatchResult.getResults();
						_.each(parmMatchedData, function(parmValue, parmName, list){
							parmsData[parmName] = parmValue;
						}, this);
						out.setData("parmsData", parmsData);

						//build parms for operation
						var operationParms = [];
						_.each(parmsData, function(parmData, parmName, list){
							operationParms.push(new node_OperationParm(parmData, parmName));
						}, this);

						//execute data operation
						var executeOperationRequest = loc_getExecuteOperationRequest(operationOperand[node_COMMONTRIBUTECONSTANT.OPERAND_DATATYPEID], operationOperand[node_COMMONTRIBUTECONSTANT.OPERAND_OPERATION], operationParms, {
							success : function(requestInfo, data){
								return data;
							}
						});
						return executeOperationRequest;
					}
				});
				_.each(operationOperand[node_COMMONTRIBUTECONSTANT.OPERAND_MATCHERSPARMS], function(parmMatchers, parmName, list){
					var parmMatchRequest = loc_getMatchDataTaskRequest(parmsData[parmName], parmMatchers, {});
					parmsMatcherRequest.addRequest(parmName, parmMatchRequest);
				}, this);
				return parmsMatcherRequest;
			}
		});
		_.each(parmsOperand, function(parmOperand, parmName, list){
			var parmOperandRequest = loc_getExecuteOperandRequest(expression, parmOperand, variables, {});
			parmsOperandRequest.addRequest(parmName, parmOperandRequest);
		}, this);
		out.addRequest(parmsOperandRequest);
		
		return out;
	}

	
	//convert individual data according to matchers
	var loc_getMatchDataTaskRequest = function(data, matchers, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);

		var service = new node_ServiceInfo("MatchData", {"data":data, "matcher":matchers});
		
		var dataTypeId = data[node_COMMONTRIBUTECONSTANT.DATA_DATATYPEID];
		var matcher = matchers[dataTypeId];
		
		var out;
		if(matcher==undefined){
			//if converter does not created, then get it
		}
		else{
			var sourceDataTypeId = matcher[node_COMMONTRIBUTECONSTANT.DATATYPERELATIONSHIP_SOURCE];
			var targetDataTypeId = matcher[node_COMMONTRIBUTECONSTANT.DATATYPERELATIONSHIP_TARGET];
			if(sourceDataTypeId==targetDataTypeId){
				//no need to convert
				out = node_createServiceRequestInfoSimple(service, function(requestInfo){
					return requestInfo.getService().parms.data;
				}, handlers, requestInfo);
			}
			else{
				out = node_createServiceRequestInfoSequence(service, handlers, requestInfo);
				var matcherSegments = matcher[node_COMMONTRIBUTECONSTANT.DATATYPERELATIONSHIP_PATH];

				var converters = [];
				for(var i=0; i<matcherSegments.length; i++){
					var sourceId = node_namingConvensionUtility.parseLevel2(matcherSegments[i])[1];
					var targetId;
					if(i<matcherSegments.length-1){
						targetId = node_namingConvensionUtility.parseLevel2(matcherSegments[i+1])[1];
					}
					else{
						targetId = matcher[node_COMMONTRIBUTECONSTANT.DATATYPERELATIONSHIP_TARGET];
					}
					converters.push({
						sourceConverter : "converter;"+sourceId+"from",
						targetId : targetId,
					});
				}
				
				var converterData = data;
				for(var i=0; i<converters.length; i++){
					var converterRequest = loc_getExecuteConverterToRequest(converterData, converters[i].sourceConverter, converters[i].targetId, {
						success : function(requestInfo, convertedData){
							converterData = convertedData;
						}
					}, out);
					out.addRequest(converterRequest);
				}

				out.setRequestProcessors({
					success : function(reqInfo, result){
						return converterData;
					}, 
				});
			}
			return out;
		}
	};

	//execute conterter
	var loc_getExecuteConverterToRequest = function(data, targetDataTypeId, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		var out = node_createServiceRequestInfoService(new node_ServiceInfo("ExecuteConverter", {"data":data, "targetDataTypeId":targetDataTypeId}), handlers, requestInfo);
		
		var converterResourceId = node_resourceUtility.createConverterToResourceId(data[node_COMMONTRIBUTECONSTANT.DATA_DATATYPEID]);
		var getResourceRequest = nosliw.runtime.getResourceService().getGetResourcesRequest([converterResourceId]);
		
		var resourceRequestDependency = new node_DependentServiceRequestInfo(getResourceRequest, {
			success : function(requestInfo, resourcesTree){
				return node_expressionUtility.executeConvertToResource(converterResourceId, data, targetDataTypeId, resourcesTree);
			}
		});
		
		out.setDependentService(resourceRequestDependency);
		return out;
	};
	
	//execute data operation
	var loc_getExecuteOperationRequest = function(dataTypeId, operation, parmArray, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		var out = node_createServiceRequestInfoService(new node_ServiceInfo("ExecuteOperation", {"dataType":dataTypeId, "operation":operation, "parms":parmArray}), handlers, requestInfo);
		
		var dataOperationResourceId = node_resourceUtility.createOperationResourceId(dataTypeId, operation);
		var getResourceRequest = nosliw.runtime.getResourceService().getGetResourcesRequest([dataOperationResourceId]);

		var resourceRequestDependency = new node_DependentServiceRequestInfo(getResourceRequest, {
			success : function(requestInfo, resourcesTree){
				return node_expressionUtility.executeOperationResource(dataOperationResourceId, parmArray, resourcesTree);
			}
		});
		
		out.setDependentService(resourceRequestDependency);
		return out;
	};	


	
	
	var loc_out = {
		getExecuteOperationRequest : function(dataTypeId, operation, parmsArray, handlers, requester_parent){
			return loc_getExecuteOperationRequest(dataTypeId, operation, parmsArray, handlers, requester_parent);
		},
			
		executeExecuteOperationRequest : function(dataTypeId, operation, parmsArray, handlers, requester_parent){
			var requestInfo = this.getExecuteOperationRequest(dataTypeId, operation, parmsArray, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo, false);
		},

		
		getExecuteExpressionRequest : function(expression, variables, handlers, requester_parent){
			return loc_getExecuteExpressionRequest(expression, variables, handlers, requester_parent);
		},
			
		executeExecuteExpressionRequest : function(expression, variables, handlers, requester_parent){
			var requestInfo = this.getExecuteExpressionRequest(expression, variables, handlers, requester_parent);
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
			node_resourceUtility = packageObj.getNodeData("resource.resourceUtility");
			node_buildServiceProvider = packageObj.getNodeData("request.buildServiceProvider");
			node_requestServiceProcessor = packageObj.getNodeData("request.requestServiceProcessor");
			node_COMMONCONSTANT = packageObj.getNodeData("constant.COMMONCONSTANT");
			node_COMMONTRIBUTECONSTANT = packageObj.getNodeData("constant.COMMONTRIBUTECONSTANT");
			node_createServiceRequestInfoSequence = packageObj.getNodeData("request.request.createServiceRequestInfoSequence");
			node_ServiceInfo = packageObj.getNodeData("common.service.ServiceInfo");
			node_basicUtility = packageObj.getNodeData("common.utility.basicUtility");
			node_createServiceRequestInfoSet = packageObj.getNodeData("request.request.createServiceRequestInfoSet");
			node_createServiceRequestInfoService = packageObj.getNodeData("request.request.createServiceRequestInfoService");
			node_createServiceRequestInfoSimple = packageObj.getNodeData("request.request.createServiceRequestInfoSimple");
			node_OperationContext = packageObj.getNodeData("expression.entity.OperationContext");
			node_OperationParm = packageObj.getNodeData("expression.entity.OperationParm");
			node_OperationParms = packageObj.getNodeData("expression.entity.OperationParms");
			node_DependentServiceRequestInfo = packageObj.getNodeData("request.request.entity.DependentServiceRequestInfo");  
			node_expressionUtility = packageObj.getNodeData("expression.expressionUtility");
			node_namingConvensionUtility = packageObj.getNodeData("common.namingconvension.namingConvensionUtility");
		}
	};
	nosliw.registerModule(module, packageObj);
})(packageObj);
