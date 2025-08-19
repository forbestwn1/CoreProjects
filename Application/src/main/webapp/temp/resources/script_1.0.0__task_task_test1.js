
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"script",
"version":"1.0.0"
},
"id":"*task_task_test1"
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
		
		updateView : function(view){
		},

		getTaskExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var valuePortContainer = node_getEntityObjectInterface(loc_out).getInternalValuePortContainer();
			var withValuePort = loc_envInterface[node_CONSTANT.INTERFACE_WITHVALUEPORT];
			
			out.addRequest(node_interactiveUtility.getTaskRequestValuesFromValuePort(valuePortContainer, {
				success : function(request, requestValues){
					var resultValues = {};
					_.each(requestValues, function(data, name){
						var resultName = "success";
						var requestPre = 'task_request_';
						var resultPre = 'task_result_';
						if(name.startsWith(requestPre)){
							var coreName = name.substring(requestPre.length);
							var resultVarName = resultPre+resultName+"_"+coreName;
							resultValues[resultVarName] = data;
						}
					});
					
					var result = {
					    "resultName": "success",
					    "resultValue": resultValues
					};
					return  node_interactiveUtility.setTaskResultToValuePort(result, valuePortContainer, {
						success : function(){
							loc_taskResult = result;
							return result;
						}
					});
				}
			}));
			

/*			
			var result = {
			    "resultName": "success",
			    "resultValue": {
			        "task_result_success_string1": {
			            "dataTypeId": "test.string;1.0.0",
			            "valueFormat": "JSON",
			            "value": "default value of parm111111",
			            "info": {}
			        }
			    }
			};

			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(node_interactiveUtility.setTaskResultToValuePort(result, valuePortContainer, {
				success : function(){
					loc_taskResult = result;
					return result;
				}
			}));
*/
			
			return out;
		},
		
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	loc_out = node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK, loc_facadeTaskCore);
	return loc_out;
}

}, {"loadPattern":"file"
});

