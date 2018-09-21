{
	name : "casedefault",
	description : "",
	attributes : [
	],
	context: {
		public : {
		},
		private : {
			"private_found" : {
				path : "internal_found"
			}
		},
		
	},
	script : function(context, parentResourceView, uiTagResource, attributes, env){

		var loc_env = env;
		
		var loc_resourceView;
		
		var loc_out = 
		{
			ovr_initViews : function(startEle, endEle, requestInfo){
				loc_resourceView = loc_env.createDefaultUIResourceView(requestInfo);
			},
			
			ovr_postInit : function(){	},

			ovr_preInit : function(){	},

			found : function(found){
				if(found==true){
					loc_resourceView.detachViews();
				}
				else{
					loc_resourceView.insertAfter(loc_env.getStartElement());
				}
			}
				
		};		
		return loc_out;
	}
}
