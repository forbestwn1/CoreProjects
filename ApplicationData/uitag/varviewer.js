{
	name : "varviewer",
	description : "",
	attributes : [
	],
	context: {
	},
	events : {
		
	},
	requires:{
	},
	script : function(env){

		var node_dataUtility = nosliw.getNodeData("uidata.data.utility");

		var loc_env = env;
	
		var loc_view;
		var loc_viewInput;
		var loc_viewData;
		
		var loc_out = 
		{
			initViews : function(requestInfo){
				loc_view = $('<div/>');
				loc_viewInput = $('<input type="text"/>');	
				loc_viewData = $('<textarea rows="15" cols="150" id="aboutDescription" style="resize: none;" data-role="none"></textarea>');
				loc_view.append(loc_viewInput);
				loc_view.append(loc_viewData);
				
				loc_viewInput.bind('change', function(){
					var variable = nosliw.runtime.getUIVariableManager().getVariable(loc_viewInput.val());
					
					env.executeDataOperationRequestGet(variable, "", {
						success : function(requestInfo, data){
							loc_viewData.val(JSON.stringify(node_dataUtility.getValueOfData(data), null, 4));
						}
					});
				});

				return loc_view;
			},
		};
		return loc_out;
	}
}
