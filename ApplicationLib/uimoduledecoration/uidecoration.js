function(gate){
	
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_createUIDecorationsRequest = nosliw.getNodeData("uipage.createUIDecorationsRequest");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");

	var loc_gate = gate;

	var loc_out = {
			
		getLifeCycleRequest : function(transitName, handlers, request){
			var out;
			if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				var decorationInfo = loc_gate.getConfigureData().decoration;
				if(decorationInfo!=undefined){
					_.each(loc_gate.getComponentCore().getUIs(), function(ui, uiName){
						var decs;
						if(decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_UI]!=undefined)  decs = decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_UI][uiName]; 
						if(decs==undefined) decs = decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_GLOBAL];
						out.addRequest(node_createUIDecorationsRequest(decs, {
							success : function(request, uiDecoration){
								ui.addDecoration(uiDecoration);
							}
						}));
					});
				}
			}
			return out;
		},
	};
	return loc_out;
}
