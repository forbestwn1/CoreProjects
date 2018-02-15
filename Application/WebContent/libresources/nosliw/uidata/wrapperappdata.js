//get/create package
var packageObj = library.getChildPackage("wrapper.appdata");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONCONSTANT;
var node_createEventObject;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithId;
var node_basicUtility;
var node_dataUtility;
var node_wrapperFactory;
var node_namingConvensionUtility;
var node_appDataWrapperUtility;	
var node_createServiceRequestInfoSequence;
var node_ServiceInfo;
var node_uiDataOperationServiceUtility;
var node_requestServiceProcessor;
var node_OperationParm;
var node_parseSegment;
var node_parsePathSegment;
var node_createServiceRequestInfoSimple;
var node_createServiceRequestInfoSet;
//*******************************************   Start Node Definition  ************************************** 	
var node_createWraperData = function(){
	
	var loc_getDirectChildValueRequest = function(parentValue, path, handlers, request){
		var operationParms = [];
		operationParms.push(new node_OperationParm(parentValue, "base"));
		operationParms.push(new node_OperationParm({
			dataTypeId: "test.string;1.0.0",
			value : path
		}, "name"));

		return nosliw.runtime.getExpressionService().getExecuteOperationRequest(
				parentValue.dataTypeId, 
				node_COMMONCONSTANT.DATAOPERATION_COMPLEX_GETCHILDDATA, 
				operationParms, handlers, request);
	}; 

	var loc_getCurrentSegmentChildValueRequest = function(parentValue, segs, handlers, request){
		return loc_getDirectChildValueRequest(parentValue, segs.next(), handlers, request);			
	};
	
	var loc_getSegmentsChildValueRequest = function(parentValue, segs, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetSegsChildValue", {"parent":parentValue, "segs":segs}), handlers, request);
		if(segs.hasNext()){
			out.addRequest(loc_getCurrentSegmentChildValueRequest(parentValue, segs, {
				success : function(request, segChildValue){
					return loc_getSegmentsChildValueRequest(segChildValue, segs);
				}
			}));
		}
		else{
			//end of segments
			out.addRequest(node_createServiceRequestInfoSimple({}, function(request){
				return parentValue;
			})); 
		}
		return out;
	};
	
	var loc_getOperationBaseRequest = function(value, path, first, lastReverse, handlers, request){
		var segs = node_parsePathSegment(path, first, lastReverse);
		var out = loc_getSegmentsChildValueRequest(value, segs, handlers, request);
		out.setRequestProcessors({
			success : function(request, value){
				return {
					base : value,
					attribute : segs.getRestPath()
				}
			}
		});
		return out;
	};
	
	
	var loc_out = {		
		
			//get child value by path
			getChildValueRequest : function(parentValue, path, handlers, request){
				var pathSegs = node_parsePathSegment(path);
				var out = loc_getSegmentsChildValueRequest(parentValue, pathSegs, handlers, request);
				out.setRequestProcessors({
					success : function(request, childValue){
						return node_dataUtility.cloneValue(childValue);
					}
				});
				return out;
			},
			
			getDataOperationRequest : function(value, dataOperationService, handlers, request){
				var command = dataOperationService.command;
				var operationData = dataOperationService.parms;

				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("DataOperation", {"command":command, "operationData":operationData}), handlers, request);

				if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
					out.addRequest(loc_getOperationBaseRequest(value, operationData.path, undefined, 1, {
						success : function(requestInfo, operationBase){
							var operationParms = [];
							operationParms.push(new node_OperationParm(operationBase.base, "base"));
							operationParms.push(new node_OperationParm({
								dataTypeId: "test.string;1.0.0",
								value : operationBase.path
							}, "name"));
							operationParms.push(new node_OperationParm(operationData.value, "value"));

							return nosliw.runtime.getExpressionService().getExecuteOperationRequest(
									operationBase.base.dataTypeId, 
									node_COMMONCONSTANT.DATAOPERATION_COMPLEX_SETCHILDDATA, 
									operationParms);
						}
					}));
				}
				else if(command==node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT){
					out.addRequest(loc_getOperationBaseRequest(value, operationData.path, undefined, 0, {
						success : function(requestInfo, operationBase){
							var operationParms = [];
							operationParms.push(new node_OperationParm(value, "base"));
							out.addRequest(nosliw.runtime.getExpressionService().getExecuteOperationRequest(
								value.dataTypeId, 
								node_COMMONCONSTANT.DATAOPERATION_COMPLEX_ISACCESSCHILDBYID, 
								operationParms, {
									success : function(request, isAccessChildById){
										if(isAccessChildById.value){
											//througth id
											
										}
										else{
											//throught index,
											var operationParms = [];
											operationParms.push(new node_OperationParm(operationBase.base, "base"));
											operationParms.push(new node_OperationParm({
												dataTypeId: "test.integer;1.0.0",
												value : operationData.index
											}, "index"));
											operationParms.push(new node_OperationParm(operationData.value, "child"));
											out.addRequest(nosliw.runtime.getExpressionService().getExecuteOperationRequest(
												value.dataTypeId, 
												node_COMMONCONSTANT.DATAOPERATION_COMPLEX_ADDCHILD, 
												operationParms, {
													success : function(request, childValue){
														return value;
													}
												}
											));
										}										
									}
								}
							));
						}
					}));
					
				}
				else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
					
				}
				else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETE){
					
				}
				
				return out;
			},

			getGetElementsRequest : function(value, handlers, request){
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetElements", {"value":value}), handlers, request); 
				
				var operationParms = [];
				operationParms.push(new node_OperationParm(value, "base"));
				out.addRequest(nosliw.runtime.getExpressionService().getExecuteOperationRequest(
					value.dataTypeId, 
					node_COMMONCONSTANT.DATAOPERATION_COMPLEX_ISACCESSCHILDBYID, 
					operationParms, {
						success : function(request, isAccessChildById){
							if(isAccessChildById.value){
								//througth id
								
							}
							else{
								//throught index,
								//get length first
								var operationParms = [];
								operationParms.push(new node_OperationParm(value, "base"));
								return nosliw.runtime.getExpressionService().getExecuteOperationRequest(
									value.dataTypeId, 
									node_COMMONCONSTANT.DATAOPERATION_COMPLEX_LENGTH, 
									operationParms, {
										success : function(request, arrayValueLength){
											var allElesRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("", {}), {
												success : function(request, setResult){
													var elements = [];
													for(var i=0; i<arrayValueLength.value; i++){
														var eleValue = setResult.getResult(i+"");
														elements.push({
															value : eleValue
														});
													}
													return elements;
												}
											}); 

											for(var i=0; i<arrayValueLength.value; i++){
												var operationParms = [];
												operationParms.push(new node_OperationParm(value, "base"));
												operationParms.push(new node_OperationParm({
													dataTypeId: "test.integer;1.0.0",
													value : i,
												}, "index"));
												allElesRequest.addRequest(i+"", nosliw.runtime.getExpressionService().getExecuteOperationRequest(
													value.dataTypeId, 
													node_COMMONCONSTANT.DATAOPERATION_COMPLEX_GETCHILDDATABYINDEX, 
													operationParms));
											}
											return allElesRequest;
										}
								});
							}
						}
					})
				);
				return out;
			}, 
			
			getWrapperType : function(){	return node_CONSTANT.DATA_TYPE_APPDATA;		},
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.appdata.utility", function(){node_appDataWrapperUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParm", function(){node_OperationParm = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parsePathSegment", function(){node_parsePathSegment = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});

nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){
	//register wrapper faction
	this.getData().registerWrapperFactoryByDataType([node_CONSTANT.DATA_TYPE_APPDATA], node_createWraperData);
});


//Register Node by Name
packageObj.createChildNode("createWraperData", node_createWraperData); 

})(packageObj);
