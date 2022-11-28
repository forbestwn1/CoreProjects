function(parms, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");

	var loc_stateValueView;
	var loc_parmsView;
	var loc_configureView;
	
	var loc_parms = parms;
	var loc_configure = configure;

	var loc_getUpdateRuntimeContextRequest = function(runtimeContext){
		var rootViewWrapper = $('<div/>');
		$(runtimeContext.view).append(rootViewWrapper);

		var stateValueViewWrapper = $('<div><span>state:</span></div>');
		loc_stateValueView = $('<input type="text" style="background-color:pink">');
		stateValueViewWrapper.append(loc_stateValueView);
		rootViewWrapper.append(stateValueViewWrapper);
	};

	var loc_out = {
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){		
			return loc_getUpdateRuntimeContextRequest(runtimeContext);	
		},
		
		//lifecycle handler
		getLifeCycleRequest : function(transitName, handlers, request){
			if(!transitName.startsWith("_")){
//				return node_createErrorData("code", "message", "data");
				var k = aaa.bbb.ccc;
			}
		},
		
	};
	return loc_out;
}
