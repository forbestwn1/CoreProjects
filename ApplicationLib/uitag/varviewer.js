{
	name : "varviewer",
	description : "",
	attributes : [
	],
	valueStructure: {
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
				loc_viewData = $('<textarea rows="5" cols="150" id="aboutDescription" style="resize: none;" data-role="none"></textarea>');
				loc_view.append(loc_viewInput);
				loc_view.append(loc_viewData);
				
				loc_viewInput.bind('change', function(){
					var variableInfo = nosliw.runtime.getVariableManager().getVariableInfo(loc_viewInput.val());
					
					env.executeDataOperationRequestGet(variableInfo.variable, "", {
						success : function(requestInfo, data){
							var content = {
								value : node_dataUtility.getValueOfData(data),
								usage : variableInfo.usage
							};
							loc_viewData.val(JSON.stringify(content, null, 4));
						}
					});
				});

				return loc_view;
			},
		};
		return loc_out;
	}
}
