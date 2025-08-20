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
	var node_valueInVarOperationServiceUtility;
	var node_createValuePortValueContext;
	var node_createValuePort;
	var node_makeObjectWithType;
//*******************************************   Start Node Definition  ************************************** 	

var node_makeObjectWithValuePortInterface = function(rawEntity){
	
	var loc_rawEntity = rawEntity;
	
	var loc_getExternalValuePort = function(valuePortGroup, valuePortName){   
		var loc_valuePort;
		
		var complexEntityInterface = node_getEntityObjectInterface(loc_rawEntity);
		if(complexEntityInterface!=undefined){
			loc_valuePort = complexEntityInterface.getExternalValuePortContainer().createValuePort(valuePortGroup, valuePortName);
		}
		
		if(loc_valuePort!=undefined){
			return node_buildValuePort(loc_valuePort);
		}
	};

	var loc_getInternalValuePort = function(valuePortGroup, valuePortName){   
		var loc_valuePort;
		
		var complexEntityInterface = node_getEntityObjectInterface(loc_rawEntity);
		if(complexEntityInterface!=undefined){
			loc_valuePort = complexEntityInterface.getInternalValuePortContainer().createValuePort(valuePortGroup, valuePortName);
		}
		
		if(loc_valuePort!=undefined){
			return node_buildValuePort(loc_valuePort);
		}
	};
	
	//external value port interface
	var loc_withValuePortInterfaceExternal = node_buildWithValuePort({
		getValuePort : function(valuePortGroup, valuePortName){   
			return loc_getExternalValuePort(valuePortGroup, valuePortName);
		},
	});

	//internal value port interface
	var loc_withValuePortInterfaceInternal = node_buildWithValuePort({
		getValuePort : function(valuePortGroup, valuePortName){   
			return loc_getInternalValuePort(valuePortGroup, valuePortName);
		},
		creatVariableByName : function(varName){
			if(loc_rawEntity.creatVariableByName!=undefined){
				return loc_rawEntity.creatVariableByName(varName);
			}
		}	
	});

	//interal entity env value port interface
	var embededEntityInterface =  node_getEmbededEntityInterface(rawEntity);
	if(embededEntityInterface!=null){
		embededEntityInterface.setEnvironmentInterface(node_CONSTANT.INTERFACE_WITHVALUEPORT, loc_withValuePortInterfaceInternal);
	}
	
	return node_buildInterface(rawEntity, node_CONSTANT.INTERFACE_WITHVALUEPORT, loc_withValuePortInterfaceExternal);
};
	
var node_getWithValuePortInterface = function(baseObject){
	return node_getInterface(baseObject, node_CONSTANT.INTERFACE_WITHVALUEPORT);
};

var node_buildWithValuePort = function(rawObject){
	var loc_rawObject = rawObject;

	var interfaceDef	= {
		getValuePort : function(valuePortGroup, valuePortName){	},
		creatVariableByName : function(varName){}	
	};
	var loc_out = _.extend({}, interfaceDef, loc_rawObject);
	return loc_out;
};

//interface for value port
var node_buildValuePort = function(rawValuePort){
	var interfaceDef = {
		
		getValueRequest : function(elmentId, handlers, request){        },

		setValueRequest : function(elmentId, value, handlers, request){        },
		setValuesRequest : function(setValueInfos, handlers, request){        },

		createVariable : function(elementId){},
	};
	var out = _.extend({}, interfaceDef, rawValuePort);
	return node_makeObjectWithType(out, node_CONSTANT.TYPEDOBJECT_TYPE_VALUEPORT);
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.interfacedef.getEmbededEntityInterface", function(){node_getEmbededEntityInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.valueInVarOperationServiceUtility", function(){node_valueInVarOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.createValuePortValueContext", function(){node_createValuePortValueContext = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.createValuePort", function(){node_createValuePort = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});

//Register Node by Name
packageObj.createChildNode("makeObjectWithValuePortInterface", node_makeObjectWithValuePortInterface); 
packageObj.createChildNode("getWithValuePortInterface", node_getWithValuePortInterface); 
packageObj.createChildNode("buildValuePort", node_buildValuePort); 
packageObj.createChildNode("buildWithValuePort", node_buildWithValuePort); 

})(packageObj);
