
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_makeObjectWithType;
	var node_resourceUtility;
	var node_createPackageDebugView;
	var node_createConfigure;
	var node_basicUtility;
	var node_componentUtility;
	var node_namingConvensionUtility;
	var node_getEntityTreeNodeInterface;
	var node_makeObjectWithApplicationInterface;
	var node_getApplicationInterface;
	var node_taskUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

//bundle is executable resource unit
var node_createDynamicCore = function(dynamicDef, bundleCore, configure){

	var loc_dynamicDef;
	var loc_bundleCore;
	
	var loc_dynamicInput;
	var loc_currentTaskEntityCore;

	var loc_configure;
	var loc_configureValue;
	
	var loc_parentView;
	
	var loc_init = function(dynamicDef, bundleCore, configure){
		loc_dynamicDef = dynamicDef;
		loc_bundleCore = bundleCore;
		loc_configure = configure;
		loc_configureValue = node_createConfigure(loc_configure).getConfigureValue();

	};

	var loc_getDynamicInputEntityCore = function(){
		return loc_bundleCore.getDynamicInputContainer().getDyanmicInputEntityCoreRequest(loc_dynamicDef[node_COMMONATRIBUTECONSTANT.VALUEOFDYNAMIC_DYNAMICID]);
	};

	var loc_facadeTaskCore = {
		//return a task
		getTaskCore : function(){
			return node_getApplicationInterface(loc_currentTaskEntityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK).getTaskCore();
		},
	};


	var loc_out = {

		getDynamicInput : function(){
			return loc_bundleCore.getDynamicInputContainer().getDynamicInput(oc_dynamicDef[node_COMMONATRIBUTECONSTANT.VALUEOFDYNAMIC_DYNAMICID]);
		},

		getEntityInitRequest : function(handlers, request){
			return loc_bundleCore.getDynamicInputContainer().prepareDyanmicInputRequest(loc_dynamicDef[node_COMMONATRIBUTECONSTANT.VALUEOFDYNAMIC_DYNAMICID], handlers, request);
		},









		getValuePortContainerProviderEntityCore : function(){    return loc_getDynamicInputEntityCore();      },






		getDataType: function(){    return  "dyanmic";   },

		setDynamicInput : function(dynamicInput){   loc_dynamicInput = dynamicInput;       },

		getExecuteTaskWithAdapterRequest : function(adapterName, taskSetup, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var taskFactory = node_getApplicationInterface(loc_dynamicInput.getDynamicTaskFactoryEntity(), node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_FACTORY);
			out.addRequest(taskFactory.getCreateTaskEntityRequest({
				success : function(request, entityCore){
					loc_currentTaskEntityCore = entityCore;
					var taskCore = node_getApplicationInterface(entityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK).getTaskCore();
					taskCore.addTaskSetup(loc_dynamicInput.getTaskSetup());
					return node_taskUtility.getExecuteEntityTaskWithAdapterRequest(loc_out, adapterName, taskSetup);
				}
			}));
			return out;
		},

		getPreInitRequest : function(handlers, request){   },

		setEnvironmentInterface : function(envInterface){	loc_envInterface = envInterface;	},
		
		updateView : function(view){    
			loc_parentView = view;
		},
		
	};
	
	loc_init(dynamicDef, bundleCore, configure);
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_DYNAMIC);

	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("debug.createPackageDebugView", function(){node_createPackageDebugView = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithApplicationInterface", function(){node_makeObjectWithApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getApplicationInterface", function(){node_getApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("task.taskUtility", function(){node_taskUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createDynamicCore", node_createDynamicCore); 

})(packageObj);
