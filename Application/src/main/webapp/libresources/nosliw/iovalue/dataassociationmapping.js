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

var loc_getValuePort = function(valuePortEndPoint, baseEntityCore){
	
	//get core entity
	var valuePortRef = valuePortEndPoint[node_COMMONATRIBUTECONSTANT.ENDPOINTINTUNNELVALUEPORT_VALUEPORTREF];
	var relativePath = valuePortRef[node_COMMONATRIBUTECONSTANT.REFERENCEVALUEPORT_BRICKREFERENCE][node_COMMONATRIBUTECONSTANT.REFERENCEBRICKLOCAL_RELATIVEPATH];
	var valuePortId = valuePortRef[node_COMMONATRIBUTECONSTANT.REFERENCEVALUEPORT_VALUEPORTKEY];
	
	var hostEntityCore = baseEntityCore;
	if(relativePath!=undefined && relativePath!=""){
		var segs = relativePath.split(node_COMMONCONSTANT.SEPERATOR_LEVEL2);
		_.each(segs, function(seg, i){
			var treeNodeInterface = node_getEntityTreeNodeInterface(hostEntityCore);
			if(seg.startsWith(node_COMMONCONSTANT.NAME_PARENT)) {
				hostEntityCore = treeNodeInterface.getParentCore();
			}
			else if(seg.startsWith(node_COMMONCONSTANT.NAME_CHILD)) {
				var ss = seg.split("\\"+node_COMMONCONSTANT.SEPERATOR_LEVEL1);
				var childTreeNode = treeNodeInterface.getChild(ss[1])
				hostEntityCore = childTreeNode.getChildValue().getCoreEntity();
			}
		});		
	}
	
	return node_getWithValuePortInterface(hostEntityCore).getValuePort(valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORT_TYPE], valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORT_NAME]);
};

var node_getExecuteMappingDataAssociationRequest = function(association, baseEntityCore, handlers, request){

	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteAssociation", {}), handlers, request);

	var tunnels = association[node_COMMONATRIBUTECONSTANT.DATAASSOCIATIONMAPPING_TUNNEL];
	_.each(tunnels, function(tunnel, i){
		var fromEndPoint = tunnel[node_COMMONATRIBUTECONSTANT.TUNNEL_FROMENDPOINT];
		var toEndPoint = tunnel[node_COMMONATRIBUTECONSTANT.TUNNEL_TOENDPOINT];
		var matchers = tunnel[node_COMMONATRIBUTECONSTANT.TUNNEL_MATCHERS];

		var executeTunnelRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteTunnel", {}));
		
		var executeFromEndPointRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("fromEndPoint", {}), {
			success: function(request, fromValue){
				var toValuePort = loc_getValuePort(toEndPoint, baseEntityCore);
				var toValuePortEleInfo = node_createValuePortElementInfo(toEndPoint[node_COMMONATRIBUTECONSTANT.ENDPOINTINTUNNELVALUEPORT_VALUESTRUCTUREID], toEndPoint[node_COMMONATRIBUTECONSTANT.ENDPOINTINTUNNELVALUEPORT_ITEMPATH]);
				return toValuePort.setValueRequest(toValuePortEleInfo, fromValue);
			}
		});
		var fromEndPointType = fromEndPoint[node_COMMONATRIBUTECONSTANT.ENDPOINTINTUNNEL_TYPE];
		if(fromEndPointType==node_COMMONCONSTANT.TUNNELENDPOINT_TYPE_VALUEPORT){
			var fromValuePort = loc_getValuePort(fromEndPoint, baseEntityCore);
			var fromValuePortEleInfo = node_createValuePortElementInfo(fromEndPoint[node_COMMONATRIBUTECONSTANT.ENDPOINTINTUNNELVALUEPORT_VALUESTRUCTUREID], fromEndPoint[node_COMMONATRIBUTECONSTANT.ENDPOINTINTUNNELVALUEPORT_ITEMPATH]);
			executeFromEndPointRequest.addRequest(fromValuePort.getValueRequest(fromValuePortEleInfo));
		}
		else if(fromEndPointType==node_COMMONCONSTANT.TUNNELENDPOINT_TYPE_CONSTANT){
			
		}
		executeTunnelRequest.addRequest(executeFromEndPointRequest);
		out.addRequest(executeTunnelRequest);
	});
	
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
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("variable.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("getExecuteMappingDataAssociationRequest", node_getExecuteMappingDataAssociationRequest); 

})(packageObj);
