//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_expressionUtility;
	var node_dataRuleUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_uiTagUtility = function(env, uiTagDef){
	
	
	var loc_out = {
		
		getValidateDataRequest : function(varName, uiTagEnv, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(uiTagEnv.getDataOperationRequestGet(varName, "", {
				success : function(requestInfo, uiData){
					var dataValue;
					if(uiData!=undefined)  dataValue = uiData.value;

					var dataEleDef = uiTagEnv.getTagContextElementDefinition(varName);
					var dataInfo = dataEleDef[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION]
						[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_DEFINITION]
						[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_CRITERIA];
					var rules = dataInfo[node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_RULE];
					var	ruleMatchers = dataInfo[node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_RULEMATCHERS];
					return node_expressionUtility.getMatchDataTaskRequest(dataValue, ruleMatchers==undefined?undefined:ruleMatchers[node_COMMONATRIBUTECONSTANT.MATCHERSCOMBO_MATCHERS], {
						success : function(request, matcheredData){
							return node_dataRuleUtility.getRulesValidationRequest(matcheredData, rules);
						}
					});
				}
			}));

			return out;
		},
	};
	
	
	return loc_out;
}();
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("data.dataRuleUtility", function(){node_dataRuleUtility = this.getData();	});

//Register Node by Name
packageObj.createChildNode("uiTagUtility", node_uiTagUtility); 

})(packageObj);
