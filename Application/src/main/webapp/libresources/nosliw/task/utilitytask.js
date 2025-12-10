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
	var node_getEntityObjectInterface;
	var node_getBasicEntityObjectInterface;
	var node_utilityNamedVariable;

//*******************************************   Start Node Definition  ************************************** 	

var node_taskUtility = function(){


  var loc_out = {
	  
    createTaskFunctionWithSettingValuePortValues : function(valuePortGroupType, valuePortName, setValueByName){
        		
		return function(coreEntity, handlers, request){
			//set event data to value port
			var valuePortContainer = node_getEntityObjectInterface(coreEntity).getExternalValuePortContainer();
							
			return node_utilityNamedVariable.setValuesPortValueRequest(
				valuePortContainer,
				valuePortGroupType,
				valuePortName,
				setValueByName,
				handlers, request);
		};
	},

    createTaskFunctionWithSettingValuePortValuesByGroupName : function(valuePortGroupName, valuePortName, setValueByName){
        		
		return function(coreEntity, handlers, request){
			//set event data to value port
			var valuePortContainer = node_getEntityObjectInterface(coreEntity).getExternalValuePortContainer();
							
			return node_utilityNamedVariable.setValuesPortValueByGroupNameRequest(
				valuePortContainer,
				valuePortGroupName,
				valuePortName,
				setValueByName,
				handlers, request);
		};
	}
  };

  return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){	node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.getApplicationInterface", function(){node_getApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getBasicEntityObjectInterface", function(){node_getBasicEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.utilityNamedVariable", function(){node_utilityNamedVariable = this.getData();});

//Register Node by Name
packageObj.createChildNode("taskUtility", node_taskUtility); 

})(packageObj);
