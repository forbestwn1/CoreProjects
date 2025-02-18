//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildComponentInterface;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_componentUtility;
	var node_getEntityObjectInterface;
	var node_utilityNamedVariable;
	var node_makeObjectWithApplicationInterface;
	var node_interactiveUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createFlowTaskPlugin = function(){
	
	var loc_out = {

		getCreateEntityCoreRequest : function(entityDef, internalValuePortContainerId, externalValuePortContainerId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createFlowTaskCore(entityDef, configure);
			}, handlers, request);
		},
	};

	return loc_out;
};


var loc_createFlowTaskCore = function(entityDef, configure){
	
	var loc_valueContext;

	var loc_entityDef = entityDef;

	var loc_envInterface = {};
	
	var loc_taskContext;

	var loc_facadeTaskFactory = {
		//return a task
		createTask : function(taskContext){
			loc_taskContext = taskContext;
			return loc_out;
		},
	};

	var loc_getExecuteActivityRequest = function(target, handlers, request){
		
	};

	var loc_getProcessNextRequest = function(next, handlers, request){
		
	};

	var loc_out = {

		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createAttributeRequest(node_COMMONATRIBUTECONSTANT.BLOCKTASKFLOWFLOW_ACTIVITY, undefined));
			return out;
		},
		
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

			var start = loc_entityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTASKFLOWFLOW_START);



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

	loc_out = node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASKFACTORY, loc_facadeTaskFactory);
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.utilityNamedVariable", function(){node_utilityNamedVariable = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithApplicationInterface", function(){node_makeObjectWithApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("task.interactiveUtility", function(){node_interactiveUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createFlowTaskPlugin", node_createFlowTaskPlugin); 

})(packageObj);
