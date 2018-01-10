{
	name : "case",
	description : "",
	attributes : [
		{
			name : "source"
		},
		{
			name : "context"
		}
	],
	context: {
		inherit : true,
		public : {
		},
		private : {
		},
		
	},
	script : function(context, parentResourceView, uiTagResource, attributes, tagEnv){

		var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
		var node_createDataOperationRequest = nosliw.getNodeData("uidata.dataoperation.createDataOperationRequest");
		var node_DataOperationService = nosliw.getNodeData("uidata.dataoperation.DataOperationService");
		var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
		var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
		var node_createUIResourceViewFactory = nosliw.getNodeData("uiresource.createUIResourceViewFactory");
		var node_createContextElementInfo = nosliw.getNodeData("uidata.context.createContextElementInfo");
		var node_createContext = nosliw.getNodeData("uidata.context.createContext");
		
		
		var loc_context = context;

		var loc_uiTagResource = uiTagResource;
	
		var loc_parentResourceView = parentResourceView;

		var loc_tagEnv = tagEnv;
		
		var loc_caseValue = attributes.value;
		
		var loc_dataContextEleName = "private_caseVariable";
	
		var loc_valueVariable = loc_context.createVariable(node_createContextVariable(loc_dataContextEleName));

		var loc_view;

		var loc_startEle;
		
		var loc_endEle;
		
		var loc_resourceView;
		
		var loc_out = 
		{
			ovr_initViews : function(startEle, endEle, requestInfo){
				loc_startEle = startEle;
				loc_endEle = endEle;
				
				loc_resourceView = node_createUIResourceViewFactory().createUIResourceView(loc_uiTagResource, loc_tagEnv.getId(), loc_parentResourceView, loc_context, requestInfo);
				return loc_resourceView.getViews();
			},
			
				ovr_postInit : function(){
				},

			ovr_preInit : function(){	},
		};
		return loc_out;
	}
}
