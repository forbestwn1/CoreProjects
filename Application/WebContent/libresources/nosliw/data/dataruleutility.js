//get/create package
var packageObj = library;    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONATRIBUTECONSTANT;
var node_COMMONCONSTANT;
var node_basicUtility;
var node_parseSegment;
var node_createContextElementInfo;
var node_createContext;
var node_createContextVariableInfo;
var node_createServiceRequestInfoSequence;
var node_createServiceRequestInfoSimple;
var node_ServiceInfo;
var node_createServiceRequestInfoSet;
var node_uiDataOperationServiceUtility;
var node_dataUtility;
var node_requestUtility;

//*******************************************   Start Node Definition  ************************************** 	
var node_utility = function(){

	var loc_getSubDataByPath = function(data, path){
		if(path==undefined || path=="")   return data;
		if(path.indexOf(".")==-1){
			return data.value[path];
		}
	};
	
	var loc_isValidForEnumDataSet = function(data, enumDataSet){
		for(var i in enumDataSet){
			if(node_dataUtility.isDataEqual(data, enumDataSet[i])==true)   return true;
		}
		return false;
	};
	
	var loc_getCreateErrorMessageRequest = function(rule, errorMsg){
		var errorMessage = rule[node_COMMONATRIBUTECONSTANT.ENTITYINFO_DESCRIPTION];
		if(node_basicUtility.isStringEmpty(errorMessage)){
			errorMessage = errorMsg;
		}
		return node_createServiceRequestInfoSimple(undefined, function(request){
			return errorMessage;
		})
	};
	
	var loc_getRuleValidationRequest = function(data, rule, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

		var targetData = loc_getSubDataByPath(data, rule[node_COMMONATRIBUTECONSTANT.DATARULE_PATH]);

		var ruleType = rule[node_COMMONATRIBUTECONSTANT.DATARULE_RULETYPE];
		if(ruleType==node_COMMONCONSTANT.DATARULE_TYPE_ENUM){
			var enumCode = rule[node_COMMONATRIBUTECONSTANT.DATARULEENUMCODE_ENUMCODE];
			if(enumCode!=undefined){
				out.addRequest(nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([enumCode], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CODETABLE, {
					success : function(requestInfo, resources){
						var codeTableResource = resources[enumCode];
						var enumDataSet = codeTableResource[node_COMMONATRIBUTECONSTANT.CODETABLE_DATASET];
						if(loc_isValidForEnumDataSet(targetData, enumDataSet)==false)		return loc_getCreateErrorMessageRequest(rule, "Not valid value");
						else		return node_requestUtility.getEmptyRequest();
					}
				}));
			}
			else{
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
					var enumDataSet = rule[node_COMMONATRIBUTECONSTANT.DATARULEENUMDATA_DATASET];
					if(loc_isValidForEnumDataSet()==false)		return loc_getCreateErrorMessageRequest(rule, "Not valid value");
					else		return node_requestUtility.getEmptyRequest();
				}));
			}
		}
		else if(ruleType==node_COMMONCONSTANT.DATARULE_TYPE_MANDATORY){
			//mandatory rule
			if(targetData==undefined||targetData.value==undefined){
				out.addRequest(loc_getCreateErrorMessageRequest(rule, "Cannot be blank"));
				return out;
			}
		}
		else if(ruleType==node_COMMONCONSTANT.DATARULE_TYPE_JSSCRIPT){
			var script = "var loc_validationScriptFunction = function(that){" + rule[node_COMMONATRIBUTECONSTANT.DATARULEJSSCRIPT_SCRIPT] + "};";
			eval(script);
			//js script validation
			if(loc_validationScriptFunction!=undefined){
				if(!loc_validationScriptFunction(targetData)){
					out.addRequest(loc_getCreateErrorMessageRequest(rule, "Value validation fail"));
					return out;
				}
			}
		}
		else if(ruleType==node_COMMONCONSTANT.DATARULE_TYPE_EXPRESSION){
			var validationExpression = rule[node_COMMONATRIBUTECONSTANT.DATARULEEXPRESSION_EXPRESSIONEXECUTE];
			
			if(validationExpression!=undefined){
				var variableValue = {};
				variableValue[node_COMMONATRIBUTECONSTANT.DATARULEEXPRESSION_VARIABLENAME] = targetData;
				out.addRequest(nosliw.runtime.getExpressionService().getExecuteExpressionRequest(validationExpression, undefined, variableValue, undefined, undefined, {
					success : function(request, expressionResult){
						if(expressionResult.value==false){
							return loc_getCreateErrorMessageRequest(rule, "Expression validation fail");
						}
						else{
							return node_requestUtility.getEmptyRequest();
						}
					}
				}));
			}
		}
		out.addRequest(node_requestUtility.getEmptyRequest());
		return out;
	};
	
	var loc_out = {
		
		getRulesValidationRequest : function(data, rules, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var rulesRequest = node_createServiceRequestInfoSequence();
			var errorMsgs = [];
			_.each(rules, function(rule, i){
				var ruleRequest = node_createServiceRequestInfoSequence();
				var ifruleRequest = node_createServiceRequestInfoSimple(undefined, function(request){
					var errorMsgs = request.getData("errorMsgs");
					if(errorMsgs.length==0){
						var validateRequest = loc_getRuleValidationRequest(request.getData("data"), request.getData("rule"), {
							success : function(request, msg){
								if(!node_basicUtility.isStringEmpty(msg)){
									errorMsgs.push(msg);
								}
							}
						});
						validateRequest.setData(errorMsgs, "errorMsgs");
						return validateRequest;
					}
				}); 
				ifruleRequest.setData(errorMsgs, "errorMsgs");
				ifruleRequest.setData(data, "data");
				ifruleRequest.setData(rule, "rule");
				ruleRequest.addRequest(ifruleRequest);
				rulesRequest.addRequest(ruleRequest);
			});

			var lastRequest = node_createServiceRequestInfoSimple(undefined, function(request){
				var errorMsgs = request.getData("errorMsgs");
				return errorMsgs;
			});
			lastRequest.setData(errorMsgs, "errorMsgs");
			rulesRequest.addRequest(lastRequest);

			out.addRequest(rulesRequest);
			
			return out;
		},
			
		getRulesValidationRequest1 : function(data, rules, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var rulesRequest = node_createServiceRequestInfoSet(undefined, {
				success : function(request, resultSet){
					var errorMsgs = [];
					var results = resultSet.getResults();
					_.each(results, function(msg){
						if(!node_basicUtility.isStringEmpty(msg)){
							errorMsgs.push(msg);
						}
					});
					return errorMsgs;
				}
			});
			_.each(rules, function(rule, i){
				rulesRequest.addRequest(i, loc_getRuleValidationRequest(data, rule));
			})
			
			out.addRequest(rulesRequest);
			
			return out;
		},

	
	};
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextVariableInfo", function(){node_createContextVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("dataRuleUtility", node_utility); 

})(packageObj);
