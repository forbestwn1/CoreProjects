//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_createServiceRequestInfoSequence;
//*******************************************   Start Node Definition  ************************************** 	

var node_createLoginService = function(miniAppService){

	var loc_miniAppService = miniAppService;
	
	var loc_out = {

		getLoginRequest(userInfo, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("MiniAppLogin", {"userInf":userInfo}), handlers, requestInfo);
			if(userInfo==undefined){
				userInfo = {};
				var userId = localStorage.userId;
				if(userId!=undefined){
					userInfo.user = {};
					userInfo.user.id = userId;
				}
			}
			out.addRequest(loc_miniAppService.getLoginRequest(userInfo, {
				success : function(requestInfo, userInfo){
//					userInfo = undefined;   //kkkkkk
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
