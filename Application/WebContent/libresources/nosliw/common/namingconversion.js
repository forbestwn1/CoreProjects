//get/create package
var packageObj = library.getChildPackage("namingconvension");    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

var basicUtility = function(){
	
	return {
		cascadePath : function(path1, path2, normal){
			return this.cascadeParts(path1, path2, NOSLIWCOMMONCONSTANT.CONS_SEPERATOR_PATH, normal);
		},

		cascadePart : function(part1, part2, normal){
			return this.cascadeParts(part1, part2, NOSLIWCOMMONCONSTANT.CONS_SEPERATOR_PART, normal);
		},
		
		cascadeParts : function(part1, part2, seperator, normal){
			//if normal, just put together
			if(normal==true)  return part1 + seperator + part2;
			
			//otherwise, do smart way
			var out;
			if(nosliwCommonUtility.isStringEmpty(part1)){
				out = part2;
			}
			else{
				if(nosliwCommonUtility.isStringEmpty(part2))  out = part1;
				else	out = part1 + seperator + part2;
			}
			return out;
		},

		/*
		 * get all sub path from full path
		 */
		parsePathInfos : function(fullPath){
			return fullPath.split(NOSLIWCOMMONCONSTANT.CONS_SEPERATOR_PATH);
		},
		
		/*
		 * get all sub path from full path
		 */
		parseDetailInfos : function(details){
			return details.split(NOSLIWCOMMONCONSTANT.CONS_SEPERATOR_DETAIL);
		},
		
	};
}();

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("basicUtility", basicUtility); 

var module = {
	start : function(packageObj){
	}
};
nosliw.registerModule(module, packageObj);
})(packageObj);
