{
	name : "include",
	description : "",
	attributes : [
		{
			name : "source"
		},
		{
			name : "context"
		}
	],
	context: {
		inherit : true,
		public : {
		},
		private : {
		},
		
	},
	script : function(env){
		var loc_env = env;
		
		var loc_out = 
		{
			initViews : function(requestInfo){
				var resourceView = loc_env.createDefaultUIResourceView(requestInfo);
				return resourceView.getViews();
			},
			
			postInit : function(){	},

			preInit : function(){	},
		};
		return loc_out;
	}
}
