function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");

	var loc_stateValueView;
	var loc_parmsView;
	var loc_configureView;
	
	var loc_getUpdateRuntimeContextRequest = function(runtimeContext){
		var rootViewWrapper = $('<div>I am here</div>');
		$(runtimeContext.view).append(rootViewWrapper);
	};

	var loc_out = {
		
		updateView : function(view){
			var rootViewWrapper = $('<div>I am here</div>');
			$(view).append(rootViewWrapper);
		},
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){		
			return loc_getUpdateRuntimeContextRequest(runtimeContext);	
		},
		
	};
	return loc_out;
}
