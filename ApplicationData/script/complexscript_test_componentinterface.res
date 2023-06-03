function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");

	var loc_logView;
	var loc_logText = "";
	
	var loc_getUpdateRuntimeContextRequest = function(runtimeContext){
		var rootViewWrapper = $('<div/>');
		$(runtimeContext.view).append(rootViewWrapper);

		loc_logView = $('<textarea rows="3" cols="150" style="resize: none;" data-role="none"></textarea>');
		rootViewWrapper.append(loc_logView);
	};

	var loc_log = function(log){
		loc_logText = loc_logText + "\n" + transitName;
		loc_logView.val(loc_logText);
	};

	var loc_out = {

		getPreInitRequest : function(handlers, request){
			loc_log("getPreInitRequest called");
		},
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){		
			return loc_getUpdateRuntimeContextRequest(runtimeContext);	
		},
		
	};
	return loc_out;
}
