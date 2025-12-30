//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
var node_basicUtility;	
var node_namingConvensionUtility;
//*******************************************   Start Node Definition  ************************************** 	

//entity to describe relative variable : parent + path to parent
var node_createPathToBaseVariableInfo = function(){
	
	var loc_segments = [];
	
	var loc_path = "";
	var loc_baseVariable;
	
	var loc_out = {
		
		addSegment : function(segmentInfo){
			loc_segments.push(segmentInfo);
			if(segmentInfo.baseVariable!=undefined){
				loc_baseVariable = segmentInfo.baseVariable;
			}
			if(!node_basicUtility.isStringEmpty(segmentInfo.pathToParent)){
				loc_path = node_namingConvensionUtility.cascadePath(loc_path, segmentInfo.pathToParent, false);
			}
		},
		
		getSegments : function(){
			return loc_segments;
		},
		
		getPath : function(){
			return loc_path;
		},
		
		getBaseVariable : function(){
			return loc_baseVariable;
		}
		
	};
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createPathToBaseVariableInfo", node_createPathToBaseVariableInfo); 

})(packageObj);
