{
	name : "options",
	type : "data",
	description : "",
	attributes : [
		{
			name : "id"
		},
		{
			name : "data"
		}
	],
	valueStructure: {
		group : {
			public : {
				flat : {
				},
			},
			private : {
				flat : {
					internal_data: {
						definition: {
							path : "<%=&(nosliwattribute_data)&%>",
							definition : {
								criteria : "test.options;1.0.0"
							}
						}
					}
				},
			},
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
		var loc_dataVariable;
		var loc_view;
		
		var loc_revertChange = function(){
			
		};

		var loc_getViewData = function(){
			return {
				dataTypeId: "test.options;1.0.0",
				value: {
					value : loc_view.val(),
					optionsId : loc_env.getAttributeValue("id")
				}
			};
		};

		var loc_updateView = function(request){
			loc_env.executeDataOperationRequestGet(loc_dataVariable, "", {
				success : function(requestInfo, data){
					if(data==undefined)  loc_view.val();
					else loc_view.val(data.value.value);
				}
			}, request);
		};

		var loc_setupUIEvent = function(){
			loc_view.bind('change', function(){
				loc_env.executeBatchDataOperationRequest([
					loc_env.getDataOperationSet(loc_dataVariable, "", loc_getViewData())
				]);
			});
		};

		var loc_out = 
		{
			preInit : function(){
				loc_dataVariable = loc_env.createVariable("internal_data");
			},
				
			initViews : function(requestInfo){	
				loc_view = $('<select style="background:#e6dedc;border:solid red"/>');	
				var operationParms = [];
				var optionId = loc_env.getAttributeValue("id");
				if(optionId==undefined)  optionId = "schoolType";
				operationParms.push(new node_OperationParm(
					{
						dataTypeId: "test.string;1.0.0",
						value: optionId
					}, "optionsId"));
				
				loc_env.executeExecuteOperationRequest("test.options;1.0.0", "all", operationParms, {
					success : function(request, optionsValueArray){
						_.each(optionsValueArray.value, function(optionsValue, i){
							loc_view.append($('<option>', {
								value: optionsValue.value,
								text: optionsValue.value
							}));
						});
						loc_updateView(request);
					}
				}, requestInfo);
				return loc_view;
			},
				
			postInit : function(request){
				loc_updateView(request);
				loc_setupUIEvent();

				loc_dataVariable.registerDataOperationEventListener(undefined, function(event, eventData, request){
					loc_updateView(request);
				}, this);
			},

			destroy : function(){	
				loc_dataVariable.release();	
				loc_view.remove();
			},
			
			createContextForDemo : function(id, parentContext, request) {
				var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
				var node_createData = nosliw.getNodeData("variable.valueinvar.entity..createData");
				var node_createContextElementInfo = nosliw.getNodeData("variable.context.createContextElementInfo");
				var node_createContext = nosliw.getNodeData("variable.context.createContext");
				
				var dataVarPar;
				if(parentContext!=undefined)	dataVarPar = parentContext.getContextElement("data");
				var dataVarEleInfo = undefined;
				if(dataVarPar!=undefined){
					dataVarEleInfo = node_createContextElementInfo("internal_data", dataVarPar);
				}
				else{
					var data = node_createData({value:"Select", dataTypeId:"test.options;1.0.0"}, node_CONSTANT.WRAPPER_TYPE_APPDATA);
					dataVarEleInfo = node_createContextElementInfo("internal_data", data);
				}
				
				var elementInfosArray = [dataVarEleInfo];
				return node_createContext(id, elementInfosArray, request);
			}
			
		};
		return loc_out;
	}
}
