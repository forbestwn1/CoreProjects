//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
//*******************************************   Start Node Definition  ************************************** 	

	/*
	 * create expression content object
	 * type: 
	 * 		text, attribute, tagAttribute
	 */
	var node_createUIResourceScriptExpression = function(scriptExpression, uiResourceView, requestInfo){
		
		var loc_constants = {};
		
		var loc_expressions = {};
		
		var loc_contextVarGroup;
		
		var loc_scriptFunction;
		
		var loc_dataEventObject = node_createEventObject();
		
		var loc_contextVarsGroupHandler = function(requestInfo){
			loc_out.getExecuteScriptExpressionRequest({
				success : function(requestInfo, data){
					loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_SUCCESS, data);
				},
				error : function(requestInfo, serviceData){
					loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_ERROR, serviceData);
				},
				exception : function(requestInfo, serviceData){
					loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_EXCEPTION, serviceData);
				}
			}, requesterInfo);
		}
		
		var lifecycleCallback = {};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(scriptExpression, uiResourceView, requestInfo){
			loc_constants = uiResourceView.getConstants;
			
			loc_expressions = scriptExpression[node_COMMONATRIBUTECONSTANT.SCRIPTEXPRESSION_EXPRESSIONS];
			
			loc_scriptFunction = scriptExpression[node_COMMONATRIBUTECONSTANT.SCRIPTEXPRESSION_SCRIPTFUNCTION];

			var varNames = scriptExpression[node_COMMONATRIBUTECONSTANT.SCRIPTEXPRESSION_VARIABLENAMES];
			var contextVariables = [];
			_.each(varNames, function(varName, index){
				contextVariables.push(node_createContextVariable(varName));
			});
			loc_contextVarGroup(uiResourceView.getContext(), contextVariables, loc_contextVarsGroupHandler, this);
		};
			
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){
		};

		var loc_out = {
			ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},
			
			getExecuteScriptExpressionRequest : function(handlers, requester_parent){
				//parepare variable parms
				var variables = loc_contextVarGroup.getVariables();
				var variableParms = {};
				_.each(variables, function(variable, index){
					var varName = variable.contextPath;
					var varValue = variable.getData().value;
					variableParms[varName] = varValue;
				});

				var out = node_createExpressionService.getExecuteScriptExpressionRequest(loc_scriptFunction, loc_expressions, variableParms, loc_constants, handlers, requester_parent)
				return out;
			},
			
			registerListener : function(listenerEventObj, handler){
				loc_dataEventObject.registerListener(undefined, listenerEventObj, handler);
			}
			
		};

		//append resource and object life cycle method to out obj
		loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
		loc_out.init(expressionContent, type, uiResourceView, requestInfo);
		return loc_out;
	};

	//*******************************************   End Node Definition  ************************************** 	

	//populate dependency node data
	nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});


	//Register Node by Name
	packageObj.createChildNode("createUIResourceScriptExpression", node_createUIResourceScriptExpression); 

	})(packageObj);
