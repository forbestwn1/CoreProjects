//get/create package
var packageObj = library.getChildPackage("service");    

(function(packageObj){
	//get used node
	var node_resourceUtility;
	var node_requestServiceProcessor;
	var node_buildServiceProvider;
	var node_COMMONATRIBUTECONSTANT;
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
	var node_dataUtility;
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
		var variablesInfo = expression[node_COMMONATRIBUTECONSTANT.EXPRESSION_VARIABLEINFOS];
			
		//if have variables, convert variables
			var varsMatchers = expression[node_COMMONATRIBUTECONSTANT.EXPRESSION_VARIABLESMATCHERS];
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
							var executeOperandRequest = loc_getExecuteOperandRequest(expression, expression[node_COMMONATRIBUTECONSTANT.EXPRESSION_OPERAND], out.getData("variables"), {
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
		var operandType = operand[node_COMMONATRIBUTECONSTANT.OPERAND_TYPE];
		switch(operandType){
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_CONSTANT:
			out = node_createServiceRequestInfoSimple(new node_ServiceInfo("ExecuteConstantOperand", {"operand":operand, "variables":variables}), 
					function(requestInfo){  
						return requestInfo.getService().parms.operand[node_COMMONATRIBUTECONSTANT.OPERAND_DATA];  
					}, 
					handlers, requestInfo);
			break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_VARIABLE: 
			out = node_createServiceRequestInfoSimple(new node_ServiceInfo("ExecuteVariableOperand", {"operand":operand, "variables":variables}), 
					function(requestInfo){  
						var varName = requestInfo.getService().parms.operand[node_COMMONATRIBUTECONSTANT.OPERAND_VARIABLENAME];
						return requestInfo.getService().parms.variables[varName];  
					}, 
					handlers, requestInfo);
		    break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_OPERATION:
			out = loc_getExecuteOperationOperandRequest(expression, operand, variables, handlers, requestInfo);
			break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_REFERENCE:
			out = loc_getExecuteExpressionRequest(expression[node_COMMONATRIBUTECONSTANT.EXPRESSION_REFERENCES][operand[node_COMMONATRIBUTECONSTANT.OPERAND_REFERENCENAME]], variables, handlers, requestInfo);
			break;
		}
		return out;
	};

	//execute operation operand
	var loc_getExecuteOperationOperandRequest = function(expression, operationOperand, variables, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteOperationOperand", {"operationOperand":operationOperand, "variables":variables}), handlers, requestInfo);

		//cal all parms
		var parmsOperand = operationOperand[node_COMMONATRIBUTECONSTANT.OPERAND_PARMS];
		var parmsOperandRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("CalOperationParms", {"parms":parmsOperand}), {
			success : function(requestInfo, setResult){
				var parmsData = setResult.getResults();
				out.setData("parmsData", parmsData);
				
				//match parms and base
				var parmsMatcherRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("MatchOperationParms", {"parmsData":parmsData, "matchers":operationOperand[node_COMMONATRIBUTECONSTANT.OPERAND_MATCHERSPARMS]}), {
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
						var dataTypeId = operationOperand[node_COMMONATRIBUTECONSTANT.OPERAND_DATATYPEID];
						var executeOperationRequest = loc_getExecuteOperationRequest(dataTypeId, operationOperand[node_COMMONATRIBUTECONSTANT.OPERAND_OPERATION], operationParms, {
							success : function(requestInfo, data){
								return data;
							}
						});
						return executeOperationRequest;
					}
				});
				_.each(operationOperand[node_COMMONATRIBUTECONSTANT.OPERAND_MATCHERSPARMS], function(parmMatchers, parmName, list){
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
		
		if(data==undefined){
			var kkkk = 5555;
			kkkk++;
		}
		
		var dataTypeId = data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID];
		var matcher = matchers[dataTypeId];
		
		var out;
		if(matcher==undefined){
			//if converter does not created, then get it
		}
		else{
			var relationship = matcher[node_COMMONATRIBUTECONSTANT.MATCHER_RELATIONSHIP];
			var subMatchers = matcher[node_COMMONATRIBUTECONSTANT.MATCHER_SUBMATCHERS];
			var sourceDataTypeId = relationship[node_COMMONATRIBUTECONSTANT.DATATYPERELATIONSHIP_SOURCE];
			var targetDataTypeId = relationship[node_COMMONATRIBUTECONSTANT.DATATYPERELATIONSHIP_TARGET];
			
			if(sourceDataTypeId==targetDataTypeId){
				if(node_basicUtility.isEmptyObject(subMatchers)==true){
					//no need to convert
					out = node_createServiceRequestInfoSimple(service, function(requestInfo){
						return requestInfo.getService().parms.data;
					}, handlers, requestInfo);
				}
				else{
					out = node_createServiceRequestInfoService(service, handlers, requestInfo);
					var resourceRequestDependency = new node_DependentServiceRequestInfo(loc_getSubMatchDataTaskRequest(data, subMatchers), {
						success : function(requestInfo, convertedSubData){
							return convertedSubData;
						}
					});
					out.setDependentService(resourceRequestDependency);
				}
			}
			else{
				out = node_createServiceRequestInfoSequence(service, handlers, requestInfo);
				var matcherSegments = relationship[node_COMMONATRIBUTECONSTANT.DATATYPERELATIONSHIP_PATH];

				var targets = [];
				var sourceId = relationship[node_COMMONATRIBUTECONSTANT.DATATYPERELATIONSHIP_SOURCE];
				var targetId;
				for(var i=0; i<matcherSegments.length; i++){
					targetId = node_namingConvensionUtility.parseLevel2(matcherSegments[i])[1];
					targets.push(targetId);
					sourceId = targetId;
				}
				
				var converterData = data;
				for(var i=0; i<targets.length; i++){
					var converterRequest = loc_getExecuteConverterToRequest(converterData, targets[i], {
						success : function(requestInfo, convertedData){
							converterData = convertedData;
						}
					}, out);
					out.addRequest(converterRequest);
				}

				//convert sub data
				if(node_basicUtility.isEmptyObject(subMatchers)==true){
					out.addRequest(loc_getSubMatchDataTaskRequest(converterData, subMatchers, {
						success : function(requestInfo, convertedData){
							converterData = convertedData;
						}
					}));
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

	//convert data according to sub matchers
	var loc_getSubMatchDataTaskRequest = function(data, submatchers, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);

		//get all subNames involved in match
		var subNames = [];
		_.each(submatchers, function(submatcher, name){subNames.push(name)});

		var subDatas = {};
		
		//get all sub data
		var getSubDatasRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("GetSubDatas", {"data":data, "subNames":subNames}), {
			success : function(requestInfo, subDatasResult){
				subDatas = subDatasResult.getResults();
			}
		});
		
		_.each(subNames, function(name){
			//get each sub data request
			getSubDatasRequest.addRequest(name, loc_out.getExecuteOperationRequest(data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID], "getSubData", [{"name":node_dataUtility.createStringData("name")}], {}));
		});

		//convert all sub data
		var convertSubDatasRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("MatchSubDatas", {"data":subDatas, "subNames":subNames}), {
			success : function(requestInfo, subDatasResult){
				subDatas = subDatasResult.getResults();
			}
		});
		
		//convert each sub data
		_.each(subNames, function(name){
			convertSubDatasRequest.addRequest(name, loc_getMatchDataTaskRequest(subDatas[name], submatchers[name], {}));
		});
		
		//set all sub data
		var setSubDatasRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("SetSubDatas", {"subDatas":subDatas, "subNames":subNames}), {
			success : function(requestInfo, subValuesResult){
			}
		});
			
		_.each(subNames, function(name){
			setSubDatasRequest.addRequest(name, loc_out.getExecuteOperationRequest(data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID], "setSubData", [{"name":node_dataUtility.createStringData("name")}, {"data":subDatas[name]}], {}));
		});
			
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("SubMatchers", {"data":data, "submatchers":submatchers}), handlers, requestInfo);
		out.addRequest(getSubDatasRequest);
		out.addRequest(convertSubDatasRequest);
		out.addRequest(setSubDatasRequest);
		return out;
	};
	
	//execute conterter
	var loc_getExecuteConverterToRequest = function(data, targetDataTypeId, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		var out = node_createServiceRequestInfoService(new node_ServiceInfo("ExecuteConverter", {"data":data, "targetDataTypeId":targetDataTypeId}), handlers, requestInfo);
		
		var converterResourceId = node_resourceUtility.createConverterToResourceId(data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID]);
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
				nosliw.logging.info("************************  operation");
				nosliw.logging.info(dataTypeId, operation);
				_.each(parmArray, function(parm, index){
					nosliw.logging.info("Parm", parm.name, ":",JSON.stringify(parm.value));
				});

				var out = node_expressionUtility.executeOperationResource(dataOperationResourceId, parmArray, resourcesTree);

				nosliw.logging.info("Out", ":",JSON.stringify(out));
				nosliw.logging.info("************************  operation");

				return out;
			}
		});
		
		out.setDependentService(resourceRequestDependency);
		return out;
	};	


	var loc_getExecuteScriptExpressionRequest = function(script, expressions, variables, scriptConstants, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		//calculate multiple expression
		var executeMultipleExpressionRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("ExecuteMultipleExpression", {"expressions":expressions, "variables":variables}), {});
		_.each(expressions, function(expression, name){
			executeMultipleExpressionRequest.addRequest(name, loc_getExecuteExpressionRequest(expression, variables, {}));
		});

		var executeScriptExpressionRequest = node_createServiceRequestInfoService(new node_ServiceInfo("ExecuteScriptExpression", {"script":script, "expressions":expressions, "variables":variables}), handlers, requestInfo);
		var requestDependency = new node_DependentServiceRequestInfo(executeMultipleExpressionRequest, {
			success : function(requestInfo, expressionsResult){
				var expressionsData = expressionsResult.getResults();
				return script.call(undefined, expressionsData, scriptConstants);
			}
		});
		executeScriptExpressionRequest.setDependentService(requestDependency);

		return executeScriptExpressionRequest;
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
		},

		/**
		 * Execute script expression
		 * 		script : function with parameter map (name : expression result)
		 * 		expressions : map (name : expression)
		 * 		variables : variables for expression
		 */
		getExecuteScriptExpressionRequest : function(script, expressions, variables, scriptConstants, handlers, requester_parent){
			return loc_getExecuteScriptExpressionRequest(script, expressions, variables, scriptConstants, handlers, requester_parent);
		},
	
		executeExecuteScriptExpressionRequest : function(script, expressions, variables, scriptConstants, handlers, requester_parent){
			var requestInfo = this.getExecuteScriptExpressionRequest(script, expressions, variables, scriptConstants, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo, false);
		},
	};
	
	loc_out = node_buildServiceProvider(loc_out, "expressionService");
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationContext", function(){node_OperationContext = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParm", function(){node_OperationParm = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParms", function(){node_OperationParms = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("expression.dataUtility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createExpressionService", node_createExpressionService); 
	
})(packageObj);
