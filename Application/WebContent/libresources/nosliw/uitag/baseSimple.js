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

var node_createUITagOnBaseSimple = function(tagDefScriptFun, envObj){
	var loc_tagDefScriptFun = tagDefScriptFun;
	var loc_envObj = envObj;

	var loc_coreObj;
	
	var loc_dataVariable;
	var loc_currentData;
	
	var loc_createCoreObj = function(){
		loc_coreObj = loc_tagDefScriptFun(loc_coreEnvObj); 
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

	var loc_coreEnvObj = {
			
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
			
			loc_envObj.executeBatchDataOperationRequest([
				loc_envObj.getDataOperationSet(loc_dataVariable, "", loc_currentData)
			]);
			loc_envObj.trigueEvent("valueChanged", loc_currentData);
		},
		
		trigueEvent : function(eventName, eventData){
			if(eventName=='dataChanged'){
				this.onDataChange(eventData);
			}
		},
		
	};
	
	var loc_out = {
		
		created : function(){
			loc_createCoreObj();
		},
		preInit : function(request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("uiTagPreInitRequest", {}), undefined, request);
			loc_dataVariable = loc_envObj.createVariableByName("internal_data");
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
	};
	
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
packageObj.createChildNode("createUITagOnBaseSimple", node_createUITagOnBaseSimple); 

})(packageObj);
