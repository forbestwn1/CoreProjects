function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_createValuePortElementInfo = nosliw.getNodeData("valueport.createValuePortElementInfo");
	var node_complexEntityUtility = nosliw.getNodeData("complexentity.complexEntityUtility");
	var node_taskUtility = nosliw.getNodeData("task.taskUtility");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_getWithValuePortInterface = nosliw.getNodeData("valueport.getWithValuePortInterface");
	var node_getEntityObjectInterface = nosliw.getNodeData("complexentity.getEntityObjectInterface");
	var node_utilityNamedVariable = nosliw.getNodeData("valueport.utilityNamedVariable");
	var node_makeObjectWithApplicationInterface = nosliw.getNodeData("component.makeObjectWithApplicationInterface");
	var node_interactiveUtility = nosliw.getNodeData("task.interactiveUtility");

	var loc_valueContext;

	var loc_envInterface = {};
	
	var loc_taskContext;

	var loc_facadeTaskFactory = {
		//return a task
		createTask : function(taskContext){
			loc_taskContext = taskContext;
			return loc_out;
		},
	};

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		
	};

	var loc_out = {
		
		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(){
		},
		
		updateView : function(view){
		},

		getTaskInitRequest : function(handlers, request){
			if(loc_taskContext!=undefined){
				return loc_taskContext.getInitTaskRequest(loc_out, handlers, request);
			}
		},

		getTaskExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var valuePortContainer = node_getEntityObjectInterface(loc_out).getInternalValuePortContainer();
			var withValuePort = loc_envInterface[node_CONSTANT.INTERFACE_WITHVALUEPORT];

			var dataVariable = valuePortContainer.getVariableByName(node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_INTERACTIVETASK, node_COMMONCONSTANT.VALUEPORT_NAME_INTERACT_REQUEST, node_COMMONCONSTANT.NAME_ROOT_DATA);
			out.addRequest(dataVariable.getGetValueRequest({
				success : function(request, dataValue){
					var taskResult;
					if(dataValue==undefined){
						var errorMessage = {
							"dataTypeId": "test.string;1.0.0",
							"value": "value should not be empty"
						};
						var errorValue = {};
						errorValue[node_COMMONCONSTANT.NAME_ROOT_ERROR] = errorMessage; 
						return node_utilityNamedVariable.setValuesPortValueRequest(valuePortContainer, node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_INTERACTIVETASK, node_interactiveUtility.getResultValuePortNameByResultName(node_COMMONCONSTANT.TASK_RESULT_FAIL), errorValue, {
							success : function(request){
								loc_taskResult = {
								    "resultName": node_COMMONCONSTANT.TASK_RESULT_FAIL,
								    "resultValue": errorMessage
								};
							}
						});
					}
					else{
						loc_taskResult = {
						    "resultName": "success"
						};
					}
					
					return taskResult;
				}
			}));

			return out;
		},
		
		getTaskResult : function(){   return loc_taskResult;    }
		
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	loc_out = node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASKFACTORY, loc_facadeTaskFactory);
	return loc_out;
}
