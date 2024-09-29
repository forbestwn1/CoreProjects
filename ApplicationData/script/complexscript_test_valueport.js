function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_uiDataOperationServiceUtility = nosliw.getNodeData("variable.uidataoperation.uiDataOperationServiceUtility");
	var node_basicUtility = nosliw.getNodeData("common.utility.basicUtility");
	var node_createValuePortElementInfo = nosliw.getNodeData("valueport.createValuePortElementInfo");

	var loc_parms;
    var loc_scriptVars;
    var loc_unknownVars;
    var loc_extendVars;
	var loc_configure;

	var loc_tasks;

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
	
		var varDomain = bundleCore.getVariableDomain();
		loc_valueContext = varDomain.creatValuePortContainer(valueContextId);

	};

	var loc_updateDataDisplay = function(varInfo){
		varInfo.variable.executeDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(), {
			success : function(request, data){
				var value;
				if(data!=undefined&&data.value!=undefined){
				    value = JSON.stringify(data.value, null, 4);
				}
				varInfo.displayView.text(value);
			}	
		});
	};
	
	var loc_createVariableView = function(varInfo){
		var varContainerViewWrapper =  $('<div style="border:solid 3px;"></div>');

		var varViewWrapper = $('<div></div>');
		var varNameView = $('<p>Variable:'+node_basicUtility.stringify(varInfo.reference)+'</p>');
		var varIdView = $('<p>Id:'+varInfo.variable.prv_id+'</p>');
		varViewWrapper.append(varNameView);	
		varViewWrapper.append(varIdView);	
		
		varContainerViewWrapper.append(varViewWrapper);	

		var viewWrapper = $('<div>Value:</div>');
		varInfo.view = $('<textarea rows="1" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
		viewWrapper.append(varInfo.view);
		varContainerViewWrapper.append(viewWrapper);	
		varInfo.view.bind('change', function(){
			var value = varInfo.view.val();
			if(value==undefined || value==""){}
			else {
				value = {
					dataTypeId: "test.string;1.0.0",
					value: varInfo.view.val()
				};
			}
		
			varInfo.variable.executeDataOperationRequest(node_uiDataOperationServiceUtility.createSetOperationService("", value));
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
		},
		
		updateView : function(view){
			var rootView =  $('<div>' + '</div>');
			var trigueView = $('<button>Check value port container</button>');
			var intputView = $('<textarea rows="1" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
			var resultView = $('<textarea rows="5" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
			
			rootView.append(trigueView);
			rootView.append(intputView);
			rootView.append(resultView);
			$(view).append(rootView);


			eventTrigueView.click(function() {
				var valuePortContainerId = intputView.val();
				var varDomain = bundleCore.getVariableDomain();
				var valuePortContainer = varDomain.getValuePortContainer();
				
			});

			
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
