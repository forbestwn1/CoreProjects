//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_namingConvensionUtility;
	var node_ResourceId;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = 
{
		buildResourceTree : function(tree, resource){
			var resourceId = resource.resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_ID];
			var typeId = resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_RESOURCETYPEID];
			var type = typeId[node_COMMONATRIBUTECONSTANT.IDRESOURCETYPE_RESOURCETYPE];
			var version = typeId[node_COMMONATRIBUTECONSTANT.IDRESOURCETYPE_VERSION];
			var id = resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID];
			var typeResources = tree[type];
			if(typeResources==undefined){
				typeResources = {};
				tree[type] = typeResources;
			}
			var versionResource = typeResources[version];
			if(versionResource==undefined){
				versionResource = {};
				typeResources[version] = versionResource;
			}
			versionResource[id] = resource; 
		},

		getResourceFromTree : function(tree, resourceId){
			var typeId = resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_RESOURCETYPEID];
			var type = typeId[node_COMMONATRIBUTECONSTANT.IDRESOURCETYPE_RESOURCETYPE];
			var version = typeId[node_COMMONATRIBUTECONSTANT.IDRESOURCETYPE_VERSION];
			var id = resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID];
			var typeResources = tree[type];
			if(typeResources==undefined)  return undefined;
			var versionResources = typeResources[version];
			if(versionResources==undefined)  return;
			return versionResources[id];
		},
		
		getResourcesByTypeFromTree : function(tree, resourceType){
			var typeResources = tree[resourceType];
			return typeResources;
		},
		
		createOperationResourceId : function(dataTypeId, operation){
			return new node_ResourceId(node_namingConvensionUtility.cascadeLevel1(dataTypeId, operation), node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_OPERATION+";1.0.0");
		},

		createConverterResourceId : function(dataTypeId){
			return new node_ResourceId(node_namingConvensionUtility.cascadeLevel1(dataTypeId), node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CONVERTER+";1.0.0");
		},
		
		buildReourceCoreIdLiterate : function(id){
			var out = id;

			if(!id.startsWith(node_COMMONCONSTANT.RESOURCEID_LITERATE_STARTER_SIMPLE)){
				out = node_COMMONCONSTANT.RESOURCEID_LITERATE_STARTER_SIMPLE+id;
			}
			
//			if(!id.startsWith(node_COMMONCONSTANT.SEPERATOR_RESOURCEID_START)){
//				out = node_COMMONCONSTANT.SEPERATOR_RESOURCEID_START+node_COMMONCONSTANT.RESOURCEID_TYPE_SIMPLE+node_COMMONCONSTANT.SEPERATOR_RESOURCEID_STRUCTURE+id;
//			}

			return out;
		}
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){node_ResourceId = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
