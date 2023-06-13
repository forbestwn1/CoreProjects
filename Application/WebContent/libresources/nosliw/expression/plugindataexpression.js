//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildComponentInterface;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_ResourceId;
	var node_resourceUtility;
	var node_componentUtility;
	var node_createServiceRequestInfoSimple;
	var node_expressionUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createDataExpressionGroupPlugin = function(){
	
	var loc_out = {

		getCreateComplexEntityCoreRequest : function(complexEntityDef, valueContextId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createDataExpressionGroupComponentCore(complexEntityDef, valueContextId, bundleCore, configure);
			}, handlers, request);
		},
	};

	return loc_out;
};

var loc_createDataExpressionGroupComponentCore = function(complexEntityDef, valueContextId, bundleCore, configure){

	var loc_complexEntityDef = complexEntityDef;
	
	var loc_valueContextId = valueContextId;
	var loc_bundleCore = bundleCore;
	
	var loc_out = {
		
		getAllExpressionIds : function(){
			var out = [];
			var expressions = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXPRESSIONGROUP_EXPRESSIONS);
			_.each(expressions, function(expression, i){
				out.push(expression[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]);
			});
			return out;
		},
		
		getExecuteDataExpressionRequest : function(dataExpressionId, handlers, request){
			var varDomain = loc_bundleCore.getVariableDomain();
			loc_valueContext = varDomain.getValueContext(loc_valueContextId);
			
			var expressions = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXPRESSIONGROUP_EXPRESSIONS);
			var expressionItem;		
			_.each(expressions, function(expression, i){
				if(expression[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]==dataExpressionId){
					expressionItem = expression;
				}
			});
			
			var variables = {};
			var variablesInfo = complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXPRESSIONGROUP_VARIABLEINFOS);
			_.each(variablesInfo[node_COMMONATRIBUTECONSTANT.CONTAINERVARIABLECRITERIAINFO_VARIABLES], function(varInfo, i){
				var varKey = varInfo[node_COMMONATRIBUTECONSTANT.CONTAINERVARIABLECRITERIAINFO_VARIABLEKEY]; 
				var varId = varInfo[node_COMMONATRIBUTECONSTANT.CONTAINERVARIABLECRITERIAINFO_VARIABLEID];
				var rootElemId = varId[node_COMMONATRIBUTECONSTANT.IDVARIABLE_ROOTELEMENTID];
				var variable = loc_valueContext.createVariable(
						rootElemId[node_COMMONATRIBUTECONSTANT.IDROOTELEMENT_VALUESTRUCTUREID], 
						rootElemId[node_COMMONATRIBUTECONSTANT.IDROOTELEMENT_ROOTNAME],
						varId[node_COMMONATRIBUTECONSTANT.IDVARIABLE_ELEMENTPATH]);
				variables[varKey] = variable;	
			});
			
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("calExpression"), handlers, request);
						
			var calVarsRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("calVariables", {}), {
				success : function(request, results){
					return node_expressionUtility.getExecuteExpressionItemRequest(expressionItem, results.getResults());
				}	
			});
			
			_.each(variables, function(variable, key){
				calVarsRequest.addRequest(key, variable.getGetValueRequest({
					success : function(request, data){
						var value = data;
						if(data!=undefined&&data.value!=undefined){
						    value = data.value;
						}
						return value;
					}	
				}));
			});
			
			out.addRequest(calVarsRequest);
			return out;
		},
		
	};
	
	return loc_out;
	
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createDataExpressionGroupPlugin", node_createDataExpressionGroupPlugin); 

})(packageObj);
