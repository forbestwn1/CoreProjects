//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSet;
	var node_ioTaskUtility;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSequence;
	
//*******************************************   Start Node Definition  ************************************** 	

//execute data association of mirror type
var node_getExecuteMirrorDataAssociationRequest = function(inputDataSet, association, outputIODataSet, daName, handlers, request){
	
	var service = new node_ServiceInfo("ExecuteMirrorDataAssociation", {});
	var out = node_createServiceRequestInfoSequence(service, handlers, request);
	var setRequest = node_createServiceRequestInfoSet(undefined, {
		success : function(request, resultSet){
			return outputIODataSet;
		}
	});

	_.each(inputDataSet, function(inputData, name){
		var inputFlat = association[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATION_INPUT][name];
		var outputFlat = association[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATION_OUTPUT][name];
		
		var outputData = inputData;
		if(inputFlat!=outputFlat){
			if(inputFlat==true){
				//input is flat and output is not flat
				//put input into public categary
				outputData = {
					public : inputData
				}
			}
			else{
				//input is not flat, output is flat
				//make output flat
				outputData = {};
				_.each(node_ioTaskUtility.getContextTypes(), function(categary, index){
					var context = inputData[categary];
					if(context!=undefined){
						_.each(context, function(value, name){
							outputData[name] = value;
						});
					}
				});
			}
		}
		
		setRequest.addRequest(name, node_ioTaskUtility.outputToDataSetIORequest(outputIODataSet, outputData, name, true));
	});
	
	out.addRequest(setRequest);
	return out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.ioTaskUtility", function(){node_ioTaskUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});

//Register Node by Name
packageObj.createChildNode("getExecuteMirrorDataAssociationRequest", node_getExecuteMirrorDataAssociationRequest); 

})(packageObj);
