//get/create package
var packageObj = library.getChildPackage("service");    

(function(packageObj){
	//get used node
	var node_createConfigures;
	var node_COMMONATRIBUTECONSTANT;
	var node_createServiceRequestInfoRemote;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_buildServiceProvider;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_createMiniAppService = function(){

	var loc_configureName = "miniApp";
	
	nosliw.registerSetNodeDataEvent("runtime", function(){
		//register remote task configure
		var configure = node_createConfigures({
			url : loc_configureName,
//			contentType: "application/json; charset=utf-8"
		});
		
		nosliw.runtime.getRemoteService().registerSyncTaskConfigure(loc_configureName, configure);
	});

	
	loc_out = {

		executeLoginRequest : function(userInfo, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var remoteRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_LOGIN, userInfo), undefined, handlers, requestInfo);
			node_requestServiceProcessor.processRequest(remoteRequest);
		},

		executeLoadMiniAppRequest : function(miniAppId, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_LOADMINIAPP_ID] = miniAppId;
			var remoteRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_LOADMINIAPP, parms), undefined, handlers, requestInfo);
			node_requestServiceProcessor.processRequest(remoteRequest);
		},

	};
	
	loc_out = node_buildServiceProvider(loc_out, "miniAppService");
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.setting.createConfigures", function(){node_createConfigures = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoRemote", function(){node_createServiceRequestInfoRemote = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

//Register Node by Name
packageObj.createChildNode("createMiniAppService", node_createMiniAppService); 

})(packageObj);
