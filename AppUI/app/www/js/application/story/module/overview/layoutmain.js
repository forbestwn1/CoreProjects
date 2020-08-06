/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createMainLayout = function(){
	
	var loc_marginX = 30;
	var loc_marginY = 30;
	var loc_gapX = 20;
	var loc_gapY = 20;
	
	var loc_calculated = false;
	var loc_children = [];
	var loc_childNeededSize = [];
	var loc_totalChildrenHeight = 0; 
	var loc_neededSize;
	
	var loc_x;
	var loc_y;
	var loc_width;
	var loc_height;
	
	var loc_calculateNeededSize = function(){
		if(loc_children.length==0){
			loc_neededSize = {
				width : 1000,
				height : 500
			}
		}
		else{
			var width = loc_marginX * 2;
			var height = loc_marginY * 2;
			var maxWidth = 0;
			loc_totalChildrenHeight = 0;
	    	_.each(loc_children, function(child, i){
	    		var size = child.getNeededSize();
	    		loc_childNeededSize.push(size);
	    		if(size.width>maxWidth)  maxWidth = size.width;
	    		if(i>=1)  height = height + loc_gapY;
	    		height = height + size.height;
	    		loc_totalChildrenHeight = loc_totalChildrenHeight + size.height;
	    	});
	    	loc_neededSize = {
	    		width : width + maxWidth,
	    		height : height,
	    	};
		}
		loc_calculated = true;
		return loc_neededSize;
	};
	
	var loc_updateChildLocation = function(x, y, width, height){
		if(loc_calculated==false)  loc_calculateNeededSize();
		
		if(loc_children.length==0){
			return;
		}
		
		var totalChildrenHeight = height - loc_marginY * 2 - loc_gapY * (loc_children.length-1);
		var childWidth = width - loc_marginX * 2;
		var startY = y + loc_marginY;
    	_.each(loc_children, function(child, i){
    		var neededSize = loc_childNeededSize[i];
    		var childHeight = neededSize.height * totalChildrenHeight / loc_totalChildrenHeight;
    		child.setLocation(x+loc_marginX, startY, childWidth, childHeight);
    		startY = startY + childHeight + loc_marginY;
    	});
	};
	
	var loc_out = {
		
		addChild : function(child){
			loc_children.push(child);
		},
		
		getChildren : function(){  return loc_children;   },
		
		getNeededSize : function(){
			if(loc_calculated==false)  loc_calculateNeededSize();
			return loc_neededSize;
		},
		
		setLocation : function(x, y, width, height){
			loc_x = x;
			loc_y = y;
			loc_width = width;
			loc_height = height;
			loc_updateChildLocation(x, y, width, height);
			return this.getLocation();
		},

		getLocation : function(){
			return {
				x : loc_x,
				y : loc_y,
				width : loc_width,
				height : loc_height,
			};
		},
	};
	
	return loc_out;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createMainLayout", node_createMainLayout); 

})(packageObj);
