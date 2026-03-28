//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_basicUtility;
	var node_getObjectType;
	var node_getObjectId;
	var node_ResourceId;
	var node_resourceUtility;
	var node_createConfigure;
	var node_getEntityTreeNodeInterface;
	var node_namingConvensionUtility;
	var loc_createCoreEntityPackage;
	var node_createReferenceCoreEntity;
	var node_pathUtility
    var node_createAdapterInfo;

//*******************************************   Start Node Definition  ************************************** 	

var node_configureUtility = function(){

	var loc_out = {

		getRootConfigureRequest : function(configure, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
	
			var getConfigureValueRequest = node_createServiceRequestInfoSequence(undefined, {
				success : function(request, configureObject){
					
					var configureValue = configureObject==undefined?undefined:configureObject[node_basicUtility.buildNosliwFullName(node_CONSTANT.CONFIGURE_ROOT_VALUE)];
					var configureGlobal;
					var configureParms;
					
					if(configureValue!=undefined){
						configureGlobal = configureObject[node_basicUtility.buildNosliwFullName(node_CONSTANT.CONFIGURE_ROOT_GLOBAL)];
						configureParms = configureObject[node_basicUtility.buildNosliwFullName(node_CONSTANT.CONFIGURE_ROOT_PARM)];
					}
					else{
						configureValue = configureObject;
					}
					
					return node_createConfigure(configureValue, configureGlobal, configureParms);
				}
			});
			
			if(typeof configure === 'object'){
				getConfigureValueRequest.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
					return configure;
				}));
			}
			else if(typeof configure === 'string'){
				var configureName = configure;
				var settingName;
				var index = configure.indexOf("-");
				if(index!=-1){
					configureName = configure.substring(0, index);
					settingName = configure.substring(index+1);
				}
				
				var configureResourceId = new node_ResourceId(configureName, node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CONFIGURE, "1.0.0");
				getConfigureValueRequest.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(configureResourceId, {
					success : function(requestInfo, resourceTree){
						var configureValue = node_resourceUtility.getResourceFromTree(resourceTree, configureResourceId).resourceData[node_COMMONATRIBUTECONSTANT.RESOURCEDATACONFIGURE_CONFIGURE];
						if(settingName!=undefined)   configureValue = configureValue[settingName];
						return configureValue;
					}
				}));
			}
			out.addRequest(getConfigureValueRequest);
			return out;
		},
		
	};

	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectId", function(){node_getObjectId = this.getData();});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){	node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("configure.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.entity.createCoreEntityPackage", function(){node_createCoreEntityPackage = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.entity.createReferenceCoreEntity", function(){node_createReferenceCoreEntity = this.getData();});
nosliw.registerSetNodeDataEvent("common.path.pathUtility", function(){node_pathUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.entity.createAdapterInfo", function(){node_createAdapterInfo = this.getData();});


//Register Node by Name
packageObj.createChildNode("configureUtility", node_configureUtility); 

})(packageObj);
