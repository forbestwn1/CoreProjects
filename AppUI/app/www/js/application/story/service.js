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
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_createStoryService = function(){

	var loc_configureName = "buildstory";
	
	nosliw.registerSetNodeDataEvent("runtime", function(){
		//register remote task configure
		var configure = node_createConfigures({
			url : loc_configureName,
//			contentType: "application/json; charset=utf-8"
		});
		
		nosliw.runtime.getRemoteService().registerSyncTaskConfigure(loc_configureName, configure);
	});

	
	var loc_out = {

		//resource discovery
		getDefaultUITagRequest : function(dataTypeCriteria, handlers, requester_parent){
			//gateway request
			var gatewayId = node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_UITAG;
			var command = node_COMMONATRIBUTECONSTANT.GATEWAYUITAG_COMMAND_GETDEFAULTTAG;
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.GATEWAYUITAG_COMMAND_GETDEFAULTTAG_CRITERIA] = dataTypeCriteria;
			var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms);
			
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoService(undefined, handlers, requestInfo);
			out.setDependentService(new node_DependentServiceRequestInfo(gatewayRequest));
			return out;
		},
			
		getGetDesignRequest : function(userInfo, designId, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getDesign", {}), handlers, requestInfo);
			
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.STORYBUILDSERVLET_COMMAND_GETDESIGN_ID] = designId;
			var remoteRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.STORYBUILDSERVLET_COMMAND_GETDESIGN, parms), undefined, {
				success : function(requestInfo, design){
					return design;
				}
			}, requestInfo);
			out.addRequest(remoteRequest);
			return out;
		},
			
		getNewDesignRequest : function(userInfo, directorId, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("newDesign", {}), handlers, requestInfo);
			
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.STORYBUILDSERVLET_COMMAND_NEWDESIGN_DIRECTORID] = directorId;
			var remoteRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.STORYBUILDSERVLET_COMMAND_NEWDESIGN, parms), undefined, {
				success : function(requestInfo, newDesign){
					return newDesign;
				}
			}, requestInfo);
			out.addRequest(remoteRequest);
			return out;
		},
		
		executeGetNewDesignRequest : function(userInfo, directorId, handlers, request){
			var requestInfo = this.getNewDesignRequest(userInfo, storyId, directorId, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},

		getDoDesignRequest : function(userInfo, designId, changes, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("design", {}), handlers, requestInfo);
			
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.STORYBUILDSERVLET_COMMAND_DESIGN_DESIGNID] = designId;
			parms[node_COMMONATRIBUTECONSTANT.STORYBUILDSERVLET_COMMAND_DESIGN_CHANGE] = changes;
			var remoteRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.STORYBUILDSERVLET_COMMAND_DESIGN, parms), undefined, {
				success : function(requestInfo, serviceData){
					return serviceData;
				}
			}, requestInfo);
			out.addRequest(remoteRequest);
			return out;
		},
	
		executeDoDesignRequest : function(userInfo, designId, changes, handlers, request){
			var requestInfo = this.getDoDesignRequest(userInfo, designId, changes, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},

	};
	
	loc_out = node_buildServiceProvider(loc_out, "storyService");
	
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("createStoryService", node_createStoryService); 

})(packageObj);
