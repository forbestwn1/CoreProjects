//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_getLifecycleInterface;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_expressionUtility;
	var node_dataRuleUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUITagOnBaseSimple = function(env, uiTagDef){
	
	var loc_env = env;
	var loc_uiTagDef = uiTagDef;
	var loc_coreObj;
	
	var loc_dataVariable;
	var loc_currentData;
	
	var loc_enumDataSet;
	
	//mandatory rule
	var loc_isMandatory = false;
	var loc_mandatoryRuleDescription;
	
	//script rule
	var loc_validationScriptFunction;
	var loc_jsScriptRuleDescription;
	
	//expression rule
	var loc_validationExpression;
	var loc_expressionRuleDescription;
	
	var loc_processDataRuleRequest = function(request){
		var out = node_createServiceRequestInfoSequence(undefined, undefined, request);

		//emum rule
		var dataEleDef = loc_env.getTagContextElementDefinition("internal_data");
		var dataInfo = dataEleDef[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION]
			[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_DEFINITION]
			[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_CRITERIA];
		var rules = dataInfo[node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_RULE];
		var	ruleMatchers = [node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_RULEMATCHERS];

		var rules = dataEleDef[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION]
				[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_DEFINITION]
				[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_CRITERIA]
				[node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_RULE];
		var enumRule;
		for(var i in rules){
			var rule = rules[i];
			var ruleType = rule[node_COMMONATRIBUTECONSTANT.DATARULE_RULETYPE];
			if(ruleType==node_COMMONCONSTANT.DATARULE_TYPE_ENUM){
				enumRule = rule;
			}
		}
		if(enumRule!=null){
			var enumCode = enumRule[node_COMMONATRIBUTECONSTANT.DATARULEENUMCODE_ENUMCODE];
			if(enumCode!=undefined){
				return nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([enumCode], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CODETABLE, {
					success : function(requestInfo, resources){
						var codeTableResource = resources[enumCode];
						loc_enumDataSet = codeTableResource[node_COMMONATRIBUTECONSTANT.CODETABLE_DATASET];
						return loc_getProcessEnumDataSetRequest(ruleMatchers);
					}
				});
			}
			else{
				loc_enumDataSet = enumRule[node_COMMONATRIBUTECONSTANT.DATARULEENUMDATA_DATASET];
				out.addRequest(loc_getProcessEnumDataSetRequest(ruleMatchers));
			}
		}
		return out;
	};
	
	var loc_getProcessEnumDataSetRequest = function(ruleMatchers){
		var reverseMatchers = ruleMatchers==undefined?undefined:ruleMatchers[node_COMMONATRIBUTECONSTANT.MATCHERSCOMBO_REVERSEMATCHERS];
		if(reverseMatchers==undefined)  return;
		
		var out = node_createServiceRequestInfoSequence(undefined, undefined, request);
		var enumMatcherRequest = node_createServiceRequestInfoSet(undefined, {
			success : function(requestInfo, subDatasResult){
				_.each(subDatasResult.getResults(), function(data, i){
					loc_enumDataSet[i] = data;
				});
			}
		});
		out.addRequest(enumMatcherRequest);
		
		_.each(loc_enumDataSet, function(enumData, i){
			//get each sub data request
			enumMatcherRequest.addRequest(i, node_expressionUtility.getMatchDataTaskRequest(dataValue, ruleMatchers==undefined?undefined:ruleMatchers[node_COMMONATRIBUTECONSTANT.MATCHERSCOMBO_MATCHERS]));
		});
		return out;
	};

	var loc_processDataRuleRequest1 = function(request){
		//emum rule
		var dataEleDef = loc_env.getTagContextElementDefinition("internal_data");
		var rules = dataEleDef[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION]
				[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_DEFINITION]
				[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_CRITERIA]
				[node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_RULE];
		var enumRule;
		for(var i in rules){
			var rule = rules[i];
			var ruleType = rule[node_COMMONATRIBUTECONSTANT.DATARULE_RULETYPE];
			if(ruleType==node_COMMONCONSTANT.DATARULE_TYPE_ENUM){
				enumRule = rule;
			}
			else if(ruleType==node_COMMONCONSTANT.DATARULE_TYPE_MANDATORY){
				loc_isMandatory = true;
				loc_mandatoryRuleDescription = rule[node_COMMONATRIBUTECONSTANT.ENTITYINFO_DESCRIPTION];
			}
			else if(ruleType==node_COMMONCONSTANT.DATARULE_TYPE_JSSCRIPT){
				var script = "loc_validationScriptFunction = function(that){" + rule[node_COMMONATRIBUTECONSTANT.DATARULEJSSCRIPT_SCRIPT] + "};";
				eval(script);
				loc_jsScriptRuleDescription = rule[node_COMMONATRIBUTECONSTANT.ENTITYINFO_DESCRIPTION];
			}
			else if(ruleType==node_COMMONCONSTANT.DATARULE_TYPE_EXPRESSION){
				loc_validationExpression = rule[node_COMMONATRIBUTECONSTANT.DATARULEEXPRESSION_EXPRESSIONEXECUTE];
				loc_expressionRuleDescription = rule[node_COMMONATRIBUTECONSTANT.ENTITYINFO_DESCRIPTION];
			}
		}
		if(enumRule!=null){
			var enumCode = enumRule[node_COMMONATRIBUTECONSTANT.DATARULEENUMCODE_ENUMCODE];
			if(enumCode!=undefined){
				return nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([enumCode], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CODETABLE, {
					success : function(requestInfo, resources){
						var codeTableResource = resources[enumCode];
						loc_enumDataSet = codeTableResource[node_COMMONATRIBUTECONSTANT.CODETABLE_DATASET];
					}
				});
			}
			else{
				loc_enumDataSet = enumRule[node_COMMONATRIBUTECONSTANT.DATARULEENUMDATA_DATASET];
			}
		}
	};
	
	var loc_updateView = function(request){
		loc_env.executeDataOperationRequestGet(loc_dataVariable, "", {
			success : function(requestInfo, data){
				if(data==undefined){
					loc_currentData = undefined;
				}
				else{
					loc_currentData = data.value;
				}
				loc_coreObj.updateView(loc_currentData);
			}
		}, request);
	};

	var loc_revertChange = function(){
		
	};

	var loc_createCoreObj = function(){
		loc_coreObj = _.extend({
			initViews : function(requestInfo){},
			updateView : function(data, request){},
			getViewData : function(){},
			getDataForDemo : function(){},
			destroy : function(request){},
		}, loc_uiTagDef[node_COMMONATRIBUTECONSTANT.UITAGDEFINITION_SCRIPT].call(loc_out, loc_baseObj));
	};
	
	var loc_baseObj = {
			
		getDataFlowType : function(){
			var out = loc_env.getAttributeValue(node_COMMONATRIBUTECONSTANT.STORYNODEUIDATA_ATTRIBUTE_DATAFLOW);
			if(out==undefined)	out = node_COMMONCONSTANT.DATAFLOW_IO; 
			return out;
		},

		getEnumDataSet : function(){
			return loc_enumDataSet;
		},
		
		onDataChange : function(data){
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
			
			loc_env.executeBatchDataOperationRequest([
				loc_env.getDataOperationSet(loc_dataVariable, "", loc_currentData)
			]);
			loc_env.trigueEvent("valueChanged", loc_currentData);
		},
		
		trigueEvent : function(eventName, eventData){
			if(eventName=='dataChanged'){
				this.onDataChange(eventData);
			}
		},
		
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(env, uiTagDef, handlers, requestInfo){
	};
	
	var loc_out = {
		findFunctionDown : function(name){
			
		},	
		created : function(){
			loc_createCoreObj();
		},
		preInit : function(request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("uiTagPreInitRequest", {}), undefined, request);
			loc_dataVariable = loc_env.createVariable("internal_data");
			out.addRequest(loc_processDataRuleRequest());
			return out;
		},
		initViews : function(handlers, request){
			return loc_coreObj.initViews(handlers, request);
		},
		postInit : function(request){
			loc_updateView(request);
			
			loc_dataVariable.registerDataChangeEventListener(undefined, function(event, eventData, request){
				loc_updateView(request);
			}, this);
			
//			loc_dataVariable.registerDataOperationEventListener(undefined, function(event, eventData, request){
//				loc_updateView(request);
//			}, this);
		},
		destroy : function(request){
			loc_dataVariable.release();	
			loc_coreObj.destroy();
		},
		createContextForDemo : function(id, parentContext, matchersByName, request){
			var node_createData = nosliw.getNodeData("uidata.data.entity.createData");
			var node_createContextElementInfo = nosliw.getNodeData("uidata.context.createContextElementInfo");
			var node_createContext = nosliw.getNodeData("uidata.context.createContext");
			
			var dataVarPar;
			if(parentContext!=undefined)	dataVarPar = parentContext.getContextElement("data");
			var dataVarEleInfo = undefined;
			if(dataVarPar!=undefined){
				var matchersCombo = matchersByName==undefined?{}:matchersByName["internal_data"];
				var info;
				if(matchersCombo!=undefined){
					info = {
							matchers : matchersCombo[node_COMMONATRIBUTECONSTANT.MATCHERSCOMBO_MATCHERS],
							reverseMatchers : matchersCombo[node_COMMONATRIBUTECONSTANT.MATCHERSCOMBO_REVERSEMATCHERS]
					};
				}
				dataVarEleInfo = node_createContextElementInfo("internal_data", dataVarPar, undefined, undefined, info);
			}
			else{
				var data = node_createData(loc_coreObj.getDataForDemo(), node_CONSTANT.WRAPPER_TYPE_APPDATA);
				dataVarEleInfo = node_createContextElementInfo("internal_data", data);
			}
			
			var elementInfosArray = [dataVarEleInfo];
			return node_createContext(id, elementInfosArray, request);
		},
		
		getValidateDataRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_env.getDataOperationRequestGet(loc_dataVariable, "", {
				success : function(requestInfo, data){

					var dataValue;
					if(data!=undefined)  dataValue = data.value;

					var dataEleDef = loc_env.getTagContextElementDefinition("internal_data");
					var dataInfo = dataEleDef[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION]
						[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_DEFINITION]
						[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_CRITERIA];
					var rules = dataInfo[node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_RULE];
					var	ruleMatchers = [node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_RULEMATCHERS];
					return node_expressionUtility.getMatchDataTaskRequest(dataValue, ruleMatchers==undefined?undefined:ruleMatchers[node_COMMONATRIBUTECONSTANT.MATCHERSCOMBO_MATCHERS], {
						success : function(request, matcheredData){
							return node_dataRuleUtility.getRulesValidationRequest(matcheredData, rules);
						}
					});
				}
			}));

			return out;
		},

		getValidateDataRequest1 : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_env.getDataOperationRequestGet(loc_dataVariable, "", {
				success : function(requestInfo, data){
					var dataValue;
					if(data!=undefined)  dataValue = data.value;
					
					//mandatory rule
					if(loc_isMandatory==true&&dataValue==undefined){
						return loc_mandatoryRuleDescription || "Cannot be blank";
					}
					
					//js script validation
					if(loc_validationScriptFunction!=undefined){
						if(!loc_validationScriptFunction(dataValue))  return loc_jsScriptRuleDescription || "Value validation fail";
					}
					
					//expression validation
					if(loc_validationExpression!=undefined){
						var variableValue = {};
						variableValue[node_COMMONATRIBUTECONSTANT.DATARULEEXPRESSION_VARIABLENAME] = dataValue;
						return nosliw.runtime.getExpressionService().getExecuteExpressionRequest(loc_validationExpression, undefined, variableValue, undefined, undefined, {
							success : function(request, expressionResult){
								if(expressionResult.value==false){
									return loc_expressionRuleDescription || "Expression validation fail";
								}
								else{
									return node_createServiceRequestInfoSimple(undefined, function(requestInfo){	});
								}
							}
						});
					}
					
					//valid value, return empty
					return node_createServiceRequestInfoSimple(undefined, function(requestInfo){	});
				}
			}));

			return out;
		},

	};
	
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_getLifecycleInterface(loc_out).init(env, uiTagDef);
	
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("data.dataRuleUtility", function(){node_dataRuleUtility = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createUITagOnBaseSimple", node_createUITagOnBaseSimple); 

})(packageObj);
