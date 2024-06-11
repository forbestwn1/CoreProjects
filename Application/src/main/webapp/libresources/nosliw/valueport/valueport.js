//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_CONSTANT;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSequence;
	var node_getEmbededEntityInterface;
	var node_buildInterface;
	var node_getEntityObjectInterface;
	var node_getInterface;
	var node_uiDataOperationServiceUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_makeObjectWithValuePortInterface = function(rawEntity){
	
	var loc_rawEntity = rawEntity;
	
	var loc_getExternalValuePort = function(valuePortGroup, valuePortName){   
		var loc_valuePort;
		if(valuePortGroup==node_COMMONCONSTANT.VALUEPORT_TYPE_VALUECONTEXT){
			var complexEntityInterface = node_getEntityObjectInterface(loc_rawEntity);
			if(complexEntityInterface!=undefined){
				loc_valuePort = loc_createValuePortValueContext(complexEntityInterface.getValueContextId(), complexEntityInterface.getBundle().getVariableDomain());
			}
		}
		else{
			if(loc_rawEntity.getExternalValuePort!=undefined){
				loc_valuePort = loc_rawEntity.getExternalValuePort(valuePortGroup, valuePortName);
			}
		}
		
		if(loc_valuePort!=undefined){
			return node_buildValuePort(loc_valuePort);
		}
	};
	
	var loc_getInternalValuePort = function(valuePortGroup, valuePortName){   
		var loc_valuePort;
		if(valuePortGroup==node_COMMONCONSTANT.VALUEPORT_TYPE_VALUECONTEXT){
			var complexEntityInterface = node_getEntityObjectInterface(loc_rawEntity);
			if(complexEntityInterface!=undefined){
				loc_valuePort = loc_createValuePortValueContext(complexEntityInterface.getValueContextId(), complexEntityInterface.getBundle().getVariableDomain());
			}
		}
		else{
			if(loc_rawEntity.getInternalValuePort!=undefined){
				loc_valuePort = loc_rawEntity.getInternalValuePort(valuePortGroup, valuePortName);
			}
		}
		
		if(loc_valuePort!=undefined){
			return node_buildValuePort(loc_valuePort);
		}
	};
	
	var loc_interfaceEntity = {

		getValuePort : function(valuePortGroup, valuePortName){   
			return loc_getExternalValuePort(valuePortGroup, valuePortName);
		},
	};

	var embededEntityInterface =  node_getEmbededEntityInterface(rawEntity);
	if(embededEntityInterface!=null){
		embededEntityInterface.setEnvironmentInterface(node_CONSTANT.INTERFACE_WITHVALUEPORT, {
			getValuePort : function(valuePortGroup, valuePortName){
				return loc_getInternalValuePort(valuePortGroup, valuePortName);
			}
		});
	}
	
	return node_buildInterface(rawEntity, node_CONSTANT.INTERFACE_WITHVALUEPORT, loc_interfaceEntity);
};
	
var node_getWithValuePortInterface = function(baseObject){
	return node_getInterface(baseObject, node_CONSTANT.INTERFACE_WITHVALUEPORT);
};


var loc_createValuePortValueContext = function(valueContextId, varDomain){
	
	var loc_varDomain = varDomain;
	var loc_valueContext = varDomain.getValueContext(valueContextId);
	
	
	var loc_out = {
		
		createVariable : function(elementId){
			return loc_valueContext.createVariableById(elementId);			
		},
		
		getValueRequest : function(elementId, handlers, request){        
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("setValuesRequest", {}), handlers, request);
			out.addRequest(loc_valueContext.getValueStructure(elementId.getValueStructureRuntimeId()).getDataOperationRequest(elementId.getRootName(), node_uiDataOperationServiceUtility.createGetOperationService(elementId.getElementPath()), {
				success: function(request, dataValue){
					return dataValue.value;
				}
			}));
			return out;
		},

		setValueRequest : function(elementId, value, handlers, request){        
			return loc_valueContext.getValueStructure(elementId.getValueStructureRuntimeId()).getDataOperationRequest(elementId.getRootName(), node_uiDataOperationServiceUtility.createSetOperationService(elementId.getElementPath(), value), handlers, request);
		},
		
		setValuesRequest : function(setValueInfos, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("setValuesRequest", {}), handlers, request);
			_.each(setValueInfos, function(setValueInfo, i){
				var elementId = setValueInfo.elementId;
				out.addRequest(loc_valueContext.getValueStructure(elementId.getValueStructureRuntimeId()).getDataOperationRequest(elementId.getRootName(), node_uiDataOperationServiceUtility.createSetOperationService(elementId.getElementPath(), setValueInfo.value)));
			});
			return out;			
		},
		
	};
	
	return loc_out;
};


//interface for component external env
var node_buildValuePort = function(rawValuePort){
	var interfaceDef = {
		
		getValueRequest : function(elmentId, handlers, request){        },

		setValueRequest : function(elmentId, value, handlers, request){        },
		setValuesRequest : function(setValueInfos, handlers, request){        },

		createVariable : function(elementId){},
	};
	return _.extend({}, interfaceDef, rawValuePort);
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.embeded.getEmbededEntityInterface", function(){node_getEmbededEntityInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("variable.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("makeObjectWithValuePortInterface", node_makeObjectWithValuePortInterface); 
packageObj.createChildNode("getWithValuePortInterface", node_getWithValuePortInterface); 
packageObj.createChildNode("buildValuePort", node_buildValuePort); 

})(packageObj);
