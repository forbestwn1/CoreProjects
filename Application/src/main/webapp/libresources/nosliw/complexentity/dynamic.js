
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
	var nod_createVariableDomain;
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
var node_createDynamicCore = function(dynamicDef, configure){

	var loc_dynamicDef;
	
	var loc_dynamicTaskInput;
	var loc_currentTaskEntityCore;

	var loc_configure;
	var loc_configureValue;
	
	
	
	
	
	//variable domain for this bundle
	var loc_variableDomain;

	var loc_envInterface;
	
	var loc_runtimeEnv;

	var loc_parentView;
	
	var loc_init = function(dynamicDef, configure){
		loc_dynamicDef = dynamicDef;
		loc_configure = configure;
		loc_configureValue = node_createConfigure(loc_configure).getConfigureValue();

	};

	var loc_facadeTaskCore = {
		//return a task
		getTaskCore : function(){
			return node_getApplicationInterface(loc_currentTaskEntityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK).getTaskCore();
		},
	};


	var loc_out = {

		getDataType: function(){    return  "dyanmic";   },

		setDynamicTaskInput : function(dynamicTaskInput){   loc_dynamicTaskInput = dynamicTaskInput;       },

		getCurrentTaskEntityCore : function(){    return loc_currentTaskEntityCore;      },

		getExecuteTaskWithAdapterRequest : function(adapterName, taskSetup, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var taskFactory = node_getApplicationInterface(loc_dynamicTaskInput.getDynamicTaskFactoryEntity(), node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASKFACTORY);
			out.addRequest(taskFactory.getCreateTaskEntityRequest({
				success : function(request, entityCore){
					loc_currentTaskEntityCore = entityCore;
					var taskCore = node_getApplicationInterface(entityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK).getTaskCore();
					taskCore.addSetup(loc_dynamicTaskInput.getTaskSetup());
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
		



		

		getBundleDefinition : function(){		return loc_bundleDef;	},

		getVariableDomain : function(){		return loc_variableDomain;	},

		getUpdateRuntimeEnvRequest : function(runtimeEnv, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				loc_runtimeEnv = runtimeEnv;
			}, handlers, request);
		},
		
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_DYNAMIC);

	loc_init(dynamicDef, configure);
	
	return node_makeObjectWithApplicationInterface(loc_out, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK, loc_facadeTaskCore);
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.createVariableDomain", function(){nod_createVariableDomain = this.getData();});
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
