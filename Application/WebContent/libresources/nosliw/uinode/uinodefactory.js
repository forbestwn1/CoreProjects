//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_createContext;
	var node_createContextElementInfo;
	var node_dataUtility;
	var node_uiResourceUtility;
	var node_createEmbededScriptExpressionInContent;
	var node_createEmbededScriptExpressionInAttribute;
	var node_createEmbededScriptExpressionInTagAttribute;
	var node_getLifecycleInterface;
	var node_basicUtility;
	var node_createUITagRequest;
	var node_createEventObject;
	var node_createUIDataOperationRequest;
	var node_requestServiceProcessor;
	var node_uiDataOperationServiceUtility;
	var node_UIDataOperation;
	var node_contextUtility;
	var node_IOTaskResult;
	var node_createDynamicIOData;
	var node_createUITag;
	var node_createUINodeGroupView;
//*******************************************   Start Node Definition  ************************************** 	

var node_uiNodeViewFactory = function(){
	
	var loc_out = {
			
		getCreateUINodeViewRequest : function(uiNodes, id, parentContext, matchers, handlers, requestInfo){
			var uiNodeGroupView = node_createUINodeGroupView(uiNodes, id, parentContext);
			
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createUINodeViewRequest", {}), handlers, requestInfo);
			_.each(uiNodeGroupView.getChildren(), function(uiNodeView, i){
				out.addRequest(loc_processUINodeViewRequest(uiNodeView, parentContext, matchers));
			});

			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(requestInfo){
				return uiNodeGroupView;
			}));
			return out;
		},

	};
	
	return loc_out;
}();	

var loc_processUINodeViewRequest = function(uiNodeView, parentContext, matchers, handlers, requestInfo){
	var uiNodeType = uiNodeView.getUINodeType();
	if(uiNodeType==node_COMMONCONSTANT.STORYNODE_TYPE_HTML){
		//for html related ui node
		return loc_processUIHtmlViewRequest(uiNodeView, parentContext, matchers, handlers, requestInfo);
	}
	else if(uiNodeType==node_COMMONCONSTANT.STORYNODE_TYPE_UIDATA){
		//for tag related ui node
		return loc_processUITagViewRequest(uiNodeView, parentContext, matchers, handlers, requestInfo);
	}
};

var loc_processUIHtmlViewRequest = function(uiHtmlNodeView, parentContext, matchers, handlers, requestInfo){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUIHtmlNode", {}), handlers, requestInfo);

	var id = uiHtmlNodeView.getId();
	//create children tag view
	var createChildrenTagRequest = node_createServiceRequestInfoSequence(undefined);
	var childrenViews = uiHtmlNodeView.getChildrenView();
	_.each(childrenViews, function(childView, id){
		createChildrenTagRequest.addRequest(loc_processUINodeViewRequest(childView, parentContext));
	});
	out.addRequest(createChildrenTagRequest);
	
	return out;
};

var loc_processUITagViewRequest = function(uiNodeTagView, parentContext, matchers, handlers, requestInfo){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUITagNode", {}), handlers, requestInfo);
	var uiNode = uiNodeTagView.getUINode();
	var tagId = uiNodeTagView.getTagId();
	out.addRequest(nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([tagId], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UITAG, {
		success : function(requestInfo, resources){
			var uiTagDefResourceObj = resources[tagId];

			var uiTag = node_createUITag(
					uiTagDefResourceObj, 
					uiNodeTagView.getId(), 
					uiNodeTagView.getAttributes(), 
					parentContext,
					{
						mode : node_CONSTANT.TAG_RUNTIME_MODE_DEMO,
//						startElement : uiNodeTagView.getStartElement(),
//						endElement : uiNodeTagView.getEndElement(),
						viewContainer : uiNodeTagView.getViewContainer(),
						matchers : matchers
					}, 
					uiNode.getBody());
			var initRequest = node_getLifecycleInterface(uiTag).initRequest({
				success : function(requestInfo){
					return uiNodeTagView.setUITag(uiTag);
				}
			}, requestInfo);
			return initRequest;
		}
	}));
	return out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.utility", function(){node_uiResourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createEmbededScriptExpressionInContent", function(){node_createEmbededScriptExpressionInContent = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createEmbededScriptExpressionInAttribute", function(){node_createEmbededScriptExpressionInAttribute = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createEmbededScriptExpressionInTagAttribute", function(){node_createEmbededScriptExpressionInTagAttribute = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createUITagRequest", function(){node_createUITagRequest = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.createUIDataOperationRequest", function(){node_createUIDataOperationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.UIDataOperation", function(){node_UIDataOperation = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.IOTaskResult", function(){node_IOTaskResult = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createDynamicData", function(){node_createDynamicIOData = this.getData();});
nosliw.registerSetNodeDataEvent("uitag.createUITag", function(){node_createUITag = this.getData();});
nosliw.registerSetNodeDataEvent("uinode.entity.createUINodeGroupView", function(){node_createUINodeGroupView = this.getData();});


//Register Node by Name
packageObj.createChildNode("uiNodeViewFactory", node_uiNodeViewFactory); 

})(packageObj);
