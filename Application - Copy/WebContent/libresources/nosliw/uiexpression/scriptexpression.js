//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_createEventObject;
	var node_getLifecycleInterface;
	var node_createContextVariableInfosGroup;
	var node_requestServiceProcessor;
	var node_createContextVariableInfo;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoService;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_uiDataOperationServiceUtility;
	var node_UIDataOperation
	var node_createUIDataOperationRequest
	var node_dataUtility;

//*******************************************   Start Node Definition  ************************************** 	

	/*
	 * script expression unit
	 * type: 
	 * 		text, attribute, tagAttribute
	 */
	var node_createUIResourceScriptExpression = function(scriptExpression, constants, context, requestInfo){
		
		var loc_constants = {};
		
		var loc_expressions = {};
		
		var loc_contextVarGroup;
		
		var loc_scriptFunction;
		
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
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(scriptExpression, constants, context, requestInfo){
			loc_constants = constants;
			
			loc_expressions = scriptExpression[node_COMMONATRIBUTECONSTANT.SCRIPTEXPRESSION_EXPRESSIONS];
			
			loc_scriptFunction = scriptExpression[node_COMMONATRIBUTECONSTANT.SCRIPTEXPRESSION_SCRIPTFUNCTION];

			var varNames = scriptExpression[node_COMMONATRIBUTECONSTANT.SCRIPTEXPRESSION_VARIABLENAMES];
			var contextVariables = [];
			_.each(varNames, function(varName, index){
				contextVariables.push(node_createContextVariableInfo(varName));
			});
			loc_contextVarGroup = node_createContextVariableInfosGroup(context, contextVariables, loc_contextVarsGroupHandler, this);
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
							return nosliw.runtime.getExpressionService().getExecuteScriptRequest(loc_scriptFunction, loc_expressions, variableParms, loc_constants);
						}
					});
				//parepare variable parms
				var variables = loc_contextVarGroup.getVariables();
				_.each(variables, function(variable, index){
					var getVarValueRequest = node_createUIDataOperationRequest(undefined, new node_UIDataOperation(variable, node_uiDataOperationServiceUtility.createGetOperationService("")));
					allVarValuesRequest.addRequest(variable.contextPath, getVarValueRequest);
				});
				
				out.addRequest(allVarValuesRequest);

				return out;
			},

			getExecuteScriptExpressionRequest1 : function(handlers, requester_parent){
				//parepare variable parms
				var variables = loc_contextVarGroup.getVariables();
				var variableParms = {};
				_.each(variables, function(variable, index){
					var varName = variable.contextPath;
					var varValue = variable.getData().value;
					variableParms[varName] = varValue;
				});

				var out = nosliw.runtime.getExpressionService().getExecuteScriptRequest(loc_scriptFunction, loc_expressions, variableParms, loc_constants, handlers, requester_parent)
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
		node_getLifecycleInterface(loc_out).init(scriptExpression, constants, context, requestInfo);
		return loc_out;
	};

	//*******************************************   End Node Definition  ************************************** 	

	//populate dependency node data
	nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
	nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
	nosliw.registerSetNodeDataEvent("uidata.context.createContextVariablesGroup", function(){node_createContextVariableInfosGroup = this.getData();});
	nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
	nosliw.registerSetNodeDataEvent("uidata.context.createContextVariableInfo", function(){node_createContextVariableInfo = this.getData();});
	nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
	nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
	nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
	nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
	nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
	nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
	nosliw.registerSetNodeDataEvent("uidata.uidataoperation.UIDataOperation", function(){node_UIDataOperation = this.getData();});
	nosliw.registerSetNodeDataEvent("uidata.uidataoperation.createUIDataOperationRequest", function(){node_createUIDataOperationRequest = this.getData();});
	nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});

	//Register Node by Name
	packageObj.createChildNode("createUIResourceScriptExpression", node_createUIResourceScriptExpression); 

	})(packageObj);
