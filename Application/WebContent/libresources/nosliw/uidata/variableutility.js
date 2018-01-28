//get/create package
var packageObj = library.getChildPackage("variable");    

(function(packageObj){
//get used node
var node_getObjectType;
var node_makeObjectWithType;
var node_CONSTANT;	
var node_createData;
var node_namingConvensionUtility;
var node_basicUtility;
//*******************************************   Start Node Definition  ************************************** 	
/**
 * 
 */
var node_utility = function(){
	
	return {
		
		getVariableInfoFromRoot : function(relativeVariableInfo){

			var parent = relativeVariableInfo.parent; 
			if(parent.isBase()){
				return relativeVariableInfo;
			}
			else{
				var relativeInfo = parent.getRelativeInfo();
				return this.getVariableInfoFromRoot(new node_RelativeEntityInfo(relativeInfo.parent, node_dataUtility.combinePath(relativeInfo.path,relativeVariableInfo.path)));
			}
		},
	};	
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.entity.createData", function(){node_createData = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("variable", node_utility); 

})(packageObj);
