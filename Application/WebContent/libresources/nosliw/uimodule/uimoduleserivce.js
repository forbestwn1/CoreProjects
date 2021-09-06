//get/create package
var packageObj = library.getChildPackage("service");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_contextUtility;
	var node_createConfigure;
	var node_loadComponentResourceRequest;
	var node_getObjectType;
	var node_componentUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIModuleService = function(){
	
	var loc_out = {
		
		getGetUIModuleRuntimeRequest : function(id, module, configure, ioInput, state, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteUIModuleResource"), handlers, request);

			//build module decoration info array from module configure
			var moduleDecInfos = node_componentUtility.buildDecorationInfoArrayFromConfigure(configure, 'moduleDecoration', node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIMODULEDECORATION);

			//build ui decoration configure 
			var uiDecorationInfos = node_componentUtility.buildDecorationInfoArrayFromConfigure(configure, "uiDecoration"); 
			
			out.addRequest(node_loadComponentResourceRequest(
				typeof module === 'string'? 
					{
						resourceId : module,
						type : node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIMODULE
					} : module, 
					{
						decoration : moduleDecInfos,
						type : node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIMODULEDECORATION
					},
				{
					success : function(request, componentInfo){
						//create ui module runtime
						if(typeof module==='string' && node_getObjectType(state)==node_CONSTANT.TYPEDOBJECT_TYPE_BACKUPSERVICE){
							//create by resource id, then version should be set according to resource version
							state.setVersion("5.0.0");   //kkkkk
						}
						return node_createModuleRuntimeRequest(id, componentInfo.componentResource, configure, componentInfo.decoration, uiDecorationInfos, configure.getConfigureValue().root, ioInput, state, {
							success : function(request, uiModuleRuntime){
								return uiModuleRuntime;
							}
						}, request);
					}
				}));
			
			return out;
		},			
			
		executeGetUIModuleRuntimeRequest : function(id, module, configure, ioInput, state, handlers, request){
			var requestInfo = this.getGetUIModuleRuntimeRequest(id, module, configure, ioInput, state, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
	};

	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
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
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIModuleService", node_createUIModuleService); 

})(packageObj);
