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
		public : {
		},
		private : {
		},
		info : {
			inherit : "public"
		}
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
