//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONTRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_namingConvensionUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = 
{
		buildResourceTree : function(tree, resource){
			var resourceId = resource.resourceInfo[node_COMMONTRIBUTECONSTANT.RESOURCEINFO_ID];
			var type = resourceId[node_COMMONTRIBUTECONSTANT.RESOURCEID_TYPE];
			var id = resourceId[node_COMMONTRIBUTECONSTANT.RESOURCEID_ID];
			var typeResources = tree[type];
			if(typeResources==undefined){
				typeResources = {};
				tree[type] = typeResources;
			}
			typeResources[id] = resource; 
		},

		getResourceFromTree : function(tree, resourceId){
			var type = resourceId[node_COMMONTRIBUTECONSTANT.RESOURCEID_TYPE];
			var id = resourceId[node_COMMONTRIBUTECONSTANT.RESOURCEID_ID];
			var typeResources = tree[type];
			if(typeResources==undefined)  return undefined;
			return typeResources[id];
		},
		
		createOperationResourceId : function(dataTypeId, operation){
			var out = {};
			out[node_COMMONTRIBUTECONSTANT.RESOURCEID_ID] = node_namingConvensionUtility.cascadeLevel1(dataTypeId, operation); 
			out[node_COMMONTRIBUTECONSTANT.RESOURCEID_TYPE] = node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_OPERATION; 
			return out;
		},

		createConverterToResourceId : function(dataTypeId){
			var out = {};
			out[node_COMMONTRIBUTECONSTANT.RESOURCEID_ID] = node_namingConvensionUtility.cascadeLevel1(dataTypeId, node_COMMONCONSTANT.DATAOPERATION_TYPE_CONVERTTO); 
			out[node_COMMONTRIBUTECONSTANT.RESOURCEID_TYPE] = node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CONVERTER; 
			return out;
		},
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONTRIBUTECONSTANT", function(){node_COMMONTRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
