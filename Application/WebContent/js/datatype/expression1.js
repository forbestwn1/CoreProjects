/**
 * 	expression:   expression object
 * 	contextVariables : all possible context variables used in expression 
 * 	context : context object
 */
var nosliwCreateExpression = function(expression, contextVariablesArray, context){
	
	//sync task name for remote call 
	var loc_moduleName = "expression";
	
	//default requester 
	var loc_requester = new NosliwRequester(NOSLIWCONSTANT.REQUESTER_TYPE_SERVICE, loc_moduleName); 
	
	//original expression
	 var loc_expression = expression[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_EXPRESSION_EXPRESSION];
	 //operand object
	 var loc_operand = expression[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_EXPRESSION_OPERAND];
	 //variable information map: variable data type info
	 var loc_variablesInfo = expression[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_EXPRESSION_VARIABLESINFO];
	 //if this script is runnable
	 var loc_isScriptRunnable = expression[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_EXPRESSION_SCRIPTRUNNALBE];
	 
	 //constant data in expression
	 var loc_constants = {};
	 _.each(expression[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_EXPRESSION_CONSTANTS], function(constant, name, list){
		 loc_constants[name] = nosliwCreateData(constant.value, constant.dataTypeInfo);
	 }, this);
		 
	 //all data type related with this expression
	 var loc_allRelatedDataTypeInfos = expression[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_EXPRESSION_ALLDATATYPEINFOS];
	 
	 //store all related data types
	 var loc_allRelatedDataTypes = {};

	 //store all variables
	 //data of this map can be : data, wrapper, variable
	 var loc_contextVariablesGroup = undefined;

	 //expression result
	 var loc_result = undefined;
	 //whether the result is valid
	 var loc_isResultValid = false;
	 
	 //event source
	 var loc_eventSource = {};
	 var loc_listeners = [];
	 /*
	  * get variable data by varName
	  * try to get from variables first
	  * if no, then get from constants
	  */
	var loc_getVariableData = function(varName){
		var out = undefined;
		var variable = loc_contextVariablesGroup.getVariable(varName);
		if(variable!=undefined){
			out = nosliwObjectEntityUtility.getObjectEntityData(variable);
		}
		
		if(out==undefined){
			variable = loc_constants[varName];
			if(variable!=undefined){
				out = nosliwObjectEntityUtility.getObjectEntityData(variable);
			}
		}
		return out;
	}; 
	 
	/*
	 * get requester according to req
	 * 		undefined : use default one
	 */
	var loc_getRequesterParent = function(req){
		if(req==undefined)   return loc_requester;
		else return req;
	};
		
	var loc_getRequestServiceExecuteExpression = function(){		return new NosliwServiceInfo("executeExpression", {});		};
	var loc_getRequestServiceExecuteOperand = function(operand){		return new NosliwServiceInfo("executeOperand", {"operand":operand});		};
	 
	
	var loc_getRequestInfoExecuteOperand = function(operand, handlers, requester_parent){
		var service = loc_getRequestServiceExecuteOperand(operand);
		
		var executeOperandReqInfo = undefined;
		
		var operandType = operand[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_OPERAND_TYPE];
		if(operandType==NOSLIWCOMMONCONSTANT.CONS_EXPRESSION_OPERAND_CONSTANT){
			var object = operand[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_OPERAND_CONSTANT_DATA];
			var out = nosliwObjectEntityUtility.getObjectEntityData();
			executeOperandReqInfo = nosliwCreateServiceRequestInfoService(service, handlers, requester_parent);
			executeOperandReqInfo.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(function(requestInfo){
				requestInfo.executeSuccessHandler(out, this);
			}, this));
		}
		else if(operandType==NOSLIWCOMMONCONSTANT.CONS_EXPRESSION_OPERAND_VARIABLE){
			var varName = operand[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_OPERAND_VARIABLE_VARNAME];
			var out = loc_getVariableData(varName);
			//if cannot find, then use empty data
			if(out==undefined)  out = nosliwDataUtility.createEmptyData();
			executeOperandReqInfo = nosliwCreateServiceRequestInfoService(service, handlers, requester_parent);
			executeOperandReqInfo.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(function(requestInfo){
				requestInfo.executeSuccessHandler(out, this);
			}, this));
		}
		else if(operandType==NOSLIWCOMMONCONSTANT.CONS_EXPRESSION_OPERAND_DATAOPERATION ||
				operandType==NOSLIWCOMMONCONSTANT.CONS_EXPRESSION_OPERAND_DATATYPEOPERATION ||
				operandType==NOSLIWCOMMONCONSTANT.CONS_EXPRESSION_OPERAND_NEWOPERATION){
			var operation = operand[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_OPERAND_OPERATION_OPERATION];
			var baseOperand = operand[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_OPERAND_DATAOPERATION_BASEDATA];
			var baseDataTypeInfo = operand[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_OPERAND_OPERATION_BASEDATATYPEINFO];

			executeOperandReqInfo = nosliwCreateRequestSequence(service, handlers, requester_parent);
			
			var parmsRequest = nosliwCreateRequestSet(service, {
				success : function(requestInfo, results){
					var parms = [];
					var baseData = results.getResult("baseData");
					if(baseData!=undefined){ 
						baseDataTypeInfo = baseData.dataTypeInfo;
						parms.push(baseData);
					}
					
					for(i=0; i<10; i++){
						var parmData = results.getResult("parm"+i);
						if(parmData!=undefined){
							parms.push(parmData);
						}
					}
					requestInfo.getParentRequest().setData("parmsData", parms);
					return nosliwCommonUtility.createEmptyValue();
				},
			});
			if(baseOperand!=undefined){
				var baseDataRequest = loc_getRequestInfoExecuteOperand(baseOperand, {
					success : function(requestInfo, data){
						if(nosliwDataUtility.isEmptyData(data)==false){
							baseDataTypeInfo = data.dataTypeInfo;
						}
					}
				});
				parmsRequest.addRequest("baseData", baseDataRequest);
			}

			var parmsOperand = operand[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_OPERAND_OPERATION_PARAMETERS];
			for(var i=0; i<parmsOperand.length; i++){
				var parmRequest = loc_getRequestInfoExecuteOperand(parmsOperand[i], {});
				parmsRequest.addRequest("parm"+i, parmRequest);
			}

			executeOperandReqInfo.addRequest(parmsRequest);

			executeOperandReqInfo.addRequest(function(requestInfo){
				var parms = requestInfo.getData("parmsData");
				if(baseDataTypeInfo==undefined){
					return nosliwDataUtility.createEmptyData(); 
				}
					
				var dataOperationRequest = nosliw.getDataTypeManager().getRequestInfoExecuteDataOperation(baseDataTypeInfo, operation, parms, {
					success : function(requestInfo, data){
						return data;
					}
				});
				return dataOperationRequest;
			});
		}
		else if(operandType==NOSLIWCOMMONCONSTANT.CONS_EXPRESSION_OPERAND_PATHOPERATION){
			var path = operand[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_OPERAND_PATH_PATH];
			var baseOperand = operand[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_OPERAND_PATH_BASEDATA];
			var baseOperandType = baseOperand[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_OPERAND_TYPE];
			if(baseOperandType==NOSLIWCOMMONCONSTANT.CONS_EXPRESSION_OPERAND_VARIABLE){
				//can convert to context variable
				var contextVarName = nosliwCreateContextVariable(baseOperand[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_OPERAND_VARIABLE_VARNAME], path).key;
				var out = loc_getVariableData(contextVarName);
				//if cannot find, then use empty data
				if(out==undefined)  out = nosliwDataUtility.createEmptyData();
				executeOperandReqInfo = nosliwCreateServiceRequestInfoService(service, handlers, requester_parent);
				executeOperandReqInfo.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(function(requestInfo){
					requestInfo.executeSuccessHandler(out, this);
				}, this));
			}
			else{
				//do path operation
				
			}
		}
		
		return executeOperandReqInfo;
	};
	
	var loc_invalidResult = function(){
		 loc_result = undefined;
		 loc_isResultValid = false;
	};
	
	var loc_setResult = function(result){
		 loc_result = result;
		 loc_isResultValid = true;
	};
	
	var loc_triggerEvent = function(event, data, requestInfo){
		return nosliwRequestUtility.triggerEventWithRequest(loc_eventSource, event, data, requestInfo);
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT"] = function(expression, contextVariablesArray, context){
		 var a = [];
		 _.each(contextVariablesArray, function(contextVariable, key, list){
			 if(context.getContextElement(contextVariable.name)!=null){
				 //valid contextVariable
				 a.push(contextVariable);
			 }
		 }, this);
		 
		 loc_contextVariablesGroup = nosliwCreateContextVariablesGroup(context, a, function(requestInfo){
			 loc_invalidResult();
			 loc_out.requestExecuteExpression({
				 success : function(requestInfo, data){
					 loc_setResult(data);
					 loc_triggerEvent(NOSLIWCONSTANT.EXPRESSION_EVENT_DONE, data, requestInfo);
				 },
			 }, requestInfo);
		 }, this);

		nosliw.getDataTypeManager().requestGetDataTypes(loc_allRelatedDataTypeInfos, 
		{
			success : function(requestInfo, dataTypeArrays){
				loc_out.finishInit();
			},
		});
		
		return false;
	};
	
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE"] = function(){};
	
	
	var loc_out = {
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},

		setVariableContext : function(context){
			_.each(context, function(variable, name, list){
				this.addVariable(name, variable);
			}, this);
		},
		
		addVariable : function(name, variable){		loc_variables[name] = variable;		},

		getRequestInfoExecuteExpression : function(handlers, requester_parent){
			var executeOperandRequest = undefined;
			if(loc_isResultValid==true){
				//result already been execute, do not need to execute again
				executeOperandRequest = nosliwCreateServiceRequestInfoService(loc_getRequestServiceExecuteOperand(), handlers, loc_getRequesterParent(requester_parent));
				executeOperandRequest.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(function(requestInfo){
					requestInfo.executeSuccessHandler(loc_result, this);
				}, this));
			}
			else{
				//execute 
				executeOperandRequest = loc_getRequestInfoExecuteOperand(loc_operand, handlers, requester_parent);
			}
			return executeOperandRequest;
		},	

		requestExecuteExpression : function(handlers, requester_parent){
			var requestInfo = this.getRequestInfoExecuteExpression(handlers, requester_parent);
			return nosliw.getRequestServiceManager().processRequest(requestInfo, true); 
		},
		
		getResult : function(){
			var out = undefined;
			if(loc_isResultValid==true)  out = loc_result;
			return out;
		},
		
		/*
		 * register handler for execute event
		 */
		registerEvent : function(handler){
			var listener = nosliwRequestUtility.registerEventWithRequest(loc_eventSource, NOSLIWCONSTANT.EVENT_EVENTNAME_ALL, handler, this);
			loc_listeners.push(listener);
			return listener;
		},
	};
	
	//append resource life cycle method to out obj
	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out, loc_moduleName);
	loc_out.init(expression, contextVariablesArray, context);
	
	return loc_out;
};
