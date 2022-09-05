//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_makeObjectWithComponentLifecycle;
	var node_makeObjectWithComponentManagementInterface;
	var node_getComponentManagementInterface;
	var node_createComponentCoreComplex;
	var node_componentUtility;
	var node_getComponentLifecycleInterface;
	var node_createServiceRequestInfoSimple;
	var node_createEventObject;
	var node_basicUtility;
	var node_requestServiceProcessor;
	var node_createComponentState;
	
//*******************************************   Start Node Definition  ************************************** 	

//runtime is the one that 
//        expose lifecycle and interface inteface
//        manage backup
//        manage roll back
//parms:
//    componentCore core object
//    decoration
//    runtimeContext : other infor related with runtime obj, rootView, backupState
var node_createComponentRuntime = function(componentCore, decorationInfos, request){
	
	//interface env for access to external env
	var loc_interfaceEnv;
	
	var loc_componentCoreComplex;

	var loc_lifeCycleStatus;
	
	var loc_eventListener = node_createEventObject();

	var loc_init = function(componentCore, decorationInfos, request){
		//build core complex using core and decoration
		loc_componentCoreComplex = node_createComponentCoreComplex(componentCore, decorationInfos);
	};

	var loc_getComponentCore = function(){   return loc_componentCoreComplex.getCore();   };
	var loc_getTaskEnv = function(){   return loc_componentCoreComplex.getInterface();    };

	
	
	
	
	var loc_getContextIODataSet = function(){  return loc_getComponentCore().getContextIODataSet();   };


	//runtime environment provided for component complex
	var loc_runtimeEnv = {
		//process request
		processRequest : function(request){  node_requestServiceProcessor.processRequest(request); },

		getLifecycleStatus : function(){   return loc_lifeCycleStatus;     },
		
		//env interfact to access external env
		getEnvInterface : function(){     },
		
		
		
		getTaskEnv : function(){    return loc_getTaskEnv();    },
		
		getContextIODataSet : function(){   return loc_getContextIODataSet();   }, 
		
		getVariableGroup : function(){}
	};

	var loc_out = {

		getLifecycleEntity : function(){	return loc_componentCoreComplex.getLifecycleEntity();	},
			
		getInitRequest : function(runtimeContext, runtimeInterface, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("InitComponentRuntime", {}), handlers, request);

			out.addRequest(loc_componentCoreComplex.getPreInitRequest({
				success : function(request){
					return loc_componentCoreComplex.getUpdateRuntimeInterfaceRequest(runtimeInterface, {
						success : function(request){
							return loc_componentCoreComplex.getUpdateRuntimeContextRequest(runtimeContext, {
								success : function(request){
									return loc_componentCoreComplex.getPostInitRequest();
								}
							});
						}
					});
				}
			}));

			return out;
		},

		getPreInitRequest : function(handlers, request){	
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("PreInit", {}), handlers, request);
			out.addRequest(loc_componentCoreComplex.getPreInitRequest());
			return out;
		},
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UpdateRuntimeContextInComponentRuntime", {}), handlers, request);
			out.addRequest(loc_componentCoreComplex.getUpdateRuntimeContextRequest(runtimeContext));
			return out;
		},

		getUpdateRuntimeInterfaceRequest : function(runtimeInterface, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UpdateRuntimeInterfaceInComponentRuntime", {}), handlers, request);
			out.addRequest(loc_componentCoreComplex.getUpdateRuntimeInterfaceRequest(runtimeInterface));
			return out;
		},
		
		getPostInitRequest : function(handlers, request){	
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("PostInit", {}), handlers, request);
			out.addRequest(loc_componentCoreComplex.getPostInitRequest());
			return out;
		},
		
		getLifeCycleRequest : function(transitName, handlers, request){
			var task = node_componentUtility.createLifecycleTask(transitName, this);
			return task.getProcessRequest(handlers, request);
		},
		
			

		
		
			
			
			
			
		setInterfaceEnv : function(interfaceEnv){		loc_interfaceEnv = interfaceEnv;	},
			
		
		
		getUpdateSystemDataRequest : function(domain, systemData, handlers, request){
			return loc_getComponentCore().getUpdateSystemDataRequest(domain, systemData, handlers, request);
		},
		
		//component management interface 
		getContextIODataSet :  function(){  return loc_getContextIODataSet();  },
		getExecuteCommandRequest : function(command, parms, handlers, request){
			var out = node_componentUtility.getProcessNosliwCommand(loc_out, command, parms, handlers, request);
			if(out==undefined){
				out = loc_componentCoreComplex.getExecuteCommandRequest(command, parms, handlers, request);    
			}
			return out;
		},
		registerEventListener : function(listener, handler, thisContext){  return loc_componentCoreComplex.registerEventListener(listener, handler, thisContext);   },
		unregisterEventListener : function(listener){   return loc_componentCoreComplex.unregisterEventListener(listener);  },
		registerValueChangeEventListener : function(listener, handler, thisContext){   return loc_componentCoreComplex.registerValueChangeEventListener(listener, handler, thisContext);   },
		unregisterValueChangeEventListener : function(listener){   return loc_componentCoreComplex.unregisterValueChangeEventListener(listener);    }
	};
	
	loc_init(componentCore, decorationInfos, request);
	
//	loc_out = node_makeObjectWithComponentLifecycle(loc_out, loc_lifecycleCallback, loc_lifecycleTaskCallback, loc_out);
//	//listen to lifecycle event and update lifecycle status
//	node_getComponentLifecycleInterface(loc_out).registerEventListener(loc_eventListener, function(eventName, eventData, request){
//		if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION){
//			loc_lifeCycleStatus = eventData.to;
//		}
//	});
	
	loc_out = node_makeObjectWithComponentManagementInterface(loc_out, loc_out, loc_out);

	loc_out.dataType = loc_componentCoreComplex.getCore().getDataType();
	loc_out.id = nosliw.generateId();
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentLifecycle", function(){node_makeObjectWithComponentLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentManagementInterface", function(){node_makeObjectWithComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentCoreComplex", function(){node_createComponentCoreComplex = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentState", function(){node_createComponentState = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentRuntime", node_createComponentRuntime); 

})(packageObj);
