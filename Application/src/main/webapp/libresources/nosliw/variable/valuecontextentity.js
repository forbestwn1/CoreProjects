//get/create package
var packageObj = library.getChildPackage("valuecontext");    

(function(packageObj){
//get used node
var node_basicUtility;
var node_namingConvensionUtility;
var node_CONSTANT;
var node_COMMONCONSTANT;
var node_COMMONATRIBUTECONSTANT;
var node_makeObjectWithType;
var node_getObjectType;
var node_createValueStructureVariableInfo;

//*******************************************   Start Node Definition  ************************************** 	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valuestructure.createValueStructureVariableInfo", function(){node_createValueStructureVariableInfo = this.getData();});

//Register Node by Name

})(packageObj);
