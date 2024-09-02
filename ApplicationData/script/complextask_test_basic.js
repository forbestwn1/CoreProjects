function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_createValuePortElementInfo = nosliw.getNodeData("valueport.createValuePortElementInfo");

	var loc_parms;
    var loc_scriptVars;
	var loc_configure;

	var loc_tasks;

	var loc_valueContext;

	var loc_envInterface = {};

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		
		loc_parms = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCTESTCOMPLEXSCRIPT_PARM);
    	loc_events = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTESTCOMPLEXSCRIPT_EVENT);
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
			_.each(loc_events, function(event, i){
				var eventName = event[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME];
				var eventTrigueView = $('<button>Triggue Event : '+eventName+'</button>');
				containerView.append(eventTrigueView);	

				eventTrigueView.click(function() {
					var relativePath = event[node_COMMONATRIBUTECONSTANT.TESTEVENT_EVENTINFO][node_COMMONATRIBUTECONSTANT.IDVALUEPORTINBUNDLE_BRICKID][node_COMMONATRIBUTECONSTANT.IDBRICKINBUNDLE_RELATIVEPATH];
					var handlerEntityCore = node_complexEntityUtility.getBrickCoreByRelativePath(baseEntityCore, relativePath);
					var handlerFactoryInterface = node_makeObjectWithApplicationInterface(handlerEntityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASKFACTORY);
					
					var taskContext = {
						initRequest : function(){
							//set event data to value port
							
						}
					};
					var task = handlerFactoryInterface.createTask(taskContext);
					var taskExeRequest = task.getExecuteRequest();
					node_requestServiceProcessor.processRequest(taskExeRequest);
				});
			});
			
			rootView.append(containerView);	
		},
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	return loc_out;
}
