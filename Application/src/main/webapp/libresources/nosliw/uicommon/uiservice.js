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
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createUIContentPlugin;
	var node_createUIPagePlugin;
	var node_createUITagPlugin;
	var node_createUITagDebuggerPlugin;
	var node_createUIContentWrapperDebuggerPlugin;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIService = function(complexEntityService){
	
	var loc_complexEntityService = complexEntityService;
	
	var loc_init = function(){
		loc_complexEntityService.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIPAGE, "1.0.0", node_createUIPagePlugin());
		loc_complexEntityService.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UICONTENT, "1.0.0", node_createUIContentPlugin());
		loc_complexEntityService.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UICUSTOMERTAG, "1.0.0", node_createUITagPlugin());
		loc_complexEntityService.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UICUSTOMERTAGDEBUGGER, "1.0.0", node_createUITagDebuggerPlugin());
		loc_complexEntityService.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIWRAPPERCONTENTCUSTOMERTAGDEBUGGER, "1.0.0", node_createUIContentWrapperDebuggerPlugin());
	};
	
	var loc_out = {
		
	};

	loc_init();
	
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});

nosliw.registerSetNodeDataEvent("uicontent.createUIContentPlugin", function(){node_createUIContentPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("uicontent.createUIPagePlugin", function(){node_createUIPagePlugin = this.getData();});
nosliw.registerSetNodeDataEvent("uicontent.createUITagPlugin", function(){node_createUITagPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("uicontent.createUITagDebuggerPlugin", function(){node_createUITagDebuggerPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("uicontent.createUIContentWrapperDebuggerPlugin", function(){node_createUIContentWrapperDebuggerPlugin = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIService", node_createUIService); 

})(packageObj);
