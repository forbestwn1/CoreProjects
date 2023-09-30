function(evnObj){
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");

	var loc_evnObj = evnObj;
	var loc_view;
	
	var loc_getViewData = function(){
		var value = loc_view.val();
		if(value==undefined || value=="")  return;
		return {
			dataTypeId: "test.string;1.0.0",
			value: loc_view.val()
		};
	};

	
	var loc_out = 
	{
		initViews : function(requestInfo){
			loc_view = $('<input type="text" style="display:inline;background:#e6dedc"/>');	
			loc_view.bind('change', function(){
				loc_evnObj.onDataChange(loc_getViewData());
			});					
			return loc_view;
		},
		
		updateView : function(data, request){
			if(data==undefined || data.value==undefined)  loc_view.val("");
			else loc_view.val(data.value);
		},
		
		getViewData : function(){
			return loc_getViewData();
		},
		
		destroy : function(){
			loc_view.remove();
		}
	};	
		
	return loc_out;
}
