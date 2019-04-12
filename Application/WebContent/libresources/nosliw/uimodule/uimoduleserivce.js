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
	var node_resourceUtility;
	var node_createConfigure;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIModuleService = function(){
	
	var loc_out = {

		getGetUIModuleRuntimeRequest : function(module, ioInput, configure, componentDecorationFactorys, handlers, requester_parent){
			configure = node_createConfigure(configure);
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteUIModuleResource", {"module":module}), handlers, requestInfo);

			var resourceIds = [];
			var moduleId;
			var uiModuleDef;
			if(typeof module === 'string'){
				moduleId = {};
				moduleId[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = module; 
				moduleId[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIMODULE; 
				resourceIds.push(moduleId);
			}
			else{
				uiModuleDef = module;
			}

			var decFacResourcesInfo = [];
			if(componentDecorationFactorys!=undefined){
				_.each(componentDecorationFactorys, function(decFacDef, i){
					var decFacInfo = {};
					if(typeof decFacDef == "string"){
						decFacInfo.id = decFacDef;
						decFacInfo.name = decFacDef;
					}
					else{
						decFacInfo.id = decFacDef.id;
						decFacInfo.name = decFacDef.name;
					}
					decFacInfo.resourceId = {};
					decFacInfo.resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = decFacInfo.id; 
					decFacInfo.resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIMODULEDECORATION; 
					resourceIds.push(decFacInfo.resourceId);
					decFacResourcesInfo.push(decFacInfo);
				});
			}

			//load ui module resource and env factory resource
			out.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(resourceIds, {
				success : function(requestInfo, resourceTree){
					var uiModuleDef = requestInfo.getData("module");
					
					var componentDecorationInfos = requestInfo.getData("decResourceInfo");
					_.each(componentDecorationInfos, function(decFacInfo, i){
						decFacInfo.coreFun = node_resourceUtility.getResourceFromTree(resourceTree, decFacInfo.resourceId).resourceData;
					});
					
					if(moduleId!=undefined)  uiModuleDef = node_resourceUtility.getResourceFromTree(resourceTree, moduleId).resourceData;
					
					//create ui module runtime
					return node_createModuleRuntimeRequest(uiModuleDef, ioInput, configure, componentDecorationInfos, {
						success : function(request, uiModuleRuntime){
							return uiModuleRuntime;
						}
					});
				}
			}).withData(uiModuleDef, "module").withData(decFacResourcesInfo, "decResourceInfo"));
			
			return out;
		},
			
		executeGetUIModuleRuntimeRequest : function(id, ioInput, configure, componentDecorationFactorys, handlers, requester_parent){
			var requestInfo = this.getGetUIModuleRuntimeRequest(id, ioInput, configure, componentDecorationFactorys, handlers, requester_parent);
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
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUIModuleService", node_createUIModuleService); 

})(packageObj);
