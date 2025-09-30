//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_createEventObject;
	var node_getLifecycleInterface;
	var node_createVariablesGroup;
	var node_requestServiceProcessor;
	var node_createContextVariableInfo;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoService;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_valueInVarOperationServiceUtility;
	var node_ValueInVarOperation
	var node_createValueInVarOperationRequest
	var node_dataUtility;

//*******************************************   Start Node Definition  ************************************** 	

	/*
	 * script expression unit
	 * type: 
	 * 		text, attribute, tagAttribute
	 */
	var node_createUIResourceScriptExpression = function(varNames, scriptFun, supportFuns, expressions, constants, context, requestInfo){
		
		var loc_constants = {};
		
		var loc_expressions = {};
		
		var loc_contextVarGroup;
		
		var loc_scriptFunction;
		
		var loc_supportFuns;
		
		//store result from last time calculation
		var loc_result;
		
		var loc_dataEventObject = node_createEventObject();
		
		var loc_contextVarsGroupHandler = function(requestInfo){
			loc_out.executeExecuteScriptExpressionRequest({
				success : function(requestInfo, data){
					loc_result = data;
					loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_SUCCESS, data);
				},
				error : function(requestInfo, serviceData){
					loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_ERROR, serviceData);
				},
				exception : function(requestInfo, serviceData){
					loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_EXCEPTION, serviceData);
				}
			}, requestInfo);
		}
		
		var lifecycleCallback = {};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(varNames, scriptFun, supportFuns, expressions, constants, context, requestInfo){
			loc_constants = constants;
			
			loc_expressions = expressions;
			
			loc_scriptFunction = scriptFun; 
			
			loc_supportFuns = supportFuns;

			var contextVariables = [];
			_.each(varNames, function(varName, index){
				contextVariables.push(node_createContextVariableInfo(varName));
			});
			loc_contextVarGroup = node_createVariablesGroup(context, contextVariables, loc_contextVarsGroupHandler, this);
		};
			
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){
			loc_contextVarGroup.destroy();
			loc_scriptFunction = undefined;
			loc_constants = undefined;
			loc_expressions = undefined;
		};

		var loc_out = {
				
			getExecuteScriptExpressionRequest : function(handlers, requester_parent){
				
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteScriptExpression", {}), handlers, requester_parent);
				
				var allVarValuesRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("GetAllVariableValues", {}),
					{
						success : function(request, varsValue){
							var variableParms = {};
							_.each(varsValue.getResults(), function(varData, varName){
								variableParms[varName] = node_dataUtility.getValueOfData(varData);
							});
							return nosliw.runtime.getExpressionService().getExecuteScriptRequest(loc_scriptFunction, loc_supportFuns, loc_expressions, variableParms, loc_constants);
						}
					});
				//parepare variable parms
				var hasEmptyVariable = false;  //whether variable is ready
				var variables = loc_contextVarGroup.getVariables();
				_.each(variables, function(variable, varFullName){
					if(!variable.isEmpty())  hasEmptyVariable = true;
					var getVarValueRequest = node_createValueInVarOperationRequest(undefined, new node_ValueInVarOperation(variable, node_valueInVarOperationServiceUtility.createGetOperationService("")));
					allVarValuesRequest.addRequest(varFullName, getVarValueRequest);
				});
				
				if(!hasEmptyVariable)	out.addRequest(allVarValuesRequest);
				else out.addRequest(node_createServiceRequestInfoSimple(undefined, function(){return undefined;}));

				return out;
			},

			executeExecuteScriptExpressionRequest : function(handlers, requester_parent){
				var requestInfo = this.getExecuteScriptExpressionRequest(handlers, requester_parent);
				node_requestServiceProcessor.processRequest(requestInfo);
			},
			
			registerListener : function(listenerEventObj, handler){
				loc_dataEventObject.registerListener(undefined, listenerEventObj, handler);
			},
			
			refresh : function(requestInfo){
				loc_contextVarGroup.triggerEvent(requestInfo);
			},
			
			getResult : function(){
				return loc_result;
			},

			destroy : function(requestInfo){  node_getLifecycleInterface(loc_out).destroy(requestInfo);  },
		};

		//append resource and object life cycle method to out obj
		loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
		node_getLifecycleInterface(loc_out).init(varNames, scriptFun, supportFuns, expressions, constants, context, requestInfo);
		return loc_out;
	};

	//*******************************************   End Node Definition  ************************************** 	

	//populate dependency node data
	nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
	nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
	nosliw.registerSetNodeDataEvent("variable.createVariablesGroup", function(){node_createVariablesGroup = this.getData();});
	nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
	nosliw.registerSetNodeDataEvent("variable.context.createContextVariableInfo", function(){node_createContextVariableInfo = this.getData();});
	nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
	nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
	nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
	nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
	nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
	nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.valueInVarOperationServiceUtility", function(){node_valueInVarOperationServiceUtility = this.getData();});
	nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.ValueInVarOperation", function(){node_ValueInVarOperation = this.getData();});
	nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.createValueInVarOperationRequest", function(){node_createValueInVarOperationRequest = this.getData();});
	nosliw.registerSetNodeDataEvent("variable.valueinvar.utility", function(){node_dataUtility = this.getData();});

	//Register Node by Name
	packageObj.createChildNode("createUIResourceScriptExpression", node_createUIResourceScriptExpression); 

	})(packageObj);
