function(complexEntityDef, variableContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");

	var loc_attachments;

	var loc_stateValueView;
	var loc_parmsView;
	
	var loc_init = function(complexEntityDef, variableContextId, bundleCore, configure){
		loc_attachments = complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTCOMPLEXSCRIPT_ATTACHMENT);

	};

	var loc_out = {
		
		updateView : function(view){
			var wrapperView =  $('<div></div>');
	
			_.each(loc_attachments, function(attachment, i){
				var attrWrapperView =  $('<div></div>');
				attrWrapperView.append($('<br>'));
				attrWrapperView.append($('<br>'));
				attrWrapperView.append($('<span>Type : ' + attachment[node_COMMONATRIBUTECONSTANT.INFOATTACHMENTRESOLVE_VALUETYPE]+'</span>'));
				attrWrapperView.append($('<br>'));
				attrWrapperView.append($('<span>Name : ' + attachment[node_COMMONATRIBUTECONSTANT.INFOATTACHMENTRESOLVE_ITEMNAME]+'</span>'));
				attrWrapperView.append($('<br>'));
				var valueView = $('<textarea rows="10" cols="150" style="resize: none;" data-role="none"></textarea>');
				valueView.val(JSON.stringify(attachment[node_COMMONATRIBUTECONSTANT.INFOATTACHMENTRESOLVE_ENTITYSTR], null, 4));
				attrWrapperView.append(valueView);
				attrWrapperView.append($('<br>'));
				
				wrapperView.append(attrWrapperView);
			});
			$(view).append(wrapperView);
		},
		
	};
	
	loc_init(complexEntityDef, variableContextId, bundleCore, configure);
	return loc_out;
}
