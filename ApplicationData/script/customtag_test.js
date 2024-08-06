function(envObj){
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");

	var loc_envObj = envObj;

	var loc_wrapperView;
	var loc_inputView;
	var loc_contentView;
	
	var loc_currentData;

	var loc_uiContent;
	
	var loc_attributes = {};

	var loc_getViewData = function(){
		var value = loc_inputView.val();
		if(value==undefined || value=="")  return;
		return {
			dataTypeId: "test.string;1.0.0",
			value: loc_inputView.val()
		};
	};

	var loc_out = 
	{
		
		created : function(){    
			
		},
		
		preInit : function(request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("uiTagPreInitRequest", {}), undefined, request);
			
			_.each(loc_envObj.getAllAttributeNames(), function(name, i){
				var attrValue = loc_envObj.getAttributeValue(name);
				console.log(name + " : " + attrValue);
			});
			
			out.addRequest(loc_envObj.getCreateDefaultUIContentRequest(undefined, {
				success: function(request, uiConentNode){
					loc_uiContent = uiConentNode.getChildValue().getCoreEntity();
				}
			}));
			
			return out;
		},
		
		initViews : function(handlers, request){
			loc_wrapperView = $('<div/>');
			loc_inputView = $('<input type="text" style="display:inline;background:#e6dedc"/>');	
			loc_contentView = $('<div/>');
			loc_wrapperView.append(loc_inputView);
			loc_wrapperView.append(loc_contentView);

			loc_inputView.bind('change', function(){
				loc_envObj.onDataChange(loc_getViewData());
			});
			
//			loc_uiContent.updateView(loc_contentView);					
			return loc_wrapperView;
		},
        
		updateView : function(data, request){
			if(data==undefined || data.value==undefined)  loc_inputView.val("");
			else loc_inputView.val(data.value);
		},

		updateView1 : function(data, request){
			var flowDataType = loc_base.getDataFlowType();
			if(flowDataType!=node_COMMONCONSTANT.DATAFLOW_IN){
				if(data==undefined || data.value==undefined)  loc_view.val("");
				else loc_view.val(data.value);
			}
			else{
				if(data==undefined || data.value==undefined)  loc_view.text("");
				else loc_view.text(data.value);
			}
		},

		postInit : function(request){
		},

		updateAttributes : function(attributes, request){
			_.each(attributes, function(attrValue, name){
				console.log(name + " : " + attrValue);
			});
			loc_envObj.trigueEvent("attrchange", attributes, request);
		},

		destroy : function(request){
			loc_inputView.remove();
		},
	};	
		
	return loc_out;
}
