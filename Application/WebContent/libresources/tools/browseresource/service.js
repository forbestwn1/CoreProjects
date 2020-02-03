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

var node_createService = function(){

	var loc_configureName = "browseresource";
	
	nosliw.registerSetNodeDataEvent("runtime", function(){
		//register remote task configure
		var configure = node_createConfigures({
			url : loc_configureName,
//			contentType: "application/json; charset=utf-8"
		});
		
		nosliw.runtime.getRemoteService().registerSyncTaskConfigure(loc_configureName, configure);
	});

	
	loc_out = {

		getResourceTreeRequest : function(handlers, requestInfo){
//			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var remoteRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo("browseResource", ""), undefined, handlers, requestInfo);
			return remoteRequest;
		},

		executeResourceTreeRequest : function(handlers, requester_parent){
			var remoteRequest = this.getResourceTreeRequest(handlers, requester_parent);
			node_requestServiceProcessor.processRequest(remoteRequest);
		},
	};
	
	loc_out = node_buildServiceProvider(loc_out, "browseresource");

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
packageObj.createChildNode("createService", node_createService); 

})(packageObj);
