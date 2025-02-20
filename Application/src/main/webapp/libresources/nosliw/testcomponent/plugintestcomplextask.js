//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildComponentInterface;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_ResourceId;
	var node_resourceUtility;
	var node_componentUtility;
	var node_createValuePortElementInfo;
	var node_makeObjectWithApplicationInterface;
	var node_valuePortUtilityResolvedVariable;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createTestComplexTaskPlugin = function(){
	
	var loc_out = {

		getCreateEntityCoreRequest : function(complexEntityDef, internalValuePortContainerId, externalValuePortContainerId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createTestTaskCore(complexEntityDef, internalValuePortContainerId, bundleCore, configure)
			}, handlers, request);
		},
	};

	return loc_out;
};

var loc_createTestTaskCore = function(complexEntityDef, valueContextId, bundleCore, configure){

	var loc_valueContext;

	var loc_envInterface = {};

	var loc_taskContext;
	var loc_taskResult;
	
	var loc_variablesInTask;
	var loc_taskInteractive;
	var loc_taskInteractiveResult;
	var loc_expressionInteractive;

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		var varDomain = bundleCore.getVariableDomain();
		loc_valueContext = varDomain.creatValuePortContainer(valueContextId);
    	loc_variablesInTask = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTESTCOMPLEXTASK_VARIABLE);
    	loc_taskInteractive = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTESTCOMPLEXTASK_INTERACTIVETASK);
    	loc_taskInteractiveResult = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTESTCOMPLEXTASK_INTERACTIVETASKRESULT);
    	loc_expressionInteractive = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTESTCOMPLEXTASK_INTERACTIVEEXPRESSION);
	};

	var loc_facadeTaskFactory = {
		//return a task
		createTask : function(taskContext){
			loc_taskContext = taskContext;
			return loc_out;
		},
	};

	var loc_out = {
		
		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(){
		},
		
		updateView : function(view){
			var rootView =  $('<div>Task' + '</div>');
			$(view).append(rootView);
			
			if(loc_taskInteractive!=undefined){
				var taskInteractiveTrigueView = $('<button>Execute Task Interactive</button>');
				rootView.append(taskInteractiveTrigueView);
				taskInteractiveTrigueView.click(function() {
					var executeTaskInteractiveRequest = node_taskUtility.getExecuteTaskWithAdapterRequest(loc_out, undefined, loc_taskContext);
					node_requestServiceProcessor.processRequest(executeTaskInteractiveRequest);
				});
			}
			else if(loc_expressionInteractive!=undefined){
				var expressionInteractiveTrigueView = $('<button>Execute Expression Interactive</button>');
				rootView.append(expressionInteractiveTrigueView);
				expressionInteractiveTrigueView.click(function() {
					var executeExpressionInteractiveRequest = node_taskUtility.getExecuteTaskWithAdapterRequest(loc_out, undefined, loc_taskContext);
					node_requestServiceProcessor.processRequest(executeExpressionInteractiveRequest);
				});
			}
			
		},
		
		getTaskInitRequest : function(handlers, request){
			if(loc_taskContext!=undefined){
				return loc_taskContext.getInitTaskRequest(loc_out, handlers, request);
			}
		},
		
		getTaskExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var withValuePort = loc_envInterface[node_CONSTANT.INTERFACE_WITHVALUEPORT];

			if(loc_taskInteractive!=undefined){
				var requestPirs = [];
				var requestPre = 'task_request_'
				var resultPre = 'task_result_'
				
				//find all rquest/result var pair
				_.each(loc_variablesInTask, function(varResolve, name){
					if(name.startsWith(requestPre)){
						var coreName = name.substring(requestPre.length);
						var resultVarName = resultPre+loc_taskInteractiveResult+"_"+coreName;
						if(loc_variablesInTask[resultVarName]!=undefined){
							requestPirs.push([name, resultVarName]);
						}
					}
				});
				
				_.each(requestPirs, function(pair, i){
					var varAssignRequest = node_createServiceRequestInfoSequence();
					varAssignRequest.addRequest(node_valuePortUtilityResolvedVariable.getValuePortValueRequest(withValuePort, loc_variablesInTask[pair[0]], {
						success : function(request, varValue){
							return node_valuePortUtilityResolvedVariable.setValuePortValueRequest(withValuePort, loc_variablesInTask[pair[1]], varValue);
						}
					}));
					out.addRequest(varAssignRequest);
				});
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
					loc_taskResult = {};
					loc_taskResult.resultName = loc_taskInteractiveResult;
					return loc_taskResult;
				}));								
			}
			else if(loc_expressionInteractive!=undefined){
				var requestPirs = [];
				var requestForResultVarName = 'expression_request_forresult'
				var resultVarName = node_COMMONCONSTANT.NAME_ROOT_RESULT;

				var varAssignRequest = node_createServiceRequestInfoSequence();
				varAssignRequest.addRequest(node_valuePortUtilityResolvedVariable.getValuePortValueRequest(withValuePort, loc_variablesInTask[requestForResultVarName], {
					success : function(request, varValue){
						return node_valuePortUtilityResolvedVariable.setValuePortValueRequest(withValuePort, loc_variablesInTask[resultVarName], varValue);
					}
				}));
				out.addRequest(varAssignRequest);

				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
					loc_taskResult = {};
					loc_taskResult.resultName = loc_taskInteractiveResult;
					return loc_taskResult;
				}));								
			}
			else{
				//
				var varsRequest = node_createServiceRequestInfoSet(undefined, {
					success : function(request, variablesResult){
						loc_taskResult = {};
						loc_taskResult.resultValue = variablesResult.getResults();
						loc_taskResult.resultName = "success";
						return loc_taskResult;
					}
				});
				_.each(loc_variablesInTask, function(varId, name){
					varsRequest.addRequest(name, node_valuePortUtilityResolvedVariable.getValuePortValueRequest(withValuePort, varId));
				});
	
				out.addRequest(varsRequest);
			}
			
			return out;
		},
		
		getTaskResult : function(){   return loc_taskResult;    }
		
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	loc_out = node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASKFACTORY, loc_facadeTaskFactory);
	return loc_out;
}


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.createValuePortElementInfo", function(){node_createValuePortElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithApplicationInterface", function(){node_makeObjectWithApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.valuePortUtilityResolvedVariable", function(){node_valuePortUtilityResolvedVariable = this.getData();});


//Register Node by Name
packageObj.createChildNode("createTestComplexTaskPlugin", node_createTestComplexTaskPlugin); 

})(packageObj);
