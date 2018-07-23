//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_createEventObject;
	var node_getLifecycleInterface;
	var node_createUIResourceScriptExpression;
//*******************************************   Start Node Definition  ************************************** 	

	/*
	 * put ui script expression together
	 * type: 
	 * 		text, attribute, tagAttribute
	 */
	var node_createUIResourceEmbededScriptExpression = function(embededScriptExpression, constants, context, requestInfo){
		
		var loc_scriptExpressions = {};
		
		var loc_scriptFunction;
		
		var loc_dataEventObject = node_createEventObject();
		
		var lifecycleCallback = {};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(embededScriptExpression, constants, context, requestInfo){

			loc_scriptFunction = embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_SCRIPTFUNCTION];
			
			_.each(embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_SCRIPTEXPRESSIONS], function(scriptExpression, id){
				var scriptFun = embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_SCRIPTEXPRESSIONSCRIPTFUNCTION][id];
				var scriptExprssionObj = node_createUIResourceScriptExpression(scriptExpression, scriptFun, constants, context, requestInfo);
				loc_scriptExpressions[id] = scriptExprssionObj;
				scriptExprssionObj.registerListener(loc_dataEventObject, function(eventName, data){
					switch(eventName){
					case node_CONSTANT.REQUESTRESULT_EVENT_SUCCESS:
						var result = loc_calculateResult();
						loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_SUCCESS, result);
						break;
					case node_CONSTANT.REQUESTRESULT_EVENT_ERROR:
						loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_ERROR, data);
						break;
					case node_CONSTANT.REQUESTRESULT_EVENT_EXCEPTION:
						loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_EXCEPTION, data);
						break;
					}
				});
				
			});
		};
		
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){	
			_.each(loc_scriptExpressions, function(scriptExpression, id){
				scriptExpression.destroy();
			});
			loc_dataEventObject.clearup();
			loc_dataEventObject = undefined;
		};

		var loc_calculateResult = function(){
			var scriptExpressionResults = {};
			_.each(loc_scriptExpressions, function(scriptExpression, id){
				scriptExpressionResults[id] = scriptExpression.getResult();
			});
		
			return loc_scriptFunction.call(loc_out, scriptExpressionResults);
		};
		
		
		var loc_out = {

			getExecuteEmbededScriptExpressionRequest : function(handlers, requester_parent){
				var requestInfo = loc_out.getRequestInfo(requester_parent);

				//calculate multiple script expression
				var executeMutipleScriptExpressionRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("ExecuteMutipleEmbededScriptExpression", {"scriptExpressions":loc_scriptExpressions}), {});
				_.each(loc_scriptExpressions, function(scriptExpression, id){
					var scriptExpressionRequest = scriptExpression.getExecuteScriptExpressionRequest(undefined, requestInfo);
					executeEmbededScriptExpressionRequest.addRequest(id, scriptExpressionRequest);
				});

				var executeEmbededScriptExpressionRequest = node_createServiceRequestInfoService(new node_ServiceInfo("ExecuteEmbedScriptExpression", {"script":script, "expressions":expressions, "variables":variables}), handlers, requestInfo);
				var requestDependency = new node_DependentServiceRequestInfo(executeMutipleScriptExpressionRequest, {
					success : function(requestInfo, scriptExpressionsResult){
						var scriptExpressionsData = scriptExpressionsResult.getResults();
						return loc_scriptFunction.call(loc_out, scriptExpressionsData);
					}
				});
				executeEmbededScriptExpressionRequest.setDependentService(requestDependency);
				return executeEmbededScriptExpressionRequest;
			},
				
			registerListener : function(listenerEventObj, handler){
				loc_dataEventObject.registerListener(undefined, listenerEventObj, handler);
			},
			
			refresh : function(requestInfo){
				_.each(loc_scriptExpressions, function(scriptExpression, id){
					scriptExpression.refresh(requestInfo);
				});
			},
			
			destroy : function(requestInfo){  node_getLifecycleInterface(loc_out).destroy(requestInfo);  },
		};

		//append resource and object life cycle method to out obj
		loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
		node_getLifecycleInterface(loc_out).init(embededScriptExpression, constants, context, requestInfo);
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
	nosliw.registerSetNodeDataEvent("uiexpression.createUIResourceScriptExpression", function(){node_createUIResourceScriptExpression = this.getData();});

	//Register Node by Name
	packageObj.createChildNode("createUIResourceEmbededScriptExpression", node_createUIResourceEmbededScriptExpression); 

	})(packageObj);
