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
	var node_storyUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIDataConnectionLink = function(uiNodeId, parentContextDef, module){
	
	var loc_uiNodeId = uiNodeId;
	var loc_module = module;

	var loc_links = {};
	
	var loc_init = function(){
		var story = loc_module.getStory();
		var storyUINode = node_storyUtility.getNodeById(story, loc_uiNodeId);
		var storyUINodeType = storyUINode[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE];
		
		if(storyUINodeType==node_COMMONCONSTANT.STORYNODE_TYPE_UIDATA){
			var contextPath = storyUINode[node_COMMONATRIBUTECONSTANT.STORYNODEUIDATA_DATAINFO][node_COMMONATRIBUTECONSTANT.UIDATAINFO_CONTEXTPATH];
			var nodeDataContextDef = storyUINode[node_COMMONATRIBUTECONSTANT.STORYNODEUI_DATASTRUCTURE][node_COMMONATRIBUTECONSTANT.UIDATASTRUCTUREINFO_CONTEXT];
			var nodeDataDef = parentContextDef[node_COMMONATRIBUTECONSTANT.CONTEXTGROUP_GROUP][contextPath[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_CONTEXTCATEGARY]][node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT][contextPath[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_CONTEXTNAME]];
			var varStoryNodeId = nodeDataDef[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION][node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_SOLIDNODEREF][node_COMMONATRIBUTECONSTANT.REFERENCECONTEXTNODE_ROOTNODEID];
			var varStoryNode = node_storyUtility.getNodeById(story, varStoryNodeId);

			var link = new joint.shapes.standard.Link({
				  attrs: {
				      '.connection': { 'stroke-dasharray': '2,5' }
				  }
			});

			var nodeElement1 = loc_module.getNodeElementById(varStoryNodeId);
			var nodeElement2 = loc_module.getNodeElementById(loc_uiNodeId);
			link.source(nodeElement1.getElement());
			link.target(nodeElement2.getElement());
			loc_links[varStoryNodeId] = link; 
		}
		
	};
	
	var loc_out = {
		
		getLinks : function(){	return loc_links;	},

		addToPaper : function(graph){
			_.each(loc_links, function(link, varNodeId){
				graph.addCells(link);
			});
		},
	};
	
	loc_init();
	
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
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUIDataConnectionLink", node_createUIDataConnectionLink); 

})(packageObj);