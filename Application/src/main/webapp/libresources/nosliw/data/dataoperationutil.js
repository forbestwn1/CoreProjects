//get/create package
var packageObj = library.getChildPackage("utility");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_OperationParm;
	var node_createServiceRequestInfoSet;
	var node_getObjectType;
	var node_parsePathSegment;
	var node_createServiceRequestInfoSimple;
//*******************************************   Start Node Definition  ************************************** 	

var node_dataOperationUtility = function()
{

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

	var loc_out = {
		//get child value by path
		getChildValueRequest : function(parentValue, path, handlers, request){
			var pathSegs;
			if(node_getObjectType(path)==node_CONSTANT.TYPEDOBJECT_TYPE_PATHSEGMENT)  pathSegs = path;
			else pathSegs = node_parsePathSegment(path);
			
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request); 
			out.addRequest(loc_getSegmentsChildValueRequest(parentValue, pathSegs, {
				success : function(request, childValue){
					return childValue;
				}
			}));
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
	};
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParm", function(){node_OperationParm = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parsePathSegment", function(){node_parsePathSegment = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});

//Register Node by Name
packageObj.createChildNode("dataOperationUtility", node_dataOperationUtility); 
	
})(packageObj);
