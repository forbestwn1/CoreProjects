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
	var node_makeObjectWithApplicationInterface;
	var node_createServiceRequestInfoSet;
    var node_createTaskContainerInterface;
 	var node_createTaskInterface;
 	var node_createValuePortValueContext;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createScriptExpressionGroupPlugin = function(){
	
	var loc_out = {
		getCreateEntityCoreRequest : function(complexEntityDef, valueContextId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createScriptExpressionGroupComponentCore(complexEntityDef, valueContextId, bundleCore, configure);
			}, handlers, request);
		},
	};

	return loc_out;
};

var loc_createScriptExpressionGroupComponentCore = function(complexEntityDef, valueContextId, bundleCore, configure){

	var loc_complexEntityDef = complexEntityDef;
	var loc_valueContextId = valueContextId;
	var loc_bundleCore = bundleCore;
	var loc_valueContext = loc_bundleCore.getVariableDomain().getValueContext(loc_valueContextId);
	var loc_envInterface = {};
	var loc_dataExpressionGroupRuntime;
	
	var loc_expressionGroup = loc_complexEntityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKSCRIPTEXPRESSIONGROUP_VALUE);

	var loc_valuePort = node_createValuePortValueContext(loc_valueContextId, loc_bundleCore.getVariableDomain());
	
	var loc_getAllExpressionItems = function(){
		return loc_expressionGroup[node_COMMONATRIBUTECONSTANT.CONTAINER_ITEM];
	};
	
	var loc_getItemById = function(itemId){
		var expressions = loc_getAllExpressionItems();
		var out;		
		_.each(expressions, function(expression, i){
			if(expression[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]==itemId){
				out = expression;
			}
		});
		return out;
	}
	
	var loc_getExecuteItemRequest = function(expressionId, handlers, request){
		var expressionItem = loc_getItemById(expressionId);
		var withValuePortInterface = {
			getValuePort : function(valuePortGroup, valuePortName){
				return loc_valuePort;
			}
		};
		
		return node_expressionUtility.getExecuteScriptExpressionRequest(expressionItem[node_COMMONATRIBUTECONSTANT.ITEMINCONTAINERSCRIPTEXPRESSION_SCRIPTEXPRESSION], withValuePortInterface, handlers, request);
	};
	

	var loc_facadeTaskContainer = node_createTaskContainerInterface({
		getAllItemIds : function(){
			var out = [];
			var expressions = loc_getAllExpressionItems()
			_.each(expressions, function(expression, i){
				out.push(expression[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]);
			});
			return out;
		},
		
		getExecuteItemRequest : function(expressionId, handlers, request){
			return loc_getExecuteItemRequest(expressionId, handlers, request);
		},
		
	});
	
	
	
	
	
	
	
	
	var loc_out = {
		
		getEntityInitRequest1 : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createAttributeRequest(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEX_DATAEEXPRESSIONGROUP, undefined, {
				success : function(request, childNode){
					loc_dataExpressionGroupRuntime = childNode.getChildValue();
				}
			}));
			return out;
		},
		
		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},
	};
	
	loc_out = node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASKCONTAINER, loc_facadeTaskContainer);
//	loc_out = node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK, loc_facadeTask);
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
nosliw.registerSetNodeDataEvent("component.makeObjectWithApplicationInterface", function(){node_makeObjectWithApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("task.createTaskContainerInterface", function(){	node_createTaskContainerInterface = this.getData();	});
nosliw.registerSetNodeDataEvent("task.createTaskInterface", function(){	node_createTaskInterface = this.getData();	});
nosliw.registerSetNodeDataEvent("valueport.createValuePortValueContext", function(){	node_createValuePortValueContext = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createScriptExpressionGroupPlugin", node_createScriptExpressionGroupPlugin); 

})(packageObj);
