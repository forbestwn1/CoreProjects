/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage("module.userapps");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_ServiceRequestExecuteInfo;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoCommon;
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_createMiniAppService;
	var node_miniAppUtility;
	var node_createComponentGroup;
	var node_createComponentMiniApp;
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "userApps";

var node_createModuleUserApps = function(){

	var loc_vueModel = {
		userInfo : {
			group : [],
			miniApp : []
		}
	};
	
	var loc_vueComponent = {
		data : function(){
			return loc_vueModel;
		},
		components : {
			group : node_createComponentGroup(),
			miniapp : node_createComponentMiniApp()
		},
		template : `
			<div class="list accordion-list">
				<ul>
					<group 
						v-for="groupMiniApp in userInfo.groupMiniApp"
						v-bind:group="groupMiniApp.group"
						v-bind:miniapps="groupMiniApp.miniApp"
					>
					</group>
				
					<miniapp 
						v-for="miniapp in userInfo.miniApp"
						v-bind:miniapp="miniapp"
					>
					</miniapp>
				</ul>
			</div>
		`
	};
	
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(handlers, requestInfo){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("userAppsInit", {}), handlers, requestInfo);

		return out;
	};

	var showUserInfo = function(userInfo){
		_.each(userInfo[node_COMMONATRIBUTECONSTANT.USERINFO_MINIAPPS], function(miniApp, index){
			appendMiniApp(loc_view, miniApp, userInfo[node_COMMONATRIBUTECONSTANT.USERINFO_USER][node_COMMONATRIBUTECONSTANT.USER_ID]);
		});

		_.each(userInfo[node_COMMONATRIBUTECONSTANT.USERINFO_GROUPMINIAPP], function(groupMiniAppInstance, index){
			appendMiniAppGroup(loc_view, groupMiniAppInstance);
		});
	};
	
	var loc_out = {
		
		refreshRequest : function(userInfo, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("RefreshUserApps", {}), handlers, requestInfo);
			out.addRequest(node_createServiceRequestInfoSimple(new node_ServiceInfo("RefreshUserApps", {}), 
				function(requestInfo){
				loc_vueModel.userInfo = userInfo;
				})); 
			return out;
		},

		
		addMiniApp : function(){
			
		},
		
		getVueModule : function(){
			return loc_vueComponent;
		}
	};
	
	node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_makeObjectWithName(loc_out, loc_mduleName);
	
	return loc_out;

};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.createMiniAppService", function(){node_createMiniAppService = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.utility", function(){node_miniAppUtility = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createComponentGroup", function(){node_createComponentGroup = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createComponentMiniApp", function(){node_createComponentMiniApp = this.getData();});

//Register Node by Name
packageObj.createChildNode("createModuleUserApps", node_createModuleUserApps); 

})(packageObj);
