/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

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
	var node_createEventObject;
	var node_createUINode;
	var node_createStoryService;
	var node_storyUtility;
	var node_storyUIUtility;
	var node_uiNodeViewFactory;
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "userApps";

var node_createModuleUI = function(parm){

	var loc_root = parm;

	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_storyService = node_createStoryService();
	
	var loc_componentData = {
		page : {
			available : false
		}
	};
	
	var loc_vue;

	var loc_triggerEvent = function(eventName, eventData, request){
		if(request==undefined)  request = node_createServiceRequestInfoCommon();
		loc_eventSource.triggerEvent(eventName, eventData, request);
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(handlers, requestInfo){
		Vue.component('story-uinode', node_createUINode());
		loc_vue = new Vue({
			el: loc_root,
			data: loc_componentData,
			components : {
			},
			computed : {
				isPageAvailable : function(){
					return !(this.page.available == false);
				},
			},
			methods : {
				onSelectNode : function(miniApp) {
					loc_triggerEvent("select", node_createMiniAppInfo(miniApp));
				},
				showUIView : function(){
					
				},
			},
			template :
				`
				    <div class="block">
				    	<div>
							<story-uinode
								v-if="isPageAvailable"
								v-bind:data="page"
						  		v-on:showUIView="showUIView"
							></story-uinode>
						</div>

					    <div>
					    	
					    </div>
				    </div>
				    
				`
		});
		
	};

	var loc_out = {
		
		refreshRequest : function(designId, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("RefreshPageModule", {}), handlers, requestInfo);
			
			var that  = this;
			out.addRequest(loc_storyService.getGetDesignRequest(undefined, designId, {
				success : function(request, design){
					loc_design = design;
					var story = loc_design[node_COMMONATRIBUTECONSTANT.DESIGNSTORY_STORY];
					var pageTree = node_storyUIUtility.buildPageTree(story);
					loc_componentData.page = pageTree;

					var pageChildrenNode = [];
					_.each(pageTree.getChildrenInfo(), function(childInfo, i){
						pageChildrenNode.push(childInfo.childNode);
					});
					
					return node_uiNodeViewFactory.getCreateUINodeViewRequest(pageChildrenNode, pageTree.getStoryNodeId(), undefined, {
						success : function(request, uiNodeViewGroup){
							uiNodeViewGroup.appendTo($("#uiDiv"));
						}
					});
				}
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
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){	node_createServiceRequestInfoCommon = this.getData();	});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.ui.createUINode", function(){node_createUINode = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.service.createStoryService", function(){node_createStoryService = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUIUtility", function(){node_storyUIUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uinode.uiNodeViewFactory", function(){node_uiNodeViewFactory = this.getData();});

//Register Node by Name
packageObj.createChildNode("createModuleUI", node_createModuleUI); 

})(packageObj);
