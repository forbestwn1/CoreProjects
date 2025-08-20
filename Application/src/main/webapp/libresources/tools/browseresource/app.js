//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_CONSTANT;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_getLifecycleInterface;
	var node_createComponentUserApps;
	var node_createComponentUserInfo;
	var node_createEventObject;
	var node_createComponentChildren;
	var node_createComponentGroup;
	var node_createComponentResource;
	var node_createComponentInfo;
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "userApps";

var node_createResourceTreeApp = function(parm, resources){

	var loc_root = parm;

	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_componentData = {
		resources : {},
		info : {}
	};

	var loc_vue;

	var loc_triggerEvent = function(eventName, eventData, request){
		loc_eventSource.triggerEvent(eventName, eventData, request);
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(parm, resources){
		
		Vue.component('resource-info', node_createComponentInfo());
		Vue.component('resource-children', node_createComponentChildren());
		Vue.component('resource-group', node_createComponentGroup());
		Vue.component('resource-resource', node_createComponentResource());
		
		loc_vue = new Vue({
			el: loc_root,
			data: loc_componentData,
			methods : {
				onSelectResource : function(resourceInfo) {
					loc_componentData.info = resourceInfo;
				},
			},
			template : `
				<div class="row">
				    <!-- Each "cell" has col-[width in percents] class -->
				    <div class="col col-50 resizable">
						<div class="treeview" style="overflow-y: scroll; height:400px;">
						  	<resource-children 
						  		v-bind:data="resources"
								v-on:selectResource="onSelectResource"
						  	></resource-children>
						</div>
						<span class="resize-handler"></span>
				    </div>
				    <div id="infoDiv" class="col col-50 resizable">
				    	<div>
						  	<resource-info 
						  		v-bind:data="info"
						  	></resource-info>
				    	</div>
				    </div>
				</div>
			`
		});
		
		loc_componentData.resources = resources;
	};

	var loc_out = {
		
		refreshRequest : function(resources, configureData, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("RefreshUserApps", {}), handlers, requestInfo);
			out.addRequest(node_createServiceRequestInfoSimple(new node_ServiceInfo("RefreshUserApps", {}), 
				function(requestInfo, resources){
					loc_componentData.resources = resources;
				})); 
			return out;
		},

		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },
	};
	
	node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_makeObjectWithName(loc_out, loc_mduleName);
	
	node_getLifecycleInterface(loc_out).init(parm, resources);

	return loc_out;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createComponentUserApps", function(){node_createComponentUserApps = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createComponentUserInfo", function(){node_createComponentUserInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("browseresource.createComponentChildren", function(){node_createComponentChildren = this.getData();});
nosliw.registerSetNodeDataEvent("browseresource.createComponentGroup", function(){node_createComponentGroup = this.getData();});
nosliw.registerSetNodeDataEvent("browseresource.createComponentResource", function(){node_createComponentResource = this.getData();});
nosliw.registerSetNodeDataEvent("browseresource.createComponentInfo", function(){node_createComponentInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("createResourceTreeApp", node_createResourceTreeApp); 

})(packageObj);
