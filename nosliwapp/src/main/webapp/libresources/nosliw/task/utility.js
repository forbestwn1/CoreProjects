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
	var node_namingConvensionUtility;
	var node_complexEntityUtility;
	var node_getApplicationInterface;

//*******************************************   Start Node Definition  ************************************** 	

var node_taskUtility = {
	
	getInvokeTaskRequest : function(taskInfo, taskInput, requirement, bundleCore, handlers, request){
		var mainEntityDefPath = bundleCore.getMainEntityDefinitionPath();
		var taskDefPath = taskInfo.getEntityPath();
		
		if(!node_basicUtility.isStringEmpty(mainEntityDefPath)){
			taskDefPath = taskDefPath.subString(mainEntityDefPath.length);
		}
		
		var pathSegs = node_namingConvensionUtility.parsePathInfos(taskDefPath);
		var i = 0;
		var path = "";
		while(i<pathSegs.length-1){
			path = node_namingConvensionUtility.cascadePath(path, pathSegs[i]);
			i++;
		}
		var attr = pathSegs[pathSegs.length-1];
		
		var taskParentCoreEntity = node_complexEntityUtility.getDescendant(bundleCore.getMainEntity().getCoreEntity(), path);
		
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var adapterName = node_COMMONCONSTANT.NAME_DEFAULT;
		var adapterInfo = taskInput==undefined?undefined:taskInput.getAdpaterInfo();
		if(adapterInfo!=undefined){
			adapterName = adapterInfo.name;
		}

		var extraInfo = {
			taskInput : taskInput,
			requirement : requirement
		};
		out.addRequest(node_complexEntityUtility.getAttributeAdapterExecuteRequest(taskParentCoreEntity, attr, adapterName, extraInfo));

		return out;
	},

	getInvokeTaskRequest1 : function(taskInfo, taskInput, bundleCore, handlers, request){
		var mainEntityDefPath = bundleCore.getMainEntityDefinitionPath();
		var taskDefPath = taskInfo[node_COMMONATRIBUTECONSTANT.INFOTASK_PATH];
		
		if(!node_basicUtility.isStringEmpty(mainEntityDefPath)){
			taskDefPath = taskDefPath.subString(mainEntityDefPath.length);
		}
		
		var pathSegs = node_namingConvensionUtility.parsePathInfos(taskDefPath);
		var i = 0;
		var path = "";
		while(i<pathSegs.length-1){
			path = node_namingConvensionUtility.cascadePath(path, pathSegs[i]);
			i++;
		}
		var attr = pathSegs[pathSegs.length-1];
		
		var mainEntityCore = bundleCore.getMainEntity().getCoreEntity();
		return this.getTaskAttributeExecuteRequest(node_complexEntityUtility.getDescendant(mainEntityCore, path).getCoreEntity(), attr, taskInput, handlers, request);
	},

	getInvokeTaskItemRequest : function(taskInfo, taskInput, bundleCore, handlers, request){
		var mainEntityDefPath = bundleCore.getMainEntityDefinitionPath();
		var taskDefPath = taskInfo[node_COMMONATRIBUTECONSTANT.INFOTASK_PATH];
		
		if(!node_basicUtility.isStringEmpty(mainEntityDefPath)){
			taskDefPath = taskDefPath.subString(mainEntityDefPath.length);
		}
		
		var postSegInfo = node_basicUtility.parsePostSegment(taskDefPath);
		var mainEntityCore = bundleCore.getMainEntity().getCoreEntity();
		return this.getTaskItemAttributeExecuteRequest(node_complexEntityUtility.getDescendant(mainEntityCore, postSegInfo.segments).getCoreEntity(), postSegInfo.post, taskInfo[node_COMMONATRIBUTECONSTANT.INFOTASK_NAME], taskInput, handlers, request);
	},

	getTaskItemAttributeExecuteRequest : function(parentCoreEntity, attrName, itemName, taskInput, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

		var adapterName = node_COMMONCONSTANT.NAME_DEFAULT;
		var adapterInfo = taskInput==undefined?undefined:taskInput.adapterInfo;
		if(adapterInfo!=undefined){
			adapterName = adapterInfo.name;
			out.addRequest(node_complexEntityUtility.getAttributeAdapterExecuteRequest(parentCoreEntity, attrName, adapterName, taskInput));
		}

		var taskEntityCore = node_getEntityTreeNodeInterface(parentCoreEntity).getChild(attrName).getChildValue().getCoreEntity();
		var taskInterface = node_getApplicationInterface(taskEntityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASKCONTAINER);
		out.addRequest(taskInterface.getExecuteItemRequest(itemName, taskInput));
		
		return out;
	},
	
	getTaskEntityExecuteRequest : function(parentCoreEntity, attrName, taskInput, adapterInfo, requirement, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

		var adapterName = node_COMMONCONSTANT.NAME_DEFAULT;
		var adapterInfo = taskInput==undefined?undefined:taskInput.adapterInfo;
		if(adapterInfo!=undefined){
			adapterName = adapterInfo.name;
			out.addRequest(node_complexEntityUtility.getAttributeAdapterExecuteRequest(parentCoreEntity, attrName, adapterName, taskInput));
		}

		var taskEntityCore = node_getEntityTreeNodeInterface(parentCoreEntity).getChild(attrName).getChildValue().getCoreEntity();
		var taskInterface = node_getApplicationInterface(taskEntityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK);
		out.addRequest(taskInterface.getExecuteRequest(taskInput, handlers, request));
		
		return out;
	},

	getTaskAttributeExecuteRequest : function(parentCoreEntity, attrName, taskInput, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

		var adapterName = node_COMMONCONSTANT.NAME_DEFAULT;
		var adapterInfo = taskInput==undefined?undefined:taskInput.adapterInfo;
		if(adapterInfo!=undefined){
			adapterName = adapterInfo.name;
			out.addRequest(node_complexEntityUtility.getAttributeAdapterExecuteRequest(parentCoreEntity, attrName, adapterName, taskInput));
		}

		var taskEntityCore = node_getEntityTreeNodeInterface(parentCoreEntity).getChild(attrName).getChildValue().getCoreEntity();
		var taskInterface = node_getApplicationInterface(taskEntityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK);
		out.addRequest(taskInterface.getExecuteRequest(taskInput, handlers, request));
		
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
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.getApplicationInterface", function(){node_getApplicationInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("taskUtility", node_taskUtility); 

})(packageObj);