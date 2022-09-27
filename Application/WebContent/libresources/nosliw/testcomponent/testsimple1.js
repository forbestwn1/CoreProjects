//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createConfigure;
	var node_createErrorData;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createTestSimple1 = function(testSimple1Def){
	
	var loc_coreObject;
	
	var loc_init = function(testSimple1Def){
		var attributes = testSimple1Def[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITY_ATTRIBUTE];
		var scriptFun = attributes[node_COMMONATRIBUTECONSTANT.EXECUTABLETESTSIMPLE1_SCRIPT];
		var scriptParms = attributes[node_COMMONATRIBUTECONSTANT.EXECUTABLETESTSIMPLE1_PARM];
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("error.entity.createErrorData", function(){node_createErrorData = this.getData();});

//Register Node by Name
packageObj.createChildNode("createTestSimple1", node_createTestSimple1); 

})(packageObj);
