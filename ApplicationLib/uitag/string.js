{
	name : "string",
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
								criteria : "test.string;1.0.0"
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
		var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
		var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
		var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");

		var loc_env = env;
		var loc_dataVariable;
		var loc_view;
		
		var loc_enum;
		
		var loc_revertChange = function(){
			
		};

		var loc_getViewData = function(){
			return {
				dataTypeId: "test.string;1.0.0",
				value: loc_view.val()
			};
		};

		var loc_updateView = function(request){
			loc_env.executeDataOperationRequestGet(loc_dataVariable, "", {
				success : function(requestInfo, data){
					if(data==undefined || data.value==undefined)  loc_view.val("");
					else loc_view.val(data.value.value);
				}
			}, request);
		};

		var loc_setupUIEvent = function(){
			loc_view.bind('change', function(){
				var data = loc_getViewData();
				loc_env.executeBatchDataOperationRequest([
					loc_env.getDataOperationSet(loc_dataVariable, "", data)
				]);
				loc_env.trigueEvent("valueChanged", data);
			});
		};

		var loc_out = 
		{
			preInit : function(requestInfo){
				loc_dataVariable = loc_env.createVariable("internal_data");

				//emum rule
				var dataVarDef = loc_env.getTagContextDefinition().internal_data;
				var rules = dataVarDef[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION]
						[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_DEFINITION]
						[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_CRITERIA]
						[node_COMMONATRIBUTECONSTANT.VARIABLEINFO_DATAINFO]
						[node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_RULE];
				var enumRule;
				for(var i in rules){
					if(rules[i][node_COMMONATRIBUTECONSTANT.DATARULE_RULETYPE]==node_COMMONCONSTANT.DATARULE_TYPE_ENUM){
						enumRule = rules[i];
						break;
					} 
				}
				if(enumRule!=null){
					var enumCode = enumRule[node_COMMONATRIBUTECONSTANT.DATARULE_ENUMCODE];
					if(enumCode!=undefined){
						var gatewayParms = {
							"id" : enumCode,
						};
						return nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest("options", "getValues", gatewayParms, {
							success : function(requestInfo, optionsValues){
								loc_enum = optionsValues;
							}
						});
					}
				}
				
			},
				
			initViews : function(requestInfo){	
				if(loc_enum==undefined){
					loc_view = $('<input type="text" style="background:#e6dedc"/>');	
				}
				else{
					loc_view = $('<select style="background:#e6dedc;border:solid red"/>');	
					for(var i in loc_enum){
						loc_view.append($('<option>', {
							value: loc_enum[i],
							text: loc_enum[i]
						}));
					}
				}
				
				return loc_view;
			},
				
			postInit : function(requestInfo){
				loc_updateView(requestInfo);
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
					var data = node_createData({value:"Hello World", dataTypeId:"test.string;1.0.0"}, node_CONSTANT.WRAPPER_TYPE_APPDATA);
					dataVarEleInfo = node_createContextElementInfo("internal_data", data);
				}
				
				var elementInfosArray = [dataVarEleInfo];
				return node_createContext(id, elementInfosArray, request);
			}
		};
		return loc_out;
	}
}
