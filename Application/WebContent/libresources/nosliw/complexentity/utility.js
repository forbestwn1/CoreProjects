//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_basicUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_complexEntityUtility = {
	
	getAttributeType : function(attributeObj){
		return attributeObj[node_COMMONATRIBUTECONSTANT.ATTRIBUTEENTITY_ENTITYTYPE];
	},

	getConfigureRequest : function(configure, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		if(typeof configure === 'object'){
			loc_configure = node_createConfigure(configure, undefined, loc_configureParms);
		}
		else if(typeof configure === 'string'){
			var configureName = configure;
			var settingName;
			var index = configure.indexOf("_");
			if(index!=-1){
				configureName = configure.subString(0, index);
				settingName = configure.subString(index+1);
			}
			
			var configureResourceId = new node_ResourceId(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CONFIGURE, configureName);
			out.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(configureResourceId, {
				success : function(requestInfo, resourceTree){
					loc_configure = node_resourceUtility.getResourceFromTree(resourceTree, configureResourceId).resourceData[node_COMMONATRIBUTECONSTANT.EXECUTABLECONFIGURE_SCRIPT];
					if(settingName!=undefined)   loc_configure = loc_configure[settingName];
				}
			}));
		}
		return out;
	},
	
};

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("complexEntityUtility", node_complexEntityUtility); 

})(packageObj);
