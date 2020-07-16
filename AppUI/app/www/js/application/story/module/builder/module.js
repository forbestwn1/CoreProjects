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
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "storyBuilder";

var node_createModuleStoryBuilder = function(parm){

	var loc_root = parm;

	var loc_storyService = node_createStoryService();
	
	var loc_story;
	var loc_changeHistory;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	
	
	var loc_componentData = {
		page : {}
	};
	
	var loc_vue;

	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(handlers, requestInfo){
/*		
		Vue.component('story-uinode', node_createUINode());
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
				onSelectNode : function(miniApp) {
					loc_triggerEvent("select", node_createMiniAppInfo(miniApp));
				},
			},
			template :
				`
				    <div class="block">
						<story-uinode 
							v-bind:data="page"
						></story-uinode>
				    </div>
				`
		});
*/		
	};

	var loc_processChange = function(change){
		
	};
	
	var loc_processQuestion = function(question){
		
	};
	
	var loc_out = {
		
		refreshRequest : function(undefined, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("RefreshStoryBuilderModule", {}), handlers, requestInfo);
			
			out.addRequest(loc_storyService.getNewDesignRequest(undefined, "pageSimple", {
				success : function(request, design){
					loc_story = design[node_COMMONATRIBUTECONSTANT.DESIGNSTORY_STORY];
					var changeHistory = design[node_COMMONATRIBUTECONSTANT.DESIGNSTORY_CHANGEHISTORY];
					var changeBatch = changeHistory[changeHistory.length-1];
					_.each(changeBatch[node_COMMONATRIBUTECONSTANT.CHANGEBATCH_CHANGES], function(change){
						loc_processChange(change);
					});
					
					loc_processQuestion(changeBatch[node_COMMONATRIBUTECONSTANT.CHANGEBATCH_QUESTION]);
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
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.service.createStoryService", function(){node_createStoryService = this.getData();});

//Register Node by Name
packageObj.createChildNode("createModuleStoryBuilder", node_createModuleStoryBuilder); 

})(packageObj);
