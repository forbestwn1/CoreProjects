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
	var node_requestServiceProcessor;
	var node_contextUtility;
	var node_createUIModuleRequest;
	var node_createConfigure;
	var node_loadComponentResourceRequest;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIModuleService = function(){
	
	var loc_out = {

		getGetUIModuleRuntimeRequest : function(id, module, configure, ioInput, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteUIModuleResource"), handlers, request);

			configure = node_createConfigure(configure);
			var componentDecorationInfo = configure.getConfigureData().moduleDecoration;
			out.addRequest(node_loadComponentResourceRequest(
				typeof module === 'string'? 
					{
						componentResourceId : module,
						type : node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIMODULE
					} : module, 
				componentDecorationInfo==undefined?undefined:
					{
						decoration : componentDecorationInfo,
						type : node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIMODULEDECORATION
					},
				{
					success : function(request, componentInfo){
						//create ui module runtime
						return node_createModuleRuntimeRequest(id, componentInfo.component, configure, componentInfo.decoration, configure.getConfigureData().root, ioInput, {
							success : function(request, uiModuleRuntime){
								return uiModuleRuntime;
							}
						});
					}
				}));
			
			return out;
		},			
			
		executeGetUIModuleRuntimeRequest : function(id, module, configure, ioInput, handlers, requester_parent){
			var requestInfo = this.getGetUIModuleRuntimeRequest(id, module, configure, ioInput, handlers, requester_parent);
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
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.createModuleRuntimeRequest", function(){node_createModuleRuntimeRequest = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("component.loadComponentResourceRequest", function(){node_loadComponentResourceRequest = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUIModuleService", node_createUIModuleService); 

})(packageObj);
