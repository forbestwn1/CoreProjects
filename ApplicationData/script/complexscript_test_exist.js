function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");

	var loc_stateValueView;
	var loc_parmsView;
	var loc_configureView;
	
	var loc_out = {
		
		updateView : function(view){
			var rootViewWrapper = $('<div>I am here</div>');
			$(view).append(rootViewWrapper);
		},
		
	};
	return loc_out;
}
