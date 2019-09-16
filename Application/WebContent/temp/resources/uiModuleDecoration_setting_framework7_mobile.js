
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"id":"setting_framework7_mobile",
"type":"uiModuleDecoration"
},
"children":[],
"dependency":{},
"info":{}
}, //module decoration that for setting 
function(gate){
	var node_createServiceRequestInfoCommon = nosliw.getNodeData("request.request.createServiceRequestInfoCommon");
	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_ServiceRequestExecuteInfo = nosliw.getNodeData("request.entity.ServiceRequestExecuteInfo");

	var loc_gate = gate;
	var loc_uiModule = loc_gate.getComponentCore();
	
	var loc_out = {
			
		getProcessCommandRequest : function(command, parms, handlers, request){
			var coreCommand = getNosliwCoreName(command);
			if(coreCommand=="application_updateModuleInfo"){
				var contextUpdate = {};
				if(parms.persist!=undefined)  contextUpdate.persist = parms.persist;
				if(parms.modified!=undefined)  contextUpdate.modified = parms.modified;
				if(parms.name!=undefined)  contextUpdate.name = parms.name;

				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				_.each(loc_uiModule.getUIs(), function(ui, index){
					ui.getUpdateExtraContextDataRequest("setting", contextUpdate);
				});
				return out;
			}
		},
		
		getLifeCycleRequest : function(transitName, handlers, request){
			var out;
			if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT){
				out = node_createServiceRequestInfoCommon(undefined, handlers, request);
				out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(requestInfo){
					//put ui to root
					_.each(loc_uiModule.getUIs(), function(ui, index){
						ui.getPage().appendTo(loc_uiModule.getRootView());
					});
					
					out.successFinish();
				}));
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY){
				node_destroyUtil(loc_decoration);
			}
			return out;
		},	
	};
	return loc_out;
}
, {"loadPattern":"file"
});

