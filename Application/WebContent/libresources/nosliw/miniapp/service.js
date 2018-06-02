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
	var node_createServiceRequestInfoSequence;
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

		getLoadMiniAppUIEntryRequest : function(userId, miniAppId, uiEntry, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("LoadMiniAppUIEntry", {"userId":userId,"minAppId":miniAppId,"uiEntry":uiEntry}), handlers, requester_parent);
			
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_LOADMINIAPP_APPID] = miniAppId;
			parms[node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_LOADMINIAPP_USERID] = userId;
			parms[node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_LOADMINIAPP_ENTRY] = uiEntry;
			var remoteRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_LOADMINIAPP, parms), undefined, {
				success : function(requestInfo, uiEntry){
					var depResourceIds = uiEntry[node_COMMONATRIBUTECONSTANT.INSTANCEMINIAPPUIENTRY_RESOURCES];
					var loadResourceRequest = nosliw.runtime.getResourceService().getGetResourcesRequest(depResourceIds, {
						success : function(requestInfo){
							return uiEntry;
						}
					});
					return loadResourceRequest;
				}
			}, requestInfo);
			out.addRequest(remoteRequest);
			return out;
		},

		executeLoadMiniAppUIEntryRequest : function(userId, miniAppId, uiEntry, handlers, requester_parent){
			var requestInfo = this.getLoadMiniAppUIEntryRequest(userId, miniAppId, uiEntry, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createMiniAppService", node_createMiniAppService); 

})(packageObj);
