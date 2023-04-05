//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createTestSimple1Plugin = function(){
	
	var loc_out = {

		createEntity : function(entityDef){
			return node_createTestSimple1(entityDef);
		},
			
	};

	return loc_out;
};

	
var node_createTestSimple1 = function(testSimple1Def){
	
	var loc_coreObject;
	
	var loc_init = function(testSimple1Def){
		var scriptFun = testSimple1Def.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTSIMPLE1_SCRIPT);
		var scriptParms = testSimple1Def.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTSIMPLE1_PARM);
		loc_coreObject = scriptFun(scriptParms);
	};
	
	var loc_out = {
		
		callBack : function(){
			if(loc_coreObject.callBack!=undefined){
				return loc_coreObject.callBack.apply(loc_coreObject, arguments);
			}
		},
			
	};
	
	loc_init(testSimple1Def);
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createTestSimple1Plugin", node_createTestSimple1Plugin); 

})(packageObj);
