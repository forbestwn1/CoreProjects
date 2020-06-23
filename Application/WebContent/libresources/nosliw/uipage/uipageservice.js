//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_resourceUtility;
	var node_buildServiceProvider;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_uiResourceViewFactory;
	var node_createServiceRequestInfoSimple;
	var node_createUIPage;
	var node_createServiceRequestInfoSequence;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIPageService = function(){
	
	var loc_uiResourceViewFactory = node_uiResourceViewFactory;
	
	var loc_getResourceViewId = function(){	return nosliw.generateId();	};
	
	var loc_out = {

			getCreateUIPageRequest : function(pageId, context, handlers, requester_parent){
//				pageId = node_resourceUtility.buildReourceCoreIdLiterate(pageId);
				var requestInfo = loc_out.getRequestInfo(requester_parent);
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUIResourceView", {"name":pageId}), handlers, requestInfo);

				out.addRequest(nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([pageId], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIRESOURCE, {
					success : function(requestInfo, uiResources){
						var uiResource = uiResources[pageId];
						return loc_uiResourceViewFactory.getCreateUIViewRequest(uiResource, loc_getResourceViewId(), undefined, context, {
							success : function(requestInfo, uiView){
								return node_createUIPage(uiView);
							}
						});
					}
				}));
				
				return out;
			},	
			executeCreateUIPageRequest : function(pageId, context, handlers, requester_parent){
				var requestInfo = this.getCreateUIPageRequest(pageId, context, handlers, requester_parent);
				node_requestServiceProcessor.processRequest(requestInfo);
			},
			
			getGenerateUIPageRequest : function(uiResource, context, handlers, requester_parent){
				var requestInfo = loc_out.getRequestInfo(requester_parent);
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUIResourceView", {"name":name}), handlers, requestInfo);
				out.addRequest(loc_uiResourceViewFactory.getCreateUIViewRequest(uiResource, loc_getResourceViewId(), undefined, context, {
					success : function(requestInfo, uiView){
						return node_createUIPage(uiView);
					}
				}));
				return out;
			},	
			executeGenerateUIPageRequest : function(uiResource, context, handlers, requester_parent){
				var requestInfo = this.getGenerateUIPageRequest(uiResource, context, handlers, requester_parent);
				node_requestServiceProcessor.processRequest(requestInfo);
			},
	};

	loc_out = node_buildServiceProvider(loc_out, "uiResourceService");
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.uiResourceViewFactory", function(){node_uiResourceViewFactory = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createUIPage", function(){node_createUIPage = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createUIPageService", node_createUIPageService); 

})(packageObj);
