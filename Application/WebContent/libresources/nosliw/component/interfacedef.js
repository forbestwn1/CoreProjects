//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createEventObject;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_requestServiceProcessor;
	var node_createConfigure;
	var node_basicUtility;
	var node_createComponentDebugView;

//*******************************************   Start Node Definition  ************************************** 	
	
var node_buildRuntimeObject = function(rawRuntimeObject){
	
	var interfaceDef = {
		getInitRequest : function(handlers, request){	},

		getUpdateViewRequest : function(view, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){return view}, handlers, request);
		},

		
		getUpdateSystemDataRequest : function(domain, systemData, handlers, request){
			return loc_getComponentCore().getUpdateSystemDataRequest(domain, systemData, handlers, request);
		},
		
		//component management interface 
		getContextIODataSet :  function(){  return loc_getContextIODataSet();  },
		getExecuteCommandRequest : function(command, parms, handlers, request){		},

		registerEventListener : function(listener, handler, thisContext){     },
		unregisterEventListener : function(listener){     },
		registerValueChangeEventListener : function(listener, handler, thisContext){      },
		unregisterValueChangeEventListener : function(listener){      }
	};
	
	return _.extend({}, interfaceDef, rawRuntimeObject);
};
	
	
//interface for decoration plug in
var node_buildDecorationPlugInObject = function(rawPluginObj){
	var loc_rawPluginObj = rawPluginObj;
	
	var loc_out = {

		getInterface : function(){   return loc_rawPluginObj.getInterface==undefined?undefined:loc_rawPluginObj.getInterface();  },
		
		processComponentCoreEvent : function(eventName, eventData, request){	return loc_rawPluginObj.processComponentCoreEvent==undefined? undefined:loc_rawPluginObj.processComponentCoreEvent(eventName, eventData, request);	},
		
		processComponentCoreValueChangeEvent : function(eventName, eventData, request){	return loc_rawPluginObj.processComponentCoreValueChangeEvent==undefined? undefined:loc_rawPluginObj.processComponentCoreValueChangeEvent(eventName, eventData, request);},
			
		getProcessCommandRequest : function(command, parms, handlers, request){	return loc_rawPluginObj.getProcessCommandRequest==undefined? undefined:loc_rawPluginObj.getProcessCommandRequest(command, parms, handlers, request);},
		getProcessNosliwCommandRequest : function(command, parms, handlers, request){	return this.getProcessCommandRequest(node_basicUtility.buildNosliwFullName(command), parms, handlers, request);},
		
		getUpdateViewRequest : function(view, handlers, request){
			if(loc_rawPluginObj.getUpdateViewRequest!=undefined){
				return loc_rawPluginObj.getUpdateViewRequest(view, handlers, request);
			}
			else{
				//callback not defined, then return view 
				return node_createServiceRequestInfoSimple(undefined, function(request){
					return view;
				}, handlers, request);
			}
		},
		
		getLifeCycleRequest : function(transitName, handlers, request){	return loc_rawPluginObj.getLifeCycleRequest==undefined?undefined:loc_rawPluginObj.getLifeCycleRequest(transitName, handlers, request);},
		setLifeCycleStatus : function(status){},
		
		//restore data in state to decoration 
		getRestoreStateDataRequest : function(handlers, request){return loc_rawPluginObj.getRestoreStateDataRequest==undefined? undefined:loc_rawPluginObj.getRestoreStateDataRequest(handlers, request);},
	};
	
	return loc_out;
};

//external interface env for current entity
var node_buildInterfaceEnv = function(rawInterfaceEnv){

	var interfaceDef = {
		//all interface 
		getAllInterfaceInfos : function(){},
		
		//
		getInterfaceExecutable : function(interfaceName){  },
	};

	return _.extend({}, interfaceDef, rawInterfaceEnv);
};


