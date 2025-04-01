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
	var node_createTaskCore;
	var node_getInterface;
	var node_createTaskSetup;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createFlowTaskPlugin = function(){
	
	var loc_out = {

		getCreateEntityCoreRequest : function(entityDef, internalValuePortContainerId, externalValuePortContainerId, bundleCore, configure, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return loc_createFlowTaskCore(entityDef, configure);
			}, handlers, request);
		},
	};

	return loc_out;
};

var loc_createFlowContext = function(){
	
	var loc_value = {};
	
	var loc_out = {

		getValue : function(name){
			return loc_value[name];
		},
		
		setValue : function(name, value){
			loc_value[name] = value;
		}
		
	};

	return loc_out;
};


var loc_createFlowTaskCore = function(entityDef, configure){
	
	var loc_entityDef = entityDef;

	var loc_envInterface = {};
	
	var loc_activities;
	
	var loc_taskResult;

	var loc_taskCore;

	var loc_runtimeEnvForFlowContext;

	var loc_facadeTaskCore = {
		//return a task
		getTaskCore : function(){
			return loc_taskCore;
		},
	};

	var loc_init = function(entityDef, configure){
		loc_taskCore = node_createTaskCore(loc_out, loc_out);

		loc_runtimeEnvForFlowContext = node_createValueContainerSimple(node_COMMONCONSTANT.VALUEADDRESSCATEGARY_FLOWCONTEXT);
		loc_runtimeEnvForFlowContext.setValue(node_CONSTANT.NAME_RUNTIMEENVITEM_FLOWCONTEXT, loc_createFlowContext());
	};

	var loc_createTaskSetupForActivity = function(){
		var out = [];
		
		//runtime env from bundle
		var bundle = node_getEntityObjectInterface(loc_out).getBundle();
		out.push(node_createTaskSetup(undefined, node_getInterface(bundle, node_CONSTANT.INTERFACE_VALUECONTAINERPROVIDER).getValueContainer()));
		
		//runtime env from flow
		out.push(node_createTaskSetup(undefined, loc_taskCore.getRuntimeEnv()));
		
		//setup for flow context
		out.push(node_createTaskSetup(undefined, loc_runtimeEnvForFlowContext));
		
		return out;
	};
	
	var loc_createValueContainerForActivity = function(){
		var out = node_createValueContainerList();
		
		var bundle = node_getEntityObjectInterface(loc_out).getBundle();
		out.addChild(node_getInterface(bundle, node_CONSTANT.INTERFACE_VALUECONTAINERPROVIDER).getValueContainer());

		out.addChild(loc_runtimeEnvForFlowContext);
		
		return out;
	};
	
	var loc_getExecuteTargetRequest = function(target, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var activity = loc_activities.getChildrenEntity()[target[node_COMMONATRIBUTECONSTANT.TASKFLOWTARGET_ACTIVITY]].getCoreEntity();
		out.addRequest(activity.getExecuteActivityRequest(target[node_COMMONATRIBUTECONSTANT.TASKFLOWTARGET_ADAPTER], loc_createValueContainerForActivity(), loc_createTaskSetupForActivity(), {
			success : function(request, target){
				var targetActivityName = target[node_COMMONATRIBUTECONSTANT.TASKFLOWTARGET_ACTIVITY];
				if(targetActivityName.startsWith("#")){
					var i = targetActivityName.indexOf("_");
					var resultName = targetActivityName.substring(i+1);
					var valuePortContainer = node_getEntityObjectInterface(loc_out).getInternalValuePortContainer();
					var withValuePort = loc_envInterface[node_CONSTANT.INTERFACE_WITHVALUEPORT];
					var valueStructures = valuePortContainer.getValueStructuresByGroupTypeAndValuePortName(node_COMMONCONSTANT.VALUEPORTGROUP_TYPE_INTERACTIVETASK, node_interactiveUtility.getResultValuePortNameByResultName(resultName));
					
					for(var vsId in valueStructures){
						return valueStructures[vsId].getAllElementsValuesRequest({
							success : function(request, values){
								return {
								    "resultName": resultName,
								    "resultValue": values
								};
							}
						});
					}
				}
				else{
					return loc_getExecuteTargetRequest(target);
				}
			}
		}));
		return out;
	};

	var loc_out = {

		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getEntityInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_envInterface[node_CONSTANT.INTERFACE_ENTITY].createAttributeRequest(node_COMMONATRIBUTECONSTANT.BLOCKTASKFLOWFLOW_ACTIVITY, undefined, {
				success : function(request, childNode){
					loc_activities = childNode.getChildValue().getCoreEntity();;
				}
			}));
			return out;
		},
		
		getPreInitRequest : function(){
		},
		
		updateView : function(view){
		},

		getTaskExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

			var start = loc_entityDef.getAttributeValue(node_COMMONATRIBUTECONSTANT.BLOCKTASKFLOWFLOW_START);
			var startTarget = start[node_COMMONATRIBUTECONSTANT.TASKFLOWNEXT_TARGET].default;
			out.addRequest(loc_getExecuteTargetRequest(startTarget, {
				success : function(request, taskResult){
					loc_taskResult = taskResult;
				}
			}));
			return out;
		},
		
		getTaskResult : function(){   return loc_taskResult;    }
		
	};
	
	loc_init(entityDef, configure);
	loc_out = node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK, loc_facadeTaskCore);
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
nosliw.registerSetNodeDataEvent("task.createTaskCore", function(){node_createTaskCore = this.getData();});
nosliw.registerSetNodeDataEvent("common.valuecontainer.createValueContainerSimple", function(){node_createValueContainerSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("task.createTaskSetup", function(){node_createTaskSetup = this.getData();});

//Register Node by Name
packageObj.createChildNode("createFlowTaskPlugin", node_createFlowTaskPlugin); 

})(packageObj);
