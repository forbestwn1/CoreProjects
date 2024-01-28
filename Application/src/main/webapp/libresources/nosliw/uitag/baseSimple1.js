//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_getLifecycleInterface;
	var node_basicUtility;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_expressionUtility;
	var node_dataRuleUtility;
	var node_uiTagUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUITagOnBaseSimple = function(env, uiTagDef){
	
	var loc_env = env;
	var loc_uiTagDef = uiTagDef;
	var loc_coreObj;
	
	var loc_dataVariable;
	var loc_currentData;
	
	var loc_enumDataSet = {};
	
	//mandatory rule
	var loc_isMandatory = false;
	var loc_mandatoryRuleDescription;
	
	//script rule
	var loc_validationScriptFunction;
	var loc_jsScriptRuleDescription;
	
	//expression rule
	var loc_validationExpression;
	var loc_expressionRuleDescription;
	
	var loc_DEFAULTPATH = "AAAAAAAAAAAAAAAAAAAAA";
	
	var loc_processDataRuleRequest = function(request){
		var out = node_createServiceRequestInfoSequence(undefined, undefined, request);

		//emum rule
		var dataEleDef = loc_env.getTagContextElementDefinition("internal_data");
		var dataInfo = dataEleDef[node_COMMONATRIBUTECONSTANT.ROOTSTRUCTURE_DEFINITION]
			[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_DEFINITION]
			[node_COMMONATRIBUTECONSTANT.ELEMENTSTRUCTURE_CRITERIA];
		var rules = dataInfo[node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_RULE];
		var	ruleMatchers = dataInfo[node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_RULEMATCHERS];

		var enumRule;
		for(var i in rules){
			var rule = rules[i];
			var ruleType = rule[node_COMMONATRIBUTECONSTANT.DATARULE_RULETYPE];
			if(ruleType==node_COMMONCONSTANT.DATARULE_TYPE_ENUM){
				enumRule = rule;
			}
		}
		if(enumRule!=null){
			var ruleTargetPath = enumRule[node_COMMONATRIBUTECONSTANT.DATARULE_PATH];
			if(!(ruleMatchers!=null && !node_basicUtility.isStringEmpty(ruleTargetPath))){
				var path = ruleTargetPath;
				if(node_basicUtility.isStringEmpty(path))  path = loc_DEFAULTPATH;
				var enumCode = enumRule[node_COMMONATRIBUTECONSTANT.DATARULEENUMCODE_ENUMCODE];
				if(enumCode!=undefined){
					return nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([enumCode], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CODETABLE, {
						success : function(requestInfo, resources){
							var codeTableResource = resources[enumCode];
							var enumDataSet = codeTableResource[node_COMMONATRIBUTECONSTANT.CODETABLE_DATASET];
							loc_enumDataSet[path] = enumDataSet;
							return loc_getProcessEnumDataSetMatchersRequest(enumDataSet, ruleMatchers);
						}
					});
				}
				else{
					var enumDataSet = enumRule[node_COMMONATRIBUTECONSTANT.DATARULEENUMDATA_DATASET];
					loc_enumDataSet[path] = enumDataSet;
					out.addRequest(loc_getProcessEnumDataSetMatchersRequest(enumDataSet, ruleMatchers));
				}
			}
		}
		return out;
	};
	
	var loc_getProcessEnumDataSetMatchersRequest = function(enumDataSet, ruleMatchers){
		var reverseMatchers = ruleMatchers==undefined?undefined:ruleMatchers[node_COMMONATRIBUTECONSTANT.MATCHERSCOMBO_REVERSEMATCHERS];
		if(reverseMatchers==undefined)  return;
		
		var out = node_createServiceRequestInfoSequence(undefined, undefined, undefined);
		var enumMatcherRequest = node_createServiceRequestInfoSet(undefined, {
			success : function(requestInfo, subDatasResult){
				_.each(subDatasResult.getResults(), function(data, i){
					enumDataSet[i] = data;
				});
			}
		});
		out.addRequest(enumMatcherRequest);
		
		_.each(enumDataSet, function(enumData, i){
			//get each sub data request
			enumMatcherRequest.addRequest(i, node_expressionUtility.getMatchDataTaskRequest(enumData, ruleMatchers==undefined?undefined:ruleMatchers[node_COMMONATRIBUTECONSTANT.MATCHERSCOMBO_REVERSEMATCHERS]));
		});
		return out;
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
			getValidationRequest : function(request){},
			destroy : function(request){},
		}, loc_uiTagDef[node_COMMONATRIBUTECONSTANT.UITAGDEFINITION_SCRIPT].call(loc_out, loc_baseObj));
	};
	
	var loc_baseObj = {
			
		getDataFlowType : function(){
			var out = loc_env.getAttributeValue(node_COMMONATRIBUTECONSTANT.STORYNODEUIDATA_ATTRIBUTE_DATAFLOW);
			if(out==undefined)	out = node_COMMONCONSTANT.DATAFLOW_IO; 
			return out;
		},

		getEnumDataSet : function(path){
			if(node_basicUtility.isStringEmpty(path))  path = loc_DEFAULTPATH;
			return loc_enumDataSet[path];
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
		},
		destroy : function(request){
			loc_dataVariable.release();	
			loc_coreObj.destroy();
		},
		createContextForDemo : function(id, parentContext, matchersByName, request){
			var node_createData = nosliw.getNodeData("variable.data.entity.createData");
			var node_createContextElementInfo = nosliw.getNodeData("variable.context.createContextElementInfo");
			var node_createContext = nosliw.getNodeData("variable.context.createContext");
			
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
			var ruleValidationRequest = node_uiTagUtility.getValidateDataRequest("internal_data", loc_env);
			var coreValidationRequest = loc_coreObj.getValidationRequest({
				success: function(request, errMsg){
					if(errMsg==undefined)	return ruleValidationRequest;
					else  return node_createServiceRequestInfoSimple(undefined, function(request){return [errMsg];});
				}
			});
			if(coreValidationRequest!=undefined)   out.addRequest(coreValidationRequest);
			else out.addRequest(ruleValidationRequest);
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
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("data.dataRuleUtility", function(){node_dataRuleUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("uitag.uiTagUtility", function(){node_uiTagUtility = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createUITagOnBaseSimple1", node_createUITagOnBaseSimple); 

})(packageObj);
