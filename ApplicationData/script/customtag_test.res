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
	
	var loc_dataVariable;
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

	var loc_updateView1 = function(data, request){
		if(data==undefined || data.value==undefined)  loc_inputView.val("");
		else loc_inputView.val(data.value);
	};

	var loc_updateView = function(request){
		loc_envObj.executeDataOperationRequestGet(loc_dataVariable, "", {
			success : function(requestInfo, data){
				if(data==undefined){
					loc_currentData = undefined;
				}
				else{
					loc_currentData = data.value;
				}
				loc_updateView1(loc_currentData);
			}
		}, request);
	};

	var loc_onDataChange = function(data){
		if(data==undefined){
			loc_currentData = data;
		}
		else{
			if(loc_currentData==undefined){
				loc_currentData = data;
			}
			else{
				loc_currentData[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID] = data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID]; 
				loc_currentData[node_COMMONATRIBUTECONSTANT.DATA_VALUE] = data[node_COMMONATRIBUTECONSTANT.DATA_VALUE]; 
			}
		}
		
		loc_envObj.executeBatchDataOperationRequest([
			loc_envObj.getDataOperationSet(loc_dataVariable, "", loc_currentData)
		]);
		loc_envObj.trigueEvent("valueChanged", loc_currentData);
	};
	
	var loc_trigueEvent = function(eventName, eventData){
		if(eventName=='dataChanged'){
			this.onDataChange(eventData);
		}
	};


	var loc_out = 
	{
		
		created : function(){    
			
		},
		
		preInit : function(request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("uiTagPreInitRequest", {}), undefined, request);
			loc_dataVariable = loc_envObj.createVariableByName("internal_data");
			
			_.each(loc_envObj.getAllAttributeNames(), function(name, i){
				var attrValue = loc_envObj.getAttributeValue(name);
				console.log(name + " : " + attrValue);
			});
			
			out.addRequest(loc_envObj.getCreateDefaultUIContentRequest({
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
				loc_onDataChange(loc_getViewData());
			});
			
			loc_uiContent.updateView(loc_contentView);					
			return loc_wrapperView;
		},

		postInit : function(request){
			loc_updateView(request);
			
			loc_dataVariable.registerDataChangeEventListener(undefined, function(event, eventData, request){
				loc_updateView(request);
			}, this);
		},

		updateAttributes : function(attributes, request){
			_.each(attributes, function(attrValue, name){
				console.log(name + " : " + attrValue);
			});
		},

		destroy : function(request){
			loc_dataVariable.release();	
			loc_inputView.remove();
		},
	};	
		
	return loc_out;
}
