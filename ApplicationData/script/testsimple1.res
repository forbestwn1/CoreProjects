function(parms){

	var loc_stateValueView;

	var loc_getUpdateRuntimeContextRequest = function(runtimeContext){
			var stateValueViewWrapper = $('<div>state:</div>');
			loc_stateValueView = $('<input type="text">');
			stateValueViewWrapper.append(loc_stateValueView);
			$(runtimeContext.view).append(stateValueViewWrapper);
	};

	var loc_getGetStateDataRequest = function(){
		return loc_stateValueView.val();
	};
		
	var loc_getRestoreStateDataRequest = function(value){
		loc_stateValueView.val(value);
	};

	var loc_out = {
		
		callBack : function(){
			var command = arguments[0];
			if(command=="getUpdateRuntimeContextRequest"){
				return loc_getUpdateRuntimeContextRequest(arguments[1]);
			}		
			else if(command=="getGetStateDataRequest"){
				return loc_getGetStateDataRequest();
			}		
			else if(command=="getRestoreStateDataRequest"){
				return loc_getRestoreStateDataRequest(arguments[1]);
			}		
		},

	};
	return loc_out;
}
