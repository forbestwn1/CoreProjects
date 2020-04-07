//get/create package
var packageObj = library;    

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

var node_utility = function() 
{
	//execute data operation
	var loc_getExecuteOperationRequest = function(dataTypeId, operation, parmArray, handlers, requestInfo){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteOperation", {"dataType":dataTypeId, "operation":operation, "parms":parmArray}), handlers, requestInfo);

		var dataOperationResourceId = node_resourceUtility.createOperationResourceId(dataTypeId, operation);
		var getResourceRequest = nosliw.runtime.getResourceService().getGetResourcesRequest([dataOperationResourceId], {
			success : function(requestInfo, resourcesTree){
				var opResult = node_expressionUtility.executeOperationResource(dataOperationResourceId, parmArray, resourcesTree);
				return opResult;
			}
		});
		out.addRequest(getResourceRequest);
		return out;
	};	

	var loc_getExecuteExpressionRequest = function(expression, itemName, variables, constants, references, handlers, requestInfo){
		//expression item
		if(itemName==undefined || itemName=='')   itemName = node_COMMONCONSTANT.NAME_DEFAULT;
		var expressionItem = expression[node_COMMONATRIBUTECONSTANT.EXPRESSIONGROUP_EXPRESSIONS][itemName];
		
		return loc_getExecuteExpressionItemRequest(expressionItem, variables, constants, references, handlers, requestInfo);
	};
	
	var loc_getExecuteExpressionItemRequest = function(expressionItem, variables, constants, references, handlers, requestInfo){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteExpressionItem", {}), handlers, requestInfo);

		//execute operand
		var executeOperandRequest = loc_getExecuteOperandRequest(expressionItem[node_COMMONATRIBUTECONSTANT.EXECUTABLEEXPRESSION_OPERAND], variables, constants, references, {
			success : function(requestInfo, operandResult){
				var outputMatchers = expressionItem[node_COMMONATRIBUTECONSTANT.EXECUTABLEEXPRESSION_OUTPUTMATCHERS];
				if(outputMatchers!=undefined){
					return node_expressionUtility.getMatchDataTaskRequest(operandResult, outputMatchers);
				}
				else return operandResult;
			}
		}, null);
		out.addRequest(executeOperandRequest);
		return out;
	};
	
	//convert individual data according to matchers
	var loc_getMatchDataTaskRequest = function(data, matchers, handlers, requestInfo){
		var service = new node_ServiceInfo("MatchData", {"data":data, "matcher":matchers});
		
		var dataTypeId = data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID];
		var matcher;
		if(matchers!=undefined){
			matcher = matchers[dataTypeId];
		}
		
		var out;
		if(matcher==undefined){
			//if converter does not created, then get it
			nosliw.error("no matches for data type: " + dataTypeId);
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
					var converterRequest = loc_getExecuteConverterToRequest(converterData, targets[i], matcher.reverse, {
						success : function(requestInfo, convertedData){
							converterData = convertedData;
						}
					}, out);
					out.addRequest(converterRequest);
				}

				//convert sub data
				if(!node_basicUtility.isEmptyObject(subMatchers)==true){
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
	var loc_getSubMatchDataTaskRequest = function(data, submatchers, handlers, requestInfo){

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

	//execute operation operand
	var loc_getExecuteOperationOperandRequest = function(operationOperand, variables, constants, references, handlers, requestInfo){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteOperationOperand", {"operationOperand":operationOperand, "variables":variables}), handlers, requestInfo);

		//cal all parms
		var parmsOperand = operationOperand[node_COMMONATRIBUTECONSTANT.OPERAND_PARMS];
		var parmsOperandRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("CalOperationParms", {"parms":parmsOperand}), {
			success : function(requestInfo, setResult){
				var parmsData = setResult.getResults();
				
				//match parms and base
				var parmsMatcherRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("MatchOperationParms", {"parmsData":parmsData, "matchers":operationOperand[node_COMMONATRIBUTECONSTANT.OPERAND_MATCHERSPARMS]}), {
					success : function(requestInfo, parmMatchResult){
						var parmsData = requestInfo.getData();
						var parmMatchedData = parmMatchResult.getResults();
						_.each(parmMatchedData, function(parmValue, parmName, list){
							parmsData[parmName] = parmValue;
						}, this);
						out.setData(parmsData);

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
				}).withData(parmsData);
				_.each(operationOperand[node_COMMONATRIBUTECONSTANT.OPERAND_MATCHERSPARMS], function(parmMatchers, parmName, list){
					var parmMatchRequest = loc_getMatchDataTaskRequest(parmsData[parmName], parmMatchers, {});
					parmsMatcherRequest.addRequest(parmName, parmMatchRequest);
				}, this);
				return parmsMatcherRequest;
			}
		});
		_.each(parmsOperand, function(parmOperand, parmName, list){
			var parmOperandRequest = loc_getExecuteOperandRequest(parmOperand, variables, constants, references, {
				success :function(request, parmValue){
					return parmValue;
				}
			});
			parmsOperandRequest.addRequest(parmName, parmOperandRequest);
		}, this);
		out.addRequest(parmsOperandRequest);
		
		return out;
	};

	var loc_getExecuteOperandRequest = function(operand, variables, constants, references, handlers, requestInfo){
		var out;
		var operandType = operand[node_COMMONATRIBUTECONSTANT.OPERAND_TYPE];
		switch(operandType){
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_CONSTANT:
			out = node_createServiceRequestInfoSimple(new node_ServiceInfo("ExecuteConstantOperand", {"operand":operand, "constants":constants}), 
					function(requestInfo){
						var constantName = requestInfo.getService().parms.operand[node_COMMONATRIBUTECONSTANT.OPERAND_NAME];
						var constantData = requestInfo.getService().parms.operand[node_COMMONATRIBUTECONSTANT.OPERAND_DATA];
						if(constantData==undefined)  constantData = requestInfo.getService().parms.constants[constantName];
						return constantData;
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
			out = loc_getExecuteOperationOperandRequest(operand, variables, constants, references, handlers, requestInfo);
			break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_REFERENCE:
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteReferenceOperand", {}), handlers, requestInfo);
			//
			var varMapping = operand[node_COMMONATRIBUTECONSTANT.OPERAND_VARMAPPING];
			var refVarValue = {};
			_.each(varMapping, function(parentVar, refVar){
				refVarValue[refVar] = variables[parentVar];
			});
			
			//if have variables, convert variables
			var varsMatchers = operand[node_COMMONATRIBUTECONSTANT.OPERAND_VARMATCHERS];
			if(varsMatchers==undefined)  varsMatchers = {};
			var varsMatchRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("MatcherVariable", {"variables":variables, "variablesMatchers":varsMatchers}), 
					{			
						success : function(reqInfo, setResult){
							var variables = reqInfo.getData();
							var matchedVars = {};
							_.each(variables, function(varData, varName, list){
								var matchedVar = setResult.getResult(varName);
								if(matchedVar==undefined){
									matchedVar = variables[varName];
								}
								matchedVars[varName] = matchedVar;
							}, this);
							reqInfo.setData(matchedVars);
							
							//execute operand
							return loc_getExecuteExpressionRequest(operand[node_COMMONATRIBUTECONSTANT.OPERAND_EXPRESSION], operand[node_COMMONATRIBUTECONSTANT.OPERAND_ELEMENTNAME], variables, constants, references);
						}, 
					}, 
					null).withData(refVarValue);
				
			_.each(refVarValue, function(varData, varName, list){
				var varMatchers = varsMatchers[varName];
				if(varMatchers!=undefined){
					var request = loc_getMatchDataTaskRequest(varData, varMatchers, {}, null);
					varsMatchRequest.addRequest(varName, request);
				}
			}, this);
			out.addRequest(varsMatchRequest);
			break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_ATTRIBUTEOPERATION:
			break;
		}
		return out;
	};

	
	var loc_out = {
	
		getExecuteOperationRequest : function(dataTypeId, operation, parmArray, handlers, requestInfo){
			return loc_getExecuteOperationRequest(dataTypeId, operation, parmArray, handlers, requestInfo);
		},
			
		executeOperationResource : function(resourceId, parmArray, resourcesTree){
			var dataOperationResource = node_resourceUtility.getResourceFromTree(resourcesTree, resourceId);
			var dataOperationFun = dataOperationResource[node_COMMONATRIBUTECONSTANT.RESOURCE_RESOURCEDATA];
			var dataOperationInfo = dataOperationResource[node_COMMONATRIBUTECONSTANT.RESOURCE_INFO][node_COMMONATRIBUTECONSTANT.RESOURCEMANAGERJSOPERATION_INFO_OPERATIONINFO];
			
			//build operation context
			var operationContext = new node_OperationContext(resourcesTree, dataOperationResource.resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_DEPENDENCY]);
			
			//base data is "this" in operation function
			var baseData;
			var operationParmArray = [];
			var parmsDefinitions = dataOperationInfo[node_COMMONATRIBUTECONSTANT.DATAOPERATIONINFO_PAMRS];
			_.each(parmArray, function(parm, index, list){
				var parmName = parm[node_COMMONATRIBUTECONSTANT.OPERATIONPARM_NAME];
				if(parmName==undefined){
					//if no parm name, then use base name
					parmName = dataOperationInfo[node_COMMONATRIBUTECONSTANT.DATAOPERATIONINFO_BASEPARM];
					parm[node_COMMONATRIBUTECONSTANT.OPERATIONPARM_NAME] = parmName;
				}
				
				var parmDefinition = parmsDefinitions[parmName];
				var isBase = false;
				if(parmDefinition[node_COMMONATRIBUTECONSTANT.DATAOPERATIONPARMINFO_ISBASE]=="true"){
					isBase = true;
					baseData = parm[node_COMMONATRIBUTECONSTANT.OPERATIONPARM_DATA];
				}
				operationParmArray.push(new node_OperationParm(parm[node_COMMONATRIBUTECONSTANT.OPERATIONPARM_DATA], parmName, isBase));
			}, this);
			
			nosliw.logging.info("************************  operation   ************************");
			nosliw.logging.info(resourceId);
			_.each(parmArray, function(parm, index){
				nosliw.logging.info("Parm " + parm.name+":", parm.data);
			});
			
			var operationResult = dataOperationFun.call(baseData, new node_OperationParms(operationParmArray), operationContext);

			nosliw.logging.info("Out : ", operationResult);
			nosliw.logging.info("***************************************************************");
			
			return operationResult;
		},

		executeConvertResource : function(resourceId, data, dataTypeId, reverse, resourcesTree){
			var dataOperationResource = node_resourceUtility.getResourceFromTree(resourcesTree, resourceId);
			var dataOperationFun = dataOperationResource.resourceData;
			
			//build operation context
			var operationContext = new node_OperationContext(resourcesTree, dataOperationResource.resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_DEPENDENCY]);
			
			//data is "this" in operation function
			var result = dataOperationFun.call(data, data, dataTypeId, reverse, operationContext);
			return result;
		},
		
		getExecuteGetSubDataRequest : function(data, name, handler, requester_parent){
			getSubDatasRequest.addRequest(name, loc_out.getExecuteOperationRequest(data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID], "getSubData", parmsArray, {}));
		},
		
		//execute general operand
		getExecuteOperandRequest : function(operand, variables, constants, references, handlers, requester_parent){
			return loc_getExecuteOperandRequest(operand, variables, constants, references, handlers, requester_parent);
		},

		getMatchDataTaskRequest : function(data, matchers, handlers, requester_parent){
			return 	loc_getMatchDataTaskRequest(data, matchers, handlers, requester_parent);
		},
		
		getExecuteExpressionRequest : function(expression, eleName, variables, constants, references, handlers, requestInfo){
			return loc_getExecuteExpressionRequest(expression, eleName, variables, constants, references, handlers, requestInfo);
		},
		
		getExecuteExpressionItemRequest : function(expressionItem, variables, constants, references, handlers, requestInfo){
			return loc_getExecuteExpressionItemRequest(expressionItem, variables, constants, references, handlers, requestInfo);
		},

	};
	return loc_out;
}();

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
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
