//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	var node_uiDataOperationServiceUtility;
	var node_namingConvensionUtility;
	var node_getEntityTreeNodeInterface;
	var node_getWithValuePortInterface;
	var node_createValuePortElementInfo;
	var node_getObjectType;
	var node_ElementIdValuePair;
	var node_complexEntityUtility;
	var node_getEntityObjectInterface;
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_getValuePort = function(valuePortEndPoint, baseEntityCore){
	
	//get core entity
	var valuePortRef = valuePortEndPoint[node_COMMONATRIBUTECONSTANT.ENDPOINTINTUNNELVALUEPORT_VALUEPORTREF];
	var relativePath = valuePortRef[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBUNDLE_BRICKID][node_COMMONATRIBUTECONSTANT.IDBRICKINBUNDLE_RELATIVEPATH];
	var valuePortId = valuePortRef[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBUNDLE_VALUEPORTID];
	var hostEntityCore = node_complexEntityUtility.getBrickCoreByRelativePath(baseEntityCore, relativePath);
	
	var valuePortSide = valuePortRef[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBUNDLE_VALUEPORTSIDE];
	var valuePortContainer;
	if(valuePortSide==node_COMMONCONSTANT.VALUEPORTGROUP_SIDE_INTERNAL){
		valuePortContainer = node_getEntityObjectInterface(hostEntityCore).getInternalValuePortContainer();
	}
	else{
		valuePortContainer = node_getEntityObjectInterface(hostEntityCore).getExternalValuePortContainer();
	}
	
	return valuePortContainer.createValuePort(valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_GROUP], valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_NAME]);
//	return node_getWithValuePortInterface(hostEntityCore).getValuePort(valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_GROUP], valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_NAME]);
};


var loc_getAllFromValueRequest = function(tunnels, baseEntityCore, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getAllFromValueRequest", {}), handlers, request);

	var fromValues = [];
	var executeFromEndPointsRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("fromEndPoint", {}), {
		success : function(request){
			var matchersRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("getAllMatchersRequest", {}), {
				success : function(){
					return fromValues;
				}
			});
			
			_.each(tunnels, function(tunnel, i){
				var matchers = tunnel[node_COMMONATRIBUTECONSTANT.TUNNEL_MATCHERS];
	      		matchersRequest.addRequest(nosliw.runtime.getExpressionService().getMatchDataRequest(fromValues[i], matchers, {
					success: function(request, value){
						fromValues[i] = value;
					}
				}));
				
			});
			
			return matchersRequest;
		}
	});
	_.each(tunnels, function(tunnel, i){
		var matchers = tunnel[node_COMMONATRIBUTECONSTANT.TUNNEL_MATCHERS];
		
		var fromEndPoint = tunnel[node_COMMONATRIBUTECONSTANT.TUNNEL_FROMENDPOINT];
		var fromEndPointType = fromEndPoint[node_COMMONATRIBUTECONSTANT.ENDPOINTINTUNNEL_TYPE];
		if(fromEndPointType==node_COMMONCONSTANT.TUNNELENDPOINT_TYPE_VALUEPORT){
			var fromValuePort = loc_getValuePort(fromEndPoint, baseEntityCore);
			var fromValuePortEleInfo = node_createValuePortElementInfo(fromEndPoint[node_COMMONATRIBUTECONSTANT.ENDPOINTINTUNNELVALUEPORT_VALUESTRUCTUREID], fromEndPoint[node_COMMONATRIBUTECONSTANT.ENDPOINTINTUNNELVALUEPORT_ITEMPATH]);
			executeFromEndPointsRequest.addRequest(fromValuePort.getValueRequest(fromValuePortEleInfo, {
				success: function(request, value){
					fromValues.push(value);
				}
			}));
		}
		else if(fromEndPointType==node_COMMONCONSTANT.TUNNELENDPOINT_TYPE_CONSTANT){
			executeFromEndPointsRequest.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				fromValues.push(fromEndPoint[node_COMMONATRIBUTECONSTANT.ENDPOINTINTUNNELCONSTANT_VALUE]);			
			}));
		}
	});
	out.addRequest(executeFromEndPointsRequest);
	
	return out;
}

