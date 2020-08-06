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

var node_createBasicLayout = function(){
	
	var loc_marginX = 30;
	var loc_marginY = 30;
	var loc_gapX = 20;
	var loc_gapY = 20;
	
	var loc_calculated = false;
	var loc_children = [];
	var loc_childNeededSize = [];
	var loc_totalChildrenWidth = 0; 
	var loc_neededSize;
	
	var loc_x;
	var loc_y;
	var loc_width;
	var loc_height;
	
	var loc_calculateNeededSize = function(){
		if(loc_children.length==0){
			loc_neededSize = {
				width : 100,
				height : 50
			}
		}
		else{
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
	    		height : height + maxHeight,
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
		
		var totalChildrenWidth = width - loc_marginX * 2 - loc_gapX * (loc_children.length-1);
		var childHeight = height - loc_marginY * 2;
		var startX = x + loc_marginX;
    	_.each(loc_children, function(child, i){
    		var neededSize = loc_childNeededSize[i];
    		var childWidth = neededSize.width * totalChildrenWidth / loc_totalChildrenWidth;
    		child.setLocation(startX, y+loc_marginY, childWidth, childHeight);
    		startX = startX + childWidth + loc_marginX;
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
packageObj.createChildNode("createBasicLayout", node_createBasicLayout); 

})(packageObj);
