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
	var node_createUIViewFactory;
	var node_createServiceRequestInfoSimple;
	var node_createUIResourceView;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIResourceService = function(){
	
	var loc_uiResourceViewFactory = node_createUIViewFactory();
	
	var loc_getResourceViewId = function(){	return nosliw.generateId();	};
	
	var loc_out = {

			getCreateUIResourceViewRequest : function(name, externalContext, handlers, requester_parent){
				var requestInfo = loc_out.getRequestInfo(requester_parent);
				var out = node_createServiceRequestInfoService(new node_ServiceInfo("CreateUIResourceView", {"name":name}), handlers, requestInfo)

				var getUIResourceRequest = nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([name], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIRESOURCE, {});
				
				
				out.setDependentService(new node_DependentServiceRequestInfo(getUIResourceRequest, {
					success : function(requestInfo, uiResources){
						var uiResource = uiResources[name];
						return node_createUIResourceView(loc_uiResourceViewFactory.createUIView(uiResource, loc_getResourceViewId(), undefined, externalContext, requestInfo));
					}
				}));
				return out;
			},	
			executeCreateUIResourceViewRequest : function(name, externalContext, handlers, requester_parent){
				var requestInfo = this.getCreateUIResourceViewRequest(name, handlers, requester_parent);
				node_requestServiceProcessor.processRequest(requestInfo);
			},
			
			getGenerateUIResourceViewRequest : function(uiResource, externalContext, handlers, requester_parent){
				var requestInfo = loc_out.getRequestInfo(requester_parent);
				var out = node_createServiceRequestInfoSimple(new node_ServiceInfo("GenerateUIResourceView", {"uiResource":uiResource}), 
					function(requestInfo){
						return node_createUIResourceView(loc_uiResourceViewFactory.createUIView(uiResource, loc_getResourceViewId(), undefined, externalContext, undefined));
					}, 
					handlers, requestInfo);
				return out;
			},	
			executeGenerateUIResourceViewRequest : function(uiResource, externalContext, handlers, requester_parent){
				var requestInfo = this.getGenerateUIResourceViewRequest(uiResource, handlers, requester_parent);
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
nosliw.registerSetNodeDataEvent("uiresource.createUIViewFactory", function(){node_createUIViewFactory = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("uiresource.createUIResourceView", function(){node_createUIResourceView = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIResourceService", node_createUIResourceService); 

})(packageObj);
