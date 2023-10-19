function(envObj){
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_createHandleEachElementProcessor = nosliw.getNodeData("variable.orderedcontainer.createHandleEachElementProcessor");

	var loc_envObj = envObj;

	var loc_view;
	var loc_containerVariable;
	
	var loc_handleEachElementProcessor;
	
	var loc_getUpdateViewRequest = function(handlers, requestInfo){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);

		out.addRequest(loc_handleEachElementProcessor.getLoopRequest({
			success : function(requestInfo, eles){
				var addEleRequest = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
				_.each(eles, function(ele, index){
					var variationPoints = {
						afterValueContext: function(complexEntityDef, valueContextId, bundleCore, coreConfigure){
							var loc_valueContext = bundleCore.getVariableDomain().getValueContext(valueContextId);
							var valueStructureRuntimeId = loc_valueContext.getValueStructureRuntimeIdByName("nosliw_internal");
							var valueStructure = loc_valueContext.getValueStructure();
							
						}
					}
					addEleRequest.addRequest(loc_envObj.getCreateDefaultUIContentRequest(variationPoints, {
						success: function(request, uiConentNode){
							loc_uiContent = uiConentNode.getChildValue().getCoreEntity();
						}
					}));
					
				
				
					addEleRequest.addRequest(loc_getAddEleRequest(ele.elementVar, ele.indexVar, index));
				});
				addEleRequest.setParmData("processMode", "promiseBased");
				return addEleRequest;
			}
		}));
		
		return out;
	};
	
	var loc_out = 
	{
		
		created : function(){    
		},
		
		preInit : function(request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("uiTagPreInitRequest", {}), undefined, request);
			loc_containerVariable = loc_envObj.createVariableByName("internal_data");
			
			return out;
		},
		
		initViews : function(handlers, request){
			loc_view = $('<div></div>');				}
			return loc_view;
		},

		postInit : function(request){
			loc_handleEachElementProcessor = node_createHandleEachElementProcessor(loc_containerVariable, ""); 
		},

		updateAttributes : function(attributes, request){
		},

		destroy : function(request){
		},
	};	
		
	return loc_out;
}
