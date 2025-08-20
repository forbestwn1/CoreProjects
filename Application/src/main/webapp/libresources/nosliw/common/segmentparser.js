//get/create package
var packageObj = library.getChildPackage("segmentparser");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_basicUtility;
	var node_makeObjectWithType;

//*******************************************   Start Node Definition  ************************************** 	

var parsePathSegment = function(path, first, lastReverse){
	return parseSegment(path, undefined, first, lastReverse);
};
	
/*
 * parse the path information
 */
var parseSegment = function(path, sep, first, lastReverse){
	
	var loc_segments = [];
	var loc_seperator = undefined;
	var loc_isEmpty = false;
	var loc_index = 0;

	//start and end index to iterate
	var loc_firstIndex = first;
	var loc_lastIndex ;
	
	var seperator = sep;
	if(node_basicUtility.isStringEmpty(seperator))   seperator = node_COMMONCONSTANT.SEPERATOR_PATH; 
	if(node_basicUtility.isStringEmpty(path))  loc_isEmpty = true;
	else{
		loc_seperator = seperator;
		loc_segments = path.split(seperator);
		
		if(loc_firstIndex==undefined)   loc_firstIndex = 0;
		
		if(lastReverse==undefined)   loc_lastIndex = loc_segments.length-1;
		else  loc_lastIndex = loc_segments.length-1-lastReverse;
		loc_index = loc_firstIndex;
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
			if(loc_index>loc_lastIndex)  return false;
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
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_PATHSEGMENT);
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});

//Register Node by Name
packageObj.createChildNode("parseSegment", parseSegment); 
packageObj.createChildNode("parsePathSegment", parsePathSegment); 
	
})(packageObj);
