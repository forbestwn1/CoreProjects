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
	var node_createTaskCore = nosliw.getNodeData("task.createTaskCore");

	var loc_envInterface = {};
	
	var loc_taskCore;
	
	var loc_facadeTaskCore = {
		//return a task
		getTaskCore : function(){
			return loc_taskCore;
		},
	};

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		loc_taskCore = node_createTaskCore(loc_out, loc_out);
	};

	var loc_out = {
		
		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(){
		},
		
		getTaskExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

			out.addRequest(loc_taskCore.getRuntimeEnv().getAllValueInfoRequest({
				success : function(request, valueInfo){
					console.log(JSON.stringify(valueInfo));
					return {
					    "resultName": "success"
					};
				}
			}));

			return out;
		},
		
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	loc_out = node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK, loc_facadeTaskCore);
	return loc_out;
}
