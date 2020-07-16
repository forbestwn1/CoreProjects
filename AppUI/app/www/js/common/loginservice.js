//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_createServiceRequestInfoSequence;
//*******************************************   Start Node Definition  ************************************** 	

var node_createLoginService = function(){

	var loc_configureName = "login";
	
	nosliw.registerSetNodeDataEvent("runtime", function(){
		//register remote task configure
		var configure = node_createConfigures({
			url : loc_configureName,
//			contentType: "application/json; charset=utf-8"
		});
		
		nosliw.runtime.getRemoteService().registerSyncTaskConfigure(loc_configureName, configure);
	});

	var loc_getLoginRequest = function(userInfo, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		var remoteRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(node_COMMONATRIBUTECONSTANT.MINIAPPSERVLET_COMMAND_LOGIN, userInfo), undefined, handlers, requestInfo);
		return remoteRequest;
	};

	var loc_out = {

		getLoginRequest(userInfo, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("Login", {"userInf":userInfo}), handlers, requestInfo);

			if(userInfo==undefined){
				userInfo = {};
				var userId = localStorage.userId;
				if(userId!=undefined){
					userInfo.user = {};
					userInfo.user.id = userId;
				}
			}
			out.addRequest(loc_getLoginRequest(userInfo, {
				success : function(requestInfo, userInfo){
					localStorage.userId = userInfo.user.id;
					nosliw.runtime.getSecurityService().setToken(userInfo.user.id);
					return userInfo;
				}
			}));
			return out;
		},
		
		executeLoginRequest(userInfo, handlers, requestInfo){	node_requestServiceProcessor.processRequest(this.getLoginRequest(userInfo, handlers, requestInfo));	},
		
	};
	
	return loc_out;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createLoginService", node_createLoginService); 

})(packageObj);
