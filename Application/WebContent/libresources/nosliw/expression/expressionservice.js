//get/create package
var packageObj = library.getChildPackage("service");    

(function(packageObj){
	//get used node
	var node_resourceUtility;
	var node_requestServiceProcessor;
	var node_buildServiceProvider;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_basicUtility;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoService;
	var node_createServiceRequestInfoSimple;
	var node_OperationContext;
	var node_OperationParm;
	var node_OperationParms;
	var node_DependentServiceRequestInfo;
	var node_expressionUtility;
	var node_dataUtility;
	var node_namingConvensionUtility;
	var node_objectOperationUtility;
	var node_aliasUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createExpressionService = function(){

	var loc_getExecuteScriptRequest = function(script, functions, expressionItems, variables, constants, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExpressionService_ExecuteScript", {"script":script, "expressions":expressionItems, "variables":variables}), handlers, requestInfo);

		//calculate multiple expression
		var executeMultipleExpressionItemRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("ExecuteMultipleExpression", {"expressions":expressionItems, "variables":variables}), {
			success : function(requestInfo, expressionsResult){
				var expressionsData = expressionsResult.getResults();
				return script.call(undefined, functions, expressionsData, constants, variables);
			}
		});
		_.each(expressionItems, function(expressionItem, name){
			//find variable value only for this expression
			executeMultipleExpressionItemRequest.addRequest(name, node_expressionUtility.getExecuteExpressionRequest(expressionItem, variables, constants, {}));
		});
		
		out.addRequest(executeMultipleExpressionItemRequest);
		return out;
	};

	var loc_getExecuteScriptObjectRequest = function(scriptGroupObj, id, input, constants, handlers, requester_parent){
		if(id==undefined)  id = node_COMMONCONSTANT.NAME_DEFAULT;
		var scriptObj = scriptGroupObj[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPTGROUP_ELEMENT][id];
		
		var expressions = {};
		_.each(scriptObj[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPTENTITY_EXPRESSIONREF], function(expressionId, i){
			expressions[expressionId] = scriptGroupObj[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPTGROUP_EXPRESSIONGROUP][node_COMMONATRIBUTECONSTANT.EXPRESSIONGROUP_EXPRESSIONS][expressionId]; 
		});
		
		var varNames = scriptObj[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPTENTITY_VARIABLES];
		var scriptFun = scriptObj[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPTENTITY_SCRIPTFUNCTION];
		var supportFuns = scriptObj[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPTENTITY_SUPPORTFUNCTION];

//		var expandedInput = node_aliasUtility.expandInputByFlatContext(input, scriptObj[node_COMMONATRIBUTECONSTANT.EXECUTABLESCRIPTENTITY_CONTEXT]);
		var expandedInput = input;
		
		var varInputs = {};
		_.each(varNames, function(varName, index){
			varInputs[varName] = node_objectOperationUtility.getObjectAttributeByPath(expandedInput, varName);
		});

		return loc_getExecuteScriptRequest(scriptFun, supportFuns, expressions, varInputs, constants, handlers, requester_parent);
	};

	
	var loc_out = {
		
		getExecuteOperationRequest : function(dataTypeId, operation, parmsArray, handlers, requester_parent){
			return node_expressionUtility.getExecuteOperationRequest(dataTypeId, operation, parmsArray, handlers, requester_parent);
		},
			
		executeExecuteOperationRequest : function(dataTypeId, operation, parmsArray, handlers, requester_parent){
			var requestInfo = this.getExecuteOperationRequest(dataTypeId, operation, parmsArray, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},

		getExecuteExpressionRequest : function(expression, eleName, variables, constants, references, handlers, requester_parent){
			return node_expressionUtility.getExecuteExpressionRequest(expression, eleName, variables, constants, references, handlers, requester_parent);
		},
			
		executeExecuteExpressionRequest : function(expression, eleName, variables, constants, references, handlers, requester_parent){
			var requestInfo = this.getExecuteExpressionRequest(expression, eleName, variables, constants, references, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},

		getMatchDataRequest : function(data, matchers, handlers, requester_parent){
			return node_expressionUtility.getMatchDataTaskRequest(data, matchers, handlers, requester_parent);
		},

		executeMatchDataRequest : function(data, matchers, handlers, requester_parent){
			var requestInfo = this.getMatchDataRequest(data, matchers, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		/**
		 * Execute script expression
		 * 		script : function with parameter map (name : expression result)
		 * 		expressions : map (name : expression)
		 * 		variables : variables for expression
		 * 		constants : constants in script
		 */
		getExecuteScriptRequest : function(script, functions, expressionsItems, variables, constants, handlers, requester_parent){
			return loc_getExecuteScriptRequest(script, functions, expressionsItems, variables, constants, handlers, requester_parent);
		},
	
		executeExecuteScriptExpressionRequest : function(script, functions, expressionsItems, variables, constants, handlers, requester_parent){
			var requestInfo = this.getExecuteScriptRequest(script, functions, expressionsItems, variables, constants, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		getExecuteScriptObjectRequest : function(scriptGroupObj, id, variables, constants, handlers, requester_parent){
			return loc_getExecuteScriptObjectRequest(scriptGroupObj, id, variables, constants, handlers, requester_parent);
		},
	
		executeExecuteScriptObjectRequest : function(scriptGroupObj, id, variables, constants, handlers, requester_parent){
			var requestInfo = this.getExecuteScriptObjectRequest(scriptGroupObj, id, variables, constants, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},

	};
	
	loc_out = node_buildServiceProvider(loc_out, "expressionService");
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationContext", function(){node_OperationContext = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParm", function(){node_OperationParm = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParms", function(){node_OperationParms = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("expression.dataUtility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.aliasUtility", function(){node_aliasUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createExpressionService", node_createExpressionService); 
	
})(packageObj);
