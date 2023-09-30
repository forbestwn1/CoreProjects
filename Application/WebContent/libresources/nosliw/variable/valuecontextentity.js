//get/create package
var packageObj = library.getChildPackage("valuecontext");    

(function(packageObj){
//get used node
var node_basicUtility;
var node_namingConvensionUtility;
var node_CONSTANT;
var node_COMMONCONSTANT;
var node_makeObjectWithType;
var node_getObjectType;

//*******************************************   Start Node Definition  ************************************** 	

var node_createValueContextVariableInfo = function(valueStructureRuntimeId, n, p){

	if(node_getObjectType(valueStructureRuntimeId)==node_CONSTANT.TYPEDOBJECT_TYPE_VALUECONTEXTVARIABLE)  return valueStructureRuntimeId;

	var loc_valueStructureRuntimeId;

	var loc_valueStructureVariableInfo;

	var loc_key;

	var loc_init = function(valueStructureRuntimeId, n, p){
		if(n==undefined&&p==undefined){
			var index = valueStructureRuntimeId.indexOf(node_COMMONCONSTANT.SEPERATOR_PATH);
			if(index!=-1){
				loc_valueStructureRuntimeId = valueStructureRuntimeId.substring(0, index);
				loc_valueStructureVariableInfo = node_createValueStructureVariableInfo(valueStructureRuntimeId.substring(index+1));
			}
		}
		else{
			loc_valueStructureRuntimeId = valueStructureRuntimeId;
			loc_valueStructureVariableInfo =  node_createValueStructureVariableInfo(n, p);
		}
		loc_key = node_namingConvensionUtility.cascadePath(loc_valueStructureRuntimeId, loc_valueStructureVariableInfo.key);
	};

	var loc_out = {

		valueStructureRuntimeId : valueStructureRuntimeId,

		valueStructureVariableInfo : loc_valueStructureVariableInfo,

		//key
		key : loc_key,
		
		getFullPath : function(){	return loc_key;	}
	};
	
	loc_init(valueStructureRuntimeId, n, p);
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VALUECONTEXTVARIABLE);
	
	return loc_out;
	
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createValueContextVariableInfo", node_createValueContextVariableInfo); 

})(packageObj);
