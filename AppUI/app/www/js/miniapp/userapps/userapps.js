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
		}
	];
	
	//built templates
	var loc_templates = {};
	
	var loc_view;
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(handlers, requestInfo){

		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("userAppsInit", {}), handlers, requestInfo);
		
		var templatesFile = [];
		var templatesInfoByFile = {};
		_.each(loc_templatesInfo, function(templateInfo, index){
			templatesFile.push(templateInfo.file);
			templatesInfoByFile[templateInfo.file] = templateInfo;
			loc_templates[templateInfo.name] = templateInfo;
		});
		
		var loadTemplatesRequest = node_miniAppUtility.loadTemplateRequest(templatesFile, {
				success : function(requestInfo, templateSources){
					_.each(templateSources, function(source, fileName){
						loc_templates[templatesInfoByFile[fileName].name].template = Handlebars.compile(source); 
					});
				}
		}, requestInfo);
		out.addRequest(loadTemplatesRequest);
		
		out.addRequest(node_createServiceRequestInfoSimple(new node_ServiceInfo("UIInit", {}), 
			function(requestInfo){
				loc_view = $(loc_templates.main.template());
				loc_view.listview();
				return loc_view;
			}, 
		));
		
//		
//		var out = node_createServiceRequestInfoCommon(new node_ServiceInfo("LoadTemplate", {}), handlers, requestInfo);
//		out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(requestInfo){
//			$.get("js/miniapp/userapps/userapps.html")
//			  .done((source) => {
//				  loc_template = Handlebars.compile(source);
//				  loc_view = loc_template();
//				  requestInfo.executeSuccessHandler(loc_view, out);
//			});
//		}, this));
		
		return out;
	};

	
	var showUserInfo = function(userInfo){
		var listView = $("#miniAppList");
		_.each(userInfo[node_COMMONATRIBUTECONSTANT.USERINFO_MINIAPPS], function(miniApp, index){
			appendMiniApp(listView, miniApp, userInfo[node_COMMONATRIBUTECONSTANT.USERINFO_USER][node_COMMONATRIBUTECONSTANT.USER_ID]);
		});

		_.each(userInfo[node_COMMONATRIBUTECONSTANT.USERINFO_GROUPMINIAPP], function(groupMiniAppInstance, index){
			appendMiniAppGroup(listView, groupMiniAppInstance);
		});
		
//		$("#miniAppList").listview('refresh');
	};
	
	var appendMiniApp = function(parentView, miniApp, userId){
		var miniAppId = miniApp[node_COMMONATRIBUTECONSTANT.USERMINIAPPINFO_APPID];
		var viewId = "miniAppInstanceId_" + miniAppId;
		var miniAppName = miniApp[node_COMMONATRIBUTECONSTANT.USERMINIAPPINFO_APPNAME];
		var mininAppItemView = $("<li><a id=\""+viewId+"\" href=\"#\">"+miniAppName+"</a></li>");
		parentView.append(mininAppItemView);
//		onClickMiniApp(viewId, miniAppId, userId, miniAppName);
	};
	
	var appendMiniAppGroup = function(parentView, miniAppGroup, userId){
		var groupId = miniAppGroup[node_COMMONATRIBUTECONSTANT.USERGROUPMINIAPP_GROUP][node_COMMONATRIBUTECONSTANT.GROUP_ID];
		var groupName = miniAppGroup[node_COMMONATRIBUTECONSTANT.USERGROUPMINIAPP_GROUP][node_COMMONATRIBUTECONSTANT.GROUP_NAME];
		var groupViewId = "groupMiniAppId_" + groupId;
		
		var groupView = $("<li><div data-role='collapsible' id='collapsible_"+groupViewId+"' data-collapsed='true'><h4>"+groupName+"</h4><ul data-role='listview' id='"+groupViewId+"'></ul></div></li>");
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
