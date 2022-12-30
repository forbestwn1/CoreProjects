function(complexEntityDef, variableGroupId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");

	var loc_configure = configure;

	var loc_logView;
	var loc_logText = "";

	var loc_getUpdateRuntimeContextRequest = function(runtimeContext){
		var rootViewWrapper = $('<div/>');
		$(runtimeContext.view).append(rootViewWrapper);

		loc_logView = $('<textarea rows="3" cols="150" style="resize: none;" data-role="none"></textarea>');
		rootViewWrapper.append(loc_logView);
	};

	var loc_out = {
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){		
			return loc_getUpdateRuntimeContextRequest(runtimeContext);	
		},
		
		//lifecycle handler
		getLifeCycleRequest : function(transitName, handlers, request){
			var seperator = loc_logText==""? "" : " --> "
			loc_logText = loc_logText + seperator + transitName;
			loc_logView.val(loc_logText);
		},
		
	};
	return loc_out;
}
