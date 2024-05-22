//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createConfigure;
	var node_createErrorData;
	var node_componentUtility;
	var node_requestServiceProcessor;
	var node_createDataAssociation;
	var node_createIODataSet;
	var node_ioDataFactory;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createDataAssociationAdapterPlugin = function(){
	
	var loc_out = {

		getNewAdapterRequest : function(adapterDefinition, baseEntityCore, handlers, request){
			return node_createServiceRequestInfoSimple({}, function(request){
				return loc_createDataAssociationAdapter(adapterDefinition, baseEntityCore);
			}, handlers, request);
		},
	};

	return loc_out;
};


var loc_createDataAssociationAdapter = function(dataAssociation, baseEntityCore){
	
	var loc_baseEntityCore = baseEntityCore;
	                                                                                           
	var loc_dataAssociationDef = dataAssociation.getAttributeValue(node_COMMONATRIBUTECONSTANT.ADAPTERDATAASSOCIATION_DATAASSOCIATION);
	var loc_dataAssociation = node_createDataAssociation(loc_dataAssociationDef, loc_baseEntityCore);
	
	var loc_out = {
		
		getExecuteRequest : function(handlers, request){
			return loc_dataAssociation.getExecuteRequest(handlers, request);
		}
	};
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("error.entity.createErrorData", function(){node_createErrorData = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.ioDataFactory", function(){node_ioDataFactory = this.getData();});

//Register Node by Name
packageObj.createChildNode("createDataAssociationAdapterPlugin", node_createDataAssociationAdapterPlugin); 

})(packageObj);
