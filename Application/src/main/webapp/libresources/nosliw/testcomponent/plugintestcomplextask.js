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

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		var varDomain = bundleCore.getVariableDomain();
		loc_valueContext = varDomain.creatValuePortContainer(valueContextId);
    	loc_variablesInTask = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTESTCOMPLEXTASK_VARIABLE);
    	loc_taskInteractive = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTESTCOMPLEXTASK_INTERACTIVETASK);
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
					var executeTaskInteractiveRequest = node_taskUtility.getExecuteTaskWithAdapterRequest(loc_out, loc_taskContext);
					node_requestServiceProcessor.processRequest(executeTaskInteractiveRequest);
				});
			}
			
		},
		
		getTaskInitRequest : function(handlers, request){
			return loc_taskContext.getInitTaskRequest(loc_out, handlers, request);
		},
		
		getTaskExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

			if(loc_taskInteractive!=undefined){
				_.each(loc_variablesInTask, function(varResolve, name){
					if(name.startWith('task_request_')){
						
					}
					
				});
								
			}
			else{
				var varsRequest = node_createServiceRequestInfoSet(undefined, {
					success : function(request, variablesResult){
						loc_taskResult = {};
						loc_taskResult.data = variablesResult.getResults();
						loc_taskResult.resultName = "success";
						return loc_taskResult;
					}
				});
				_.each(loc_variablesInTask, function(varResolve, name){
					var valuePortId = varResolve[node_COMMONATRIBUTECONSTANT.RESULTREFERENCERESOLVE_VALUEPORTID];
					var valuePort = loc_envInterface[node_CONSTANT.INTERFACE_WITHVALUEPORT].getValuePort(valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_GROUP], valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_NAME]);
					var valuePortEleInfo = node_createValuePortElementInfo(varResolve[node_COMMONATRIBUTECONSTANT.RESULTREFERENCERESOLVE_STRUCTUREID], varResolve[node_COMMONATRIBUTECONSTANT.RESULTREFERENCERESOLVE_FULLPATH]);
					varsRequest.addRequest(name, valuePort.getValueRequest(valuePortEleInfo));
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


//Register Node by Name
packageObj.createChildNode("createTestComplexTaskPlugin", node_createTestComplexTaskPlugin); 

})(packageObj);
