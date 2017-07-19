//get/create package
var packageObj = library.getChildPackage("segmentparser");    

(function(packageObj){
	//get used node
	var node_basicUtility;

//*******************************************   Start Node Definition  ************************************** 	


/*
 * parse the path information
 */
var parseSegment = function(path, sep){
	
	var loc_segments = [];
	var loc_seperator = undefined;
	var loc_isEmpty = false;
	var loc_index = 0;

	var seperator = sep;
	if(node_basicUtility.isStringEmpty(seperator))   seperator = NOSLIWCOMMONCONSTANT.CONS_SEPERATOR_PATH; 
	if(node_basicUtility.isStringEmpty(path))  loc_isEmpty = true;
	else{
		loc_seperator = seperator;
//		if(seperator==".")  seperator="\\.";
		loc_segments = path.split(seperator);
		loc_index = 0;
	}
		
	var loc_out = {
		isEmpty : function(){return loc_isEmpty;},
		
		next : function(){
			if(this.isEmpty())   return undefined;
			var out = loc_segments[loc_index];
			loc_index++;
			return out;
		},
	
		hasNext : function(){
			if(this.isEmpty())  return false;
			if(loc_index>=this.getSegmentSize())  return false;
			return true;
		},
		
		getSegmentSize : function(){
			if(this.isEmpty())  return 0;
			return loc_segments.length;
		},
		
		getSegments : function(){
			if(this.isEmpty())  return [];
			return loc_segments;
		},
		
		getRestPath : function(){
			if(this.isEmpty())  return undefined;
			var out = "";
			for(var i=loc_index; i<this.getSegmentSize(); i++){
				if(i!=loc_index)  out = out + loc_seperator;
				out = out + loc_segments[i];
			}
			return out;
		},
		
		getPreviousPath : function(){
			if(this.isEmpty())  return undefined;
			var out = "";
			for(var i=0; i<loc_index; i++){
				if(i!=0)  out = out+ loc_seperator;
				out = out + loc_segments[i];
			}
			return out;
		}
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("parseSegment", parseSegment); 

	var module = {
		start : function(packageObj){
			node_basicUtility = packageObj.getNodeData("common.utility.basicUtility");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
