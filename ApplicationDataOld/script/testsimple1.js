function(parms){

	var loc_stateValueView;
	var loc_parmsView;
	var loc_parms = parms;

	var loc_getUpdateRuntimeContextRequest = function(runtimeContext){
		var rootViewWrapper = $('<div style="overflow-y1: scroll; border-width:thick; border-style:solid; border-color:green"/>');
		$(runtimeContext.view).append(rootViewWrapper);

		loc_parmsView =  $('<div>parms:' + JSON.stringify(loc_parms) + '</div>');
		rootViewWrapper.append(loc_parmsView);
		
		var stateValueViewWrapper = $('<div>state:</div>');
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
		
		callBack : function(){
			var command = arguments[0];
			if(command=="getUpdateRuntimeContextRequest"){
				return loc_getUpdateRuntimeContextRequest(arguments[1]);
			}		
			else if(command=="getGetStateDataRequest"){
				return loc_getStateData();
			}		
			else if(command=="getRestoreStateDataRequest"){
				return loc_updateStateData(arguments[1]);
			}
		},

	};
	return loc_out;
}
