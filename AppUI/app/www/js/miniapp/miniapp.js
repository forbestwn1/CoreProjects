//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_createServiceRequestInfoSequence;
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_createMiniAppService;
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "minApp";

var node_createMiniApp = function(){

	
	var loc_miniAppService;
	
	
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(){

		loc_miniAppService = node_createMiniAppService();

  		$(document).trigger("miniappActive");
		
		return true;
	};

	var loc_out = {

		getMiniAppService(){  return loc_miniAppService; },

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
					localStorage.userId = userInfo.user.id;
					return userInfo;
				}
			}));
			return out;
		},
		executeLoginRequest(userInfo, handlers, requestInfo){	node_requestServiceProcessor.processRequest(this.getLoginRequest(userInfo, handlers, requestInfo));	}
		
	};
	
	
	node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_makeObjectWithName(loc_out, loc_mduleName);
	
	return loc_out;

};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.service.createMiniAppService", function(){node_createMiniAppService = this.getData();});


//Register Node by Name
packageObj.createChildNode("createMiniApp", node_createMiniApp); 

})(packageObj);
