function(evnObj){
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");

	var loc_evnObj = evnObj;
	var loc_wrapperView;
	var loc_inputView;
	var loc_contentView;
	
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
		initViews : function(requestInfo){
			loc_wrapperView = $('<div/>');
			loc_inputView = $('<input type="text" style="display:inline;background:#e6dedc"/>');	
			loc_contentView = $('<div/>');
			loc_wrapperView.append(loc_inputView);
			loc_wrapperView.append(loc_contentView);

			loc_inputView.bind('change', function(){
				loc_evnObj.onDataChange(loc_getViewData());
			});					
			return loc_wrapperView;
		},
		
		updateView : function(data, request){
			if(data==undefined || data.value==undefined)  loc_inputView.val("");
			else loc_inputView.val(data.value);
		},
		
		getViewData : function(){
			return loc_getViewData();
		},
		
		destroy : function(){
			loc_inputView.remove();
		}
	};	
		
	return loc_out;
}
