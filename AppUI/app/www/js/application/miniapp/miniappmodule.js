/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage("module.miniapp");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
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
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "userApps";

var node_createModuleMiniApp = function(root){

	var loc_appEntryId;

	var loc_appConfigure;

	var loc_mainModuleRoot = root.main;
	var loc_settingModuleRoot = root.setting;
	var loc_framework7App = root.framework7App;
	
	var loc_appRuntime;
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(handlers, requestInfo){
		loc_appConfigure = {
				global : {
					appDecoration : [
						{
							coreFun: node_createAppDecoration,
							id : "application"
						}
					],
					__appDataService : node_appDataService,
					__storeService :{
						saveData : function(categary, id, data){
							localStorage.setItem(categary+"_"+id, JSON.stringify(data));
						},
						
						retrieveData : function(categary, id){
							return JSON.parse(localStorage.getItem(categary+"_"+id));
						},
						
						clearData : function(categary, id){
							return localStorage.removeItem(categary+"_"+id);
						}
					},
					app : loc_framework7App,
				},
				components : {
					application : {
						"components" : {
							"application" : {
								global : {
									"root" : loc_mainModuleRoot,
									"decoration" : {
										global : ["Decoration_application_framework7"]
									},
									"moduleDecoration" : ["base", "uidecoration", "application_framework7_mobile", "debug"]
								}
							},
							"setting" : {
								global : {
									"root" : loc_settingModuleRoot,
									"decoration" : {
									},
									"moduleDecoration" : ["base", "uidecoration", "setting_framework7_mobile", "debug"]
								},
								components : {
									"setting_framework7_mobile" : {
										uiResource : "Decoration_setting_framework7"
									}
								}
							}
						}
					}
				}
			};
	};

	var loc_out = {
		
		getRefreshRequest : function(miniAppEntryId, handlers, requestInfo){
			var out = nosliw.runtime.getUIAppService().getGetUIAppEntryRuntimeRequest(miniAppEntryId, miniAppEntryId, loc_appConfigure, undefined,
				{
					success : function(requestInfo, appRuntime){
						loc_appRuntime = appRuntime;
						lifecycle = node_getComponentLifecycleInterface(loc_appRuntime);
						lifecycle.executeTransitRequest("activate", {
							success : function(request){
								console.log('aaa');
							}
						});

					}
				}
			);
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


//Register Node by Name
packageObj.createChildNode("createModuleMiniApp", node_createModuleMiniApp); 

})(packageObj);
