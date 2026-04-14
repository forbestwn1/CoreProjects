
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"script",
"version":"1.0.0"
},
"id":"*task_adpater_execute"
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
	var node_pathUtility = nosliw.getNodeData("common.path.pathUtility");
	var node_getEntityTreeNodeInterface = nosliw.getNodeData("complexentity.getEntityTreeNodeInterface");

	var loc_envInterface = {};
	
	var loc_taskCore;
	
	var loc_complexEntityDef;
	var loc_bundleCore;
	
	var loc_brickAlias;
	var loc_adapterName;
	
	var loc_facadeTaskCore = {
		//return a task
		getTaskCore : function(){
			return loc_taskCore;
		},
	};

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		loc_complexEntityDef = complexEntityDef;
		loc_bundleCore = bundleCore;
		loc_taskCore = node_createTaskCore(loc_out, loc_out);
        loc_brickAlias = node_complexEntityUtility.getParmValue(loc_complexEntityDef, "alias"); 
		loc_adapterName = node_complexEntityUtility.getParmValue(loc_complexEntityDef, "adapter");
	};

	var loc_out = {
		
		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(){
		},
		
		updateView : function(view){
		},

		getTaskExecuteRequest : function(taskRuntimeEnv, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

			var brickPath = loc_bundleCore.getBrickDefPathByAlias(loc_brickAlias);
			var currentDefPath = loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].getDefPath();
			var relativePath = node_pathUtility.fromAbsoluteToRelativePath(brickPath, currentDefPath);

			var coreEntityForAdapter = node_complexEntityUtility.getCoreEntityReferenceByRelativePath(loc_out, relativePath).getBaseCoreEntity();
			var adapterTreeNodeEntityInterface = node_getEntityTreeNodeInterface(coreEntityForAdapter);
			var adapter = adapterTreeNodeEntityInterface.getAdapters()[loc_adapterName];
			out.addRequest(adapter.getExecuteRequest(coreEntityForAdapter, {
				success : function(){
					return {
					    "resultName": "success",
					    "resultValue": "adapter successfully"
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

}, {"loadPattern":"file"
});

