//get/create package
var packageObj = library.getChildPackage("namingconvension");    

(function(packageObj){
	//get used node
	var node_basicUtility;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_namingConvensionUtility = function(){
	
	return {
		cascadeLevel1 : function(seg1, seg2, normal){
			return this.cascadeParts(seg1, seg2, node_COMMONCONSTANT.SEPERATOR_LEVEL1, normal);
		},

		cascadePath : function(path1, path2, normal){
			return this.cascadeParts(path1, path2, node_COMMONCONSTANT.SEPERATOR_PATH, normal);
		},

		cascadePart : function(part1, part2, normal){
			return this.cascadeParts(part1, part2, node_COMMONCONSTANT.SEPERATOR_PART, normal);
		},
		
		cascadeParts : function(part1, part2, seperator, normal){
			//if normal, just put together
			if(normal==true)  return part1 + seperator + part2;
			
			//otherwise, do smart way
			var out;
			if(node_basicUtility.isStringEmpty(part1)){
				out = part2;
			}
			else{
				if(node_basicUtility.isStringEmpty(part2))  out = part1;
				else	out = part1 + seperator + part2;
			}
			return out;
		},

		parseLevel1 : function(fullPath){
			return fullPath.split(node_COMMONCONSTANT.SEPERATOR_LEVEL1);
		},

		parseLevel2 : function(fullPath){
			return fullPath.split(node_COMMONCONSTANT.SEPERATOR_LEVEL2);
		},

		/*
		 * get all sub path from full path
		 */
		parsePathInfos : function(fullPath){
			return fullPath.split(node_COMMONCONSTANT.SEPERATOR_PATH);
		},
		
		/*
		 * get all sub path from full path
		 */
		parseDetailInfos : function(details){
			return details.split(node_COMMONCONSTANT.SEPERATOR_DETAIL);
		},
		
	};
}();

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("namingConvensionUtility", node_namingConvensionUtility); 

})(packageObj);
