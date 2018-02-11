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
		loc_getSegmentsChildValueRequest(value, segs, handlers, request);
		
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
			
			getDataOperationRequest : function(value, dataOperationService, handlers, requester_parent){
				var command = dataOperationService.command;
				var operationData = dataOperationService.parms;

				
				var command = operationService.command;
				var serviceData = operationService.parms;
				var path = serviceData.path;
				
				var rootValue = this.getRootData().value;
				var fullPath = node_namingConvensionUtility.cascadePath(this.getFullPath(), path);

				var out;
				if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
					out = node_appDataWrapperUtility.getSetChildAppDataRequest(rootValue, fullPath, serviceData.data, handlers, requester_parent);
					var that  = this;
					out.addPostProcessor({
						success : function(requestInfo, data){
							that.pri_triggerOperationEvent(node_CONSTANT.WRAPPER_EVENT_SET, path, serviceData.data, requestInfo);
						}
					});
				}
				else if(command==node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT){
					var operationData = {
							data : opValue,
							index : serviceData.index,
						};
					out = node_appDataWrapperUtility.getAddElementAppDataRequest(rootValue, fullPath, operationData, handlers, requester_parent);
					var that  = this;
					out.addPostProcessor({
						success : function(requestInfo, data){
							//trigue event
							if(path==undefined)  path="";
							that.pri_triggerOperationEvent(node_CONSTANT.WRAPPER_EVENT_ADDELEMENT, path, operationData, requestInfo);
						}
					});
				}
				else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
					
				}
				return out;
			},

			
			/*
			 * 
			 */
			pri_triggerOperationEvent : function(event, path, opValue, request){
				var rootWrapper = this.getRootWrapper();
				var rootPath = rootWrapper.getPath();
				if(rootPath==undefined)  rootPath = "";
				var fullPath = node_namingConvensionUtility.cascadePath(this.getFullPath(), path);
				if(rootPath==fullPath){
					//on root wrapper
					rootWrapper.pri_trigueDataOperationEvent(event, fullPath, opValue, request);
				}
				else{
					//on child
					if(rootPath=="")  rootWrapper.pri_triggerForwardEvent(event, fullPath, opValue, request);
					else rootWrapper.pri_triggerForwardEvent(event, fullPath.substring(rootPath.length+1), opValue, request);
				}
			},

			handleEachElement : function(handler, thatContext){	
				
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("HandleEachElementInCollection", {"collectionWrapper":this}));
				
				var getDataRequest = this.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(""), {
					success : function(requestInfo, data)
					{ 
						var containerData = data.value;  
						var operationParms = [];
						operationParms.push(new node_OperationParm(containerData, "base"));
						var getChildNamesRequest = nosliw.runtime.getExpressionService().getExecuteOperationRequest(
								containerData.dataTypeId, 
								node_COMMONCONSTANT.DATAOPERATION_COMPLEX_GETCHILDRENNAMES, 
								operationParms, {
									success : function(request, data){
										var requests = [];
										var childNames = data.value;
										_.each(childNames, function(childNameData, i){
											
											var operationParms = [];
											operationParms.push(new node_OperationParm(containerData, "base"));
											operationParms.push(new node_OperationParm(childNameData, "name"));
											var getChildNamesRequest = nosliw.runtime.getExpressionService().getExecuteOperationRequest(
													containerData.dataTypeId, 
													node_COMMONCONSTANT.DATAOPERATION_COMPLEX_GETCHILDDATA, 
													operationParms, {
														success : function(request, data){
													    	handler.call(thatContext, data, i);
														}
													});
											requests.push(getChildNamesRequest);
										});
										return requests;
									}
								});
						return getChildNamesRequest;
					}
				});
				out.addRequest(getDataRequest);
				
				node_requestServiceProcessor.processRequest(out, false);
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

nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){
	//register wrapper faction
	this.getData().registerWrapperFactoryByDataType([node_CONSTANT.DATA_TYPE_APPDATA], node_createWraperData);
});


//Register Node by Name
packageObj.createChildNode("createWraperData", node_createWraperData); 

})(packageObj);
