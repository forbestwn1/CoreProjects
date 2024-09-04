function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_createValuePortElementInfo = nosliw.getNodeData("valueport.createValuePortElementInfo");
	var node_makeObjectWithApplicationInterface = nosliw.getNodeData("component.makeObjectWithApplicationInterface");

	var loc_valueContext;

	var loc_envInterface = {};

	var loc_taskContext;
	var loc_taskResult;
	
	var loc_variablesInTask;

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		var varDomain = bundleCore.getVariableDomain();
		loc_valueContext = varDomain.creatValuePortContainer(valueContextId);
    	loc_variablesInTask = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTESTCOMPLEXTASK_VARIABLE);
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
		},
		
		getTaskInitRequest : function(handlers, request){
			return loc_taskContext.getInitTaskRequest(loc_out, handlers, request);
		},
		
		getTaskExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var varsRequest = node_createServiceRequestInfoSet(undefined, {
				success : function(request, variablesResult){
					loc_taskResult = variablesResult.getResults();
				}
			});
			_.each(loc_variablesInTask, function(varResolve, name){
				var valuePortId = varResolve[node_COMMONATRIBUTECONSTANT.RESULTREFERENCERESOLVE_VALUEPORTID];
				var valuePort = loc_envInterface[node_CONSTANT.INTERFACE_WITHVALUEPORT].getValuePort(valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_GROUP], valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_NAME]);
				var valuePortEleInfo = node_createValuePortElementInfo(varResolve[node_COMMONATRIBUTECONSTANT.RESULTREFERENCERESOLVE_STRUCTUREID], varResolve[node_COMMONATRIBUTECONSTANT.RESULTREFERENCERESOLVE_FULLPATH]);
				varsRequest.addRequest(name, valuePort.getValueRequest(valuePortEleInfo));
			});

			out.addRequest(varsRequest);
			
			return out;
		},
		
		getTaskResult : function(){   return loc_taskResult;    }
		
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	loc_out = node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASKFACTORY, loc_facadeTaskFactory);
	return loc_out;
}
