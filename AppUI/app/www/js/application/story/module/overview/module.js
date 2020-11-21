/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoCommon;
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_getLifecycleInterface;
	var node_createEventObject;
	var node_createNodeElement;
	var node_createConnectionLink;
	var node_createUIDataConnectionLink;
	var node_createStoryService;
	var node_storyOverviewUtility;
	var node_createMainLayout;
	var node_storyUtility;
//*******************************************   Start Node Definition  ************************************** 	
var loc_mduleName = "overview";
	
var node_createModuleOverview = function(parm){

	var loc_root = parm;

	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_storyService = node_createStoryService();

	var loc_triggerEvent = function(eventName, eventData, request){
		if(request==undefined)  request = node_createServiceRequestInfoCommon();
		loc_eventSource.triggerEvent(eventName, eventData, request);
	};

	var loc_paper;
	var loc_graph;
	
	var loc_nodeElements = {};
	var loc_connectionLinks = {};
	var loc_uiDataConnectionLink = {};
	
	var loc_design;
	var loc_layerTree;
	
	var loc_mainLayout = node_createMainLayout();
	
	var loc_layoutList = ["UI", "data", "service"];
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(handlers, requestInfo){
		loc_graph = new joint.dia.Graph;

//		 loc_paper = new joint.dia.Paper({
//		        el: loc_root,
//		        model: loc_graph,
//		        width: 1000,
//		        height: 1000,
//		        gridSize: 1
//		    });
	};

	var loc_buildUIDataConnectionLink = function(node, parent, uiDataConnectionLinks){
		var story = loc_design[node_COMMONATRIBUTECONSTANT.DESIGNSTORY_STORY];

		if(parent!=undefined){
			var uiStoryNodeId = node[node_COMMONATRIBUTECONSTANT.IDELEMENT_ID];
			var uiDataConnection = node_createUIDataConnectionLink(uiStoryNodeId, parent[node_COMMONATRIBUTECONSTANT.STORYNODEUI_DATASTRUCTURE][node_COMMONATRIBUTECONSTANT.UIDATASTRUCTUREINFO_CONTEXT], loc_out);
			uiDataConnectionLinks[uiStoryNodeId] = uiDataConnection;
		}
		
		var childrenInfo = node_storyUtility.getAllChildNodesInfo(node, story);
		_.each(childrenInfo, function(childInfo, i){
			loc_buildUIDataConnectionLink(childInfo.childNode, node, uiDataConnectionLinks);
		});
	};
	
	var loc_out = {

		refreshRequest : function(designId, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("RefreshStoryOverviewModule", {}), handlers, requestInfo);
			
			var that  = this;
			out.addRequest(loc_storyService.getGetDesignRequest(undefined, designId, {
				success : function(request, design){
					loc_design = design;
					var story = loc_design[node_COMMONATRIBUTECONSTANT.DESIGNSTORY_STORY];
					//build node tree according to contains relationship
					loc_layerTree = node_storyOverviewUtility.buildOverviewLayerTree(that);
					
					
					_.each(loc_layoutList, function(layoutName, i){
						var child = loc_layerTree[layoutName];
						if(child!=undefined){
							loc_mainLayout.addChild(child);
						}
					});
					
					var neededSize = loc_mainLayout.getNeededSize();
					loc_mainLayout.setLocation(0, 0, neededSize.width, neededSize.height);

					 loc_paper = new joint.dia.Paper({
					        el: loc_root,
					        model: loc_graph,
					        width: neededSize.width,
					        height: neededSize.height,
					        gridSize: 1
					 });
					 
					_.each(loc_mainLayout.getChildren(), function(child, i){
						child.addToPaper(loc_graph);
					});
					
					//io data connection link
					var connections = story[node_COMMONATRIBUTECONSTANT.STORY_CONNECTION];
					_.each(connections, function(storyConnection, id){
						if(storyConnection[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE]!=node_COMMONCONSTANT.STORYCONNECTION_TYPE_CONTAIN){
							var connectionLink = node_createConnectionLink(id, that);
							loc_connectionLinks[id] = connectionLink; 
							connectionLink.addToPaper(loc_graph);
						}
					});
					
					//ui data connection link 
					var pageNodes = node_storyUtility.getStoryNodeByType(story, "UI_page");
					if(pageNodes.length>0){
						loc_buildUIDataConnectionLink(pageNodes[0], undefined, loc_uiDataConnectionLink);
						_.each(loc_uiDataConnectionLink, function(link, id){
							link.addToPaper(loc_graph);
						});
					}					
				}
			}));
			return out;
		},
			
		getStory : function(){  return loc_design[node_COMMONATRIBUTECONSTANT.DESIGNSTORY_STORY];  },
		
		addStoryNodeElement : function(storyNodeElement){  loc_nodeElements[storyNodeElement.getStoryNodeId()] = storyNodeElement;    },
		
		getNodeElementById : function(storyNodeId){  return loc_nodeElements[storyNodeId];   },
		getConnectionLinkById : function(storyConnectionId){  return loc_connectionLinks[storyConnectionId];   },
		
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
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){	node_createServiceRequestInfoCommon = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.overview.createNodeElement", function(){node_createNodeElement = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.overview.createConnectionLink", function(){node_createConnectionLink = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.overview.createUIDataConnectionLink", function(){node_createUIDataConnectionLink = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.service.createStoryService", function(){node_createStoryService = this.getData();});

nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.overview.storyOverviewUtility", function(){ node_storyOverviewUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.overview.createMainLayout", function(){ node_createMainLayout = this.getData();});


//Register Node by Name
packageObj.createChildNode("createModuleOverview", node_createModuleOverview); 
})(packageObj);