//interface for component core 
var node_buildComponentCore = function(rawComponentCore){
	var loc_id;
	var loc_rawComponentCore = rawComponentCore;
	var loc_backupState;
	var loc_lifecycleEntity;
	var loc_configureValue = node_createConfigure(loc_rawComponentCore.getConfigure!=null?loc_rawComponentCore.getConfigure():undefined).getConfigureValue();
	var loc_debugMode = false;
	var loc_debugView;
	
	var loc_init = function(){
		var debugConf = loc_configureValue[node_basicUtility.buildNosliwFullName("debug")];
		if("true"==debugConf){
			//debug mode
			loc_debugMode = true;
		}
	};
	
	var loc_isDebugMode = function(){
		return loc_debugMode == true;
	};
	
	var loc_getDebugView = function(){
		if(loc_debugView==undefined){
			loc_debugView = node_createComponentDebugView("Component: "+loc_out.getDataType()+"_"+loc_out.getId());
		}
		return loc_debugView;
	};
	
	var loc_out = {

		//execute command
		getExecuteCommandRequest : function(commandName, parm, handlers, requestInfo){},
		getExecuteNosliwCommandRequest : function(commandName, parm, handlers, requestInfo){   this.getExecuteCommandRequest(node_basicUtility.buildNosliwFullName(commandName), parm, handlers, requestInfo);    },

		//get part by id
		getPart : function(partId){ },
		
		//value by name
		getValue : function(name){},
		setValue : function(name, value){},
		
		//************************* for debugging
		getDataType: function(){  return loc_rawComponentCore.getDataType!=undefined?loc_rawComponentCore.getDataType():node_CONSTANT.VALUE_UNKNOW;    },
		getId: function(){   return loc_rawComponentCore.getId!=undefined?loc_rawComponentCore.getId() : loc_id;    },
		setId : function(id){  loc_rawComponentCore.setId!=undefined?loc_rawComponentCore.setId(id): loc_id = id;   },
		
		//************************* interface exposed by the core external
		getAllInterfaceInfo : function(){  return loc_rawComponentCore.getAllInterfaceInfo!=undefined?loc_rawComponentCore.getAllInterfaceInfo():[];	},
		getInterfaceExecutable : function(interfaceName){   return loc_rawComponentCore.getInterfaceExecutable!=undefined?loc_rawComponentCore.getInterfaceExecutable(interfaceName):undefined;    },
		
		//*************************state
		getBackupState : function(){   return loc_backupState;    },

		getGetStateDataRequest : function(handlers, request){   },
		getRestoreStateDataRequest : function(stateData, handlers, request){   },

		//*************************event
		registerEventListener : function(listener, handler, thisContext){  },
		unregisterEventListener : function(listener){ },

		registerValueChangeEventListener : function(listener, handler, thisContext){   },
		unregisterValueChangeEventListener : function(listener){ },
		
		//***********************lifecycle
		getPreInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("WrapperPreInitRequestCore", {}), handlers, request);
			if(loc_isDebugMode()){
				loc_getDebugView().logMethodCalled("getPreInitRequest", {
					"configure" : loc_configureValue
				});
			}
			if(loc_rawComponentCore.getPreInitRequest!=undefined)  out.addRequest(loc_rawComponentCore.getPreInitRequest());
			return out;
		},
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("WrapperUpdateRuntimeContextRequestCore", {}), handlers, request);
			if(loc_isDebugMode()){
				loc_getDebugView().logMethodCalled("UpdateRuntimeContextRequest",
						{
							"runtimeContext" : runtimeContext
						});
				$(runtimeContext.view).append(loc_debugView.getView());
				runtimeContext.view = loc_debugView.getWrapperView();
			}
			
			loc_backupState = runtimeContext.backupState;
			
			loc_lifecycleEntity = runtimeContext.lifecycleEntity;
			loc_lifecycleEntity.setComponentCore(this);
			
			if(loc_rawComponentCore.getUpdateRuntimeContextRequest!=undefined)  out.addRequest(loc_rawComponentCore.getUpdateRuntimeContextRequest(runtimeContext));
			else out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){return runtimeContext}));
			return out;
		},
		
		getUpdateRuntimeInterfaceRequest : function(runtimeInteface, handlers, request){   
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UpdateRuntimeInterfaceRequestCore", {}), handlers, request);
			if(loc_isDebugMode()){
				loc_getDebugView().logMethodCalled("getUpdateRuntimeInterfaceRequest");
			}
			if(loc_rawComponentCore.getUpdateRuntimeInterfaceRequest!=undefined)  out.addRequest(loc_rawComponentCore.getUpdateRuntimeInterfaceRequest());
			return out;
		},
		

		getPostInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("WrapperPostInitRequestCore", {}), handlers, request);
			if(loc_isDebugMode()){
				loc_getDebugView().logMethodCalled("getPostInitRequest");
			}
			if(loc_rawComponentCore.getPostInitRequest!=undefined)  out.addRequest(loc_rawComponentCore.getPostInitRequest());
			return out;
		},
		
		
		getLifeCycleRequest : function(transitName, handlers, request){
			if(loc_isDebugMode()){
				loc_getDebugView().logMethodCalled("getLifeCycleRequest", {
					"transitName" : transitName
				});
			}
			
			if(loc_rawComponentCore.getLifeCycleRequest!=undefined)		return loc_rawComponentCore.getLifeCycleRequest(transitName, handlers, request);
			else return node_createServiceRequestInfoSequence(undefined, handlers, request);
		},

		getLifecycleEntity : function(){   return loc_lifecycleEntity;    },
		
		
		
		setLifeCycleStatus : function(status){},
		
		startLifecycleTask : function(){},
		endLifecycleTask : function(){},
		
	};
	loc_init();
	
	return loc_out;
};

//interface for component external env
var node_buildComponentEnv = function(rawComponentEnv){
	var interfaceDef = {
		//process request
		processRequest : function(request){   },
		//execute process
		getExecuteTaskRequest : function(task, extraInput, handlers, request){    },
		//execute process
		getExecuteTaskResourceRequest : function(taskId, input, handlers, request){    },
	};
	return _.extend({}, interfaceDef, rawComponentEnv);
};

//interface for component management interface delegate object
var node_createComponentManagementInterfaceDelegateObject = function(delegateObj){
	var loc_interface = {
		getContextIODataSet :  function(){    },
		getExecuteCommandRequest : function(command, parms, handlers, request){      },
		getExecuteNosliwCommandRequest : function(commandName, parm, handlers, requestInfo){   this.getExecuteCommandRequest(node_basicUtility.buildNosliwFullName(commandName), parm, handlers, requestInfo);    },
		registerEventListener : function(listener, handler, thisContext){     },
		unregisterEventListener : function(listener){     },
		registerValueChangeEventListener : function(listener, handler, thisContext){    },
		unregisterValueChangeEventListener : function(listener){       }
	};
	return _.extend({}, loc_interface, delegateObj);
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.debug.createComponentDebugView", function(){node_createComponentDebugView = this.getData();});

//Register Node by Name
packageObj.createChildNode("buildDecorationPlugInObject", node_buildDecorationPlugInObject); 
packageObj.createChildNode("buildComponentCore", node_buildComponentCore); 
packageObj.createChildNode("buildComponentEnv", node_buildComponentEnv); 
packageObj.createChildNode("createComponentManagementInterfaceDelegateObject", node_createComponentManagementInterfaceDelegateObject); 

})(packageObj);
