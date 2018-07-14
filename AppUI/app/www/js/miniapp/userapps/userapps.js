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
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "userApps";

var node_createModuleUserApps = function(){

	//template name and files
	var loc_templatesInfo = [
		{
			name : "main",
			file : "js/miniapp/userapps/userapps.html"
		},
		{
			name : "appitem",
			file : "js/miniapp/userapps/appitem.html"
		},
		{
			name : "appgroup",
			file : "js/miniapp/userapps/appgroup.html"
		}
	];
	
	//built templates
	var loc_templates = {};
	
	var loc_view;
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(handlers, requestInfo){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("userAppsInit", {}), handlers, requestInfo);

		//build templates
		out.addRequest(node_miniAppUtility.getBuildTemplateRequest(loc_templatesInfo, {
			success : function(requestInfo, templates){
				loc_templates = templates;
			}
		}));
		
		//init ui
		out.addRequest(node_createServiceRequestInfoSimple(new node_ServiceInfo("UIInit", {}), 
			function(requestInfo){
				loc_view = $(loc_templates.main.template());
				loc_view.listview();
				return loc_view;
			}, 
		));
		
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
	
	var appendMiniApp = function(parentView, miniApp, userId){
		var miniAppId = miniApp[node_COMMONATRIBUTECONSTANT.USERMINIAPPINFO_APPID];
		var viewId = "miniAppInstanceId_" + miniAppId;
		var miniAppName = miniApp[node_COMMONATRIBUTECONSTANT.USERMINIAPPINFO_APPNAME];
		
		var mininAppItemView = $(loc_templates.appitem.template({
			viewId : viewId,
			miniAppName : miniAppName
		}));
		
		parentView.append(mininAppItemView);
//		onClickMiniApp(viewId, miniAppId, userId, miniAppName);
	};
	
	var appendMiniAppGroup = function(parentView, miniAppGroup, userId){
		var groupId = miniAppGroup[node_COMMONATRIBUTECONSTANT.USERGROUPMINIAPP_GROUP][node_COMMONATRIBUTECONSTANT.GROUP_ID];
		var groupName = miniAppGroup[node_COMMONATRIBUTECONSTANT.USERGROUPMINIAPP_GROUP][node_COMMONATRIBUTECONSTANT.GROUP_NAME];
		var groupViewId = "groupMiniAppId_" + groupId;
		
		var groupView = $(loc_templates.appgroup.template({
			groupViewId : groupViewId,
			groupName : groupName,
		}));
		parentView.append(groupView);
		
		_.each(miniAppGroup[node_COMMONATRIBUTECONSTANT.USERGROUPMINIAPP_MINIAPPS], function(miniApp, index){
			appendMiniApp($("#"+groupViewId), miniApp, userId);
		});
		$("#"+groupViewId).listview();
		$("#collapsible_"+groupViewId).collapsible();
		
	};

	
	
	var loc_out = {
		
		refreshRequest : function(userInfo, handlers, requestInfo){
			return node_createServiceRequestInfoSimple(new node_ServiceInfo("RefreshUserApps", {}), 
				function(requestInfo){
					showUserInfo(userInfo);
				}, 
			);
		},
		
		addMiniApp : function(){
			
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


//Register Node by Name
packageObj.createChildNode("createModuleUserApps", node_createModuleUserApps); 

})(packageObj);
