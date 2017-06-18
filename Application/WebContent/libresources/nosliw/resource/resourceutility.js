//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONTRIBUTECONSTANT;
	var node_Resource;
//*******************************************   Start Node Definition  ************************************** 	

var node_resourceUtility = 
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
		}
		
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("resourceUtility", node_resourceUtility); 

	var module = {
		start : function(packageObj){
			node_COMMONTRIBUTECONSTANT = packageObj.getNodeData("constant.COMMONTRIBUTECONSTANT");
			node_Resource = packageObj.getNodeData("resource.entity.Resource");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
