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
	

	var loc_out = {
			
		getExecuteDataAssociationWithTargetRequest : function(input, inputDataAssociation, getTaskRequest, outputDataAssociationByResult, context, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteDataAssociationWithTarget", {}), handlers, request);
			//calculate input for activity first
			out.addRequest(loc_out.getExecuteDataAssociationRequest(inputDataAssociation, input, {
				success : function(requestInfo, taskInput){
					var executeIOTaskRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteIOTask", {}));
					
					executeIOTaskRequest.addRequest(getTaskRequest(taskInput, {
						success : function(request, taskResult){
							return loc_out.getBackToGlobalRequest(taskResult.resultValue, outputDataAssociationByResult[taskResult.resultName], {
								success :function(request, output){
									//build context
									var isOutputFlat = outputDataAssociationByResult[taskResult.resultName][node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATIONGROUP_FLATOUTPUT];
									if(isOutputFlat==true){
										_.each(output, function(value, name){
											context[name] = value;
										});
									}
									else{
										_.each(output, function(c, categary){
											var cc = context[categary];
											if(cc==undefined){
												cc = {};
												context[categary] = cc;
											}
											_.each(c, function(ele, name){
												cc[name] = ele;
											});
										});
									}
									return new node_IOTaskResult(taskResult.resultName, context);
								}
							});
						}
					}));
					return executeIOTaskRequest;
				}
			}));
			return out;
		},
			
		getExecuteDataAssociationRequest : function(dataAssociation, input, handlers, request){
			var out = node_createServiceRequestInfoSimple(new node_ServiceInfo("executeDataAssociationRequest", {"dataAssociation":dataAssociation, "input":input}), 
				function(requestInfo){
					if(dataAssociation==undefined)  return;
					return dataAssociation[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATIONGROUP_CONVERTFUNCTION](input, loc_dyanimicValueBuild);
				}, 
				handlers, request);
			return out;
		},

		getBackToGlobalRequest : function(data, dataAssociation, handlers, request){
			var service = new node_ServiceInfo("BackToGlobal", {"data":data, "dataAssociation":dataAssociation});
			if(dataAssociation==undefined)   return node_createServiceRequestInfoSimple(service, function(){}, handlers, request);
			
			var out = node_createServiceRequestInfoSequence(service, handlers, request);
			out.addRequest(loc_out.getExecuteDataAssociationRequest(dataAssociation, data, {
				success : function(request, globalData){
					//process matchers
					var matchersByPath = dataAssociation[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATIONGROUP_OUTPUTMATCHERS];
					if(matchersByPath==undefined)  return globalData;
					
					var matchersByPathRequest = node_createServiceRequestInfoSet(undefined, {
						success : function(request, resultSet){
							_.each(resultSet.getResults(), function(result, path){
								node_objectOperationUtility.operateObject(globalData, path, node_CONSTANT.WRAPPER_OPERATION_SET, result);
							});
							return globalData;
						}
					});
					_.each(matchersByPath, function(matchers, path){
						var valueByPath = node_objectOperationUtility.getObjectAttributeByPath(globalData, path);
						matchersByPathRequest.addRequest(path, node_createExpressionService.getMatchDataRequest(valueByPath, matchers));
					});
					return matchersByPathRequest;
				}
			}));
			return out;
		},

		
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
nosliw.registerSetNodeDataEvent("taskio.entity.IOTaskResult", function(){node_IOTaskResult = this.getData();});

//Register Node by Name
packageObj.createChildNode("ioUtility", node_utility); 

})(packageObj);
