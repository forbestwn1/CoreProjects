{
	name : "multioptions",
	description : "",
	attributes : [
		{
			name : "id"
		},
		{
			name : "data"
		}
	],
	context: {
		public : {
			
		},
		private : {
			internal_data: {
				path : "<%=&(data)&%>",
				definition : "test.array;1.0.0"
			}
		},
		info : {
			inherit : "false"
		}
	},
	events : {
		
	},
	requires:{
		"operation" : { 
		},
	},
	script : function(env){
		var node_OperationParm = nosliw.getNodeData("expression.entity.OperationParm");

		var loc_env = env;
		var loc_dataVariable = env.createVariable("internal_data");
		var loc_view;
		
		var loc_revertChange = function(){
			
		};

		var loc_getViewData = function(){
			var out =  {
				dataTypeId: "test.array;1.0.0",
				value: []
			};
			
			var selValues = loc_view.val();
			_.each(selValues, function(value, index){
				out.value.push(					{
						dataTypeId: "test.options;1.0.0",
						value: {
							value : value,
							optionsId : loc_env.getAttributeValue("id")
						}
					});
			});
			return out;
		};

		var loc_updateView = function(){
			env.executeDataOperationRequestGet(loc_dataVariable, "", {
				success : function(requestInfo, arrayData){
					var values = [];
					_.each(arrayData.value.value, function(data, index){
						values.push(data.value.value);
//						loc_view.val(data.value).prop('selected', true);
					});
					loc_view.val(values);
				}
			});
		};

		var loc_requests = {};
		
		var loc_setupUIEvent = function(){
			loc_view.change(function(){
				var request = loc_env.getBatchDataOperationRequest([
					env.getDataOperationSet(loc_dataVariable, "", loc_getViewData())
				]);
				loc_requests[request.getId()] = {};
				loc_env.processRequest(request);
			});
		};

		var loc_out = 
		{
			preInit : function(){	},
				
			initViews : function(requestInfo){	
				loc_view = $('<select multiple/>');	
				var operationParms = [];
				operationParms.push(new node_OperationParm(
					{
						dataTypeId: "test.string;1.0.0",
						value: loc_env.getAttributeValue("id")
					}, "optionsId"));
				
				var request = loc_env.executeExecuteOperationRequest("test.options;1.0.0", "all", operationParms, {
					success : function(request, optionsValueArray){
						_.each(optionsValueArray.value, function(optionsValue, i){
							loc_view.append($('<option>', {
								value: optionsValue.value,
								text: optionsValue.value
							}));
						});
						loc_updateView();
					}
				});
				return loc_view;
			},
				
			postInit : function(){
//				loc_updateView();
				loc_setupUIEvent();

				loc_dataVariable.registerDataOperationEventListener(undefined, function(event, data, request){
					if(loc_requests[request.getId()]==undefined){
						loc_updateView();
					}
				}, this);
			},

			destroy : function(){	
				loc_dataVariable.release();	
				loc_view.remove();
			},
		};
		return loc_out;
	}
}
