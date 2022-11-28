function(parms, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");

	var loc_stateValueView;
	var loc_parmsView;
	var loc_configureView;
	
	var loc_parms = parms;
	var loc_configure = configure;

	var loc_getUpdateRuntimeContextRequest = function(runtimeContext){
		loc_configureView =  $('<div>configures:' + JSON.stringify(loc_configure) + '</div>');
		$(runtimeContext.view).append(loc_configureView);
	};

	var loc_out = {
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){		
			return loc_getUpdateRuntimeContextRequest(runtimeContext);	
		},
		
	};
	return loc_out;
}
