
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"script",
"version":"1.0.0"
},
"id":"*task_complex_sequence_test"
},
"children":[],
"dependency":{},
"info":{}
}, {"script":function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_createValuePortElementInfo = nosliw.getNodeData("valueport.createValuePortElementInfo");
	var node_complexEntityUtility = nosliw.getNodeData("complexentity.complexEntityUtility");
	var node_taskExecuteUtility = nosliw.getNodeData("task.taskExecuteUtility");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_getWithValuePortInterface = nosliw.getNodeData("valueport.getWithValuePortInterface");
	var node_getEntityObjectInterface = nosliw.getNodeData("complexentity.getEntityObjectInterface");
	var node_utilityNamedVariable = nosliw.getNodeData("valueport.utilityNamedVariable");
	var node_makeObjectWithApplicationInterface = nosliw.getNodeData("component.makeObjectWithApplicationInterface");
	var node_interactiveUtility = nosliw.getNodeData("task.interactiveUtility");
	var node_createTaskCore = nosliw.getNodeData("task.createTaskCore");

    var loc_bundleCore;
    var loc_complexEntityDef;
    var loc_tasksInputName;

	var loc_envInterface = {};
	
	var loc_taskCore;
	
	var loc_facadeTaskCore = {
		//return a task
		getTaskCore : function(){
			return loc_taskCore;
		},
	};

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		loc_bundleCore = bundleCore;
		loc_complexEntityDef = complexEntityDef;
		loc_taskCore = node_createTaskCore(loc_out, loc_out);
        loc_tasksInputName = node_complexEntityUtility.getParmValue(loc_complexEntityDef, "tasks"); 
	};

	var loc_out = {
		
		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(){
		},
		
		updateView : function(view){
		},

		getTaskExecuteRequest : function(taskRuntimeEnv, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var allResults = [];
			var dynamicInputs = loc_bundleCore.getDynamicInputContainer().getDynamicInput(loc_tasksInputName);
			_.each(dynamicInputs, function(dynamicInput){
				out.addRequest(node_taskExecuteUtility.getExecuteInteractiveBrickPackageRequest(dynamicInput.getCoreEntityPackage(), undefined, {
					success : function(request, taskResult){
						allResults.push(taskResult);
					}
				}));
			});
			
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				return {
				    "resultName": "success",
				    "resultValue": allResults
				};
			}));
			
			return out;
		},
		
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	loc_out = node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK, loc_facadeTaskCore);
	return loc_out;
}

}, {"loadPattern":"file"
});

