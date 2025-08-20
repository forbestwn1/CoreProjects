/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage("module.miniapp");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_getLifecycleInterface;
	var node_appDataService;
	var node_requestServiceProcessor;
	var node_getComponentLifecycleInterface;
	var node_createAppConfigure;
	var node_storeService;
	var node_createIODataSet;
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "userApps";

var node_createMiniAppInfo = function(appInfo, groupId){
	
	var loc_appInfo = appInfo;
	var loc_groupId = groupId;
	
	var loc_out = {
		getAppInfo : function(){   return loc_appInfo;  },
		getGroupId : function(){  return  loc_groupId;  },
		setGroupId : function(groupId){  loc_groupId = groupId;   },
		setAppInfo : function(appInfo){  loc_appInfo = appInfo;   }
	};
	return loc_out;
};

var node_createModuleMiniApp = function(root){

	var loc_appEntryId;

	var loc_configureParms;
	var loc_settingName;
	
	var loc_appConfigure;

	var loc_appRuntime;
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(handlers, requestInfo){
		loc_appConfigure = node_createAppConfigure(root.settingName, root.configureParms);
		loc_configureParms = root.configureParms;
		loc_settingName = root.settingName;
	};

	var loc_out = {
		
		getRefreshRequest : function(miniAppInfo, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("RefreshMiniApp", {}), handlers, requestInfo);
			
			var miniAppEntryId = miniAppInfo.getAppInfo().id + ";main";

			//destroy current app first
			if(loc_appRuntime!=undefined)	out.addRequest(node_getComponentLifecycleInterface(loc_appRuntime).getTransitRequest("destroy"), {
				success : function(request){
					loc_appRuntime = undefined;
				}
			});

			var stateBackupService = node_createStateBackupService(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIAPPENTRY, miniAppEntryId, "1.0.0", nosliw.runtime.getStoreService());
			var ownerConfigureParms = {
				ownerConfigure : {
					defaultOwnerType : miniAppInfo.getAppInfo()[node_COMMONATRIBUTECONSTANT.MINIAPP_CATEGARY],
					ownerIdByType : {
//						"app" : miniAppInfo.getAppInfo()[node_COMMONATRIBUTECONSTANT.MINIAPP_ID],
//						"group" : miniAppInfo.getGroupId()
					}
				}
			};
			ownerConfigureParms.ownerConfigure.ownerIdByType[node_COMMONCONSTANT.MINIAPP_DATAOWNER_APP] = miniAppInfo.getAppInfo()[node_COMMONATRIBUTECONSTANT.MINIAPP_ID];
			ownerConfigureParms.ownerConfigure.ownerIdByType[node_COMMONCONSTANT.MINIAPP_DATAOWNER_GROUP] = miniAppInfo.getGroupId();
			
			var appConfigure = node_createAppConfigure(loc_settingName, _.extend(ownerConfigureParms, loc_configureParms));
			out.addRequest(nosliw.runtime.getUIAppService().getGetUIAppEntryRuntimeRequest(miniAppEntryId, miniAppEntryId, appConfigure, undefined, stateBackupService,
				{
					success : function(requestInfo, appRuntime){
						loc_appRuntime = appRuntime;
						lifecycle = node_getComponentLifecycleInterface(loc_appRuntime);
						return lifecycle.getTransitRequest("activate");
					}
				}
			));
			return out;
		},

		executeRefreshRequest : function(miniAppEntryId, handlers, requestInfo){
			var requestInfo = this.getRefreshRequest(miniAppEntryId, handlers, requestInfo);
			node_requestServiceProcessor.processRequest(requestInfo);
		},

	};
	
	node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_makeObjectWithName(loc_out, loc_mduleName);
	
	return loc_out;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.appDataService", function(){node_appDataService = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppConfigure", function(){node_createAppConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.storeService", function(){node_storeService = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createIODataSet", function(){node_createIODataSet = this.getData();});


//Register Node by Name
packageObj.createChildNode("createModuleMiniApp", node_createModuleMiniApp); 
packageObj.createChildNode("createMiniAppInfo", node_createMiniAppInfo); 

})(packageObj);
