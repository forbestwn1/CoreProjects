{
	name : "switch",
	description : "",
	attributes : [
		{
			name : "value"
		}
	],
	valueStructure: {
		public : {
		},
		internal : {
			"internal_switchValue" : {
				path : "<%=&(value)&%>"
			},
			"internal_found" : {
				"default" : false
			}
		}
	},
	script : function(env){

		var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
		
		var loc_env = env;

		var loc_value;
		
		var loc_resourceView;
		
		var loc_view;

		var loc_updateView = function(requestInfo){

			if(loc_resourceView!=undefined){
				var found = false;
				var caseTags = loc_resourceView.getTagsByName("case");
				_.each(caseTags, function(caseTag, name){
					var matched = caseTag.getTagObject().valueChanged(loc_value+"");
					if(matched==true)  found = true;
				});
			}
		};
		
		var loc_out = 
		{
			postInit : function(requestInfo){
				var out = node_createServiceRequestInfoSequence(undefined);
				out.addRequest(loc_env.getCreateDefaultUIViewRequest({
					success : function(requestInfo, uiView){
						loc_resourceView = uiView;
						loc_resourceView.appendTo(loc_view);
						loc_updateView();
					}
				}));
				return out;
			},

			preInit : function(requestInfo){
			},
		
			initViews : function(requestInfo){
				loc_view = $('<div/>');	
				return loc_view;
			},

			processAttribute : function(name, value){
				if(name=='value'){
					loc_value = value;
					loc_updateView();
				}
			},
		};
		
		return loc_out;
	}
}
