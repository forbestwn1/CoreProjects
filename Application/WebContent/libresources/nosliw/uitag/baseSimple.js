//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_getLifecycleInterface;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUITagOnBaseSimple = function(env, uiTagDef){
	
	var loc_env;
	var loc_uiTagDef;
	var loc_coreObj;
	
	var loc_dataVariable;
	
	var loc_enumDataSet;
	
	var loc_processDataRule = function(){
		//emum rule
		var dataEleDef = loc_env.getTagContextElementDefinition("internal_data");
		var rules = dataEleDef[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION]
				[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_DEFINITION]
				[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_CRITERIA]
				[node_COMMONATRIBUTECONSTANT.VARIABLEINFO_DATAINFO]
				[node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_RULE];
		var enumRule;
		for(var i in rules){
			if(rules[i][node_COMMONATRIBUTECONSTANT.DATARULE_RULETYPE]==node_COMMONCONSTANT.DATARULE_TYPE_ENUM){
				enumRule = rules[i];
				break;
			} 
		}
		if(enumRule!=null){
			var enumCode = enumRule[node_COMMONATRIBUTECONSTANT.DATARULE_ENUMCODE];
			if(enumCode!=undefined){
				var gatewayParms = {
					"id" : enumCode,
				};
				return nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_CODETABLE, node_COMMONATRIBUTECONSTANT.GATEWAYCODETABLE_COMMAND_GETCODETABLE, gatewayParms, {
					success : function(requestInfo, codeTable){
						loc_enumDataSet = codeTable[node_COMMONATRIBUTECONSTANT.CODETABLE_DATASET];
					}
				});
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
		loc_env = env;
		loc_uiTagDef = uiTagDef;
		
		loc_coreObj = _.extend({
			initViews : function(requestInfo){},
			updateView : function(data, request){},
			getViewData : function(){},
			getDataForDemo : function(){},
			destroy : function(request){},
		}, loc_uiTagDef[node_COMMONATRIBUTECONSTANT.UITAGDEFINITION_SCRIPT].call(loc_out, loc_baseObj));

	};
	
	var loc_out = {
		findFunctionDown : function(name){
			
		},	
		preInit : function(request){
			loc_dataVariable = loc_env.createVariable("internal_data");
			loc_processDataRule();
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
		createContextForDemo : function(id, parentContext){
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

//Register Node by Name
packageObj.createChildNode("createUITagOnBaseSimple", node_createUITagOnBaseSimple); 

})(packageObj);
