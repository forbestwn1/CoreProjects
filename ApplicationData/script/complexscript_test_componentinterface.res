function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");

	var loc_logView;
	var loc_logText = "";
	
	var loc_getUpdateRuntimeContextRequest = function(runtimeContext){
		var rootViewWrapper = $('<div/>');
		$(runtimeContext.view).append(rootViewWrapper);

		loc_logView = $('<textarea rows="3" cols="150" style="resize: none;" data-role="none"></textarea>');
		rootViewWrapper.append(loc_logView);
		loc_logView.val(loc_logText);
	};

	var loc_log = function(log){
		loc_logText = loc_logText + "\n" + log;
		if(loc_logView!=undefined)  loc_logView.val(loc_logText);
	};

	var loc_out = {

		getPreInitRequest : function(handlers, request){		loc_log("getPreInitRequest called");		},
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){	
			loc_log("getUpdateRuntimeContextRequest called");	
			return loc_getUpdateRuntimeContextRequest(runtimeContext);	
		},
				
		updateView : function(view){
			var rootViewWrapper = $('<div/>');
			$(view).append(rootViewWrapper);
	
			loc_logView = $('<textarea rows="3" cols="150" style="resize: none;" data-role="none"></textarea>');
			rootViewWrapper.append(loc_logView);
			loc_logView.val(loc_logText);
		},
				
		getPostInitRequest : function(handlers, request){		loc_log("getPostInitRequest called");		},
		
		setEnvironmentInterface : function(envInterface){
			var interfaceNames = [];
			_.each(envInterface, function(int, name){interfaceNames.push(name)});
			loc_log("setEnvironmentInterface called :  "+JSON.stringify(interfaceNames));
		},
		
	};
	return loc_out;
}
