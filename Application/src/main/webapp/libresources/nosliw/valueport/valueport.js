//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_CONSTANT;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_getEmbededEntityInterface;
	var node_buildInterface;
	var node_getEntityObjectInterface;
	var node_getInterface;
	var node_uiDataOperationServiceUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createValuePort = function(valuePortContainerId, varDomain){

	var loc_varDomain = varDomain;
	var loc_valuePortContainer = varDomain.getValuePortContainer(valuePortContainerId);
	
	var loc_getValueStructure = function(valueStructureRuntimeId){
		return loc_valuePortContainer.getValueStructure(valueStructureRuntimeId);
	}
	
	var loc_out = {
		
		createVariable : function(elementId){
			return loc_valuePortContainer.createVariableById(elementId);			
		},
		
		getValueRequest : function(elementId, handlers, request){        
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("setValuesRequest", {}), handlers, request);
			out.addRequest(loc_getValueStructure(elementId.getValueStructureId()).getDataOperationRequest(elementId.getRootName(), node_uiDataOperationServiceUtility.createGetOperationService(elementId.getElementPath()), {
				success: function(request, dataValue){
					return dataValue.value;
				}
			}));
			return out;
		},

		setValueRequest : function(elementId, value, handlers, request){        
			return loc_getValueStructure(elementId.getValueStructureId()).getDataOperationRequest(elementId.getRootName(), node_uiDataOperationServiceUtility.createSetOperationService(elementId.getElementPath(), value), handlers, request);
		},
		
		setValuesRequest : function(setValueInfos, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("setValuesRequest", {}), handlers, request);
			_.each(setValueInfos, function(setValueInfo, i){
				var elementId = setValueInfo.elementId;
				out.addRequest(loc_getValueStructure(elementId.getValueStructureId()).getDataOperationRequest(elementId.getRootName(), node_uiDataOperationServiceUtility.createSetOperationService(elementId.getElementPath(), setValueInfo.value)));
			});
			return out;			
		},
		
	};
	
	return loc_out;
};



var node_createValuePortValueContext = function(valueContextId, varDomain){
	
	var loc_varDomain = varDomain;
	var loc_valueContext = varDomain.getValueContext(valueContextId);
	
	
	var loc_out = {
		
		createVariable : function(elementId){
			return loc_valueContext.createVariableById(elementId);			
		},
		
		getValueRequest : function(elementId, handlers, request){        
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("setValuesRequest", {}), handlers, request);
			out.addRequest(loc_valueContext.getValueStructure(elementId.getValueStructureId()).getDataOperationRequest(elementId.getRootName(), node_uiDataOperationServiceUtility.createGetOperationService(elementId.getElementPath()), {
				success: function(request, dataValue){
					return dataValue.value;
				}
			}));
			return out;
		},

		setValueRequest : function(elementId, value, handlers, request){        
			return loc_valueContext.getValueStructure(elementId.getValueStructureId()).getDataOperationRequest(elementId.getRootName(), node_uiDataOperationServiceUtility.createSetOperationService(elementId.getElementPath(), value), handlers, request);
		},
		
		setValuesRequest : function(setValueInfos, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("setValuesRequest", {}), handlers, request);
			_.each(setValueInfos, function(setValueInfo, i){
				var elementId = setValueInfo.elementId;
				out.addRequest(loc_valueContext.getValueStructure(elementId.getValueStructureId()).getDataOperationRequest(elementId.getRootName(), node_uiDataOperationServiceUtility.createSetOperationService(elementId.getElementPath(), setValueInfo.value)));
			});
			return out;			
		},
		
	};
	
	return loc_out;
};

var node_createValuePortValueFlat = function(){
	
	var loc_value = {};
	
	var loc_setValueRequest = function(target, setValueInfos, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);      
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			_.each(setValueInfos, function(setValueInfo, i){
				var elementId = setValueInfo.elementId;
				target[elementId.getRootName()] = setValueInfo.value; 
			});
		}));
		return out;
	};
	
	var loc_getValueRequest = function(source, elmentId, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);      
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			return source[elmentId.getRootName()];
		}));
		return out;
	};
	
	var loc_out = {
		
		getValueRequest : function(elmentId, handlers, request){ 
			return loc_getValueRequest(loc_value, elmentId, handlers, request);
        },

		setValuesRequest : function(setValueInfos, handlers, request){
			return loc_setValueRequest(loc_value, setValueInfos, handlers, request);
		},
		
		setValue : function(name, value){
			loc_value[name] = value;
		},
		
		init : function(valueByName){
			loc_value = {};
			_.each(valueByName, function(value, name){
				loc_value[name] = value;
			});
		}
	};
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.embeded.getEmbededEntityInterface", function(){node_getEmbededEntityInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("variable.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createValuePortValueContext", node_createValuePortValueContext); 
packageObj.createChildNode("createValuePortValueFlat", node_createValuePortValueFlat); 
packageObj.createChildNode("createValuePort", node_createValuePort); 

})(packageObj);
