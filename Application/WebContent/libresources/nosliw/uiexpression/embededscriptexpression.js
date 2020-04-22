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
	var node_createUIResourceScriptExpression;
//*******************************************   Start Node Definition  ************************************** 	

	/*
	 * put ui script expression together
	 * type: 
	 * 		text, attribute, tagAttribute
	 */
	var node_createUIResourceEmbededScriptExpression = function(id, scriptGroup, constants, context, requestInfo){
		
		var loc_script;
		var loc_expressions = {};
		var loc_scriptExprssionObj;
		
		var loc_scriptExpressions = {};
		
		var loc_scriptFunction;
		
		var loc_dataEventObject = node_createEventObject();
		
		var lifecycleCallback = {};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(id, scriptGroup, constants, context, requestInfo){
			loc_script = scriptGroup[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPTGROUP_ELEMENT][id];
			
			_.each(loc_script[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPTENTITY_EXPRESSIONREF], function(expressionId, i){
				loc_expressions[expressionId] = scriptGroup[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPTGROUP_EXPRESSIONGROUP][node_COMMONATRIBUTECONSTANT.EXPRESSIONGROUP_EXPRESSIONS][expressionId]; 
			});
			
			var varNames = loc_script[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPTENTITY_VARIABLESINFO];
			var scriptFun = loc_script[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPTENTITY_SCRIPTFUNCTION];
			var supportFuns = loc_script[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPTENTITY_SUPPORTFUNCTION];
			loc_scriptExprssionObj = node_createUIResourceScriptExpression(varNames, scriptFun, supportFuns, loc_expressions, constants, context, requestInfo);
			loc_scriptExprssionObj.registerListener(loc_dataEventObject, function(eventName, data){
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
		};
		
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){	
			loc_scriptExprssionObj.destry();
			loc_dataEventObject.clearup();
			loc_dataEventObject = undefined;
		};

		var loc_calculateResult = function(){
			return loc_scriptExprssionObj.getResult();
		};
		
		
		var loc_out = {

			registerListener : function(listenerEventObj, handler){
				loc_dataEventObject.registerListener(undefined, listenerEventObj, handler);
			},
			
			refresh : function(requestInfo){
				loc_scriptExprssionObj.refresh(requestInfo);
			},
			
			destroy : function(requestInfo){  node_getLifecycleInterface(loc_out).destroy(requestInfo);  },
		};

		//append resource and object life cycle method to out obj
		loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
		node_getLifecycleInterface(loc_out).init(id, scriptGroup, constants, context, requestInfo);
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
	nosliw.registerSetNodeDataEvent("uiexpression.createUIResourceScriptExpression", function(){node_createUIResourceScriptExpression = this.getData();});

	//Register Node by Name
	packageObj.createChildNode("createUIResourceEmbededScriptExpression", node_createUIResourceEmbededScriptExpression); 

	})(packageObj);
