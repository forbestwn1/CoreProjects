function(complexEntityDef, variableContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");

	var loc_stateValueView;
	var loc_parmsView;
	var loc_configureView;
	
	var loc_configure = configure;

	var loc_configures = [];
	
	var loc_init = function(complexEntityDef, variableContextId, bundleCore, configure){
		var parms = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTCOMPLEXSCRIPT_PARM);

		loc_configures.push({all : configure.getConfigureValue()});
		
		var configureDefs = parms.configures;
		if(configureDefs!=undefined){
			_.each(configureDefs, function(configureDef, i){
				var configurPath;
				var configurePart;
				if(typeof configureDef === 'string'){
					configurPath = configureDef;
				}
				else if(typeof configureDef === 'object'){
					configurPath = configureDef.path;
					configurePart = configureDef.part;
				}
				
				loc_configures.push(
					{
						definition : configureDef,
						value : configure.getChildConfigure(configurPath, configurePart).getConfigureValue()		
					}
				);
			});
		}
	};

	var loc_getUpdateRuntimeContextRequest = function(runtimeContext){

		var wrapperView =  $('<div></div>');

		_.each(loc_configures, function(configureInfo, i){
			var logView = $('<textarea rows="10" cols="150" style="resize: none;" data-role="none"></textarea>');
			logView.val(JSON.stringify(configureInfo, null, 4));
			wrapperView.append(logView);
		});
		
		$(runtimeContext.view).append(wrapperView);
	};

	var loc_out = {
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){		
			return loc_getUpdateRuntimeContextRequest(runtimeContext);	
		},
		
	};
	
	loc_init(complexEntityDef, variableContextId, bundleCore, configure);
	return loc_out;
}
