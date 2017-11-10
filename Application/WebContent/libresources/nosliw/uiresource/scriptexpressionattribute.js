//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_createEventObject;
	var node_getLifecycleInterface
	var node_createUIResourceEmbededScriptExpression;
//*******************************************   Start Node Definition  ************************************** 	

	/*
	 * create expression content object
	 * type: 
	 * 		text, attribute, tagAttribute
	 */
	var node_createEmbededScriptExpressionInAttribute = function(embededScriptExpression, uiResourceView, requestInfo){
		
		//script expression definition
		var loc_embededScriptExpression = node_createUIResourceEmbededScriptExpression(embededScriptExpression, uiResourceView, requestInfo);

		//attribute name
		var loc_attribute = embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_ATTRIBUTE];
		
		//parent resource view
		var loc_uiResourceView = uiResourceView;
		//ui id for content
		var loc_uiId = loc_uiResourceView.prv_getUpdateUIId(embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_UIID]);
		//element
		var loc_ele = loc_uiResourceView.prv_getLocalElementByUIId(loc_uiId);

		var loc_dataEventObject = node_createEventObject();
		
		var loc_scriptExpressionEventHandler = function(eventName, data){
			switch(eventName){
			case node_CONSTANT.REQUESTRESULT_EVENT_SUCCESS:
				loc_ele.attr(loc_attribute, data); 
				break;
			case node_CONSTANT.REQUESTRESULT_EVENT_ERROR:
				loc_ele.attr(loc_attribute, "[Error]"); 
				break;
			case node_CONSTANT.REQUESTRESULT_EVENT_EXCEPTION:
				loc_ele.attr(loc_attribute, "[Exception]"); 
				break;
			}
		};
		
		var lifecycleCallback = {};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(embededScriptExpression, uiResourceView, requestInfo){
			loc_embededScriptExpression.registerListener(loc_dataEventObject, loc_scriptExpressionEventHandler);
			loc_out.refresh(requestInfo);
		};
			
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){		};

		var loc_out = {
				refresh : function(requestInfo){
					loc_embededScriptExpression.refresh(requestInfo);
				}
		};

		//append resource and object life cycle method to out obj
		loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
		node_getLifecycleInterface(loc_out).init(embededScriptExpression, loc_uiResourceView, requestInfo);

		return loc_out;
	};

	//*******************************************   End Node Definition  ************************************** 	

	//populate dependency node data
	nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
	nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
	nosliw.registerSetNodeDataEvent("uiresource.createUIResourceEmbededScriptExpression", function(){node_createUIResourceEmbededScriptExpression = this.getData();});

	//Register Node by Name
	packageObj.createChildNode("createEmbededScriptExpressionInAttribute", node_createEmbededScriptExpressionInAttribute); 

	})(packageObj);
