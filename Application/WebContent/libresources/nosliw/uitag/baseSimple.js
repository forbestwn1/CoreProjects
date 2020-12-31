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
//*******************************************   Start Node Definition  ************************************** 	

var node_createUITagOnBaseSimple = function(env, uiTagDef){
	
	var loc_env = env;
	var loc_uiTagDef = uiTagDef;
	var loc_coreObj;
	
	var loc_dataVariable;
	
	var loc_enumDataSet;
	
	//mandatory rule
	var loc_isMandatory = false;
	var loc_mandatoryRuleDescription;
	
	var loc_processDataRuleRequest = function(request){
		//emum rule
		var dataEleDef = loc_env.getTagContextElementDefinition("internal_data");
		var rules = dataEleDef[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION]
				[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_DEFINITION]
				[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_CRITERIA]
				[node_COMMONATRIBUTECONSTANT.VARIABLEINFO_DATAINFO]
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
		}
		if(enumRule!=null){
			var enumCode = enumRule[node_COMMONATRIBUTECONSTANT.DATARULE_ENUMCODE];
			if(enumCode!=undefined){
				return nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([enumCode], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CODETABLE, {
					success : function(requestInfo, resources){
						var codeTableResource = resources[enumCode];
						loc_enumDataSet = codeTableResource[node_COMMONATRIBUTECONSTANT.CODETABLE_DATASET];
					}
				});
			}
			else{
				loc_enumDataSet = enumRule[node_COMMONATRIBUTECONSTANT.DATARULE_DATASET];
			}
		}
	};
	
	var loc_updateView = function(request){
		loc_env.executeDataOperationRequestGet(loc_dataVariable, "", {
			success : function(requestInfo, data){
				if(data==undefined)			loc_coreObj.updateView();
				else loc_coreObj.updateView(data.value);
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
		
		trigueEvent : function(eventName, eventData){
			if(eventName=='dataChanged'){
				loc_env.executeBatchDataOperationRequest([
					loc_env.getDataOperationSet(loc_dataVariable, "", eventData)
				]);
				loc_env.trigueEvent("valueChanged", eventData);
			}
		},
		
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(env, uiTagDef, handlers, requestInfo){
	};
	
	var loc_out = {
		findFunctionDown : function(name){
			
		},	
		preInit : function(request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("uiTagPreInitRequest", {}), undefined, request);
			loc_dataVariable = loc_env.createVariable("internal_data");
			out.addRequest(loc_processDataRuleRequest());
			loc_createCoreObj();
			return out;
		},
		initViews : function(request){
			return loc_coreObj.initViews(request);
		},
		postInit : function(request){
			loc_updateView(request);
			
			loc_dataVariable.registerDataOperationEventListener(undefined, function(event, eventData, request){
				loc_updateView(request);
			}, this);
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
					//mandatory rule
					if(loc_isMandatory==true&&data==undefined){
						return loc_mandatoryRuleDescription || "Cannot be blank";
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

//Register Node by Name
packageObj.createChildNode("createUITagOnBaseSimple", node_createUITagOnBaseSimple); 

})(packageObj);
