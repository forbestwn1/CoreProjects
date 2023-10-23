//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
var node_basicUtility;	
//*******************************************   Start Node Definition  ************************************** 	

var node_createVariableDefinitionId = function(valueStructureRuntimeId, rootName, elePath){
	
	var loc_valueStructureRuntimeId;
	var loc_rootName;
	var loc_elePath;
	
	var loc_out = {
		
		getValueStructureRuntimeId : function(){   return loc_valueStructureRuntimeId;   },
		
		getRootName : function(){   return loc_rootName;     },
		
		getElementPath : function(){   return loc_elePath;    },
		
		getKey : function(){         }
	};
	return loc_out;
};


//entity to describe relative variable : parent + path to parent
var node_RelativeEntityInfo = function(parent, path){
	this.parent = parent;
	this.path = node_basicUtility.emptyStringIfUndefined(path);
	return this;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("RelativeEntityInfo", node_RelativeEntityInfo); 

})(packageObj);
