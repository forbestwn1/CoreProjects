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

var node_createBasicLayout = function(){
	
	var loc_marginX = 30;
	var loc_marginY = 30;
	var loc_gapX = 20;
	var loc_gapY = 20;
	
	var loc_children = [];
	var loc_childNeededSize = [];
	var loc_totalChildrenWidth = 0; 
	var loc_neededSize;
	
	var loc_calculateNeededSize = function(){
		
		if(loc_children.length()==0){
			return {
				width : 100,
				height : 50
			}
		}
		
		var width = loc_marginX * 2;
		var height = loc_marginY * 2;
		var maxHeight = 0;
		loc_totalChildrenWidth = 0;
    	_.each(loc_children, function(child, i){
    		var size = child.getNeededSize();
    		loc_childNeededSize.push(size);
    		if(size.height>maxHeight)  maxHeight = size.height;
    		if(i>=1)  width = width + loc_gapX;
    		width = width + size.width;
    		loc_totalChildrenWidth = loc_totalChildrenWidth + size.width;
    	});
    	loc_neededSize = {
    		width : width,
    		height : height,
    	};
	};
	
	var loc_updateChildLocation = function(x, y, width, height){
		if(loc_children.length()==0){
			return;
		}
		
		var totalChildrenWidth = width - loc_marginX * 2 - loc_gapX * loc_children.length();
		var childHeight = height - loc_marginY * 2;
		var startX = x + loc_marginX;
    	_.each(loc_children, function(child, i){
    		var neededSize = loc_childNeededSize[i];
    		var childWidth = needeSize.width * totalChildrenWidth / loc_totalChildrenWidth;
    		child.updateLocation(startX, y+loc_marginY, childWidth, childHeight);
    		startX = startX + childWidth + loc_marginX;
    	});
	};
	
	var loc_out = {
		
		addChild : function(child){
			loc_children.push(child);
		},
		
		getNeededSize : function(){
	    	var that = this;
	    	var width = 100;
	    	var height = 40;
	    	var childHeight = 0;
	    	_.each(loc_children, function(child, i){
	    		var size = child.getNeededSize();
	    		width = width + size.width;
	    		if(i>=1) width = width + 30;
	    		if(size.height> childHeight)  childHeight = size.height;
	    	});
	    	return {
	    		width : width,
	    		height : height + childHeight,
	    	}; 
			
		},
		
		setLocation : function(x, y, width, height){
			
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
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});


//Register Node by Name
packageObj.createChildNode("createBasicLayout", node_createBasicLayout); 

})(packageObj);
