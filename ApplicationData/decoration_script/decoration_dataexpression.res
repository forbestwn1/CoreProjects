function(complexEntityDef, valueContextId, bundleCore, configure){
	var node_createServiceRequestInfoCommon = nosliw.getNodeData("request.request.createServiceRequestInfoCommon");
	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
	var node_ServiceRequestExecuteInfo = nosliw.getNodeData("request.entity.ServiceRequestExecuteInfo");
	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_createUIDecorationRequest = nosliw.getNodeData("uipage.createUIDecorationRequest");
	var node_basicUtility = nosliw.getNodeData("common.utility.basicUtility");
	 
	var loc_parentView;
	var loc_mainView;
	var loc_wrapperView;
	var loc_logView;
	
	var loc_configure = configure;

	var loc_envInterface;

	var loc_logContent;

	var loc_init = function(){
		loc_logContent = loc_logContent + JSON.stringify(loc_configure.getConfigureValue(), null, 4) + "\n";
	};

	var loc_out = {

		getPreInitRequest : function(handlers, request){
			var kkk = 555;
			kkk++;
		},
			
		updateView : function(view){
			loc_parentView = $(view);
			loc_mainView = $('<div class="dock" style="border-width:thick; border-style:solid; border-color:green">Decoration Expression</div>');
			loc_wrapperView = $('<div></div>');
			loc_logView = $('<textarea rows="10" cols="150" style="resize: none;" data-role="none"></textarea>');
			loc_wrapperView.append(loc_logView);
			loc_mainView.append(loc_wrapperView);
			loc_parentView.append(loc_mainView);

			loc_logView.val(loc_logContent);
			
			return loc_wrapperView.get();
		},
		
		getPostInitRequest : function(handlers, request){
			var decorationInterface = loc_envInterface[node_CONSTANT.INTERFACE_ENV_DECORATION];
			var coreEntity = decorationInterface[node_CONSTANT.INTERFACE_ENV_DECORATION_COMMAND_GETCORE]();
			var expressionIds = coreEntity.getAllExpressionIds();
			
			loc_logContent = loc_logContent + "\n\n\n";
			_.each(expressionIds, function(expressionId, i){
				loc_logContent = loc_logContent + expressionId + "\n";
			});
			loc_logView.val(loc_logContent);
		},
		
		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},
	};
	
	loc_init();
	return loc_out;
}
