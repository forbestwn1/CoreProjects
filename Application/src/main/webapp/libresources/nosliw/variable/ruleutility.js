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

//*******************************************   Start Node Definition  **************************************

var node_createRuleItem = function(ruleDef, data){
	return {
		ruleDef : ruleDef,
		data : data
	};
};
 	
var node_utility = function(){

    var loc_getRuleDefinitionsFromVariable = function(variable){
		var definition = variable.prv_info.definition;
		if(definition!=undefined){
			var dataDefinition;
			
			var defType = definition[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_TYPE];
			if(defType==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_DATA){
				dataDefinition = definition[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DATA];
			}
			else if(defType==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE){
                definition[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DEFINITION][node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DATA];
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
				var rulePath = dataRuleDef[node_COMMONATRIBUTECONSTANT_DEFINITIONDATARULE_PATH];
					
				var opPath = opService.parms.path;
				var opValue = opService.parms.value;
					
				var ruleValue;
				var comparePath = node_dataUtility.comparePath(rulePath, opPath);
                if(comparePath.compare==0){
     				ruleValue = opValue;
     				allRuleInfo.push(node_createRuleItem(dataRuleDef, ruleValue));
   				}
                else if(comparePath.compare==1){
					//get child value
					var dataTypeInfo = node_dataUtility.getDataTypeInfoFromValue(opValue);
					out.addRequest(node_wrapperFactory.getDataTypeHelper(dataTypeInfo).getChildValueRequest(opValue, comparePath.subPath, {
						success : function(request, childData){
            				allRuleInfo.push(node_createRuleItem(dataRuleDef, childData));
						}
					}));
				}
                else if(comparePath.compare==2){
					//
					 out.addRequest(variable.getGetValueRequest({
						success: function(request, data){
							var ruleValueBeforeChange = node_basicUtility.cloneObject(data);
        					var dataTypeInfo = node_dataUtility.getDataTypeInfoFromValue(ruleValueBeforeChange);

        					return node_wrapperFactory.getDataTypeHelper(dataTypeInfo).getChildValueRequest(ruleValueBeforeChange, rulePath, {
		    				    success : function(request, childData){
         					        opService.parms.path = comparePath.subPath;
		                            return getDataOperationRequest(childData, dataOperationService, {
										success : function(reuqest, ruleData){
                        				    allRuleInfo.push(node_createRuleItem(dataRuleDef, ruleData));
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
    			 var compareWithChildPath = node_dataUtility.comparePath(opService.parms.path, childVarInfo.path);
				 if(compareWithChildPath.compare==2){
					 
					 out.addRequest(variable.getGetValueRequest({
						success: function(request, data){
							var ruleValueBeforeChange = node_basicUtility.cloneObject(data);
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
			            				        }, requestInfo);
											}
											else{
												opService.parms.vlaue = childVariableData;
    											return loc_getCollectRuleInfoRequest(childVarInfo.variable, opService, allRuleInfo);
											}
										}
									});
				    		    }
					        });
						}
					}));
				 }
			});
		}
		
	};
	
	var loc_convertBaseOperationServiceRequest = function(variable, operationService, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		var opService = operationService.clone();

		var command = operationService.command;
		var operationData = operationService.parms;
		if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
			if(variable.prv_isBase==true){
    			//if set to base, then just set directly
	    		out = node_createServiceRequestInfoSimple(undefined, function(){
					return {
						rootVariable : variable,
						operationService : opService
					};
			    }, handlers, request);
			}
			else{
				var parentVariable = variable.prv_getRelativeVariableInfo().parent;
				opService.parms.path = node_dataUtility.combinePath(parentVariable.path, opService.parms.path);
				opService.parms.value = value;
                if(parentVariable.prv_valueAdapter!=undefined){
					out = node_createServiceRequestInfoSequence({}, handlers, request);
					//apply adapter to value
					out.addRequest(valueWrapper.prv_valueAdapter.getOutValueRequest(operationData.value, {
						success: function(request, value){
							opService.parms.value = value;
							return loc_convertBaseOperationServiceRequest(parentVariable, opService);
						}
					}));
					return out;
				}
				else{
					out = loc_convertBaseOperationServiceRequest(parentVariable, opService, handlers, request);
				}
			}
		}
		return out;
	};
	
	
	var loc_out = {
		
		getExecuteRuleValidationRequest : function(variable, operationService, handlers, request){
    		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
    		out.addRequest(loc_convertBaseOperationServiceRequest(variable, operationService, {
				success : function(request, baseOpInfo){
					var allRuleInfo = [];
					loc_getCollectRuleInfoRequest(baseOpInfo.rootVariable, baseOpInfo.operationService, allRuleInfo, {
						success : function(request){
							
						}
					});
				}
			}));
    		return out;
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

//Register Node by Name
packageObj.createChildNode("variableRuleUtility", node_utility); 

})(packageObj);
