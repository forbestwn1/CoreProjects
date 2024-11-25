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
	valueStructure: {
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
		var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
		var node_requestProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	
		var loc_env = env;
		var loc_view;
		
		var loc_revertChange = function(){
			
		};

		var loc_setupUIEvent = function(){
			loc_view.bind('click', function(){
				var out = node_createServiceRequestInfoSequence({}, {});
				
				var getParmsRequest = node_createServiceRequestInfoSet({}, {
					success : function(requestInfo, result){
						var parms = {};
						_.each(result.getResults(), function(result, name){
							parms[name] = result.value;
						});
						var commandParms = {
							name : loc_env.getAttributeValue("datasource"),
							parms : parms
						};
						return loc_env.getGatewayCommandRequest("dataSource", "getData", commandParms, {
							success: function(requestInfo, data){
								return loc_env.getDataOperationRequestSet(loc_env.getAttributeValue("output"), "", data);
							}
						});
					}
				});

				var parms = loc_env.getAttributeValue("parms");
				if(parms!=undefined && parms!=""){
					var parmDefs = parms.split(";");
					_.each(parmDefs, function(parmDef, i){
						var ps = parmDef.split(":");
						getParmsRequest.addRequest(ps[0], loc_env.getDataOperationRequestGet(ps[1]));
					});
				}					
				
				out.addRequest(getParmsRequest);
				node_requestProcessor.processRequest(out, false);
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
