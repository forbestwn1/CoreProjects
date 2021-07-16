{
	name : "case",
	description : "",
	attributes : [
		{
			name : "value"
		}
	],
	valueStructure: {
		group: {
			public : {
			},
			private : {
				"private_caseVariable" : {
					path : "internal_switchVariable"
				},		
				"private_found" : {
					path : "internal_found"
				}
			},
		}
		
	},
	script : function(env){

		var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
		
		var loc_env = env;
		var loc_resourceView;
		var loc_view;
		var loc_caseValue = env.getAttributeValue("value");
		
		var loc_out = 
		{
			initViews : function(requestInfo){
				loc_view = $('<span/>');	
				return loc_view;
			},

			postInit : function(requestInfo){
				var out = node_createServiceRequestInfoSequence(undefined);
				out.addRequest(loc_env.getCreateDefaultUIViewRequest({
					success : function(requestInfo, uiView){
						loc_resourceView = uiView;
						loc_resourceView.appendTo(loc_view);
					}
				}));
				return out;
			},

			preInit : function(){},
			
			valueChanged : function(value){
				if(value==loc_caseValue){
					loc_resourceView.insertAfter(loc_env.getStartElement());
					return true;
				}
				else{
					loc_resourceView.detachViews();
					return false;
				}
			}
		};
		return loc_out;
	}
}
