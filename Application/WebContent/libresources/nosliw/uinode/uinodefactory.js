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
//*******************************************   Start Node Definition  ************************************** 	

var node_uiNodeViewFactory = function(){
	
	var loc_out = {
			
		getCreateUINodeViewRequest : function(uiNode, id, startEle, endEle, parentContext, handlers, requestInfo){
			return node_createUINodeRequest(id, uiNode, parentContext, startEle, endEle, handlers, requestInfo);
		},
		
		getCreateUIBodyViewRequest : function(uiNodeBody, id, parentView, parentContext, handlers, requestInfo){

			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUINode", {}), handlers, requestInfo);

			var nodeView = loc_createUINodeView(uiNodeBody, id, parentView, parentContext, requestInfo);
			
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUINode", {}), handlers, requestInfo);
			var uiChildren = uiNodeBody.children;
			_.each(nodeView.getChildren, function(child, i){
				out.addRequest(node_createUINodeRequest(id+"_"+i, child.getNode(), parentContext, child.getStartElement(), child.getEndElement()));
			});

			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(requestInfo){
				return nodeView;
			}));

			return out;
		},
				
	};
	
	return loc_out;
}();	
	
var node_createUINodeRequest = function(id, uiNode, parentContext, startElement, endElement, handlers, requestInfo){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUINode", {}), handlers, requestInfo);
	var tagId = uiNode.getTagId();
	out.addRequest(nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([tagId], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UITAG, {
		success : function(requestInfo, resources){
			var uiTagResourceObj = resources[tagId];

			var uiTag = node_createUITag(
					uiTagResourceObj, 
					id, 
					uiNode.attributes, 
					parentContext,
					{
						mode : node_CONSTANT.TAG_RUNTIME_MODE_DEMO,
						startElement : startElement,
						endElement : endElement
					}, 
					uiNode.body);
			var initRequest = node_getLifecycleInterface(uiTag).initRequest({
				success : function(requestInfo){
					return uiTag;
				}
			}, requestInfo);
			return initRequest;
		}
	}));
	return out;
};	
	
	
var node_createChildView = function(node, parentView){
	
	var loc_node = node;
	var loc_startEle = $("<nosliw></nosliw>");
	var loc_endEle = "";
	var loc_nodeView;
	
	parentView.append(loc_startEle);
	parentView.append(loc_endEle);
	
	var loc_out = {
		getStartEle : function(){  return loc_startEle;   },
		getEndEle : function(){  return loc_endEle;   },
		setNodeView : function(nodeView){  loc_nodeView = nodeView;    },
		getNodeView : function(){   return loc_nodeView;   },
	};
	
	return loc_out;
};
	

/*
 * method to create ui resource view according to 
 * 		uiBody body for view
 * 		attributes : 
 * 	 	name space id
 * 		parent uiresource
 */
var loc_createUINodeView = function(uiNodeBody, id, parentView, parentContext, requestInfo){

	var loc_id = id;

	var loc_childrenNode = [];
	
	//render html to temporary document fragment
	var loc_fragmentDocument = $(document.createDocumentFragment());
	var loc_parentView = $("<div></div>");
	loc_fragmentDocument.append(loc_parentView);
	
	var uiChildren = uiNodeBody.children;
	_.each(uiChildren, function(uiChild, i){
		loc_childrenNode.push(node_createChildView(uiChilde, loc_parentView));
	});

	
	loc_out = {
		
	};

	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UIVIEW);

	node_getLifecycleInterface(loc_out).init(uiBody, attributes, id, parent, context, requestInfo);
	
	return loc_out;
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


//Register Node by Name
packageObj.createChildNode("uiNodeViewFactory", node_uiNodeViewFactory); 

})(packageObj);
