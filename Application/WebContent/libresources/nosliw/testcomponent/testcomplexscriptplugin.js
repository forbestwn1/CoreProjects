//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildComponentCore;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createTestComplexScriptPlugin = function(){
	
	var loc_out = {

		createComplexEntityCore : function(complexEntityDef, variableGroupId, bundleCore, configure){
			return loc_createTestComplexScript(complexEntityDef, variableGroupId, bundleCore, configure);
		},

	};

	return loc_out;
};

	
var loc_createTestComplexScript = function(testComplexScriptDef, variableGroupId, bundleCore, configure){
	
	var loc_coreObject;
	
	var loc_init = function(testComplexScriptDef){
		var scriptFun = testComplexScriptDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTSIMPLE1_SCRIPT);
		var scriptParms = testComplexScriptDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTSIMPLE1_PARM);
		loc_coreObject = scriptFun(scriptParms, configure);
	};
	
	loc_init(testComplexScriptDef);
	return 	node_buildComponentCore(loc_coreObject, false);
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentCore = this.getData();});

//Register Node by Name
packageObj.createChildNode("createTestComplexScriptPlugin", node_createTestComplexScriptPlugin); 

})(packageObj);
