function(complexEntityDef, variableGroupId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");

	var valueDomain = bundleCore.getVariableDomain();

	var variableGroup;

	var loc_parms = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTCOMPLEXSCRIPT_PARM);
    var loc_scriptVars = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTCOMPLEXSCRIPT_PARM);
	var loc_configure = configure;

	var loc_variableInfos = [];

	var loc_init(){
		_.each(loc_scriptVars[EMBEDEDEXECUTABLEWITHVALUE_VALUE], function(varResolve, i){
			var varInfo = {
				resolve : varResolve
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
				//create variable
				varInfo.variable = variableGroup.createVariable(varInfo.resolve);
			
				varInfo.view = $('<textarea rows="3" cols="150" style="resize: none;" data-role="none"></textarea>');
				containerView.append(varInfo.view);	
				varInfo.view.bind('change', function(){
				
					var value = varInfo.view.val();
					if(value==undefined || value==""){}
					else {
						value = {
							dataTypeId: "test.string;1.0.0",
							value: varInfo.view.val()
						};
					}
				
					varInfo.variable.setValue(value);
				
				});					

				varInfo.variable.registerDataChangeEventListener(undefined, function(eventName, eventData){
					varInfo.view.val(eventData.value);
				});
			});
			
		
		},
		
	};
	
	loc_init();
	return loc_out;
};
