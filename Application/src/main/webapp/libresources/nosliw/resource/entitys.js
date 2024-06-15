//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_resourceUtility;
	var node_namingConvensionUtility;
	var node_basicUtility
//*******************************************   Start Node Definition  ************************************** 	

var node_ResourceId = function(id, typeParm1, typeParm2){
	
	if(!node_basicUtility.isStringValue(id)){
		this[node_COMMONATRIBUTECONSTANT.RESOURCEID_RESOURCETYPEID] = id[node_COMMONATRIBUTECONSTANT.RESOURCEID_RESOURCETYPEID];
		this[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = id[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID];
	}
	else{
		this[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = id;
		this.name = id;

		var resourceTypeId = {};
		if(typeParm2!=undefined){
			resourceTypeId[node_COMMONATRIBUTECONSTANT.IDRESOURCETYPE_RESOURCETYPE] = typeParm1;
			resourceTypeId[node_COMMONATRIBUTECONSTANT.IDRESOURCETYPE_VERSION] = typeParm2;
		}
		else if(node_basicUtility.isStringValue(typeParm1)){
			var typeSegs = node_namingConvensionUtility.parseLevel1(typeParm1);
			resourceTypeId[node_COMMONATRIBUTECONSTANT.IDRESOURCETYPE_RESOURCETYPE] = typeSegs[0];
			if(typeSegs.length>1)  resourceTypeId[node_COMMONATRIBUTECONSTANT.IDRESOURCETYPE_VERSION] = typeSegs[1];
		}
		else{
			resourceTypeId = typeParm1;
		}
		this[node_COMMONATRIBUTECONSTANT.RESOURCEID_RESOURCETYPEID] = resourceTypeId;
	}

	if(this[node_COMMONATRIBUTECONSTANT.RESOURCEID_RESOURCETYPEID][node_COMMONATRIBUTECONSTANT.IDRESOURCETYPE_VERSION]==undefined)  resourceTypeId[node_COMMONATRIBUTECONSTANT.RESOURCEID_RESOURCETYPEID][node_COMMONATRIBUTECONSTANT.IDRESOURCETYPE_VERSION] = node_COMMONCONSTANT.VERSION_DEFAULT;

	var normalizedId = node_resourceUtility.buildReourceCoreIdLiterate(this[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID]);
	this[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = normalizedId;
	this.name = normalizedId;
};	
	
var node_Resource = function(resourceInfo, resourceData, info){
	this.resourceInfo = resourceInfo;
	this.resourceData = resourceData;
	this.info = info;
}


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("ResourceId", node_ResourceId); 
packageObj.createChildNode("Resource", node_Resource); 

})(packageObj);
