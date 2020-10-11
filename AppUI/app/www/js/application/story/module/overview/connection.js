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
	var node_storyUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createConnectionLink = function(storyConnectionId, module){
	
	var loc_storyConnectionId = storyConnectionId;
	var loc_module = module;

	var loc_link;
	
	var loc_init = function(){
		loc_link = new joint.shapes.standard.Link({
			  attrs: {
			      '.connection': { 'stroke-dasharray': '2,5' }
			  }
		});
		var story = loc_module.getStory();
		var storyConnection = node_storyUtility.getConnectionById(story, loc_storyConnectionId);
		var storyEnd1 = storyConnection[node_COMMONATRIBUTECONSTANT.CONNECTION_END1];
		var storyEnd2 = storyConnection[node_COMMONATRIBUTECONSTANT.CONNECTION_END2];
		var storyNodeId1 = node_storyUtility.getElementIdByReference(story, storyEnd1[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_NODEREF]);
		var storyNodeId2 = node_storyUtility.getElementIdByReference(story, storyEnd2[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_NODEREF]);
		var nodeElement1 = loc_module.getNodeElementById(storyNodeId1[node_COMMONATRIBUTECONSTANT.IDELEMENT_ID]);
		var nodeElement2 = loc_module.getNodeElementById(storyNodeId2[node_COMMONATRIBUTECONSTANT.IDELEMENT_ID]);
		loc_link.source(nodeElement1.getElement());
		loc_link.target(nodeElement2.getElement());
		
		
	};
	
	var loc_out = {
		
		getLink : function(){	return loc_link;	},

		addToPaper : function(graph){
			graph.addCells(loc_link);
		},
	};
	
	loc_init();
	
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
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createConnectionLink", node_createConnectionLink); 

})(packageObj);
