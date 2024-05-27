//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_newOrderedContainer;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_buildInterface;
	var node_getInterface;
	var node_getEmbededEntityInterface;
	var node_getObjectType;

//*******************************************   Start Node Definition  **************************************
	
//interface for plug in for complex entity,
//it create component core object

var node_buildEntityPlugInObject = function(rawPluginObj){

	var interfaceDef = {
		//create component core object
		getCreateEntityCoreRequest : function(entityDef, valueContextId, bundleCore, configure, handlers, request){
			return {};
		}
	};
		
	return _.extend({}, interfaceDef, rawPluginObj);	
};


var node_buildAdapterPlugInObject = function(rawPluginObj){

	var interfaceDef = {
		//create component core object
		getNewAdapterRequest : function(adapterDefinition, baseCore, handlers, request){
			return node_createServiceRequestInfoSimple({}, function(request){
				return {
					getExecuteRequest : function(handlers, request){
						return;
					}
				};
			}, handlers, request);
		}
	};
		
	return _.extend({}, interfaceDef, rawPluginObj);	
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.newOrderedContainer", function(){node_newOrderedContainer = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.embeded.getEmbededEntityInterface", function(){node_getEmbededEntityInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});



//Register Node by Name
packageObj.createChildNode("buildEntityPlugInObject", node_buildEntityPlugInObject); 
packageObj.createChildNode("buildAdapterPlugInObject", node_buildAdapterPlugInObject); 

})(packageObj);
