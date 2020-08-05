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

var node_ChildInfo = function(nodeElement, connectionId){
	this.nodeElement = nodeElement;
	this.connectionId = connectionId;
	return this;
};
	
var node_createStoryNodeElement = function(storyNodeId, module){
	
	var loc_storyNodeId = storyNodeId;
	var loc_module = module;
	
	var loc_layer = 1;
	var loc_children = [];
	
	var loc_width = 100;
	var loc_height = 40;
	
	var loc_x;
	var loc_y;
	
	var loc_graphElement;
	
    var loc_createElement = function(){
        var element = new joint.shapes.standard.Rectangle();
        element.position(loc_x, loc_y);
        element.resize(loc_width, loc_height);
        element.attr({
            body: {
                fill: node_storyOverviewUtility.getColorByLayer(loc_layer),
            },
            label: {
                text: loc_storyNodeId,
                fill: 'white'
            }
        });
    	return element
    };
    
	var loc_out = {
		
		getStoryNodeId : function(){	return loc_storyNodeId;	},
	
		addChild : function(nodeElement, connectionId){
			var childInfo = new node_ChildInfo(nodeElement, connectionId);
			loc_children.push(childInfo);
			node_storyOverviewUtility.updateChild(childInfo, parentNode);
		},
		
		getChildren : function(){   return loc_children;   },
		
		setLayer : function(layer){    loc_layer = layer;    },
		
		getNeedSize : function(){
			return node_storyOverviewUtility.getNeedSize(this);
		},
		
		setSize : function(width, height){  
			loc_width = width;
			loc_height = height;
		},
		
		setPosition : function(x, y){
			loc_x = x;
			loc_y = y;
		},
		
		getElement : function(){
			if(loc_graphElement==undefined){
				loc_graphElement = loc_createElement();
			}
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
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});

//Register Node by Name
packageObj.createChildNode("createStoryNodeElement", node_createStoryNodeElement); 

})(packageObj);
