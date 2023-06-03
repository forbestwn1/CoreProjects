function(configure){
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

	var loc_logContent = "";

	var loc_runtimeInteface;

	var loc_init = function(){
		loc_logContent = loc_logContent + JSON.stringify(loc_configure.getConfigureValue(), null, 4) + "\n";
	};

	var loc_log = function(content){
		loc_logContent = loc_logContent + content;
		loc_logView.val(loc_logText);
	};

	var loc_out = {
			
		getUpdateRuntimeInterfaceRequest : function(runtimeInteface, handlers, request){
			loc_runtimeInteface = runtimeInteface;
		},
			
		//call back to provide runtime context : view (during init phase)
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){
			loc_parentView = $(runtimeContext.view);
			return node_createServiceRequestInfoSimple(undefined, function(request){
				loc_mainView = $('<div class="dock" style="border-width:thick; border-style:solid; border-color:green">Decoration1</div>');
				loc_wrapperView = $('<div></div>');
				loc_logView = $('<textarea rows="10" cols="150" style="resize: none;" data-role="none"></textarea>');
				loc_wrapperView.append(loc_logView);
				loc_mainView.append(loc_wrapperView);
				loc_parentView.append(loc_mainView);

				loc_logView.val(loc_logContent);
				
				return _.extend({}, runtimeContext, {
					view : loc_wrapperView.get(),
				});
			}, handlers, request);
		},

	};
	
	loc_init();
	return loc_out;
}
