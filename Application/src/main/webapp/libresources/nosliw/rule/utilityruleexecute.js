//get/create package
var packageObj = library;    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONATRIBUTECONSTANT;
var node_COMMONCONSTANT;
var node_basicUtility;
var node_parseSegment;
var node_createServiceRequestInfoSequence;
var node_createServiceRequestInfoSimple;
var node_ServiceInfo;
var node_createServiceRequestInfoSet;
var node_valueInVarOperationServiceUtility;
var node_dataUtility;
var node_requestUtility;
var node_expressionUtility;
var node_pathUtility;
var node_variableUtility;
var node_requestServiceProcessor;
var node_wrapperFactory;
var node_complexEntityUtility;
var node_createTaskSetup;
var node_taskExecuteUtility;
var node_getEntityObjectInterface;
var node_utilityNamedVariable;
var node_ruleUtility;
var node_namingConvensionUtility;

//*******************************************   Start Node Definition  **************************************

var node_createRuleValidationItem = function(ruleDef, data){
	return {
		ruleDef : ruleDef,
		data : data
	};
};
 	
var node_ruleExecuteUtility = function(){

    var loc_getRuleDefinitionsFromVariable = function(variable){
		var definition = variable.prv_info==undefined?undefined:variable.prv_info.definition;
		if(definition!=undefined){
			var dataDefinition;
			
			var defType = definition[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_TYPE];
			if(defType==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_DATA){
				dataDefinition = definition[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DATA];
			}
			else if(defType==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE&&definition[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_INHERITDEFINITION]==false){
                dataDefinition = definition[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DEFINITION][node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DATA];
			}
			
			if(dataDefinition!=undefined){
				return dataDefinition[node_COMMONATRIBUTECONSTANT.DATADEFINITION_RULE];
			}
		}
	};

	var loc_getCollectRuleInfoRequest = function(variable, operationService, allRuleInfo, handlers, request){

		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

		var command = operationService.command;
		var operationData = operationService.parms;

		if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
   			var dataRuleDefs = loc_getRuleDefinitionsFromVariable(variable);
   			
   			_.each(dataRuleDefs, function(dataRuleDef, i){
        		var opService = operationService.clone();
				var rulePath = dataRuleDef[node_COMMONATRIBUTECONSTANT.DEFINITIONDATARULE_PATH];
					
				var opPath = opService.parms.path;
				var opValue = opService.parms.value;
					
				var ruleValue;
				var comparePath = node_pathUtility.comparePath(rulePath, opPath);
                if(comparePath.compare==0){
     				ruleValue = opValue;
     				allRuleInfo.push(node_createRuleValidationItem(dataRuleDef, ruleValue));
   				}
                else if(comparePath.compare==1){
					//get child value
					var dataTypeInfo = node_dataUtility.getDataTypeInfoFromValue(opValue);
					out.addRequest(node_wrapperFactory.getDataTypeHelper(dataTypeInfo).getChildValueRequest(opValue, comparePath.subPath, {
						success : function(request, childData){
            				allRuleInfo.push(node_createRuleValidationItem(dataRuleDef, childData));
						}
					}));
				}
                else if(comparePath.compare==2){
					//
					 out.addRequest(variable.getGetValueRequest({
						success: function(request, data){
							var varValueBeforeChange = node_basicUtility.cloneObject(data);
        					var dataTypeInfo = node_dataUtility.getDataTypeInfoFromValue(varValueBeforeChange);

        					return node_wrapperFactory.getDataTypeHelper(dataTypeInfo).getChildValueRequest(varValueBeforeChange, rulePath, {
		    				    success : function(request, childData){
         					        opService.parms.path = comparePath.subPath;
		                            return node_wrapperFactory.getDataTypeHelper(dataTypeInfo).getDataOperationRequest(childData.value, opService, {
										success : function(reuqest, ruleData){
                        				    allRuleInfo.push(node_createRuleValidationItem(dataRuleDef, ruleData));
										}
									});							
				    		    }
					        });
						}
					}));
				}
			});

            var childrenVars = variable.prv_getChildren();
            _.each(childrenVars, function(childVarInfo, id){
            	 var opService = operationService.clone();
    			 var compareWithChildPath = node_pathUtility.comparePath(opService.parms.path, childVarInfo.path);
				 if(compareWithChildPath.compare==2){
					 
					 out.addRequest(variable.getGetValueRequest({
						success: function(request, data){
							var ruleValueBeforeChange = node_basicUtility.cloneObject(data.value);
        					var dataTypeInfo = node_dataUtility.getDataTypeInfoFromValue(ruleValueBeforeChange);

        					return node_wrapperFactory.getDataTypeHelper(dataTypeInfo).getDataOperationRequest(ruleValueBeforeChange, opService, {
		    				    success : function(request, childData){
									
									return node_wrapperFactory.getDataTypeHelper(dataTypeInfo).getChildValueRequest(childData, childVarInfo.path, {
										success : function(request, childVariableData){
                					        opService.parms.path = "";
                					        if(childVarInfo.variable.prv_valueAdapter!=undefined){
                    							return childVarInfo.variable.prv_valueAdapter.getInValueRequest(childVariableData, {
	    			                				success: function(request, value){
														opService.parms.value = value;
		    											return loc_getCollectRuleInfoRequest(childVarInfo.variable, opService, allRuleInfo);
                								    }
			            				        });
											}
											else{
												opService.parms.value = childVariableData;
    											return loc_getCollectRuleInfoRequest(childVarInfo.variable, opService, allRuleInfo);
											}
										}
									});
				    		    }
					        });
						}
					}));
				 }
				 else if(compareWithChildPath.compare==0){
			        if(childVarInfo.variable.prv_valueAdapter!=undefined){
    					out.addRequest(childVarInfo.variable.prv_valueAdapter.getInValueRequest(opService.parms.value, {
	           				success: function(request, value){
								opService.parms.value = value;
								return loc_getCollectRuleInfoRequest(childVarInfo.variable, opService, allRuleInfo);
    					    }
				        }));
					}
				 }
				 else if(compareWithChildPath.compare==1){
					 
				 }
			});
		}
		return out;
	};
	
	var loc_convertBaseOperationServiceRequest = function(variable, operationService, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		var opService = operationService.clone();

		var command = operationService.command;
		if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
			var toRootPathInfo = node_variableUtility.toBaseVariableConvertInfo(variable);
			
			var adapters = [];
			var path = "";
			for(var i in toRootPathInfo){
				path = node_namingConvensionUtility.cascadePath(path, toRootPathInfo[i].pathToParent, false);
				var adapter = toRootPathInfo[i].adapter;
				if(adapter!=undefined){
				    if(node_pathUtility.isEmptyPath()){
						adapters.push(adapter);
					}
					else{
						throw error;
					}
				}
			}
			opService.parms.path = path;
			
			if(adapters.length>0){
				out.addRequest(loc_getExecuteAdapterRequest(opService.parms.value, adapters, 0, {
					success : function(request, value){
	    				opService.parms.value = value;
					}
				}));
			}

    		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(){
				return {
					rootVariable : toRootPathInfo[toRootPathInfo.length-1].baseVariable,
					operationService : opService
				};
		    }));
		}
		return out;
	};

    var loc_getExecuteAdapterRequest = function(value, adapters, i, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		out.addRequest(adapters[i].getOutValueRequest(value, {
			success: function(request, value){
				if(i>=adapters.length-1){
					return value;
				}
				else{
    				return loc_getExecuteAdapterRequest(value, adapters, i+1);
				}
    		}
	    }));
		
		return out;
	};

	var loc_executeRuleValidationsRequest = function(ruleValidationItems, bundle, handlers, request){
   		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var ruleValidationResults = [];
		_.each(ruleValidationItems, function(ruleValidationItem, i){
			out.addRequest(loc_executeRuleValidationRequest(ruleValidationItem, bundle, {
				success : function(request, ruleValidationResult){
					ruleValidationResults.push(ruleValidationResult);
				}
			}));
		});
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
            return ruleValidationResults;		
		}));
        return out;		
	};

	
	var loc_executeRuleValidationRequest = function(ruleValidationItem, bundle, handlers, request){
		var ruleDef = ruleValidationItem.ruleDef;
		var dataRule = ruleDef[node_COMMONATRIBUTECONSTANT.DEFINITIONDATARULE_DATARULE];
		var ruleTaskPath = dataRule[node_COMMONATRIBUTECONSTANT.DATARULE_IMPLEMENTATION][node_COMMONATRIBUTECONSTANT.DATARULEIMPLEMENTATIONLOCAL_PATHID];
		var taskEntityCore = node_complexEntityUtility.getDescendantCore(bundle, ruleTaskPath);
		
		var taskSetup = node_createTaskSetup(
			function(coreEntity, handlers, request){
				//set event data to value port
				var externalValuePortContainer = node_getEntityObjectInterface(coreEntity).getExternalValuePortContainer();
				
				if(externalValuePortContainer.getValuePortInfoByGroupTypeAndValuePortName(node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_INTERACTIVEEXPRESSION, node_COMMONCONSTANT.VALUEPORT_TYPE_INTERACTIVE_REQUEST)!=undefined){
					//expression
    				return node_utilityNamedVariable.setValuePortValueByGroupTypeRequest(
	    				externalValuePortContainer,
		    			node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_INTERACTIVEEXPRESSION,
			    		node_COMMONCONSTANT.VALUEPORT_NAME_INTERACT_REQUEST,
				    	node_COMMONCONSTANT.NAME_ROOT_DATA,
					    ruleValidationItem.data,
					    handlers, request);
				}
				else{
					//task
    				return node_utilityNamedVariable.setValuePortValueByGroupTypeRequest(
	    				externalValuePortContainer,
		    			node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_INTERACTIVETASK,
			    		node_COMMONCONSTANT.VALUEPORT_NAME_INTERACT_REQUEST,
				    	node_COMMONCONSTANT.NAME_ROOT_DATA,
					    ruleValidationItem.data,
					    handlers, request);
				}
    		}
		);
		
   		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(node_taskExecuteUtility.getExecuteWrapperedTaskRequest(taskEntityCore, taskSetup, {
			success : function(request, taskResult){
                return {
					ruleValidationItem : ruleValidationItem,
					validationResult : taskResult 
				};			
			}
		}));
        return out;
	};
	
	var loc_out = {

		getExecuteRuleValidationForVariableOperationRequest : function(varOperation, bundleCore, handlers, request){
	        return loc_out.getExecuteRuleValidationRequest(varOperation.target, varOperation.operationService, bundleCore, handlers, request);		
		},

		executeExecuteRuleValidationForVariableOperationRequest : function(varOperation, bundleCore, handlers, request){
			var requestInfo = getExecuteRuleValidationForVariableOperationRequest(varOperation, bundleCore, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},

		getExecuteRuleValidationRequest : function(variable, operationService, bundleCore, handlers, request){
    		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
    		out.addRequest(loc_convertBaseOperationServiceRequest(node_variableUtility.getVariable(variable), operationService, {
				success : function(request, baseOpInfo){
					var allRuleInfo = [];
					return loc_getCollectRuleInfoRequest(baseOpInfo.rootVariable, baseOpInfo.operationService, allRuleInfo, {
						success : function(request){
							console.log(JSON.stringify(allRuleInfo));
							
							return loc_executeRuleValidationsRequest(allRuleInfo, bundleCore, {
								success : function(request, ruleValidationResults){
									var errorInfos = [];
									var ifSuccess = true;
									_.each(ruleValidationResults, function(ruleValidationResultInfo){
										var ruleValidationResult = ruleValidationResultInfo.validationResult;
										var ruleValidationItem = ruleValidationResultInfo.ruleValidationItem;
										if(!node_ruleUtility.isRuleValidationSuccess(ruleValidationResult)){
											ifSuccess = false;
											errorInfos.push({
												resultData: ruleValidationResult.resultValue,
												ruleName: ruleValidationItem.ruleDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME]
											});
										}
									});
									var out;
									if(ifSuccess){
										out = node_ruleUtility.createRuleValidationSuccessResult();
									}
									else{
										out = node_ruleUtility.createRuleValidationFailResult(errorInfos);
									}
									
        							console.log(JSON.stringify(out));
        							return out;
								}
							});
						}
					});
				}
			}));
    		return out;
		},
		
		executeExecuteRuleValidationRequest : function(variable, operationService, bundleCore, handlers, request){
			var requestInfo = this.getExecuteRuleValidationRequest(variable, operationService, bundleCore, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
	};
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.valueInVarOperationServiceUtility", function(){node_valueInVarOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("common.path.pathUtility", function(){node_pathUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.variable.variableUtility", function(){node_variableUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("variable.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("task.createTaskSetup", function(){node_createTaskSetup = this.getData();});
nosliw.registerSetNodeDataEvent("task.taskExecuteUtility", function(){node_taskExecuteUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.utilityNamedVariable", function(){node_utilityNamedVariable = this.getData();});
nosliw.registerSetNodeDataEvent("rule.ruleUtility", function(){node_ruleUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("ruleExecuteUtility", node_ruleExecuteUtility); 

})(packageObj);
