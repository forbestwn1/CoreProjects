//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ioTaskUtility;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_getExecuteMappingAssociationRequest = function(inputDataSet, association, outputIODataSet, targetName, name, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteAssociation", {}), handlers, request);

	//use convert function to calculate output
	var dataAssociationOutput;
	if(association!=undefined && association[node_COMMONATRIBUTECONSTANT.EXECUTABLEASSOCIATION_CONVERTFUNCTION]!=undefined){
		dataAssociationOutput = association[node_COMMONATRIBUTECONSTANT.EXECUTABLEASSOCIATION_CONVERTFUNCTION](inputDataSet, node_objectOperationUtility.assignObjectAttributeByPath);
	}

	//matchers
	out.addRequest(node_ioTaskUtility.processMatchersRequest(dataAssociationOutput, association[node_COMMONATRIBUTECONSTANT.EXECUTABLEASSOCIATION_OUTPUTMATCHERS], {
		success :function(request, value){
			//to output dataSetIO
			return node_ioTaskUtility.outputToDataSetIORequest(outputIODataSet, value, targetName, association[node_COMMONATRIBUTECONSTANT.EXECUTABLEASSOCIATION_FLATOUTPUT]);
		}
	}));
	return out;
};
	
//execute data association of mapping type
var node_getExecuteMappingDataAssociationRequest = function(inputDataSet, dataAssociationDef, outputIODataSet, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteMappingDataAssociation", {}), handlers, request);
	var executeAssociationsRequest = node_createServiceRequestInfoSet(undefined, {
		success : function(request, resultSet){
			return outputIODataSet;
		}
	});
	
	_.each(dataAssociationDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATION_ASSOCIATION], function(association, targetName){
		executeAssociationsRequest.addRequest(targetName, loc_getExecuteMappingAssociationRequest(inputDataSet, association, outputIODataSet, targetName));
	});
	out.addRequest(executeAssociationsRequest);

	return out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.ioTaskUtility", function(){node_ioTaskUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});

//Register Node by Name
packageObj.createChildNode("getExecuteMappingDataAssociationRequest", node_getExecuteMappingDataAssociationRequest); 

})(packageObj);
