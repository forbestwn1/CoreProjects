function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_valueInVarOperationServiceUtility = nosliw.getNodeData("variable.valueinvar.operation.valueInVarOperationServiceUtility");
	var node_basicUtility = nosliw.getNodeData("common.utility.basicUtility");
	var node_createValuePortElementInfo = nosliw.getNodeData("valueport.createValuePortElementInfo");
	var node_ruleExecuteUtility = nosliw.getNodeData("rule.ruleExecuteUtility");



	var loc_parms;
    var loc_scriptVars;
    var loc_unknownVars;
    var loc_extendVars;
	var loc_configure;

	var loc_tasks;

    var loc_bundleCore;

	var loc_valueContext;

	var loc_variableInfos = [];
	var loc_extendVariableInfos = [];
	
	var loc_envInterface = {};

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		
		loc_parms = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCTESTCOMPLEXSCRIPT_PARM);
    	loc_scriptVars = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTESTCOMPLEXSCRIPT_VARIABLE);
    	loc_unknownVars = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTESTCOMPLEXSCRIPT_UNKNOWNVARIABLE);
    	loc_extendVars = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTESTCOMPLEXSCRIPT_VARIABLEEXTENDED);
    	loc_tasks = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTESTCOMPLEXSCRIPT_TASK);
		loc_configure = configure;
	
        loc_bundleCore = bundleCore;	
	
		var varDomain = bundleCore.getValuePortDomain();
		loc_valueContext = varDomain.creatValuePortContainer(valueContextId);

/*
		//extended variable
		if(loc_extendVars!=undefined&&loc_extendVars.length>0){
			_.each(loc_extendVars, function(extendVar, i){
				var varInfo = {
					variable : loc_valueContext.createVariableById(extendVar[node_COMMONATRIBUTECONSTANT.EXECUTABLEVARIABLEEXPECTED_VARIABLEID])
				};
				loc_variableInfos.push(varInfo);
			});
		}
*/		
	};

	var loc_updateDataDisplay = function(varInfo){
		varInfo.variable.executeDataOperationRequest(node_valueInVarOperationServiceUtility.createGetOperationService(), {
			success : function(request, data){
				var value;
    		    value = JSON.stringify(data, null, 4);
				varInfo.displayView.text(value);
			}	
		});
	};
	
	var loc_createVariableView = function(varInfo){
		var varContainerViewWrapper =  $('<div style="border:solid 3px;"></div>');

		var varViewWrapper = $('<div></div>');
		var varNameView = $('<p>Variable:'+node_basicUtility.stringify(varInfo.reference)+'</p>');
		var varIdView = $('<p>Variable Id:'+varInfo.variable.getVariableId()+'</p>');
		varViewWrapper.append(varNameView);	
		varViewWrapper.append(varIdView);	
		
		varContainerViewWrapper.append(varViewWrapper);	

		var viewWrapper = $('<div>Value:</div>');
		varInfo.view = $('<textarea rows="3" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
		viewWrapper.append(varInfo.view);
		varContainerViewWrapper.append(viewWrapper);	
		varInfo.view.bind('change', function(){
			var value = varInfo.view.val();
			if(value==undefined || value.trim()==""){
				value = undefined;
			}
			else {
				value = JSON.parse(varInfo.view.val());
			}
			
			var operationService = node_valueInVarOperationServiceUtility.createSetOperationService("", value);
			
			node_ruleExecuteUtility.executeExecuteRuleValidationRequest(varInfo.variable, operationService, loc_bundleCore, {
				success : function(request, result){
					var kkkk = 5555;
				}
			});
		
			varInfo.variable.executeDataOperationRequest(operationService);
		});					

		varInfo.displayView = $('<span/>');
		var displayViewWrapper = $('<div>ValueDisplay:</div>');
		displayViewWrapper.append(varInfo.displayView);
		varContainerViewWrapper.append(displayViewWrapper);	

		varInfo.variable.registerDataChangeEventListener(undefined, function(eventName, eventData){
			loc_updateDataDisplay(varInfo);
		});
		loc_updateDataDisplay(varInfo);
		return varContainerViewWrapper;
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
			rootView.append(containerView);	
				
			_.each(loc_variableInfos, function(varInfo, i){
				containerView.append(loc_createVariableView(varInfo));	
			});

			_.each(loc_extendVariableInfos, function(varInfo, i){
				containerView.append(loc_createVariableView(varInfo));	
			});
			
			var unknowVarStr = JSON.stringify(loc_unknownVars, null, 4);
			var unknownView =  $('<div>Unknow Variables : '+unknowVarStr+'</div>');
			rootView.append(unknownView);	
		},
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	return loc_out;
}
