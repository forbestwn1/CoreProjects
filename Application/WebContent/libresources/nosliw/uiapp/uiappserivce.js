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
	var node_loadComponentResourceRequest;
	var node_createConfigure;
	var node_requestServiceProcessor;
	var node_createAppRuntimeRequest;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIAppService = function(){
	
	var loc_out = {

		getGetUIAppEntryRuntimeRequest : function(id, app, configure, ioInput, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteUIAppResource"), handlers, request);

			configure = node_createConfigure(configure);
			var componentDecorationInfo = configure.getConfigureValue().appDecoration;
			out.addRequest(node_loadComponentResourceRequest(
				typeof app === 'string'? 
					{
						resourceId : app,
						type : node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIAPPENTRY
					} : app, 
				componentDecorationInfo==undefined?undefined:
					{
						decoration : componentDecorationInfo,
						type : node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIAPPDECORATION
					},
				{
					success : function(request, componentInfo){
						//create ui module runtime
						return node_createAppRuntimeRequest(id, componentInfo.componentResource, configure, componentInfo.decoration, ioInput, {
							success : function(request, uiAppRuntime){
								return uiAppRuntime;
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
nosliw.registerSetNodeDataEvent("component.loadComponentResourceRequest", function(){node_loadComponentResourceRequest = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppRuntimeRequest", function(){node_createAppRuntimeRequest = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIAppService", node_createUIAppService); 

})(packageObj);
