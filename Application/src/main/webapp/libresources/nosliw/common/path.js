//get/create package
var packageObj = library.getChildPackage("path");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_buildInterface;
	var node_getInterface;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_basicUtility;
	var node_namingConvensionUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_pathUtility = function(){
	
	
	var loc_out = {

        isEmptyPath : function(path){
			if(path==undefined||path=="") return true;
			return false;
		},
        
		/**
		 * compare two path
		 * if path1 equals path2, then 0
		 * if path1 contains path2, then 1
		 * if path2 contains path1, then 2
		 * otherwise, -1
		 */
		comparePath : function(path1, path2){
			var compare = 0;
			var result = "";
			var p1 = node_basicUtility.emptyStringIfUndefined(path1)+"";
			var p2 = node_basicUtility.emptyStringIfUndefined(path2)+"";
			if(p1==p2){
				compare = 0;
				result = p1; 
			}
			else if(node_basicUtility.isStringEmpty(p1)){
				compare = 2;
				result = p2;
			}
			else if(node_basicUtility.isStringEmpty(p2)){
				compare = 1;
				result = p1;
			}
			else if(p1.startsWith(p2+".")){
				compare = 1;
				result = p1.substring((p2+".").length);
			}
			else if(p2.startsWith(p1+".")){
				compare = 2;
				result = p2.substring((p1+".").length);
			}
			else{
				compare = -1;
				result = undefined;
			}
			return {
				compare : compare,
				subPath : result
			};
		},
		
		combinePath : function(){
			var out = "";
			_.each(arguments, function(seg, index){
				out = node_namingConvensionUtility.cascadePath(out, seg);
			});
			return out;
		},
		
	}
	
	return loc_out;
}();


//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("pathUtility", node_pathUtility); 

})(packageObj);
