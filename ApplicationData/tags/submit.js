{
	name : "submit",
	description : "",
	attributes : [
		{
			name : "datasource"
		},
		{
			name : "title"
		},
		{
			name : "parms"
		},
		{
			name : "output"
		},
	],
	context: {
		inherit : true,
		public : {
			
		},
		private : {
			internal_data: {
				path : "<%=&(data)&%>",
				definition : "test.string;1.0.0"
			}
		}
	},
	events : {
		
	},
	requires:{
		"operation" : { 
			op1: "test.integer;1.0.0;add",
		},
	},
	script : function(env){

		var loc_env = env;
		var loc_dataVariable = env.createVariable("internal_data");
		var loc_view;
		
		var loc_revertChange = function(){
			
		};

		var loc_setupUIEvent = function(){
			loc_view.bind('onclick', function(){
				var setRequest = node_createServiceRequestInfoSet({}, {
					success : function(requestInfo, result){
						var commandParms = {
							name : loc_env.getAttribute("datasource"),
							parms : result.getResults()
						};
						var serviceData = loc_env.executeGatewayCommand("dataSource", "getData", commandParms);
						loc_env.executeDataOperationRequestSet(loc_env.getAttribute("output"), "", serviceData.data);
					}
				});

				var parmDefs = loc_env.getAttention("parms").split(";");
				_.each(parmDefs, function(parmDef, i){
					var ps = parmDef.split(":");
					setRequest.addRequest(ps[0], loc_env.getDataOperationRequestGet(ps[1]));
				});
				node_requestProcessor.processRequest(setRequest, false);
			});
		};

		var loc_out = 
		{
			preInit : function(){	},
				
			initViews : function(requestInfo){	
				loc_view = $('<button type="button">'+loc_env.getAttention('title')+'</button>');	
				return loc_view;
			},
				
			postInit : function(){
				loc_setupUIEvent();
			},

			processAttribute : function(name, value){},

			destroy : function(){	
				loc_dataVariable.release();	
				loc_view.remove();
			},
		};
		return loc_out;
	}
}
