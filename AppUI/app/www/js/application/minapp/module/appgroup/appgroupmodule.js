/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage("module.appgroup");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoCommon;
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_getLifecycleInterface;
	var node_createComponentUserApps;
	var node_createComponentUserInfo;
	var node_createEventObject;
	var node_createMiniAppInfo;
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "userApps";

var node_createModuleAppGroup = function(parm){

	var loc_root = parm;

	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_componentData = {
		group : {}
	};
	
	var loc_vue;

	var loc_triggerEvent = function(eventName, eventData, request){
		if(request==undefined)  request = node_createServiceRequestInfoCommon();
		loc_eventSource.triggerEvent(eventName, eventData, request);
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(handlers, requestInfo){
		loc_vue = new Vue({
			el: loc_root,
			data: loc_componentData,
			components : {
			},
			computed : {
				user : function(){
					return this.userInfo.user;
				},
			},
			methods : {
				onSelectMiniApp : function(miniApp) {
					loc_triggerEvent("selectMiniApp", node_createMiniAppInfo(miniApp));
				},
			},
			template : 
				`
				<div>
					<p class="row">
						<button class="col button button-fill color-blue" v-for="miniapp in group.miniApp" v-on:click.prevent="onSelectMiniApp(miniapp)">{{miniapp.name}}</button>
					</p>
				</div>
				`
		});
	};

	var loc_out = {
		
		refreshRequest : function(userInfo, configureData, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("RefreshUserApps", {}), handlers, requestInfo);
			out.addRequest(node_createServiceRequestInfoSimple(new node_ServiceInfo("RefreshUserApps", {}), 
				function(requestInfo){
					var group = _.find(userInfo.groupMiniApp, function(group, i){
						return group.group.id==configureData.groupId;
					});
					loc_componentData.group = group;
				})); 
			return out;
		},

		getMiniApp : function(appId){
			var app = _.find(loc_vue.group.miniApp, function(app, i){
				return app.id==appId;
			});
			return app;
		},
		
		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

	};
	
	node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_makeObjectWithName(loc_out, loc_mduleName);
	
	return loc_out;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){	node_createServiceRequestInfoCommon = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createComponentUserApps", function(){node_createComponentUserApps = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createComponentUserInfo", function(){node_createComponentUserInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.miniapp.createMiniAppInfo", function(){node_createMiniAppInfo = this.getData();});


//Register Node by Name
packageObj.createChildNode("createModuleAppGroup", node_createModuleAppGroup); 

})(packageObj);
