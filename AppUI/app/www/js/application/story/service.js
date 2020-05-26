//get/create package
var packageObj = library.getChildPackage();    

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
var node_createStoryService = function(){

	var loc_configureName = "story";
	
	nosliw.registerSetNodeDataEvent("runtime", function(){
		//register remote task configure
		var configure = node_createConfigures({
			url : loc_configureName,
//			contentType: "application/json; charset=utf-8"
		});
		
		nosliw.runtime.getRemoteService().registerSyncTaskConfigure(loc_configureName, configure);
	});

	
	loc_out = {

		getNewDesignRequest : function(userInfo, storyId, directorId, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("newDesign", {}), handlers, requestInfo);
			
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.STORYBUILDSERVLET_COMMAND_NEWDESIGN_DIRECTORID] = directorId;
			parms[node_COMMONATRIBUTECONSTANT.STORYBUILDSERVLET_COMMAND_NEWDESIGN_STORYID] = storyId;
			var remoteRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.STORYBUILDSERVLET_COMMAND_NEWDESIGN, parms), undefined, {
				success : function(requestInfo, newDesign){
					return newDesign;
				}
			}, requestInfo);
			out.addRequest(remoteRequest);
			return out;
		},
		
		executeGetNewDesignRequest : function(userInfo, storyId, directorId, handlers, request){
			var requestInfo = this.getNewDesignRequest(userInfo, storyId, directorId, handlers, request);
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

//Register Node by Name
packageObj.createChildNode("createStoryService", node_createStoryService); 

})(packageObj);
