function(env){

	var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
	var node_requestProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_createVariablesGroup = nosliw.getNodeData("variable.createVariablesGroup");
	var node_createValuePortElementInfo = nosliw.getNodeData("valueport.createValuePortElementInfo");
	var node_dataUtility = nosliw.getNodeData("variable.valueinvar.utility");

	var loc_env = env;

	var loc_view;
	var loc_viewValueStructures;
	
	var loc_contextVariableGroup = {};

	var loc_valueStructureInfo = [];
	
	var loc_updateValuStructureView = function(vsInfo, requestInfo){
		//context data
		var contextContent = {};
		var setRequest = node_createServiceRequestInfoSet({}, {
			success : function(requestInfo, result){
				_.each(result.getResults(), function(contextData, name){
					contextContent[name] = contextData!=undefined?node_dataUtility.getValueOfData(contextData):"EMPTY VARIABLE";
				});
				vsInfo.viewData.val(JSON.stringify(contextContent, null, 4));
			}
		}, requestInfo);
		var eleVars = vsInfo.valueStructureVariableGroup.getVariables();
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
			
			var vsIds = valueStructures.solid.concat(valueStructures.soft);
			_.each(vsIds, function(vsId, i){
				var wrapper = valueContext.getValueStructureWrapper(vsId);
				var valueStructure = wrapper.getValueStructure();

				var varDefIds = [];
				var rootNames = valueStructure.getElementsName();
				if(rootNames.length>0){
					_.each(rootNames, function(rootName, i){
						varDefIds.push(node_createValuePortElementInfo(vsId, rootName));
					});
					
					var valueStructureInfo = {
						name : wrapper.getName(),
						id : vsId,
						valueStructure : valueStructure
					};
	
					valueStructureInfo.valueStructureVariableGroup = node_createVariablesGroup(valueContext, varDefIds, function(request){
							loc_updateValuStructureView(this, request)
					}, valueStructureInfo);
	
					loc_valueStructureInfo.push(valueStructureInfo);
				}
			});
		},
			
		initViews : function(requestInfo){
			loc_view = $('<div/>');
			
			_.each(loc_valueStructureInfo, function(vsInfo, i){
				var vsViewWrapper = $('<div/>');
				vsInfo.wrapperView = vsViewWrapper;
				
				var viewName = $('<div>Name:'+vsInfo.name+'</div>');
				vsViewWrapper.append(viewName);
				var viewId = $('<div>Id:'+vsInfo.id+'</div>');
				vsViewWrapper.append(viewId);
				
				var viewData = $('<textarea rows="15" cols="150" id="aboutDescription" style="resize: none;" data-role="none"></textarea>');
				vsInfo.viewData = viewData;
				vsViewWrapper.append(viewData);
			
				loc_view.append(vsViewWrapper);
			});
			return loc_view;
		},
			
		postInit : function(requestInfo){
			_.each(loc_valueStructureInfo, function(vsInfo, i){
				loc_updateValuStructureView(vsInfo, requestInfo);
			});
		},
	};
	return loc_out;
}
