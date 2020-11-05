//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_storyUtility;
	var node_createStoryNodeElement;
	var node_createGroupLayer;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_utility = function(){

	var loc_nodeTypeLayer = {
		"service" : ["service", "serviceInput", "serviceOutput", "serviceInputParm", "serviceOutputItem"],
		"data" : ["constant", "variable"],
		"UI" : ["UI_page", "UI_data", "UI_html"]
	};
	
	var loc_layerByNodeType;
	
	var loc_getLayerByNodeType = function(){
		if(loc_layerByNodeType==undefined){
			loc_layerByNodeType = {};
			_.each(loc_nodeTypeLayer, function(nodeArray, layer){
				_.each(nodeArray, function(nodeType){
					loc_layerByNodeType[nodeType] = layer; 
				});
			});
		}
		return loc_layerByNodeType;
	};
	
	var loc_processStoryNode = function(storyNode, overViewTree, processed, module){
		var out;
		var storyNodeId = storyNode[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
		var story = module.getStory();
		if(processed[storyNodeId]==undefined){
			//brand new
			out = node_createStoryNodeElement(storyNodeId, module);
			module.addStoryNodeElement(out);
			var childStoryNodesInfo = node_storyUtility.getAllChildNodesInfo(storyNodeId, story);
			_.each(childStoryNodesInfo, function(childStroyNodeInfo, i){
				var childNode = loc_processStoryNode(childStroyNodeInfo.childNode, overViewTree, processed, module);
				if(childNode==undefined){
					var childStoryNodeId = childStroyNodeInfo.node[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
					childNode = overViewTree[childStoryNodeId];
					delete overViewTree[childStoryNodeId];
				}
				out.addChild(childNode, childStroyNodeInfo.connectionId);
			});
			processed[storyNodeId]= true;
		}
		return out;
	};
	
	var loc_out = {
	
		getLayerByNodeType : function(nodeType){
			var layers = {
				"service" : "service",
				"serviceInput" : "service",
				"serviceOutput" : "service",
				"serviceInputParm" : "service",
				"serviceOutputItem" : "service",
				"constant" : "data",
				"variable" : "data",
				"UI_page" : "UI",
				"UI_data" : "UI",
				"UI_html" : "UI",
			};
			return loc_getLayerByNodeType()[nodeType];
		},
		
		getUIStoryNode : function(story){
			return node_storyUtility.getStoryNodeByTypes(story, loc_nodeTypeLayer["UI"]);
		},
		
		//
		buildOverviewLayerTree : function(module){
			var that = this;
			var layerTree = {};
			var nodeTree = this.buildOverviewNodeTree(module);
			_.each(nodeTree, function(node, id){
				var layerName = that.getLayerByNodeType(node_storyUtility.getNodeById(module.getStory(), id)[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE]);
				var layer = layerTree[layerName];
				if(layer==undefined){
					layer = node_createGroupLayer(layerName, module);
					layerTree[layerName] = layer;
				}
				layer.addChild(node);
			});
			return layerTree;
		},
		
		//
		buildOverviewNodeTree : function(module){
			var overViewTree = {};
			var processed = {};
			var storyNodes = node_storyUtility.getAllNodes(module.getStory());
			_.each(storyNodes, function(storyNode, id){
				var node = loc_processStoryNode(storyNode, overViewTree, processed, module);
				if(node!=undefined){
					overViewTree[storyNode[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]] = node;
				}
			});
			return overViewTree;
		},	
		
	    updateChild : function(childInfo, parentNode){
	    	var that = this;
	    	var layer = parentNode.getLayer();
	    	var childNode = childInfo.getElementNode();
	    	childNode.setLayer(layer+1);
	    	var grandChildrenInfo = childNode.getChildren();
	    	_.each(grandChildrenInfo, function(grandChildInfo, i){
	    		that.updateChild(grandChildInfo, childNode);
	    	});
	    },
	    
	    getColorByLayer : function(layer){
	    	var color = ["pink", "blue", "red", "green", "yellow"];
	    	return color[layer];
	    },
	    
	};		
			
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.overview.createStoryNodeElement", function(){ node_createStoryNodeElement = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.overview.createGroupLayer", function(){ node_createGroupLayer = this.getData();});


//Register Node by Name
packageObj.createChildNode("storyOverviewUtility", node_utility); 

})(packageObj);
