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
	var node_createIODataSet;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
	
	var loc_dyanimicValueBuild = function(output, outputPathSegs, input, intpuPathSegs){
		var inputValue = node_objectOperationUtility.getObjectAttributeByPathSegs(input, intpuPathSegs);
		node_objectOperationUtility.operateObjectByPathSegs(output, outputPathSegs, node_CONSTANT.WRAPPER_OPERATION_SET, inputValue);
		return output;
	};
	
	var loc_executeDataAssociationConvertFun  = function(association, input){
		if(association==undefined || association[node_COMMONATRIBUTECONSTANT.EXECUTABLEASSOCIATION_CONVERTFUNCTION]==undefined) return undefined;
		input = node_createIODataSet(input).getDataSet();
		return association[node_COMMONATRIBUTECONSTANT.EXECUTABLEASSOCIATION_CONVERTFUNCTION](input, loc_dyanimicValueBuild);
	};

	var loc_getExecuteAssociationRequest = function(input, association, outputTarget, handlers, request){
		var service = new node_ServiceInfo("ExecuteAssociation", {"input":input, "association":association});
		var out = node_createServiceRequestInfoSequence(service, handlers, request);

		//use convert function to calculate output
		var output = loc_executeDataAssociationConvertFun(association, input); 
		if(output==undefined){
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(){  
				return outputTarget;  
			}));
		}
		else{
			//process matchers
			var matchersByPath = association[node_COMMONATRIBUTECONSTANT.EXECUTABLEASSOCIATION_OUTPUTMATCHERS];
			if(matchersByPath==undefined){
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(){ return output;  })); 
			}
			else{
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
			}
			
			//to target
			if(outputTarget!=undefined){
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(){ 
					var isOutputFlat = association[node_COMMONATRIBUTECONSTANT.EXECUTABLEASSOCIATION_FLATOUTPUT];
					return loc_out.assignToContext(output, outputTarget, isOutputFlat);
				})); 
			}
		}
		
		return out;
	};
	
	var loc_getExecuteMappingDataAssociationRequest = function(input, dataAssociation, target, handlers, request){
		var service = new node_ServiceInfo("ExecuteDataAssociation", {"input":input, "dataAssociation":dataAssociation});
		var out = node_createServiceRequestInfoSequence(service, handlers, request);

		var output = {};
		var executeAssociationsRequest = node_createServiceRequestInfoSet(undefined, {
			success : function(request, resultSet){
				var outputDataSet = node_createIODataSet();
				_.each(resultSet.getResults(), function(result, targetName){
					outputDataSet.setData(targetName, result);
				});
				return outputDataSet;
			}
		});
		
		var targetDataSet = node_createIODataSet(target);
		_.each(dataAssociation[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATION_ASSOCIATION], function(association, targetName){
			executeAssociationsRequest.addRequest(targetName, loc_getExecuteAssociationRequest(input, association, targetDataSet.getData(targetName)));
		});
		out.addRequest(executeAssociationsRequest);

		return out;
	};

	var loc_getExecuteDataAssociationRequest = function(input, dataAssociation, target, handlers, request){
		var type = dataAssociation[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATION_TYPE];
		if(type==node_COMMONCONSTANT.DATAASSOCIATION_TYPE_MAPPING){
			return loc_getExecuteMappingDataAssociationRequest(input, dataAssociation, target, handlers, request);
		}
	};
	
	var loc_out = {
			
		getExecuteIOTaskRequest : function(input, inputDataAssociation, getTaskRequest, outputDataAssociationByResult, output, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteIOTask", {}), handlers, request);
			//process input association
			out.addRequest(loc_getExecuteDataAssociationRequest(input, inputDataAssociation, null, {
				success : function(requestInfo, daOutputDataSet){
					var taskInput = daOutputDataSet.getData();
					//execute task
					var executeIOTaskRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteTask", {}));
					executeIOTaskRequest.addRequest(getTaskRequest(taskInput, {
						success : function(request, taskResult){
							//process output association according to result name
							var outputDataAssociation;
							if(typeof outputDataAssociationByResult === "function"){
								outputDataAssociation = outputDataAssociationByResult(taskResult.resultName);
							}
							else   outputDataAssociation = outputDataAssociationByResult[taskResult.resultName];
							
							return loc_getExecuteDataAssociationRequest(taskResult.resultValue, outputDataAssociation, output, {
								success :function(request, taskOutputDataSet){
									return new node_IOTaskResult(taskResult.resultName, taskOutputDataSet);
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
			return loc_getExecuteDataAssociationRequest(input, dataAssociation, null, handlers, request);
		},
		
		getExecuteDataAssociationToTargetRequest : function(input, dataAssociation, target, handlers, request){
			return loc_getExecuteDataAssociationRequest(input, dataAssociation, target, handlers, request);
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
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});

//Register Node by Name
packageObj.createChildNode("ioTaskUtility", node_utility); 

})(packageObj);
