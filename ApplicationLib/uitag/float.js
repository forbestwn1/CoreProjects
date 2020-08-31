{
	name : "float",
	description : "",
	attributes : [
		{
			name : "data"
		}
	],
	context: {
		group : {
			public : {
				element : {
				},
			},
			private : {
				element : {
					internal_data: {
						definition: {
							path : "<%=&(nosliwattribute_data)&%>",
							definition : {
								criteria : "test.float;1.0.0"
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
	event : [
		{
			name : "valueChanged",
			data : {
				element : {
					value : {
						definition : {
							path: "internal_data"
						}
					}
				}
			}
		}
	],
	requires:{
		"operation" : { 
			op1: "test.integer;1.0.0;add",
		},
	},
	script : function(env){

		var loc_env = env;
		var loc_dataVariable;
		var loc_view;
		
		var loc_revertChange = function(){
			
		};

		var loc_getViewData = function(){
			return {
				dataTypeId: "test.float;1.0.0",
				value: parseFloat(loc_view.val())
			};
		};

		var loc_updateView = function(request){
			env.executeDataOperationRequestGet(loc_dataVariable, "", {
				success : function(requestInfo, data){
					if(data==undefined)  loc_view.val("");
					else loc_view.val(data.value.value);
				}
			}, request);
		};

		var loc_setupUIEvent = function(){
			loc_view.bind('change', function(){
				env.executeBatchDataOperationRequest([
					env.getDataOperationSet(loc_dataVariable, "", loc_getViewData())
				]);
			});
		};

		var loc_out = 
		{
			preInit : function(){	
				loc_dataVariable = loc_env.createVariable("internal_data");
			},
				
			initViews : function(requestInfo){	
				loc_view = $('<input type="text"/>');	
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

			processAttribute : function(name, value){},

			destroy : function(){	
				loc_dataVariable.release();	
				loc_view.remove();
			},
			
			createContextForDemo : function(id, parentContext, request) {
				var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
				var node_createData = nosliw.getNodeData("uidata.data.entity.createData");
				var node_createContextElementInfo = nosliw.getNodeData("uidata.context.createContextElementInfo");
				var node_createContext = nosliw.getNodeData("uidata.context.createContext");
				
				var dataVarPar;
				if(parentContext!=undefined)	dataVarPar = parentContext.getContextElement("data");
				var dataVarEleInfo = undefined;
				if(dataVarPar!=undefined){
					dataVarEleInfo = node_createContextElementInfo("internal_data", dataVarPar);
				}
				else{
					var data = node_createData({value:100, dataTypeId:"test.float;1.0.0"}, node_CONSTANT.WRAPPER_TYPE_APPDATA);
					dataVarEleInfo = node_createContextElementInfo("internal_data", data);
				}
				
				var elementInfosArray = [dataVarEleInfo];
				return node_createContext(id, elementInfosArray, request);
			}
			
		};
		return loc_out;
	}
}
