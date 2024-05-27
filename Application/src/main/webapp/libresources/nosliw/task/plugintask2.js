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
	var node_getEntityTreeNodeInterface;
	var node_getApplicationInterface;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createTaskPlugin = function(){
	
	var loc_out = {

		getCreateComplexEntityCoreRequest : function(complexEntityDef, valueContextId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createTaskComponentCore(complexEntityDef, valueContextId, bundleCore, configure);
			}, handlers, request);
		},
	};

	return loc_out;
};

var loc_createTaskComponentCore = function(complexEntityDef, valueContextId, bundleCore, configure){

	var loc_complexEntityDef = complexEntityDef;
	var loc_valueContextId = valueContextId;
	var loc_bundleCore = bundleCore;
	var loc_valueContext = loc_bundleCore.getVariableDomain().getValueContext(loc_valueContextId);
	var loc_envInterface;
	
	var loc_facadeTask = {
		getItemVariableInfos : function(){
		},

		getRequirement : function(){
			
		},

		getExecuteRequest : function(extraInfo, handlers, request){
			return loc_taskUtility.getTaskAttributeExecuteRequest(loc_out, node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYTASK_IMPLEMENTATION, extraInfo, handlers, request);
		},
	};
	
	var loc_facadeTaskContainer = {
		getAllItemIds : function(){
		},
		
		getItemVariableInfos : function(itemId){
		},
		
		getItemRequirement : function(itemId){
			
		},
		
		getExecuteItemRequest : function(itemId, extraInfo, handlers, request){
		},
	};
	
	var loc_out = {
		
		getEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createAttributeRequest(node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYTASK_IMPLEMENTATION, undefined));
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				
			}));
			return out;
		},
		
		setEnvironmentInterface : function(envInterface){
			loc_envInterface = envInterface;
		},
	};
	
	return node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK, loc_facadeTask);
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
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getApplicationInterface", function(){node_getApplicationInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("createTaskPlugin1", node_createTaskPlugin); 

})(packageObj);
