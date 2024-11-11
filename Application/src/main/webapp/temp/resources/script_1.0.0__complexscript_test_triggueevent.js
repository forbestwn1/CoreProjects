
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceTypeId":{"resourceType":"script",
"version":"1.0.0"
},
"id":"*complexscript_test_triggueevent"
},
"children":[],
"dependency":{},
"info":{}
}, {"script":function(complexEntityDef, valueContextId, bundleCore, configure){

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

	var loc_parms;
    var loc_scriptVars;
    var loc_taskTrigguers;
    
	var loc_configure;

	var loc_tasks;

	var loc_valueContext;

	var loc_envInterface = {};

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		
		loc_parms = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCTESTCOMPLEXSCRIPT_PARM);
    	loc_taskTrigguers = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTESTCOMPLEXSCRIPT_TASKTRIGGUER);
		loc_configure = configure;
	
		var varDomain = bundleCore.getVariableDomain();
		loc_valueContext = varDomain.creatValuePortContainer(valueContextId);

	};

	var loc_out = {
		
		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(){
			if(loc_scriptVars!=undefined&&loc_scriptVars.length>0){
				//all defined variable
				_.each(loc_scriptVars, function(varResolve, i){
					
					var valuePortId = varResolve[node_COMMONATRIBUTECONSTANT.RESULTREFERENCERESOLVE_VALUEPORTID];
					
					var valuePort = loc_envInterface[node_CONSTANT.INTERFACE_WITHVALUEPORT].getValuePort(valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_GROUP], valuePortId[node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBRICK_NAME]);
					var valuePortEleInfo = node_createValuePortElementInfo(varResolve[node_COMMONATRIBUTECONSTANT.RESULTREFERENCERESOLVE_STRUCTUREID], varResolve[node_COMMONATRIBUTECONSTANT.RESULTREFERENCERESOLVE_FULLPATH]);
					
					var varInfo = {
						reference : varResolve[node_COMMONATRIBUTECONSTANT.RESULTREFERENCERESOLVE_FULLPATH],
//						variable : loc_valueContext.createResolvedVariable(varResolve),
						variable : valuePort.createVariable(valuePortEleInfo)
					};
					loc_variableInfos.push(varInfo);
				});
			}
		},
		
		updateView : function(view){
			var rootView =  $('<div>' + '</div>');
			$(view).append(rootView);
			
			var containerView =  $('<div></div>');
			_.each(loc_taskTrigguers, function(taskTrigguer, i){
				var trigguerInfo = taskTrigguer[node_COMMONATRIBUTECONSTANT.TESTTASKTRIGGUER_TRIGGUERINFO];
				var trigguerName = trigguerInfo[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME];
				var taskTrigueView = $('<button>Triggue Task : '+trigguerName+'</button>');
				var eventResultView = $('<textarea rows="5" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
				containerView.append(taskTrigueView);	
				containerView.append(eventResultView);	

				taskTrigueView.click(function() {
					var relativePath = trigguerInfo[node_COMMONATRIBUTECONSTANT.INFOTRIGGUERTASK_HANDLERID][node_COMMONATRIBUTECONSTANT.IDBRICKINBUNDLE_RELATIVEPATH];
					var handlerEntityCore = node_complexEntityUtility.getBrickCoreByRelativePath(loc_out, relativePath);
					
					var taskContext = {
						getInitTaskRequest : function(coreEntity, handlers, request){
							//set event data to value port
							var internalValuePortContainer = node_getEntityObjectInterface(coreEntity).getExternalValuePortContainer();
							
							var valuePortName;
							var rootEleName;
							var trigguerType = trigguerInfo[node_COMMONATRIBUTECONSTANT.INFOTRIGGUERTASK_TRIGGUERTYPE];
							if(trigguerType==node_COMMONCONSTANT.TASK_TRIGGUER_DATAVALIDATION){
								valuePortName = node_COMMONCONSTANT.VALUEPORT_NAME_INTERACT_REQUEST;
								rootEleName = node_COMMONCONSTANT.NAME_ROOT_DATA;
							}
							else if(trigguerType==node_COMMONCONSTANT.TASK_TRIGGUER_EVENTHANDLE){
								valuePortName = node_COMMONCONSTANT.VALUEPORT_NAME_INTERACT_REQUEST;
								rootEleName = node_COMMONCONSTANT.NAME_ROOT_EVENT;
							}
							
							return node_utilityNamedVariable.setValuePortValueByGroupNameRequest(
								internalValuePortContainer,
								trigguerInfo[node_COMMONATRIBUTECONSTANT.INFOTRIGGUERTASK_VALUEPORTGROUPNAME],
								valuePortName,
								rootEleName,
								taskTrigguer[node_COMMONATRIBUTECONSTANT.TESTTASKTRIGGUER_TESTDATA],
								handlers, request);
							
							
							
//							var eventValuePort = node_getWithValuePortInterface(coreEntity).getValuePort(eventInfo[node_COMMONATRIBUTECONSTANT.INFOTRIGGUERTASK_VALUEPORTGROUPNAME], node_COMMONCONSTANT.VALUEPORT_NAME_EVENT);
//							var internalValuePortContainer = node_getEntityObjectInterface(coreEntity).getExternalValuePortContainer();
//							var valueStructureId = internalValuePortContainer.getValueStructureIdByGroupAndValuePort(node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_EVENT, node_COMMONCONSTANT.VALUEPORT_TYPE_EVENT);
//							return eventValuePort.setValueRequest(node_createValuePortElementInfo(undefined, node_COMMONCONSTANT.NAME_ROOT_EVENT), event[node_COMMONATRIBUTECONSTANT.TESTTASKTRIGGUER_TESTDATA], handlers, request);
						}
					};
					
					var taskExeRequest = node_taskUtility.getExecuteTaskWithAdapterRequest(handlerEntityCore, taskContext, {
						success : function(request, task){
							eventResultView.val(JSON.stringify(task.getTaskResult()));
						}
					});
					node_requestServiceProcessor.processRequest(taskExeRequest);
				});
			});
			
			rootView.append(containerView);	
		},
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	return loc_out;
}

}, {"loadPattern":"file"
});

