//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildComponentInterface;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_componentUtility;
	var node_getEntityObjectInterface;
	var node_utilityNamedVariable;
	var node_makeObjectWithApplicationInterface;
	var node_interactiveUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createDynamicActivityPlugin = function(){
	
	var loc_out = {

		getCreateEntityCoreRequest : function(entityDef, internalValuePortContainerId, externalValuePortContainerId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createDynamicActivityCore(entityDef, configure);
			}, handlers, request);
		},
	};

	return loc_out;
};


var loc_createDynamicActivityCore = function(entityDef, configure){
	
	var loc_entityDef = entityDef;
	var loc_next = loc_entityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTASKFLOWACTIVITY_NEXT);
	var loc_decision = loc_next[node_COMMONATRIBUTECONSTANT.TASKFLOWNEXT_DECISION];
	var loc_target = loc_next[node_COMMONATRIBUTECONSTANT.TASKFLOWNEXT_TARGET];

	var loc_dynamicTask = loc_entityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTASKFLOWACTIVITYDYNAMIC_TASK);
	var loc_taskAddress = loc_entityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTASKFLOWACTIVITYDYNAMIC_TASKADDRESS);

	var loc_envInterface = {};
	
	var loc_dynamic;

	var loc_getDynamicTaskInputRequest = function(valueContainer, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(valueContainer.getGetValueRequest(loc_taskAddress[node_COMMONATRIBUTECONSTANT.ADDRESSVALUE_CATEGARY], loc_taskAddress[node_COMMONATRIBUTECONSTANT.ADDRESSVALUE_ID]));
		
//		out.addRequest(node_getEntityObjectInterface(loc_out).getBundle().getDynamicInputContainer().getDyanmicTaskInputRequest("default"));
		return out;
	};

	var loc_out = {

		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createAttributeRequest(node_COMMONATRIBUTECONSTANT.BLOCKTASKFLOWACTIVITYDYNAMIC_TASK, undefined, {
				success : function(request, childNode){
					loc_dynamic = childNode.getChildValue().getCoreEntity();
				}
			}));
			return out;
		},
		
		getExecuteActivityRequest : function(adapterName, valueContainer, taskSetup, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			out.addRequest(loc_getDynamicTaskInputRequest(valueContainer, {
				success : function(request, dynamicTaskInput){
					loc_dynamic.setDynamickInput(dynamicTaskInput);
					
					return loc_dynamic.getExecuteTaskWithAdapterRequest(adapterName, taskSetup, {
						success : function(request, taskResult){
							var decsionOutput = loc_decision[node_COMMONATRIBUTECONSTANT.TASKFLOWDECISIONJS_SCRIPT](taskResult);
							return loc_target[decsionOutput]; 
						}
					});
				}
			}));
			
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.utilityNamedVariable", function(){node_utilityNamedVariable = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithApplicationInterface", function(){node_makeObjectWithApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("task.interactiveUtility", function(){node_interactiveUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createDynamicActivityPlugin", node_createDynamicActivityPlugin); 

})(packageObj);
