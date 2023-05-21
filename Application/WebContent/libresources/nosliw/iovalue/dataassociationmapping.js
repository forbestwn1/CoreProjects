//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	var node_uiDataOperationServiceUtility;
	var node_namingConvensionUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_getExecuteMappingDataAssociationRequest = function(inputIODataSet, association, outputIODataSet, targetName, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteAssociation", {}), handlers, request);

	var getDataSetRequest = node_createServiceRequestInfoSet(undefined, {
		success : function(request, getDataSet){
			var setDataSetRequest = node_createServiceRequestInfoSequence({});
			var mappingPaths = association[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATIONMAPPING_MAPPINGPATH];
			_.each(mappingPaths, function(mappingPath, i){

				var toDomainName = mappingPath[node_COMMONATRIBUTECONSTANT.PATHVALUEMAPPING_TODOMAINNAME];
				var toValueStructureId = mappingPath[node_COMMONATRIBUTECONSTANT.PATHVALUEMAPPING_TOVALUESTRUCTUREID];
				var toItemPath = mappingPath[node_COMMONATRIBUTECONSTANT.PATHVALUEMAPPING_TOITEMPATH];

				var value = getDataSet.getResult(i+"");
				var dataOperationService = node_uiDataOperationServiceUtility.createSetOperationService(node_namingConvensionUtility.cascadePath(toValueStructureId, toItemPath), value);
				setDataSetRequest.addRequest(outputIODataSet.getDataOperationRequest(toDomainName, dataOperationService));
			});
			return setDataSetRequest;
		}
	});

	var mappingPaths = association[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATIONMAPPING_MAPPINGPATH];
	_.each(mappingPaths, function(mappingPath, i){
		var mappingRequest = node_createServiceRequestInfoSequence({});
		
		var fromDomainName = mappingPath[node_COMMONATRIBUTECONSTANT.PATHVALUEMAPPING_FROMDOMAINNAME];
		var fromValueStructureId = mappingPath[node_COMMONATRIBUTECONSTANT.PATHVALUEMAPPING_FROMVALUESTRUCTUREID];
		var fromItemPath = mappingPath[node_COMMONATRIBUTECONSTANT.PATHVALUEMAPPING_FROMITEMPATH];

		var fromConstant = mappingPath[node_COMMONATRIBUTECONSTANT.PATHVALUEMAPPING_FROMCONSTANT];
		
		var matchers = mappingPath[node_COMMONATRIBUTECONSTANT.PATHVALUEMAPPING_MATCHERS];
		
		if(fromConstant!=undefined){
			//from constant
			if(matchers==undefined)   return mappingRequest.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){  return fromConstant;  }));
			else{
				mappingRequest.addRequest(nosliw.runtime.getExpressionService().getMatchDataRequest(fromConstant, matchers));
			}
		}
		else{
			//from variable
			var dataOperationService = node_uiDataOperationServiceUtility.createGetOperationService(node_namingConvensionUtility.cascadePath(fromValueStructureId, fromItemPath));
			mappingRequest.addRequest(inputIODataSet.getDataOperationRequest(fromDomainName, dataOperationService, {
				success : function(request, value){
					value = value.value
					if(matchers==undefined)   return value;
					else{
						return nosliw.runtime.getExpressionService().getMatchDataRequest(value, matchers)
					}
				}
			}));
		}
		
		getDataSetRequest.addRequest(i+"", mappingRequest);
	});

	out.addRequest(getDataSetRequest);

	return out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("getExecuteMappingDataAssociationRequest", node_getExecuteMappingDataAssociationRequest); 

})(packageObj);
