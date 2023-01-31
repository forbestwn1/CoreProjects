function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");

	var loc_parmsView;
	
	var loc_configure = configure;

	var loc_parms = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTCOMPLEXSCRIPT_PARM);

	var loc_getUpdateRuntimeContextRequest = function(runtimeContext){
		loc_parmsView =  $('<div>parms:' + JSON.stringify(loc_parms) + '</div>');
		$(runtimeContext.view).append(loc_parmsView);
	};

	var loc_out = {
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){		
			return loc_getUpdateRuntimeContextRequest(runtimeContext);	
		},
	};
	return loc_out;
}
