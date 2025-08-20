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

var node_createNodeElement = function(storyNodeId, module){
	
	var loc_storyNodeId = storyNodeId;
	var loc_module = module;
	
    var loc_element = new joint.shapes.standard.Rectangle();
    loc_element.position(100, 30);
    loc_element.resize(100, 40);
    loc_element.attr({
        body: {
            fill: 'blue'
        },
        label: {
            text: loc_storyNodeId,
            fill: 'white'
        }
    });
	
	var loc_out = {
		
		getId : function(){
			return loc_storyNodeId;
		},
		
		getElement : function(){
			return loc_element;
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


//Register Node by Name
packageObj.createChildNode("createNodeElement", node_createNodeElement); 

})(packageObj);
