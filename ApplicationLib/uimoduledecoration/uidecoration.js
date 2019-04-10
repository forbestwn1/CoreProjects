function(gate){
	
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	
	var loc_gate = gate;

	var loc_out = {
			
		processComponentEvent : function(eventName, eventData, request){
		},
		
		getExecuteCommandRequest : function(command, parms, handlers, request){
			
		},
		
		getInterface : function(){
			
		},
		
		getInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var decorationInfo = loc_gate.getConfiguration().decoration;
			if(decorationInfo!=undefined){
				_.each(loc_gate.getComponent().getUIs(), function(ui, uiName){
					if(decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_UI]!=undefined)  decs = decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_UI][uiName]; 
					if(decs==undefined) decs = decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_GLOBAL];
					out.addRequest(node_createUIDecorationsRequest(decs, {
						success : function(request, uiDecoration){
							ui.addDecoration(uiDecoration);
						}
					}));
				});
			}
			return out;
		}
	};
	return loc_out;
}
