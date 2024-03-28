//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_CONSTANT;
	var node_getEmbededEntityInterface;
//*******************************************   Start Node Definition  ************************************** 	

var node_makeObjectWithValuePortInterface = function(rawEntity){
	
	var loc_rawEntity = rawEntity;
	
	var loc_interfaceEntity = {

		getValuePort : function(valuePortType, valuePortName){   
			var loc_valuePort;
			if(valuePortType==node_COMMONCONSTANT.VALUEPORT_TYPE_VALUECONTEXT){
				var complexEntityInterface = node_getComplexEntityObjectInterface(loc_rawEntity);
				if(complexEntityInterface!=undefined){
					valuePort = loc_createValuePortValueContext(complexEntityInterface.getValueContextId(), complexEntityInterface.getBundle().getVariableDomain());
				}
			}
			else{
				if(loc_rawEntity.getValuePort!=undefined){
					valuePort = loc_rawEntity.getValuePort(valuePortType, valuePortName);
				}
			}
			
			if(loc_valuePort!=undefined){
				return node_buildValuePort(valuePort);
			}
		},
		
	};

	
	var embededEntityInterface =  node_getEmbededEntityInterface(rawEntity);
	if(embededEntityInterface!=null){
		embededEntityInterface.setEnvironmentInterface(node_CONSTANT.INTERFACE_WITHVALUEPORT, {
			getValuePort : function(valuePortType, valuePortName){
				return loc_interfaceEntity.getValuePort(valuePortType, valuePortName);
			}
		});
	}
	
	return node_buildInterface(rawEntity, node_CONSTANT.INTERFACE_WITHVALUEPORT, loc_interfaceEntity);
};
	
var node_getWithValuePortInterface = function(baseObject){
	return node_getInterface(baseObject, node_CONSTANT.INTERFACE_WITHVALUEpORT);
};


var loc_createValuePortValueContext = function(valueContextId, varDomain){
	
	var loc_varDomain = bundleCore.getVariableDomain();
	var loc_valueContext = varDomain.getValueContext(valueContextId);
	
	
	var loc_out = {
		
		createVariable : function(elementId){
			return loc_varDomain.createVariableById(elementId);			
		},
		
	};
	
	return loc_out;
};


//interface for component external env
var node_buildValuePort = function(rawValuePort){
	var interfaceDef = {
		getValueRequest : function(elmentId, handlers, request){        },

		setValueRequest : function(elmentId, value, handlers, request){        },

		createVariable : function(elementId){},
	};
	return _.extend({}, interfaceDef, rawValuePort);
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.embeded.getEmbededEntityInterface", function(){node_getEmbededEntityInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("makeObjectWithValuePortInterface", node_makeObjectWithValuePortInterface); 
packageObj.createChildNode("getWithValuePortInterface", node_getWithValuePortInterface); 
packageObj.createChildNode("buildValuePort", node_buildValuePort); 

})(packageObj);
