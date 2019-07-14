/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage("module.miniapp");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_getLifecycleInterface;
	var node_createComponentUserApps;
	var node_createEventObject;
	var node_createAppDecoration;
	var node_appDataService;
	var node_requestServiceProcessor;
	var node_getComponentLifecycleInterface;
	var node_createTypicalConfigure;
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

	var loc_appConfigure;

	var loc_mainModuleRoot = root.main;
	var loc_settingModuleRoot = root.setting;
	var loc_framework7App = root.framework7App;
	
	var loc_appRuntime;
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(handlers, requestInfo){
		loc_appConfigure = node_createTypicalConfigure(loc_mainModuleRoot, loc_settingModuleRoot, node_appDataService, node_storeService, loc_framework7App);
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
			
			//get group app data
			var groupData;
			var inputIODataSet = node_createIODataSet();
			var groupId = miniAppInfo.getGroupId();
			if(groupId!=undefined){
				out.addRequest(node_appDataService.getGetAppDataRequest(nosliw.runtime.getSecurityService().createOwnerInfo(node_COMMONCONSTANT.MINIAPP_DATAOWNER_GROUP, groupId), undefined, {
					success : function(request, dataByName){
						groupData = _.find(dataByName, function(data, name){
							return true;
						});
						if(groupData!=undefined && groupData.length==1){
							inputIODataSet.setData(undefined, groupData[0].data);
						}
						
						return node_createServiceRequestInfoSimple(undefined, function(request){
							//update owner info
							nosliw.runtime.getSecurityService().setOwnerType(miniAppInfo.getAppInfo()[node_COMMONATRIBUTECONSTANT.MINIAPP_DATAOWNERTYPE]);
							nosliw.runtime.getSecurityService().setOwnerId(miniAppInfo.getAppInfo()[node_COMMONATRIBUTECONSTANT.MINIAPP_DATAOWNERID]);
						}, {
							success : function(request){
								return nosliw.runtime.getUIAppService().getGetUIAppEntryRuntimeRequest(miniAppEntryId, miniAppEntryId, loc_appConfigure, inputIODataSet,
										{
											success : function(requestInfo, appRuntime){
												loc_appRuntime = appRuntime;
												lifecycle = node_getComponentLifecycleInterface(loc_appRuntime);
												return lifecycle.getTransitRequest("activate");
											}
										}
								);
							}
						});

						
					}
				}));
			}
			
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
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createComponentUserApps", function(){node_createComponentUserApps = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppDecoration", function(){node_createAppDecoration = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.appDataService", function(){node_appDataService = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createTypicalConfigure", function(){node_createTypicalConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.storeService", function(){node_storeService = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});


//Register Node by Name
packageObj.createChildNode("createModuleMiniApp", node_createModuleMiniApp); 
packageObj.createChildNode("createMiniAppInfo", node_createMiniAppInfo); 

})(packageObj);
