//get/create package
var packageObj = library.getChildPackage("uidataoperation");    

(function(packageObj){
//get used node
var node_CONSTANT;	
var node_getObjectType;
var node_ServiceInfo;
var node_createServiceRequestInfoSet;
var node_namingConvensionUtility;
var node_dataUtility;
var node_createValuePortElementInfo;
//*******************************************   Start Node Definition  ************************************** 	

var loc_createValueStructureDataOperationRequest = function(valueStructure, valueStructureVarInfo, operationService, handlers, requester_parent){
	operationService.parms.path = node_dataUtility.combinePath(valueStructureVarInfo.path, operationService.parms.path);
	return valueStructure.getDataOperationRequest(valueStructureVarInfo.name, operationService, handlers, requester_parent);
};

//create request for data operation
//operationBase:  where store variable, valueStructure or valueContext 
var node_createUIDataOperationRequest = function(operationBase, uiDataOperation, handlers, requester_parent){
	var target = uiDataOperation.target;
	var targetType = node_getObjectType(target);
	var operationService = uiDataOperation.operationService;
	var request;
	switch(targetType)
	{
	case node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER:
		request = target.getDataOperationRequest(operationService, handlers, requester_parent);
		break;
	case node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE:
		request = target.getDataOperationRequest(operationService, handlers, requester_parent);
		break;
	case node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLEWRAPPER:
		request = target.getDataOperationRequest(operationService, handlers, requester_parent);
		break;
	case node_CONSTANT.TYPEDOBJECT_TYPE_VALUESTRUCTUREVARIABLE:
		request = loc_createValueStructureDataOperationRequest(operationBase, target, operationService, handlers, requester_parent);
		break;
	case node_CONSTANT.TYPEDOBJECT_TYPE_VALUEPORTELEMENTID:
		request = loc_createValueStructureDataOperationRequest(operationBase.getValueStructure(target.getValueStructureId()), target.getValueStructureVariableInfo(), operationService, handlers, requester_parent);
		break;
	default : 
		//target is value context element name
		var valueContextVarInfo = node_createValuePortElementInfo(target);
		request = loc_createValueStructureDataOperationRequest(operationBase.getValueStructure(valueContextVarInfo.getValueStructureId()), valueContextVarInfo.getValueStructureVariableInfo(), operationService, handlers, requester_parent);
	}
	return request;
};

/**
 * Request for batch of data operation
 * It contains a set of data operation service, so that this request is a batch of data operation as a whole
 * 
 */
var node_createBatchUIDataOperationRequest = function(valueContext, handlers, requester_parent){

	//all the child requests service  
	var loc_uiDataOperations = [];
	
	//data context
	var loc_valueContext = valueContext;
	
	var loc_index = 0;
	
	var loc_out = node_createServiceRequestInfoSet(new node_ServiceInfo("BatchUIDataOperation", {}), handlers, requester_parent);
	
	loc_out.addUIDataOperation = function(uiDataOperation){
		this.addRequest(loc_index+"", node_createUIDataOperationRequest(loc_valueContext, uiDataOperation));
		loc_index++;

		//for debugging purpose
		loc_uiDataOperations.push(uiDataOperation);
	};
	loc_out.isEmpty = function(){
		return loc_uiDataOperations.length==0;
	};
	
	return loc_out;
};

//operate on targe
//   target : variable, wrapper, context variable
//   operationService : service for operation
var node_UIDataOperation = function(targetInfo, operationService){
	this.target = targetInfo;
	this.operationService = operationService;
};


//utility method to build data operation service
var node_uiDataOperationServiceUtility = function(){

	var loc_createServiceInfo = function(command, parms){
		var out = new node_ServiceInfo(command, parms);
		out.clone = function(){
			return loc_createServiceInfo(out.command, out.parms.clone());
		}
		return out;
	};
	
	var loc_out = {
			createGetOperationData : function(path){
				return {
					path : path,
					clone : function(){
						return loc_out.createGetOperationData(this.path);
					}
				};
			},
			
			createGetOperationService : function(path){
				return loc_createServiceInfo(node_CONSTANT.WRAPPER_OPERATION_GET, this.createGetOperationData(path));
			},
			
			createSetOperationData : function(path, value, dataType){
				return {
					path : path,
					value : value,
					dataType : dataType,
					clone : function(){
						return loc_out.createSetOperationData(this.path, node_dataUtility.cloneValue(this.value), dataType);
					}
				};
			},
			
			createSetOperationService : function(path, value, dataType){
				return loc_createServiceInfo(node_CONSTANT.WRAPPER_OPERATION_SET, this.createSetOperationData(path, value, dataType));
			},

			//index or id or both
			createAddElementOperationData : function(path, value, index, id){
				return {
					path : path,              
					index : index,            	//index in array, mandatory
					id : id,					//unchanged path from parent, if not provided, then will generate unique one
					value : value,
					clone : function(){
						return loc_out.createAddElementOperationData(this.path, node_dataUtility.cloneValue(this.value), this.index, this.id);
					}
				};
			},

			createAddElementOperationService : function(path, value, index, id){
				return loc_createServiceInfo(node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT, this.createAddElementOperationData(path, value, index, id));
			},

			//index or id, cannot both
			createDeleteElementOperationData : function(path, index, id){
				return {
					path : path,
					index : index,
					id : id,
					clone : function(){
						return loc_out.createDeleteElementOperationData(this.path, this.index, this.id);
					}
				};
			},
			
			createDeleteElementOperationService : function(path, index, id){
				return loc_createServiceInfo(node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT, this.createDeleteElementOperationData(path, index, id));
			},

			createDeleteOperationData : function(path){
				return {
					path : path,
					clone : function(){
						return loc_out.createDeleteOperationData(this.path);
					}
				};
			},
			
			createDeleteOperationService : function(path){
				return loc_createServiceInfo(node_CONSTANT.WRAPPER_OPERATION_DELETE, this.createDeleteOperationData(path));
			}
	};
	
	return loc_out;
}();


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("variable.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.createValuePortElementInfo", function(){node_createValuePortElementInfo = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUIDataOperationRequest", node_createUIDataOperationRequest); 
packageObj.createChildNode("createBatchUIDataOperationRequest", node_createBatchUIDataOperationRequest); 
packageObj.createChildNode("UIDataOperation", node_UIDataOperation); 
packageObj.createChildNode("uiDataOperationServiceUtility", node_uiDataOperationServiceUtility); 

})(packageObj);