var loc_setValueToEndPointRequest = function(tunnels, values, baseEntityCore, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("setValueToEndPointRequest", {}), handlers, request);
	
	var toValuePort = loc_getValuePort(tunnels[0][node_COMMONATRIBUTECONSTANT.TUNNEL_TOENDPOINT], baseEntityCore);

	var toValuesInfo = [];
	_.each(tunnels, function(tunnel, i){
		var toEndPoint = tunnel[node_COMMONATRIBUTECONSTANT.TUNNEL_TOENDPOINT];
		toValuesInfo.push(new node_ElementIdValuePair(node_createValuePortElementInfo(toEndPoint[node_COMMONATRIBUTECONSTANT.ENDPOINTINTUNNELVALUEPORT_VALUESTRUCTUREID], toEndPoint[node_COMMONATRIBUTECONSTANT.ENDPOINTINTUNNELVALUEPORT_ITEMPATH]), values[i]));
	});

	out.addRequest(toValuePort.setValuesRequest(toValuesInfo));
	return out;
};

var node_getExecuteMappingDataAssociationRequest = function(association, baseEntityCore, name, handlers, request){

	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteAssociation", {}), handlers, request);

	var tunnels = association[node_COMMONATRIBUTECONSTANT.DATAASSOCIATIONMAPPING_TUNNEL];
	var tunnelsByTargets = {};

	//group tunnels by target value port
	_.each(tunnels, function(tunnel, i){
		var toEndPoint = tunnel[node_COMMONATRIBUTECONSTANT.TUNNEL_TOENDPOINT];
		var toValuePortRef = toEndPoint[node_COMMONATRIBUTECONSTANT.ENDPOINTINTUNNELVALUEPORT_VALUEPORTREF];
		var toValuePortKey = toValuePortRef[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBUNDLE_KEY];
		var tunnels = tunnelsByTargets[toValuePortKey];
		if(tunnels==undefined){
			tunnels = [];
			tunnelsByTargets[toValuePortKey] = tunnels;
		}
		tunnels.push(tunnel);
	});

	_.each(tunnelsByTargets, function(tunnels, targetKey){
		out.addRequest(loc_getAllFromValueRequest(tunnels, baseEntityCore, {
			success : function(request, fromValues){
				return loc_setValueToEndPointRequest(tunnels, fromValues, baseEntityCore);
			}
		}));
	});

	return out;
};


var node_getExecuteMappingDataAssociationRequest1 = function(inputIODataSet, association, outputIODataSet, targetName, handlers, request){
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
		
		var fromProvideName = mappingPath[node_COMMONATRIBUTECONSTANT.PATHVALUEMAPPING_FROMPROVIDENAME];
		var fromProvidePath = mappingPath[node_COMMONATRIBUTECONSTANT.PATHVALUEMAPPING_FROMPROVIDEPATH];
		
		var matchers = mappingPath[node_COMMONATRIBUTECONSTANT.PATHVALUEMAPPING_MATCHERS];
		
		if(fromConstant!=undefined){
			//from constant
			if(matchers==undefined)   return mappingRequest.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){  return fromConstant;  }));
			else{
				mappingRequest.addRequest(nosliw.runtime.getExpressionService().getMatchDataRequest(fromConstant, matchers));
			}
		}
		else if(fromProvideName!=undefined){
			//from provide
			var dataOperationService = node_uiDataOperationServiceUtility.createGetOperationService(node_namingConvensionUtility.cascadePath(fromProvideName, fromProvidePath));
			mappingRequest.addRequest(inputIODataSet.getDataOperationRequest(node_COMMONCONSTANT.IODATASET_PROVIDE, dataOperationService, {
				success : function(request, value){
					if(matchers==undefined)   return value;
					else{
						return nosliw.runtime.getExpressionService().getMatchDataRequest(value, matchers)
					}
				}
			}));
		}
		else{
			//from variable
			var dataOperationService = node_uiDataOperationServiceUtility.createGetOperationService(node_namingConvensionUtility.cascadePath(fromValueStructureId, fromItemPath));
			mappingRequest.addRequest(inputIODataSet.getDataOperationRequest(fromDomainName, dataOperationService, {
				success : function(request, value){
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
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("variable.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.getWithValuePortInterface", function(){node_getWithValuePortInterface = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.createValuePortElementInfo", function(){node_createValuePortElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.ElementIdValuePair", function(){node_ElementIdValuePair = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("getExecuteMappingDataAssociationRequest", node_getExecuteMappingDataAssociationRequest); 

})(packageObj);
