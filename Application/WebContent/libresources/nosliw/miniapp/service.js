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

		getSaveDataRequest : function(userId, appId, dataName, dataInfo, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_CREATEDATA_USERID] = userId;
			parms[node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_CREATEDATA_APPID] = appId;
			parms[node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_CREATEDATA_DATANAME] = dataName;
			parms[node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_CREATEDATA_DATAINFO] = dataInfo;
			var remoteRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_CREATEDATA, parms), undefined, handlers, requestInfo);
			return remoteRequest;
		},

		executeSaveDataRequest : function(userId, appId, dataName, dataInfo, handlers, requester_parent){
			var requestInfo = this.getSaveDataRequest(userId, appId, dataName, dataInfo, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},

		getUpdateDataRequest : function(dataId, dataInfo, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_UPDATEDATA_ID] = dataId;
			parms[node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_UPDATEDATA_DATAINFO] = dataInfo;
			var remoteRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_UPDATEDATA, parms), undefined, handlers, requestInfo);
			return remoteRequest;
		},

		executeUpdateDataRequest : function(dataId, dataInfo, handlers, requester_parent){
			var requestInfo = this.getUpdateDataRequest(dataId, dataInfo, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		getDeleteDataRequest : function(dataId, dataType, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_DELETEDATA_ID] = dataId;
			parms[node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_DELETEDATA_DATATYPE] = dataType;
			var remoteRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.APPSERVLET_COMMAND_DELETEDATA, parms), undefined, handlers, requestInfo);
			return remoteRequest;
		},

		executeDeleteDataRequest : function(dataId, dataType, handlers, requester_parent){
			var requestInfo = this.getDeleteDataRequest(dataId, dataType, handlers, requester_parent);	
			node_requestServiceProcessor.processRequest(requestInfo);
		},

		
		getExecuteServiceRequest : function(serviceName, serviceInfo, parms, handlers, requestInfo){
			var commandParms = {
					name : serviceInfo[serviceName][node_COMMONATRIBUTECONSTANT.DEFINITIONMINIAPPSERVICEDATASOURCE_DATASOURCEID],
					parms : parms
				};
			return nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest("dataSource", "getData", commandParms, handlers, requestInfo);
		},
		
		executeExecuteServiceRequest : function(serviceName, serviceInfo, parms){
			var requestInfo = this.getExecuteServiceRequest(serviceName, serviceInfo, parms);
			node_requestServiceProcessor.processRequest(requestInfo);
		}

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
