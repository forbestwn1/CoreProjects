script : function(env){

	var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
	var node_requestProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_createContextVariablesGroup = nosliw.getNodeData("variable.context.createContextVariablesGroup");
	var node_createContextVariableInfo = nosliw.getNodeData("variable.context.createContextVariableInfo");
	var node_dataUtility = nosliw.getNodeData("variable.data.utility");

	var loc_env = env;

	var loc_view;
	var loc_viewValueStructures;
	
	var loc_contextVariableGroup = {};

	var loc_valueStructureInfo = [];
	
	var loc_updateView = function(requestInfo){
		//context data
		var contextContent = {};
		var setRequest = node_createServiceRequestInfoSet({}, {
			success : function(requestInfo, result){
				_.each(result.getResults(), function(contextData, name){
					contextContent[name] = contextData!=undefined?node_dataUtility.getValueOfData(contextData):"EMPTY VARIABLE";
				});
				loc_viewData.val(JSON.stringify(contextContent, null, 4));
			}
		}, requestInfo);
		var eleVars = loc_contextVariableGroup.getVariables();
		_.each(eleVars, function(eleVar, eleName){
			setRequest.addRequest(eleName, loc_env.getDataOperationRequestGet(eleVar));
		});
		node_requestProcessor.processRequest(setRequest, false);
	};

	var loc_out = 
	{
		preInit : function(requestInfo){
			var valueContext = loc_env.getValueContext();
			var valueStructures = valueContext.getValueStructureRuntimeIds();
			_.each(valueStructures.solid, function(vsId, i){
				var wrapper = valueContext.getValueStructureWrapper();
				loc_valueStructureInfo.push({
					name : wrapper.getName(),
					id : vsId,
					valueStructure : wrapper.getValueStructure();
				});
			});
		
			_.each(valueStructures.soft, function(vsId, i){
				var wrapper = valueContext.getValueStructureWrapper();
				loc_valueStructureInfo.push({
					name : wrapper.getName(),
					id : vsId,
					valueStructure : wrapper.getValueStructure();
				});
			});
		
		
			loc_contextVariableGroup = node_createContextVariablesGroup(loc_env.getContext(), undefined, function(request){
				loc_updateView(request);
			});
			_.each(loc_env.getContext().getElementsName(), function(eleName, index){
				loc_contextVariableGroup.addVariable(node_createContextVariableInfo(eleName));
			});
		},
			
		initViews : function(requestInfo){
			loc_view = $('<div/>');
			loc_viewData = $('<textarea rows="15" cols="150" id="aboutDescription" style="resize: none;" data-role="none"></textarea>');
			loc_view.append(loc_viewData);
			return loc_view;
		},
			
		postInit : function(requestInfo){
			loc_updateView(requestInfo);
		},
	};
	return loc_out;
}
