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
	var node_createBasicLayout;
	var node_storyOverviewUtility;
	var node_storyUtility;
	var node_storyUIUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createStoryNodeElementChildInfo = function(nodeElement, connectionId){
	var loc_nodeElement = nodeElement;
	var loc_connectionId = connectionId;
	
	var loc_out = {
		
		getElementNode : function(){   return loc_nodeElement;   },
		getConnectionId : function(){   return loc_connectionId;     },
		
		getNeededSize : function(){   return loc_nodeElement.getNeededSize();  },
		
		setLocation : function(x, y, width, height){   return loc_nodeElement.setLocation(x, y, width, height);  },
		
		addToPaper : function(paper){  return loc_nodeElement.addToPaper(paper);  },
		
	};
	return loc_out;
};
	
var node_createStoryNodeElement = function(storyNodeId, module){
	
	var loc_storyNodeId = storyNodeId;
	var loc_module = module;
	
	var loc_layer = 1;
	
	var loc_graphElement;
	
	var loc_layout = node_createBasicLayout(); 
	
	var loc_getStoryNode = function(){
		return node_storyUtility.getNodeById(loc_module.getStory(), loc_storyNodeId);
	};
	
	var loc_getElementTitle = function(){
		var storyNode = loc_getStoryNode();
		return storyNode.type + ":" +node_storyUIUtility.getStoryElementDisplayName(storyNode);
//		return storyNode.displayName;
//		return storyNode.type + "_" + storyNode.name + "_" + storyNode.id; 
	};
	
	var loc_getLayerColor = function(){
		var storyNode = loc_getStoryNode();
		var enable = storyNode[node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE];
		if(enable==false){
			return "grey";
		}
		else{
			return node_storyOverviewUtility.getColorByLayer(loc_layer)
		}
	};
	
    var loc_createElement = function(){
        var element = new joint.shapes.standard.Rectangle();
        var location = loc_layout.getLocation();
        element.position(location.x, location.y);
        element.resize(location.width, location.height);
        element.attr({
            body: {
                fill: loc_getLayerColor(),
            },
            label: {
                text: loc_getElementTitle(),
                fill: 'white',
                'ref-y': -1*location.height/2+15,
            }
        });
    	return element
    };
    
	var loc_out = {
		getElement : function(){  return loc_graphElement;  },
			
		getStoryNodeId : function(){	return loc_storyNodeId;	},
		getStoryNode : function(){   return loc_getStoryNode();    },
	
		addChild : function(nodeElement, connectionId){
			var childInfo = node_createStoryNodeElementChildInfo(nodeElement, connectionId);
			node_storyOverviewUtility.updateChild(childInfo, this);
			loc_layout.addChild(childInfo);
		},
		
		getChildren : function(){   return loc_layout.getChildren();   },
		
		getLayer : function(){   return loc_layer;   },
		setLayer : function(layer){    loc_layer = layer;    },
		
		getNeededSize : function(){  return loc_layout.getNeededSize();  },
		
		setLocation : function(x, y, width, height){   loc_layout.setLocation(x, y, width, height);  },
		
		addToPaper : function(graph){
			if(loc_graphElement==undefined){
				loc_graphElement = loc_createElement();
			}
			graph.addCells(loc_graphElement);
			_.each(this.getChildren(), function(child, i){
				child.addToPaper(graph);
			});
			return loc_graphElement;
		},
	};
	
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
nosliw.registerSetNodeDataEvent("application.story.module.overview.createBasicLayout", function(){ node_createBasicLayout = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.overview.storyOverviewUtility", function(){ node_storyOverviewUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUIUtility", function(){node_storyUIUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createStoryNodeElement", node_createStoryNodeElement); 
packageObj.createChildNode("createStoryNodeElementChildInfo", node_createStoryNodeElementChildInfo); 

})(packageObj);
