{
	name : "switch",
	description : "",
	attributes : [
		{
			name : "variable"
		}
	],
	context: {
		public : {
		},
		internal : {
			"internal_switchVariable" : {
				path : "<%=&(variable)&%>"
			},
			"internal_found" : {
				"default" : false
			}
		}
	},
	script : function(context, parentResourceView, uiTagResource, attributes, env){

		var loc_env = env;

		var loc_valueContextEleName = "internal_switchVariable";
	
		var loc_valueVariable = env.createVariable(loc_valueContextEleName);

		var loc_resourceView;


		var loc_updateView = function(requestInfo){
			
			loc_env.executeDataOperationRequestGet(loc_valueVariable, "", {
				success : function(request, data){
					var value;
					if(data!=undefined)		value = data.value;
							
					var found = false;
					var caseTags = loc_resourceView.getTagsByName("case");
					_.each(caseTags, function(caseTag, name){
						var matched = caseTag.getTagObject().valueChanged(value);
						if(matched==true)  found = true;
					});
							
					var defaultTags = loc_resourceView.getTagsByName("casedefault");
					_.each(defaultTags, function(defaultTag, name){
						defaultTag.getTagObject().found(found);
					});
				}
			}, requestInfo);
		};
		
		var loc_out = 
		{
			ovr_postInit : function(requestInfo){
				loc_valueVariable.registerDataChangeEventListener(undefined, function(){
						loc_updateView();
				}, this);
				loc_updateView();
			},

			ovr_preInit : function(requestInfo){
			},
		
			ovr_initViews : function(startEle, endEle, requestInfo){
				loc_resourceView = loc_env.createDefaultUIView(requestInfo);
				return loc_resourceView.getViews();
			}
		};
		
		return loc_out;
	}
}
