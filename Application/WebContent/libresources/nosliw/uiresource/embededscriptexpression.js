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
	 * create expression content object
	 * type: 
	 * 		text, attribute, tagAttribute
	 */
	var node_createUIResourceEmbededScriptExpression = function(embededScriptExpression, uiResourceView, requestInfo){
		
		var loc_scriptExpressions = {};
		
		var loc_scriptFunction;
		
		var loc_dataEventObject = node_createEventObject();
		
		var lifecycleCallback = {};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(embededScriptExpression, uiResourceView, requestInfo){

			loc_scriptFunction = embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_SCRIPTFUNCTION];
			
			_.each(embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_SCRIPTEXPRESSIONS], function(scriptExpression, id){
				var scriptExprssionObj = node_createUIResourceScriptExpression(scriptExpression, uiResourceView, requestInfo);
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
		
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){	};

		var loc_calculateResult = function(){
			var scriptExpressionResults = {};
			_.each(loc_scriptExpressions, function(scriptExpression, id){
				scriptExpressionResults[id] = scriptExpression.getResult();
			});
		
			return loc_scriptFunction.call(loc_out, scriptExpressionResults);
		};
		
		
		var loc_out = {

			registerListener : function(listenerEventObj, handler){
				loc_dataEventObject.registerListener(undefined, listenerEventObj, handler);
			},
			
			refresh : function(requestInfo){
				_.each(loc_scriptExpressions, function(scriptExpression, id){
					scriptExpression.refresh(requestInfo);
				});
			}
		};

		//append resource and object life cycle method to out obj
		loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
		node_getLifecycleInterface(loc_out).init(embededScriptExpression, uiResourceView, requestInfo);
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
	nosliw.registerSetNodeDataEvent("uiresource.createUIResourceScriptExpression", function(){node_createUIResourceScriptExpression = this.getData();});

	//Register Node by Name
	packageObj.createChildNode("createUIResourceEmbededScriptExpression", node_createUIResourceEmbededScriptExpression); 

	})(packageObj);
