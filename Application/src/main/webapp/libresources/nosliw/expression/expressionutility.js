//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
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
	var node_valueContextUtility;
	var node_complexEntityUtility;
	var node_getApplicationInterface;
	var node_createValuePortElementInfo;
	var node_ResourceId;
	var node_getWithValuePortInterface;
	var node_taskUtility;
	var node_ElementIdValuePair;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function() 
{
	
	var loc_getExecuteDataExpressionRequest =function(expressionItem, valuePortEnv, constants, handlers, requestInfo){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteExpressionItem", {}), handlers, requestInfo);

		//build variable value according to alias definition in expression item
		out.addRequest(loc_getVariablesValueByKeyRequest(expressionItem[node_COMMONATRIBUTECONSTANT.WITHVARIABLE_VARIABLEINFOS], valuePortEnv, {
			success : function(request, allVariables){
				//execute operand
				return loc_getExecuteOperandRequest(expressionItem[node_COMMONATRIBUTECONSTANT.EXPRESSIONDATA_OPERAND], allVariables, constants, {
					success : function(requestInfo, operandResult){
						var outputMatchers = expressionItem[node_COMMONATRIBUTECONSTANT.DATAEXPRESSION_OUTPUTMATCHERS];
						if(outputMatchers!=undefined){
							return node_expressionUtility.getMatchDataTaskRequest(operandResult, outputMatchers);
						}
						else return operandResult;
					}
				}, null);
			}
		}));
		return out;
	};

	var loc_getVariablesValueByKeyRequest = function(varInfos, valuePortEnv, handlers, request){
		
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getVariablesValueRequest"), handlers, request);
					
		var out1 = node_createServiceRequestInfoSequence(undefined, {
			success : function(request){
				var calVarsRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("calVariables", {}), {
					success : function(request, results){
						return results.getResults();
					}
				});
				
				_.each(varInfos, function(varInfo, varKey){
					var varId = varInfo[node_COMMONATRIBUTECONSTANT.VARIABLEINFO_VARIABLEID];
					var eleInfo = node_createValuePortElementInfo(varId);
					var valuePortId = varId[node_COMMONATRIBUTECONSTANT.IDELEMENT_ROOTELEMENTID][node_COMMONATRIBUTECONSTANT.IDROOTELEMENT_VALUEPORTID][node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBUNDLE_VALUEPORTID];
					var valuePort = valuePortEnv.getValuePort(valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_GROUP], valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_NAME]);
					
					calVarsRequest.addRequest(varKey, valuePort.getValueRequest(eleInfo, {
						success : function(request, data){
							var value = data;
							return value;
						}	
					}));
				});
				return calVarsRequest;
			}
		});
		
		out.addRequest(out1);
		return out;		
	};

	//execute conterter
	var loc_getExecuteConverterToRequest = function(data, targetDataTypeId, reverse, handlers, requestInfo){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteConverter", {"data":data, "targetDataTypeId":targetDataTypeId}), handlers, requestInfo);

		var dataTypeId;
		if(reverse){
			dataTypeId = targetDataTypeId;
		}
		else{
			dataTypeId = data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID];
		}
		
		var converterResourceId = node_resourceUtility.createConverterResourceId(dataTypeId);
		var getResourceRequest = nosliw.runtime.getResourceService().getGetResourcesRequest([converterResourceId], {
			success : function(requestInfo, resourcesTree){
				var dataTypeId;
				if(reverse){
					dataTypeId = data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID];
				}
				else{
					dataTypeId = targetDataTypeId;
				}
				return loc_out.executeConvertResource(converterResourceId, data, dataTypeId, reverse, resourcesTree);
			}
		});
		out.addRequest(getResourceRequest);
		return out;
	};
	

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

	//convert individual data according to matchers
	var loc_getMatchDataTaskRequest = function(data, matchers, handlers, requestInfo){
		if(data==undefined)  return;
		
		var service = new node_ServiceInfo("MatchData", {"data":data, "matcher":matchers});
		
		if(matchers==undefined){
			return  node_createServiceRequestInfoSimple(service, function(requestInfo){
				return data;
			}, handlers, requestInfo);
		}
		
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
	var loc_getExecuteOperationOperandRequest = function(operationOperand, variables, constants, handlers, requestInfo){
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
			var parmOperandRequest = loc_getExecuteOperandRequest(parmOperand, variables, constants, {
				success :function(request, parmValue){
					return parmValue;
				}
			});
			parmsOperandRequest.addRequest(parmName, parmOperandRequest);
		}, this);
		out.addRequest(parmsOperandRequest);
		
		return out;
	};

	//execute reference operand
	var loc_getExecuteReferenceOperandRequest = function(referenceOperand, variables, constants, handlers, requestInfo){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteReferenceOperand", {}), handlers, requestInfo);

		//cal all mapping operands
		var refVarsMapping = referenceOperand[node_COMMONATRIBUTECONSTANT.OPERAND_VARMAPPING];
		var refVarsInOperandRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("CalRefVarsInOperandRequest", {"refVarsMapping":refVarsMapping}), {
			success : function(request, setResult){
				var out1 = node_createServiceRequestInfoSequence(undefined);
				out1.addRequest(nosliw.runtime.getComplexEntityService().getCreateBundleRuntimeWithInitRequest(referenceOperand[node_COMMONATRIBUTECONSTANT.OPERAND_RESOURCEID], undefined, {
					success: function(request, bundleRuntime){
						var dataExpressionLibEleCore = bundleRuntime.getCoreEntity().getMainEntityCore();
						valuePortEnv = node_getWithValuePortInterface(dataExpressionLibEleCore);
						
						var valueMappings = setResult.getResults();
						var valuePortId;
						var toValuesInfo = [];
						var varsId = referenceOperand[node_COMMONATRIBUTECONSTANT.OPERAND_VARRESOLVE];
						_.each(valueMappings, function(value, name){
							valuePortId = varsId[name][node_COMMONATRIBUTECONSTANT.IDELEMENT_ROOTELEMENTID][node_COMMONATRIBUTECONSTANT.IDROOTELEMENT_VALUEPORTID][node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBUNDLE_VALUEPORTID];
							toValuesInfo.push(new node_ElementIdValuePair(node_createValuePortElementInfo(varsId[name]), value));
						});
						var valuePort = valuePortEnv.getValuePort(valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_GROUP], valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_NAME]);
						
						var onInitTaskRequest = function(handlers, request){
							var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
							out.addRequest(valuePort.setValuesRequest(toValuesInfo));
							return out;
						};
						
						return node_taskUtility.getExecuteEntityTaskRequest(dataExpressionLibEleCore, onInitTaskRequest, undefined, undefined, {
							success : function(request, task){
								return task.getTaskResult();
							}
						});
					}
				}));
				return out1;
			}
		});
		
		_.each(refVarsMapping, function(refVarInOperand, refVarId, list){
			var refVarInOperandRequest = loc_getExecuteOperandRequest(refVarInOperand, variables, constants, {
				success :function(request, refVarInValue){
					return refVarInValue;
				}
			});
			refVarsInOperandRequest.addRequest(refVarId, refVarInOperandRequest);
		}, this);
		out.addRequest(refVarsInOperandRequest);
		
		return out;
	};
		
	//execute reference operand
	var loc_getExecuteOperandRequest = function(operand, variables, constants, handlers, requestInfo){
		var out;
		var operandType = operand[node_COMMONATRIBUTECONSTANT.OPERAND_TYPE];
		switch(operandType){
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_CONSTANT:
			out = node_createServiceRequestInfoSimple(new node_ServiceInfo("ExecuteConstantOperand", {"operand":operand}), 
					function(requestInfo){
						var constantName = requestInfo.getService().parms.operand[node_COMMONATRIBUTECONSTANT.OPERAND_NAME];
						var constantData = requestInfo.getService().parms.operand[node_COMMONATRIBUTECONSTANT.OPERAND_DATA];
						if(constantData==undefined)  constantData = constants[constantName];
						return constantData;
					}, 
					handlers, requestInfo);
			break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_VARIABLE: 
			out = node_createServiceRequestInfoSimple(new node_ServiceInfo("ExecuteVariableOperand", {"operand":operand, "variables":variables}), 
					function(requestInfo){  
						var varKey = requestInfo.getService().parms.operand[node_COMMONATRIBUTECONSTANT.OPERAND_VARIABLEKEY];
						return requestInfo.getService().parms.variables[varKey];  
					}, 
					handlers, requestInfo);
		    break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_OPERATION:
			out = loc_getExecuteOperationOperandRequest(operand, variables, constants, handlers, requestInfo);
			break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_REFERENCE:
			out = loc_getExecuteReferenceOperandRequest(operand, variables, constants, handlers, requestInfo);
			break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_ATTRIBUTEOPERATION:
			break;
		}
		return out;
	};

	var loc_getExecuteScriptExpressionRequest = function(scriptExpresion, valuePortEnv, constants, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteScriptExpressionItem", {}), handlers, request);

		//build variable value according to alias definition in expression item
		out.addRequest(loc_getVariablesValueByKeyRequest(scriptExpresion[node_COMMONATRIBUTECONSTANT.WITHVARIABLE_VARIABLEINFOS], valuePortEnv, {
			success : function(request, allVariables){
				var dataExpressions = scriptExpresion[node_COMMONATRIBUTECONSTANT.EXPRESSIONSCRIPT_DATAEXPRESSION];
				
				var calDataExpressionsRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("calDataExpressionsRequest", {}), {
					success : function(request, results){
						var expressionResults = results.getResults();
						var scriptFun = scriptExpresion[node_COMMONATRIBUTECONSTANT.EXPRESSIONSCRIPT_SCRIPTFUNCTION];
						return scriptFun(scriptExpresion[node_COMMONATRIBUTECONSTANT.EXPRESSIONSCRIPT_SUPPORTFUNCTION], expressionResults, undefined, allVariables);
					}
				});
				_.each(dataExpressions[node_COMMONATRIBUTECONSTANT.CONTAINER_ITEM], function(dataExpressionItem, dataExpressionId){
					var dataExpression = dataExpressionItem[node_COMMONATRIBUTECONSTANT.ITEMWRAPPER_VALUE];
					calDataExpressionsRequest.addRequest(dataExpressionId, loc_getExecuteOperandRequest(dataExpression[node_COMMONATRIBUTECONSTANT.EXPRESSIONDATA_OPERAND], allVariables, constants, {
						success : function(requestInfo, operandResult){
							return operandResult;
						}
					}, null));
				});
				return calDataExpressionsRequest;
			}
		}));
		return out;
	};


	var loc_out = {
	
		getExecuteOperationRequest : function(dataTypeId, operation, parmArray, handlers, requestInfo){
			return loc_getExecuteOperationRequest(dataTypeId, operation, parmArray, handlers, requestInfo);
		},
			
		executeOperationResource : function(resourceId, parmArray, resourcesTree){
			var dataOperationResource = node_resourceUtility.getResourceFromTree(resourcesTree, resourceId);
			var resourceData = dataOperationResource[node_COMMONATRIBUTECONSTANT.RESOURCE_RESOURCEDATA];
			var dataOperationFun = resourceData[node_COMMONATRIBUTECONSTANT.RESOURCEDATAJSVALUE_VALUE]; 
			var dataOperationInfo = resourceData[node_COMMONATRIBUTECONSTANT.RESOURCEDATAJSOPERATIONIMP_OPERATIONINFO];  
//			dataOperationResource[node_COMMONATRIBUTECONSTANT.RESOURCE_INFO][node_COMMONATRIBUTECONSTANT.RESOURCEMANAGERJSOPERATION_INFO_OPERATIONINFO];
			
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
			var dataOperationFun = dataOperationResource.resourceData[node_COMMONATRIBUTECONSTANT.RESOURCEDATAJSVALUE_VALUE];
			
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
		getExecuteOperandRequest : function(operand, variables, handlers, requester_parent){
			return loc_getExecuteOperandRequest(operand, variables, handlers, requester_parent);
		},

		getMatchDataTaskRequest : function(data, matchers, handlers, requester_parent){
			return 	loc_getMatchDataTaskRequest(data, matchers, handlers, requester_parent);
		},

		getExecuteDataExpressionRequest : function(dataExpression, valuePortEnv, constants, handlers, request){
			return loc_getExecuteDataExpressionRequest(dataExpression, valuePortEnv, constants, handlers, request);
		},

		getExecuteScriptExpressionRequest : function(scriptExpresion, valuePortEnv, constants, handlers, request){
			return loc_getExecuteScriptExpressionRequest(scriptExpresion, valuePortEnv, constants, handlers, request);
		},

	};
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
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
nosliw.registerSetNodeDataEvent("complexentity.valueContextUtility", function(){node_valueContextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.getApplicationInterface", function(){node_getApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.createValuePortElementInfo", function(){node_createValuePortElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){	node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("valueport.getWithValuePortInterface", function(){node_getWithValuePortInterface = this.getData();});
nosliw.registerSetNodeDataEvent("task.taskUtility", function(){node_taskUtility = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.ElementIdValuePair", function(){node_ElementIdValuePair = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
