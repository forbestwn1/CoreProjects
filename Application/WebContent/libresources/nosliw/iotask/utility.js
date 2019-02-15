//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	var node_EndActivityOutput;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_IOTaskResult;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
	
	
	var loc_dyanimicValueBuild = function(output, outputPathSegs, input, intpuPathSegs){
		var inputValue = node_objectOperationUtility.getObjectAttributeByPathSegs(input, intpuPathSegs);
		node_objectOperationUtility.operateObjectByPathSegs(output, outputPathSegs, node_CONSTANT.WRAPPER_OPERATION_SET, inputValue);
		return output;
	};
	
	var loc_getExecuteDataAssociationRequest = function(input, dataAssociation, handlers, request){
		var service = new node_ServiceInfo("ExecuteDataAssociation", {"data":input, "dataAssociation":dataAssociation});
		var out = node_createServiceRequestInfoSequence(service, handlers, request);
		if(dataAssociation==undefined || dataAssociation[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATIONGROUP_CONVERTFUNCTION]==undefined){
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(){  
				return input;  
			}));
			return out;
		}

		var output = dataAssociation[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATIONGROUP_CONVERTFUNCTION](input, loc_dyanimicValueBuild);
		//process matchers
		var matchersByPath = dataAssociation[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATIONGROUP_OUTPUTMATCHERS];
		if(matchersByPath==undefined)  return node_createServiceRequestInfoSimple(undefined, function(){ return output;  }, handlers, request); 

		var matchersByPathRequest = node_createServiceRequestInfoSet(undefined, {
			success : function(request, resultSet){
				_.each(resultSet.getResults(), function(result, path){
					node_objectOperationUtility.operateObject(output, path, node_CONSTANT.WRAPPER_OPERATION_SET, result);
				});
				return output;
			}
		});
		_.each(matchersByPath, function(matchers, path){
			var valueByPath = node_objectOperationUtility.getObjectAttributeByPath(globalData, path);
			matchersByPathRequest.addRequest(path, node_createExpressionService.getMatchDataRequest(valueByPath, matchers));
		});
		out.addRequest(matchersByPathRequest);
		return out;
	};

	var loc_getExecuteDataAssociationToTargetRequest = function(input, dataAssociation, target, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteDataAssociationToTarget", {}), handlers, request);
		out.addRequest(loc_getExecuteDataAssociationRequest(input, dataAssociation, {
			success :function(request, output){
				//assign task output back to output
				var isOutputFlat = dataAssociation[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATIONGROUP_FLATOUTPUT];
				return loc_out.assignToContext(output, target, isOutputFlat);
			}
		}));
		return out;
	};

	
	var loc_out = {
			
		getExecuteIOTaskRequest : function(input, inputDataAssociation, getTaskRequest, outputDataAssociationByResult, output, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteIOTask", {}), handlers, request);
			//process input association
			out.addRequest(loc_getExecuteDataAssociationRequest(input, inputDataAssociation, {
				success : function(requestInfo, taskInput){
					//execute task
					var executeIOTaskRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteTask", {}));
					executeIOTaskRequest.addRequest(getTaskRequest(taskInput, {
						success : function(request, taskResult){
							//process output association according to result name
							return loc_getExecuteDataAssociationToTargetRequest(taskResult.resultValue, outputDataAssociationByResult[taskResult.resultName], output, {
								success :function(request, output){
									return new node_IOTaskResult(taskResult.resultName, output);
								}
							});
						}
					}));
					return executeIOTaskRequest;
				}
			}));
			return out;
		},
			
		getExecuteDataAssociationRequest : function(input, dataAssociation, handlers, request){
			return loc_getExecuteDataAssociationRequest(input, dataAssociation, handlers, request);
		},
		
		getExecuteDataAssociationToTargetRequest : function(input, dataAssociation, target, handlers, request){
			return loc_getExecuteDataAssociationToTargetRequest(input, dataAssociation, target, handlers, request);
		},
		
		getContextTypes : function(){
			return [ 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PUBLIC, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PROTECTED, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_INTERNAL, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PRIVATE 
			];
		},
		
		assignToContext : function(source, target, isFlat){
			if(target==undefined)   target = {};
			if(isFlat==true){
				_.each(source, function(value, name){
					target[name] = value;
				});
			}
			else{
				_.each(source, function(c, categary){
					var cc = target[categary];
					if(cc==undefined){
						cc = {};
						target[categary] = cc;
					}
					_.each(c, function(ele, name){
						cc[name] = ele;
					});
				});
			}
			return target;
		}


	};
		
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.EndActivityOutput", function(){node_EndActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.IOTaskResult", function(){node_IOTaskResult = this.getData();});

//Register Node by Name
packageObj.createChildNode("ioTaskUtility", node_utility); 

})(packageObj);
