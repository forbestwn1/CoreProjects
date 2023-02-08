function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
	var node_basicUtility = nosliw.getNodeData("common.utility.basicUtility");

	var loc_parms;
    var loc_scriptVars;
	var loc_configure;

	var loc_valueContext;

	var loc_variableInfos = [];
	

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		
		loc_parms = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTCOMPLEXSCRIPT_PARM);
    	loc_scriptVars = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTCOMPLEXSCRIPT_VARIABLE);
		loc_configure = configure;
	
		var varDomain = bundleCore.getVariableDomain();
		loc_valueContext = varDomain.getValueContext(valueContextId);

		_.each(loc_scriptVars, function(varResolve, i){
			var varInfo = {
				resolve : varResolve,
				variable : loc_valueContext.createVariable(varResolve),
			};
			loc_variableInfos.push(varInfo);
		});
	};

	var loc_out = {
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){
			var rootView =  $('<div>' + '</div>');
			$(runtimeContext.view).append(rootView);
			
			var containerView =  $('<div></div>');
			rootView.append(containerView);	
				
			_.each(loc_variableInfos, function(varInfo, i){

				var varContainerViewWrapper =  $('<div style="border:solid 3px;"></div>');
				containerView.append(varContainerViewWrapper);	

				var varViewWrapper = $('<div>Variable:'+node_basicUtility.stringify(varInfo.resolve[node_COMMONATRIBUTECONSTANT.INFOREFERENCERESOLVE_ELEREFERENCE])+'</div>');
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
					varInfo.variable.executeDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(), {
						success : function(request, data){
							varInfo.displayView.text(data.value.value);
						}	
					});
				});
			});
			
		
		},
		
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	return loc_out;
}
