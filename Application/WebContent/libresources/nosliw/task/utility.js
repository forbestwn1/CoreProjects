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

var node_taskUtility = {
	
	getTaskAttributeExecuteRequest : function(parentCoreEntity, attrName, extraInfo, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

		var adapterName = node_COMMONCONSTANT.NAME_DEFAULT;
		var adapterInfo = extraInfo==undefined?undefined:extraInfo.adapterInfo;
		if(adapterInfo!=undefined)  adapterName = adapterInfo.name;

		out.addRequest(node_complexEntityUtility.getAttributeAdapterExecuteRequest(parentCoreEntity, attrName, adapterName, extraInfo));
		var taskEntityCore = node_getEntityTreeNodeInterface(parentCoreEntity).getChild(attrName).getChildValue().getCoreEntity();
		var taskInterface = node_getApplicationInterface(taskEntityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK);
		out.addRequest(taskInterface.getExecuteRequest(extraInfo, handlers, request));
		
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
packageObj.createChildNode("taskUtility", node_taskUtility); 

})(packageObj);
