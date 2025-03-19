
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
	
//*******************************************   Start Node Definition  ************************************** 	

//bundle is executable resource unit
var node_createDynamicCore = function(dynamicDef, configure){

	var loc_dynamicDef;
	
	var loc_taskFactory;
	var loc_currentTask;


	var loc_configure;
	var loc_configureValue;
	
	//variable domain for this bundle
	var loc_variableDomain;

	var loc_envInterface;
	
	var loc_runtimeEnv;

	var loc_parentView;
	
	var loc_init = function(dynamicDef, configure){
		loc_configure = configure;
		loc_configureValue = node_createConfigure(loc_configure).getConfigureValue();

	};
	

	var loc_out = {

		getDataType: function(){    return  "dyanmic";   },

		setTaskFactory : function(taskFactory){   loc_taskFactory = taskFactory;       },

		getCurrentTask : function(){   return loc_currentTask;   },
		
		

		getMainEntityNode : function(){		return loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].getChild(loc_MAIN_NAME);	},

		setEnvironmentInterface : function(envInterface){	loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(handlers, request){   return loc_getPreInitRequest(handlers, request);	},

		updateView : function(view){    
			loc_parentView = view;
			loc_getMainEntity().updateView(view);     
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
	return loc_out;
};

node_getDynamicTaskInputRequest = function(dynamicTaskInputDef, bundleCore, handlers, request){
	var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

	out.addRequest(node_createServiceRequestInfoSequence(undefined, function(request){
		var allTask = {};
		_.each(dynamicTaskInputDef[node_COMMONATRIBUTECONSTANT.INPUTDYNAMICTASK_DYNAMICTASK], function(dynamicTask, name){
			var handlerEntityCore = node_complexEntityUtility.getBrickCoreByRelativePath(bundleCore, dynamicTask[node_COMMONATRIBUTECONSTANT.DYNAMICTASK_TASKID][node_COMMONATRIBUTECONSTANT.IDBRICKINBUNDLE_RELATIVEPATH]);
			allTask[name] = handlerEntityCore;
		});
		
	}));
	return out;	
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

//Register Node by Name
packageObj.createChildNode("createDynamicCore", node_createDynamicCore); 

})(packageObj);
