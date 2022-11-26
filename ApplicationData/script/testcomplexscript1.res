function(parms, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");

	var loc_stateValueView;
	var loc_parmsView;
	var loc_configureView;
	
	var loc_parms = parms;
	var loc_configure = configure;

	var loc_getUpdateRuntimeContextRequest = function(runtimeContext){
		var rootViewWrapper = $('<div style="overflow-y1: scroll; border-width:thick; border-style:solid; border-color:green"/>');
		$(runtimeContext.view).append(rootViewWrapper);

		loc_parmsView =  $('<div>parms:' + JSON.stringify(loc_parms) + '</div>');
		rootViewWrapper.append(loc_parmsView);

		loc_configureView =  $('<div>configures:' + JSON.stringify(loc_configure) + '</div>');
		rootViewWrapper.append(loc_configureView);
		
		var stateValueViewWrapper = $('<div><span>state:</span></div>');
		loc_stateValueView = $('<input type="text" style="background-color:pink">');
		stateValueViewWrapper.append(loc_stateValueView);
		rootViewWrapper.append(stateValueViewWrapper);

		loc_updateStateData(parms.state);
	};

	var loc_getStateData = function(){
		return loc_stateValueView.val();
	};
		
	var loc_updateStateData = function(value){
		loc_stateValueView.val(value);
	};

	var loc_out = {
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){		
			return loc_getUpdateRuntimeContextRequest(runtimeContext);	
		},
		
		getGetStateDataRequest : function(handlers, request){
			return node_createServiceRequestInfoSimple({}, function(request){
				return loc_stateValueView.val();
			}, handlers, request);
		},
		
		getRestoreStateDataRequest : function(stateData, handlers, request){
			loc_stateValueView.val(stateData);
		},
		
	};
	return loc_out;
}
