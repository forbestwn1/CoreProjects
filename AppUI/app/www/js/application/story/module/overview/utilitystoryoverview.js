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

	var loc_processStoryNode = function(storyNode, overViewTree, processed, module){
		var out;
		var storyNodeId = storyNode[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
		var story = module.getStory();
		if(processed[storyNodeId]==undefined){
			//brand new
			out = node_createStoryNodeElement(storyNodeId, module);
			var childStoryNodesInfo = node_storyUtility.getAllChildNodesInfo(storyNodeId, story);
			_.each(childStoryNodesInfo, function(childStroyNodeInfo, childId){
				var childNode = loc_processStoryNode(childStroyNodeInfo.node, overViewTree, processed, module);
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
			};
			return layers[nodeType];
		},
		
		buildOverviewLayerTree : function(module){
			var that = this;
			var layerTree = {};
			var nodeTree = this.buildOverviewNodeTree(module);
			_.each(nodeTree, function(node, id){
				var layerName = that.getLayerByNodeType();
				var layer = layerTree[layerName];
				if(layer==undefined){
					layer = node_createGroupLayer(layerName, module);
					layerTree[layerName] = layer;
				}
				layer.addChild(node);
			});
			return layerTree;
		},
			
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
	    	var color = ["grey", "blue", "red", "green", "yellow"];
	    	return color[layer];
	    },
	    
	    getNeedSize : function(storyNode){
	    	var that = this;
	    	var width = 100;
	    	var height = 40;
	    	var childrenInfo = storyNode.getChildren();
	    	var childHeight = 0;
	    	_.each(childrenInfo, function(childInfo, i){
	    		var size = that.getNeedSize(childInf.nodeElement);
	    		width = width + size.width;
	    		if(i>=1) width = width + 30;
	    		if(size.height> childHeight)  childHeight = size.height;
	    	});
	    	return {
	    		width : width,
	    		height : height + childHeight,
	    	}; 
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
