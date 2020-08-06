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
//*******************************************   Start Node Definition  ************************************** 	

var node_createGroupLayer = function(name, module){
	var loc_name = name;
	var loc_module = module;

	var loc_layer = 0;
	var loc_layout = node_createBasicLayout();
	
    var loc_graphElement;
	
    var loc_createElement = function(){
        var element = new joint.shapes.standard.Rectangle();
        var location = loc_layout.getLocation();
        element.position(location.x, location.y);
        element.resize(location.width, location.height);
        element.attr({
            body: {
                fill: node_storyOverviewUtility.getColorByLayer(loc_layer),
            },
            label: {
                text: loc_name,
                fill: 'white'
            }
        });
    	return element
    };

    
	var loc_out = {
		
		addChild : function(child){		loc_layout.addChild(child);		},

		getChildren : function(){   return loc_layout.getChildren();   },
		
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
		}	
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
nosliw.registerSetNodeDataEvent("application.story.module.overview.createBasicLayout", function(){ node_createBasicLayout = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.overview.storyOverviewUtility", function(){ node_storyOverviewUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createGroupLayer", node_createGroupLayer); 

})(packageObj);
