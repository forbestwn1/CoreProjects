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

var node_createUITagOnBaseArray = function(env, uiTagDef){
	
	var loc_env = env;
	var loc_uiTagDef = uiTagDef;
	var loc_coreObj;
	
	//container data variable
	var loc_containerVariable;

	var loc_childVaraibles = [];
	
	var loc_id = 0;

	var loc_handleEachElementProcessor;
	
	var loc_generateId = function(){
		loc_id++;
		return loc_id+"";
	};
	
	var loc_getElementContextVariable = function(key){
		var out = node_createContextVariable(loc_dataContextEleName);
		out.path = node_namingConvensionUtility.cascadePath(out.path, key+"");
		return out;
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
packageObj.createChildNode("createUITagOnBaseArray", node_createUITagOnBaseArray); 

})(packageObj);
