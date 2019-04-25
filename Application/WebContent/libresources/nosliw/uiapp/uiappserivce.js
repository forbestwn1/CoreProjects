//get/create package
var packageObj = library.getChildPackage("service");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIAppService = function(){
	
	var loc_out = {
			
		getGetUIAppEntryRuntimeRequest : function(id, appEntryId, configure, ioInput, handlers, requester_parent){
			configure = node_createConfigure(configure);

			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetUIAppRuntime", {}), handlers, requestInfo);

			var appEntryResourceId = {};
			appEntryResourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = appEntryId; 
			appEntryResourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIAPPENTRY; 

//			var appConfigureResourceId = {};
//			appConfigureResourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = appConfigureId; 
//			appConfigureResourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIAPPCONFIGURE; 
			
			//load ui module resource and env factory resource
			out.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest([appEntryResourceId, appConfigureResourceId], {
				success : function(requestInfo, resourceTree){
					var appEntryDef = node_resourceUtility.getResourceFromTree(resourceTree, appEntryResourceId).resourceData;
//					var appConfigure = node_resourceUtility.getResourceFromTree(resourceTree, appConfigureResourceId).resourceData;
					
					//create ui app runtime
					return node_createAppRuntimeRequest(appEntryDef, appConfigure, appConfigure, {
						success : function(request, uiAppRuntime){
//							return uiAppRuntime.getInitRequest({
//								success :function(request, data){
//									return uiAppRuntime;
//								}
//							});
						}
					});
				}
			}));
			
			return out;
			
		},	
			
		executeGetUIAppEntryRuntimeRequest : function(id, appEntryId, appConfigureId, appStatelessData, handlers, requester_parent){
			var requestInfo = this.getGetUIAppEntryRuntimeRequest(id, appEntryId, appConfigureId, appStatelessData, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
			
	};

	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});


//Register Node by Name
packageObj.createChildNode("createUIAppService", node_createUIAppService); 

})(packageObj);
