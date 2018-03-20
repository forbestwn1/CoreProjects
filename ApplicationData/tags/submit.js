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
		var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
		var node_requestProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	
		var loc_env = env;
		var loc_view;
		
		var loc_revertChange = function(){
			
		};

		var loc_setupUIEvent = function(){
			loc_view.bind('click', function(){
				window.alert("sometext");
				var setRequest = node_createServiceRequestInfoSet({}, {
					success : function(requestInfo, result){
						var commandParms = {
							name : loc_env.getAttributeValue("datasource"),
							parms : result.getResults()
						};
						var serviceData = loc_env.executeGatewayCommand("dataSource", "getData", commandParms);
//						loc_env.executeDataOperationRequestSet(loc_env.getAttributeValue("output"), "", serviceData.data);
					}
				});

				var parmDefs = loc_env.getAttributeValue("parms").split(";");
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
				loc_view = $('<button type="button">'+loc_env.getAttributeValue('title')+'</button>');	
				loc_setupUIEvent();
				return loc_view;
			},
				
			postInit : function(){
			},

			processAttribute : function(name, value){},

			destroy : function(){	
			},
		};
		return loc_out;
	}
}
