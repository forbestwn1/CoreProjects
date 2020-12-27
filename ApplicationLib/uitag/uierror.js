{
	name : "uierror",
	type : "data",
	description : "",
	attributes : [
		{
			name : "target",
		},
		{
			name : "errorroot",
		},
	],
	context: {
		group : {
			private : {
				element : {
					"internal_errorroot": {
						definition : {
							path : "<%=&(nosliwattribute_errorroot)&%>",
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
		var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
		var node_uiDataOperationServiceUtility  = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
		var node_dataUtility = nosliw.getNodeData("uidata.data.utility");
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
					if(data==undefined || data.value==undefined)  loc_view.val("");
					else loc_view.val(data.value);
				}
			}, request);
		};

		var loc_out = 
		{
			preInit : function(request){
				loc_dataVariable = loc_env.createVariable("internal_errorroot."+loc_targetId);
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
			},

			initViews : function(request){
				loc_view = $('<sppan style="display:inline;background:#e6dedc"/></span>');
				return loc_view;
			},
			
			postInit : function(requestInfo){
				loc_dataVariable.registerDataOperationEventListener(undefined, function(event, eventData, request){
					loc_updateView(request);
				}, this);
			},
				
			createContextForDemo : function(id, parentContext, request) {
				var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
				var node_createData = nosliw.getNodeData("uidata.data.entity.createData");
				var node_createContextElementInfo = nosliw.getNodeData("uidata.context.createContextElementInfo");
				var node_createContext = nosliw.getNodeData("uidata.context.createContext");
				var data = node_createData([1, 2], node_CONSTANT.WRAPPER_TYPE_OBJECT);
				var elementInfosArray = [node_createContextElementInfo("internal_data", data)];
				return node_createContext(id, elementInfosArray, request);
			},
			

			destroy : function(request){
			},
			
		};
		return loc_out;
	}
}
