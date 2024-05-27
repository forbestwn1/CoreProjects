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
	var node_complexEntityUtility;
	var node_getEntityObjectInterface;
	var node_createUIDataOperationRequest;
	var node_UIDataOperation;
	var node_uiDataOperationServiceUtility;
	var node_createValueStructureVariableInfo;

//*******************************************   Start Node Definition  ************************************** 	

var node_valueContextUtility = {
	
	getValueContext : function(parm){
		var complexCore = node_complexEntityUtility.getComplexCoreEntity(parm);
		var complexInterface = node_getEntityObjectInterface(complexCore);
		var bundle = complexInterface.getBundle();
		var valueContext = complexInterface.getValueContext();
		return valueContext;
	},
	
	getGetValueRequest : function(entity, valueStrcutureRuntimeId, rootName, elePath, handlers, request){
		var valueContext = this.getValueContext(entity);
		var valueStructure = valueContext.getValueStructure(valueStrcutureRuntimeId);
	
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(node_createUIDataOperationRequest(valueStructure, new node_UIDataOperation(node_createValueStructureVariableInfo(rootName), node_uiDataOperationServiceUtility.createGetOperationService(elePath)), {
			success : function(request, result){
				return result.value;
			}
		}));
		return out;
	},
	
	getSetValueRequest : function(entity, valueStrcutureRuntimeId, rootName, elePath, value, handlers, request){
		var valueContext = this.getValueContext(entity);
		var valueStructure = valueContext.getValueStructure(valueStrcutureRuntimeId);
	
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(node_createUIDataOperationRequest(valueStructure, new node_UIDataOperation(node_createValueStructureVariableInfo(rootName), node_uiDataOperationServiceUtility.createSetOperationService(elePath, value)), {
			success : function(request, result){
				return result;
			}
		}));
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
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("variable.uidataoperation.createUIDataOperationRequest", function(){node_createUIDataOperationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("variable.uidataoperation.UIDataOperation", function(){node_UIDataOperation = this.getData();});
nosliw.registerSetNodeDataEvent("variable.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valuestructure.createValueStructureVariableInfo", function(){node_createValueStructureVariableInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("valueContextUtility", node_valueContextUtility); 

})(packageObj);
