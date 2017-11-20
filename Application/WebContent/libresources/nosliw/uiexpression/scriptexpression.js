//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_createEventObject;
	var node_getLifecycleInterface;
	var node_createContextVariablesGroup;
	var node_requestServiceProcessor;
	var node_createContextVariable;
//*******************************************   Start Node Definition  ************************************** 	

	/*
	 * create expression content object
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
				contextVariables.push(node_createContextVariable(varName));
			});
			loc_contextVarGroup = node_createContextVariablesGroup(context, contextVariables, loc_contextVarsGroupHandler, this);
		};
			
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){
		};

		var loc_out = {
			getExecuteScriptExpressionRequest : function(handlers, requester_parent){
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
				node_requestServiceProcessor.processRequest(requestInfo, true);
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
	nosliw.registerSetNodeDataEvent("uidata.context.createContextVariablesGroup", function(){node_createContextVariablesGroup = this.getData();});
	nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
	nosliw.registerSetNodeDataEvent("uidata.context.createContextVariable", function(){node_createContextVariable = this.getData();});
	

	//Register Node by Name
	packageObj.createChildNode("createUIResourceScriptExpression", node_createUIResourceScriptExpression); 

	})(packageObj);
