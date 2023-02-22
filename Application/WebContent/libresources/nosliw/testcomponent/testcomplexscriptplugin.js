//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildComponentCore;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_ResourceId;
	var node_resourceUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createTestComplexScriptPlugin = function(){
	
	var loc_out = {

		getCreateComplexEntityCoreRequest : function(complexEntityDef, valueContextId, bundleCore, configure, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createScriptCoreEntity"), handlers, request);

			var scriptName = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTCOMPLEXSCRIPT_SCRIPTNAME);
			var resourceId = new node_ResourceId(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_SCRIPT, scriptName);
			
			out.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(resourceId, {
				success : function(requestInfo, resourceTree){
					var scriptFun = node_resourceUtility.getResourceFromTree(resourceTree, resourceId).resourceData[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPT_SCRIPT];
					return scriptFun(complexEntityDef, valueContextId, bundleCore, configure);					
	 			}
			}));
			
			return out;
		},
	};

	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentCore = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createTestComplexScriptPlugin", node_createTestComplexScriptPlugin); 

})(packageObj);
