/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage("module.userapps");    

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
	var node_createComponentUserInfo;
	var node_createEventObject;
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "userApps";

var node_createModuleUserApps = function(parm){

	var loc_root = parm;

	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_componentData = {
		userInfo : {}
	};
	
	var loc_vue;

	var loc_triggerEvent = function(eventName, eventData, request){
		loc_eventSource.triggerEvent(eventName, eventData, request);
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(handlers, requestInfo){
		loc_vue = new Vue({
			el: loc_root,
			data: loc_componentData,
			components : {
				"user-apps" : node_createComponentUserApps(),
				"user-info" : node_createComponentUserInfo(),
			},
			computed : {
				user : function(){
					return this.userInfo.user;
				},
			},
			methods : {
				onSelectMiniApp : function(miniAppInfo) {
					loc_triggerEvent("selectMiniApp", miniAppInfo);
				},
			},
			template : `
				<div>
<!--
					<user-info
						v-bind:user="user"
					></user-info>
-->					
				  	<user-apps 
				  		v-bind:data="userInfo"
				  		v-on:selectMiniApp="onSelectMiniApp"
				  	></user-apps>
				</div>
			`
		});
	};

	var loc_out = {
		
		refreshRequest : function(userInfo, configureData, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("RefreshUserApps", {}), handlers, requestInfo);
			out.addRequest(node_createServiceRequestInfoSimple(new node_ServiceInfo("RefreshUserApps", {}), 
				function(requestInfo){
					loc_componentData.userInfo = userInfo;
				})); 
			return out;
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
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createComponentUserApps", function(){node_createComponentUserApps = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createComponentUserInfo", function(){node_createComponentUserInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});


//Register Node by Name
packageObj.createChildNode("createModuleUserApps", node_createModuleUserApps); 

})(packageObj);
