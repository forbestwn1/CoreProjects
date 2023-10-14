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
	var node_ResourceId;
	var node_resourceUtility;
	var node_createConfigure;
	var node_getEntityTreeNodeInterface;

//*******************************************   Start Node Definition  ************************************** 	

var node_complexEntityUtility = {
	
	getComplexCoreEntity : function(parm){
		var dataType = node_getObjectType(parm);
		if(dataType==node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTRUNTIME){
			return this.getComplexCoreEntity(parm.getCoreEntity());
		}
		else if(dataType==node_CONSTANT.TYPEDOBJECT_TYPE_APPLICATION){
			return this.getComplexCoreEntity(parm.getPackageRuntime());
		}
		else if(dataType==node_CONSTANT.TYPEDOBJECT_TYPE_PACKAGE){
			return this.getComplexCoreEntity(parm.getMainBundleRuntime());
		}
		else if(dataType==node_CONSTANT.TYPEDOBJECT_TYPE_BUNDLE){
			return this.getComplexCoreEntity(parm.getMainEntityRuntime());
		}
		else{
			return parm;
		}
	},
	
	getAttributeAdapterExecuteRequest : function(parentCoreEntity, attrName, adapterName, handlers, request){
		var attrNode = node_getEntityTreeNodeInterface(parentCoreEntity).getChild(attrName);
		var adapter = attrNode.getAdapters()[adapterName!=undefined?adapterName:node_COMMONCONSTANT.NAME_DEFAULT];
		return this.getAdapterExecuteRequest(parentCoreEntity, attrNode.getChildValue(), adapter, handlers, request);
	},	
	
	getAdapterExecuteRequest : function(parentCoreEntity, childRuntime, adapter, handlers, request){
		var childInput;
		var childCore = childRuntime.getCoreEntity==undefined?undefined:childRuntime.getCoreEntity();
		if(childCore==undefined)   childInput = childRuntime;
		else{
			var childCoreType = node_getObjectType(childCore);
			if(childCoreType==node_CONSTANT.TYPEDOBJECT_TYPE_BUNDLE){
				childInput = childCore.getMainEntity();
			}
			else{
				childInput = childRuntime;
			}
		}
		
		return adapter.getExecuteRequest(parentCoreEntity, childInput, handlers, request);
	},

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
			
			var configureResourceId = new node_ResourceId(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CONFIGURE, configureName);
			getConfigureValueRequest.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(configureResourceId, {
				success : function(requestInfo, resourceTree){
					var configureValue = node_resourceUtility.getResourceFromTree(resourceTree, configureResourceId).resourceData[node_COMMONATRIBUTECONSTANT.EXECUTABLECONFIGURE_SCRIPT];
					if(settingName!=undefined)   configureValue = configureValue[settingName];
					return configureValue;
				}
			}));
		}
		out.addRequest(getConfigureValueRequest);
		return out;
	},
	
};

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){	node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("complexEntityUtility", node_complexEntityUtility); 

})(packageObj);
