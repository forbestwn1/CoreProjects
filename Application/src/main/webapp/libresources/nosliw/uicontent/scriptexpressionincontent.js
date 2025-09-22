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
	var node_createTaskInContainerWatch;
    var node_createExpressionTaskWatch;
    var node_getItemValueInContainer;
//*******************************************   Start Node Definition  ************************************** 	

	/*
	 * create expression content object
	 * type: 
	 * 		text, attribute, tagAttribute
	 */
	var node_createEmbededScriptExpressionInContent = function(embededScriptExpression){
		
		var loc_embededScriptExpression = embededScriptExpression;

		var loc_ele;

		var loc_embededSScriptExpressionWatch;

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
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(viewElement, scriptExpressionGroup, valuePortEnv, runtimeEnv, requestInfo){
			loc_ele = viewElement;

            loc_embededSScriptExpressionWatch = node_createExpressionTaskWatch(node_getItemValueInContainer(scriptExpressionGroup, loc_embededScriptExpression[node_COMMONATRIBUTECONSTANT.UIEMBEDEDSCRIPTEXPRESSION_SCRIPTID]), valuePortEnv)
			loc_embededSScriptExpressionWatch.registerListener(loc_dataEventObject, loc_scriptExpressionEventHandler);

			loc_out.refresh(requestInfo);
		};
			
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){
			loc_dataEventObject.clearup();
			loc_uiResourceView = undefined;
			loc_ele = undefined;
		};

		var loc_out = {
			
			getUIId : function(){
				return loc_embededScriptExpression[node_COMMONATRIBUTECONSTANT.WITHUIID_UIID];
			},
			
			refresh : function(requestInfo){
				loc_embededSScriptExpressionWatch.refresh(requestInfo);
			},
			
			destroy : function(requestInfo){  node_getLifecycleInterface(loc_out).destroy(requestInfo);  },
		};

		//append resource and object life cycle method to out obj
		loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);

		return loc_out;
	};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("task.createTaskGroupItemWatch", function(){node_createTaskGroupItemWatch = this.getData();});
nosliw.registerSetNodeDataEvent("expression.createExpressionTaskWatch", function(){node_createExpressionTaskWatch = this.getData();});
nosliw.registerSetNodeDataEvent("common.valuecontainer.getItemValueInContainer", function(){node_getItemValueInContainer = this.getData();});


//Register Node by Name
packageObj.createChildNode("createEmbededScriptExpressionInContent", node_createEmbededScriptExpressionInContent); 

})(packageObj);
