function(complexEntityDef, variableGroupId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");

	var loc_stateValueView;
	var loc_parmsView;
	var loc_configureView;
	
	var loc_parms = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTCOMPLEXSCRIPT_PARM);
	var loc_configure = configure;

	var loc_getStateData = function(){
		return loc_stateValueView.val();
	};
		
	var loc_updateStateData = function(value){
		loc_stateValueView.val(value);
	};

	var loc_out = {
		
		updateView : function(view){
			var rootViewWrapper = $('<div style="overflow-y1: scroll; border-width:thick; border-style:solid; border-color:green"/>');
			$(view).append(rootViewWrapper);
	
			loc_parmsView =  $('<div>parms:' + JSON.stringify(loc_parms) + '</div>');
			rootViewWrapper.append(loc_parmsView);
	
			loc_configureView =  $('<div>configures:' + JSON.stringify(loc_configure) + '</div>');
			rootViewWrapper.append(loc_configureView);
			
			var stateValueViewWrapper = $('<div><span>state:</span></div>');
			loc_stateValueView = $('<input type="text" style="background-color:pink">');
			stateValueViewWrapper.append(loc_stateValueView);
			rootViewWrapper.append(stateValueViewWrapper);
	
			loc_updateStateData(parms.state);
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
