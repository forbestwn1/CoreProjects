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
	var node_createUIResourceViewFactory;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIResourceService = function(){
	
	var loc_uiResourceViewFactory = node_createUIResourceViewFactory();
	
	var loc_buildUIResourceId = function(name){
		var out = {};
		out[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = name; 
		out[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIRESOURCE; 
		return out;
	};
	
	var loc_getResourceViewId = function(){	return nosliw.generateId();	};
	
	var loc_out = {

			getGetUIResourceRequest : function(names, handlers, requester_parent){
				var requestInfo = loc_out.getRequestInfo(requester_parent);
				var out = node_createServiceRequestInfoService(new node_ServiceInfo("GetUIResource", {"names":names}), handlers, requestInfo)
				
				//get resource request
				var resourceIds = [];
				for(var i in names)		resourceIds.push(loc_buildUIResourceId(names[i]));
				var loadResourceRequest = nosliw.runtime.getResourceService().getGetResourcesRequest(resourceIds);
				
				out.setDependentService(new node_DependentServiceRequestInfo(loadResourceRequest, {
					success : function(requestInfo, resourceTree){
						//translate tree to resources by id
						var uiResources = {};
						var uiResourceResources = node_resourceUtility.getResourcesByTypeFromTree(resourceTree, node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIRESOURCE);
						_.each(uiResourceResources, function(uiResourceResource, name){
							uiResources[name] = uiResourceResource.resourceData;
						});
						return uiResources;
					}
				}));
				return out;
			},
			
			executeGetUIResourceRequest : function(names, handlers, requester_parent){
				var requestInfo = this.getGetUIResourceRequest(names, handlers, requester_parent);
				node_requestServiceProcessor.processRequest(requestInfo, true);
			},
			
			getCreateUIResourceViewRequest : function(name, handlers, requester_parent){
				var requestInfo = loc_out.getRequestInfo(requester_parent);
				var out = node_createServiceRequestInfoService(new node_ServiceInfo("CreateUIResourceView", {"name":name}), handlers, requestInfo)

				var getUIResourceRequest = this.getGetUIResourceRequest([name], {});
				out.setDependentService(new node_DependentServiceRequestInfo(getUIResourceRequest, {
					success : function(requestInfo, uiResources){
						var uiResource = uiResources[name];
						return loc_uiResourceViewFactory.createUIResourceView(uiResource, loc_getResourceViewId(), undefined, undefined, undefined);
					}
				}));
				return out;
			},	
			
			executeCreateUIResourceViewRequest : function(name, handlers, requester_parent){
				var requestInfo = this.getCreateUIResourceViewRequest(name, handlers, requester_parent);
				node_requestServiceProcessor.processRequest(requestInfo, true);
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
nosliw.registerSetNodeDataEvent("uiresource.createUIResourceViewFactory", function(){node_createUIResourceViewFactory = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIResourceService", node_createUIResourceService); 

})(packageObj);
