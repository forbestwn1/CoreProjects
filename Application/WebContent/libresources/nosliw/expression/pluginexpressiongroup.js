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

var node_createExpressionGroupPlugin = function(){
	
	var loc_out = {

		getCreateComplexEntityCoreRequest : function(complexEntityDef, valueContextId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createExpressionGroupComponentCore(complexEntityDef, valueContextId, bundleCore, configure);
			}, handlers, request);
		},
	};

	return loc_out;
};

var loc_createExpressionGroupComponentCore = function(complexEntityDef, valueContextId, bundleCore, configure){

	var loc_complexEntityDef = complexEntityDef;
	var loc_valueContextId = valueContextId;
	var loc_bundleCore = bundleCore;
	var loc_valueContext = loc_bundleCore.getVariableDomain().getValueContext(loc_valueContextId);
	var loc_envInterface = {};
	var loc_dataExpressionGroupRuntime;
	
	var loc_getAllExpressionItems = function(){
		return loc_complexEntityDef.getSimpleAttributeValue(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYEXPRESSIONSCRIPTGROUP_EXPRESSIONS)[node_COMMONATRIBUTECONSTANT.EXECUTABLECONTAINEREXPRESSION_ELEMENT];
	};
	
	var loc_out = {
		
		getComplexEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_COMPLEXENTITY].createAttributeRequest(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEXWITHDATAEXPRESSIONGROUP_DATAEEXPRESSIONGROUP, {
				success : function(request, childNode){
					loc_dataExpressionGroupRuntime = childNode.getChildValue();
				}
			}));
			return out;
		},
		
		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},
		
		getAllItemIds : function(){
			var out = [];
			var expressions = loc_getAllExpressionItems();
			_.each(expressions, function(expression, i){
				out.push(expression[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]);
			});
			return out;
		},
		
		getExecuteItemnRequest : function(expressionId, handlers, request){
			var expressions = loc_getAllExpressionItems();
			var expressionItem;		
			_.each(expressions, function(expression, i){
				if(expression[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]==expressionId){
					expressionItem = expression;
				}
			});
			return node_expressionUtility.getExecuteExpressionItemRequest(expressionItem, loc_valueContext, undefined, loc_complexEntityDef, loc_dataExpressionGroupRuntime.getCoreEntity(), handlers, request)
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
packageObj.createChildNode("createExpressionGroupPlugin", node_createExpressionGroupPlugin); 

})(packageObj);
