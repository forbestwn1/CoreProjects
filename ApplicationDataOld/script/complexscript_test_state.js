function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");

	var loc_stateValueView;
	var loc_parmsView;
	var loc_configureView;
	
	var loc_parms = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTCOMPLEXSCRIPT_PARM);
    var loc_scriptVars = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTCOMPLEXSCRIPT_PARM);
	var loc_configure = configure;

	var loc_getStateData = function(){
		return loc_stateValueView.val();
	};
		
	var loc_updateStateData = function(value){
		loc_stateValueView.val(value);
	};

	var loc_out = {
		
		updateView : function(view){
			var rootViewWrapper = $('<div/>');
			$(view).append(rootViewWrapper);
	
			var stateValueViewWrapper = $('<div><span>state:</span></div>');
			loc_stateValueView = $('<input type="text" style="background-color:pink">');
			stateValueViewWrapper.append(loc_stateValueView);
			rootViewWrapper.append(stateValueViewWrapper);
	
			loc_updateStateData(loc_parms.state);
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
