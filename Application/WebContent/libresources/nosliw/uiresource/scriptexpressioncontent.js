//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_createUIResourceScriptExpression;
	var node_createEventObject;
	var node_getLifecycleInterface
//*******************************************   Start Node Definition  ************************************** 	

	/*
	 * create expression content object
	 * type: 
	 * 		text, attribute, tagAttribute
	 */
	var node_createEmbededScriptExpressionInContent = function(embededScriptExpression, uiResourceView, requestInfo){
		
		//script expression definition
		var loc_scriptExpression = node_createUIResourceScriptExpression(embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_SCRIPTEXPRESSION], uiResourceView, requestInfo);
		
		//parent resource view
		var loc_uiResourceView = uiResourceView;
		//ui id for content
		var loc_uiId = loc_uiResourceView.prv_getUpdateUIId(embededScriptExpression[node_COMMONATRIBUTECONSTANT.UIEXPRESSIONCONTENT_UIID]);
		//element
		var loc_ele = loc_uiResourceView.prv_getLocalElementByUIId(loc_uiId);

		var loc_dataEventObject = node_createEventObject();
		
		var loc_scriptExpressionEventHandler = function(eventName, data){
			switch(eventName){
			case node_CONSTANT.REQUESTRESULT_EVENT_SUCCESS:
				loc_ele.text(data);
				break;
			case node_CONSTANT.REQUESTRESULT_EVENT_ERROR:
				loc_ele.text("[Error]");
				break;
			case node_CONSTANT.REQUESTRESULT_EVENT_EXCEPTION:
				loc_ele.text("[Exception]");
				break;
			}
		};
		
		var lifecycleCallback = {};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(embededScriptExpression, uiResourceView, requestInfo){
			
			loc_scriptExpression.registerListener(loc_dataEventObject, loc_scriptExpressionEventHandler);
			
		};
			
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){
		};

		var loc_out = {
			ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},
			
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
	nosliw.registerSetNodeDataEvent("uiresource.createUIResourceScriptExpression", function(){node_createUIResourceScriptExpression = this.getData();});
	nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});

	//Register Node by Name
	packageObj.createChildNode("createEmbededScriptExpressionInContent", node_createEmbededScriptExpressionInContent); 

	})(packageObj);
