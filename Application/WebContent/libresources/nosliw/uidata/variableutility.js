//get/create package
var packageObj = library.getChildPackage("variable");    

(function(packageObj){
//get used node
var node_RelativeEntityInfo;
var node_dataUtility;
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
nosliw.registerSetNodeDataEvent("uidata.entity.RelativeEntityInfo", function(){node_RelativeEntityInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
