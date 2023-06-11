//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_buildComponentInterface;
	var node_ServiceInfo;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createScriptBasedPlugin = function(){
	
	var loc_out = {
		getCreateComplexEntityCoreRequest : function(complexEntityDef, valueContextId, bundleCore, configure, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createScriptBasedCoreEntity"), handlers, request);

			var resourceId = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYSCRIPTBASED_RESOURCEID);
			if(false){//resourceId!=undefined&&resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_STRUCUTRE]==node_COMMONCONSTANT.RESOURCEID_TYPE_SIMPLE){
				//load script as resource
				out.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(resourceId, {
					success : function(requestInfo, resourceTree){
						var scriptFun = node_resourceUtility.getResourceFromTree(resourceTree, resourceId).resourceData[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPT_SCRIPT];
						return scriptFun(complexEntityDef, valueContextId, bundleCore, configure);
		 			}
				}));
			}
			else{
				//run script
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
					var scriptFun = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYSCRIPTBASED_SCRIPT);
					return scriptFun(complexEntityDef, valueContextId, bundleCore, configure);
				}));
			}
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createScriptBasedPlugin", node_createScriptBasedPlugin); 

})(packageObj);
