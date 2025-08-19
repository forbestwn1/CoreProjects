{
	name : "uierror",
	description : "",
	attributes : [
		{
			name : "target",
		},
		{
			name : "data",
		},
	],
	valueStructure: {
		group : {
			private : {
				flat : {
					"internal_data": {
						definition : {
							path : "<%=&(nosliwattribute_data)&%>",
						},
					}
				},
			},
		},
		info : {
			inherit : "true",
		}
	},
	event : [
		
	],
	script : function(env){

		var node_namingConvensionUtility = nosliw.getNodeData("common.namingconvension.namingConvensionUtility");
		var node_createContextVariable = nosliw.getNodeData("variable.context.createContextVariable");
		var node_valueInVarOperationServiceUtility  = nosliw.getNodeData("variable.valueinvar.operation.valueInVarOperationServiceUtility");
		var node_dataUtility = nosliw.getNodeData("variable.valueinvar.utility");
		var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
		var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
		var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
		
		var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
		var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
		var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");

		var loc_env = env;
		var loc_view;
		var loc_targetId;
		var loc_dataVariable;
		
		var loc_updateView = function(request){
			loc_env.executeDataOperationRequestGet(loc_dataVariable, "", {
				success : function(requestInfo, data){
					if(data==undefined || data.value==undefined){
						loc_view.text("");
						loc_view.hide();
					}
					else{
						loc_view.text(data.value);
						loc_view.show();
					}
				}
			}, request);
		};

		var loc_out = 
		{
			preInit : function(request){
				var query = {
					"attribute" : [
						{
							name : node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_STATICID,
							value : loc_env.getAttributeValue("target"),
						}
					]	
				};
				var targetTag = loc_env.getTags(query)[0];
				loc_targetId = targetTag.getId();
				loc_dataVariable = loc_env.createVariable("internal_data."+loc_targetId);
			},

			initViews : function(request){
				loc_view = $('<sppan style="color:red"/></span>');
				loc_view.hide();
				return loc_view;
			},
			
			postInit : function(requestInfo){
				loc_dataVariable.registerDataOperationEventListener(undefined, function(event, eventData, request){
					loc_updateView(request);
				}, this);
			},
				
			createContextForDemo : function(id, parentContext, request) {
				var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
				var node_createData = nosliw.getNodeData("variable.valueinvar.entity..createData");
				var node_createContextElementInfo = nosliw.getNodeData("variable.context.createContextElementInfo");
				var node_createContext = nosliw.getNodeData("variable.context.createContext");
				var data = node_createData([1, 2], node_CONSTANT.WRAPPER_TYPE_OBJECT);
				var elementInfosArray = [node_createContextElementInfo("internal_data", data)];
				return node_createContext(id, elementInfosArray, request);
			},
			
			getDataForDemo : function(){
				var data = node_createData("Validation Error!!!", node_CONSTANT.WRAPPER_TYPE_OBJECT);
				dataVarEleInfo = node_createContextElementInfo("internal_data", data);
			},

			createContextForDemo : function(id, parentContext, matchersByName, request) {
				var node_createData = nosliw.getNodeData("variable.valueinvar.entity..createData");
				var node_createContextElementInfo = nosliw.getNodeData("variable.context.createContextElementInfo");
				var node_createContext = nosliw.getNodeData("variable.context.createContext");
				
				var dataVarPar;
				if(parentContext!=undefined)	dataVarPar = parentContext.getContextElement("data");
				var dataVarEleInfo = undefined;
				if(dataVarPar!=undefined){
					var matchersCombo = matchersByName==undefined?{}:matchersByName["internal_data"];
					var info;
					if(matchersCombo!=undefined){
						info = {
								matchers : matchersCombo[node_COMMONATRIBUTECONSTANT.MATCHERSCOMBO_MATCHERS],
								reverseMatchers : matchersCombo[node_COMMONATRIBUTECONSTANT.MATCHERSCOMBO_REVERSEMATCHERS]
						};
					}
					dataVarEleInfo = node_createContextElementInfo("internal_data", dataVarPar, undefined, undefined, info);
				}
				else{
					var data = node_createData("Validation Error!!!", node_CONSTANT.WRAPPER_TYPE_OBJECT);
					dataVarEleInfo = node_createContextElementInfo("internal_data", data);
				}
				
				var elementInfosArray = [dataVarEleInfo];
				return node_createContext(id, elementInfosArray, request);
			},

			destroy : function(request){
			},
			
		};
		return loc_out;
	}
}
