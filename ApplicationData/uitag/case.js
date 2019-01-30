{
	name : "case",
	description : "",
	attributes : [
		{
			name : "value"
		}
	],
	context: {
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
		
	},
	script : function(context, parentResourceView, uiTagResource, attributes, env){

		var loc_env = env;
		var loc_resourceView;
		var loc_caseValue = env.getAttributeValue("value");
		
		var loc_out = 
		{
			ovr_initViews : function(startEle, endEle, requestInfo){
				loc_resourceView = loc_env.createDefaultUIView(requestInfo);
			},
			
			ovr_postInit : function(){},

			ovr_preInit : function(){},
			
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
