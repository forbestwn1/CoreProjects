
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"resourceType":"script",
"id":"*complexscript_test_value"
},
"children":[],
"dependency":{},
"info":{}
}, {"script":function(complexEntityDef, valueContextId, bundleCore, configure){

	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
	var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
	var node_basicUtility = nosliw.getNodeData("common.utility.basicUtility");

	var loc_parms;
    var loc_scriptVars;
	var loc_configure;

	var loc_valueContext;

	var loc_variableInfos = [];
	

	var loc_init = function(complexEntityDef, valueContextId, bundleCore, configure){
		
		loc_parms = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTCOMPLEXSCRIPT_PARM);
    	loc_scriptVars = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTCOMPLEXSCRIPT_VARIABLE);
    	loc_unknownVars = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLETESTCOMPLEXSCRIPT_UNKNOWNVARIABLE);
		loc_configure = configure;
	
		var varDomain = bundleCore.getVariableDomain();
		loc_valueContext = varDomain.getValueContext(valueContextId);

		if(loc_scriptVars!=undefined&&loc_scriptVars.length>0){
			_.each(loc_scriptVars, function(varResolve, i){
				var varInfo = {
					reference : varResolve[node_COMMONATRIBUTECONSTANT.INFOREFERENCERESOLVE_ELEREFERENCE],
					variable : loc_valueContext.createResolvedVariable(varResolve),
				};
				loc_variableInfos.push(varInfo);
			});
		}
		else{
			var vsIds = loc_valueContext.getValueStructureRuntimeIds();
			_.each(vsIds.solid, function(vsId, i){
				var vs = loc_valueContext.getValueStructure(vsId);
				var rootNames = vs.getElementsName();
				_.each(rootNames, function(rootName, i){
					var varInfo = {
						reference : rootName,
						variable : loc_valueContext.createVariable(vsId, rootName),
					};
					loc_variableInfos.push(varInfo);
				});
			});		

			_.each(vsIds.soft, function(vsId, i){
				var vs = loc_valueContext.getValueStructure(vsId);
				var rootNames = vs.getElementsName();
				_.each(rootNames, function(rootName, i){
					var varInfo = {
						reference : rootName,
						variable : loc_valueContext.createVariable(vsId, rootName),
					};
					loc_variableInfos.push(varInfo);
				});
			});		
		}
	};

	var loc_updateDataDisplay = function(varInfo){
		varInfo.variable.executeDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(), {
			success : function(request, data){
				var value;
				if(data!=undefined&&data.value!=undefined){
				    value = JSON.stringify(data.value, null, 4);
				}
				varInfo.displayView.text(value);
			}	
		});
	};

	var loc_out = {
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){
			var rootView =  $('<div>' + '</div>');
			$(runtimeContext.view).append(rootView);
			
			var containerView =  $('<div></div>');
			rootView.append(containerView);	
				
			_.each(loc_variableInfos, function(varInfo, i){

				var varContainerViewWrapper =  $('<div style="border:solid 3px;"></div>');
				containerView.append(varContainerViewWrapper);	

				var varViewWrapper = $('<div></div>');
				var varNameView = $('<p>Variable:'+node_basicUtility.stringify(varInfo.reference)+'</p>');
				var varIdView = $('<p>Id:'+varInfo.variable.prv_id+'</p>');
				varViewWrapper.append(varNameView);	
				varViewWrapper.append(varIdView);	
				
				varContainerViewWrapper.append(varViewWrapper);	

				var viewWrapper = $('<div>Value:</div>');
				varInfo.view = $('<textarea rows="1" cols="150" style="resize: none; border:solid 1px;" data-role="none"></textarea>');
				viewWrapper.append(varInfo.view);
				varContainerViewWrapper.append(viewWrapper);	
				varInfo.view.bind('change', function(){
					var value = varInfo.view.val();
					if(value==undefined || value==""){}
					else {
						value = {
							dataTypeId: "test.string;1.0.0",
							value: varInfo.view.val()
						};
					}
				
					varInfo.variable.executeDataOperationRequest(node_uiDataOperationServiceUtility.createSetOperationService("", value));
				});					

				varInfo.displayView = $('<span/>');
				var displayViewWrapper = $('<div>ValueDisplay:</div>');
				displayViewWrapper.append(varInfo.displayView);
				varContainerViewWrapper.append(displayViewWrapper);	

				varInfo.variable.registerDataChangeEventListener(undefined, function(eventName, eventData){
					loc_updateDataDisplay(varInfo);
				});
				loc_updateDataDisplay(varInfo);
			});
			
			var unknowVarStr = JSON.stringify(loc_unknownVars, null, 4);
			var unknownView =  $('<div>Unknow Variables : '+unknowVarStr+'</div>');
			rootView.append(unknownView);	
		},
		
	};
	
	loc_init(complexEntityDef, valueContextId, bundleCore, configure);
	return loc_out;
}

}, {"loadPattern":"file"
});

