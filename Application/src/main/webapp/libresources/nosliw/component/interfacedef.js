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
	var node_buildInterface;
	var node_getInterface;
	var node_componentUtility;
	var node_getBasicEntityObjectInterface;
	var node_getEntityTreeNodeInterface;

//*******************************************   Start Node Definition  ************************************** 	

var node_makeObjectWithApplicationInterface = function(baseObject, facadeName, interface){
	var interfaces = node_getInterface(baseObject, node_CONSTANT.INTERFACE_APPLICATIONENTITY);
	if(interfaces==undefined){
		interfaces = {};
	}
	interfaces[facadeName] = interface;
	return node_buildInterface(baseObject, node_CONSTANT.INTERFACE_APPLICATIONENTITY, interfaces);
};

var node_getApplicationInterface = function(baseObject, facadeName){
	var interfaces = node_getInterface(baseObject, node_CONSTANT.INTERFACE_APPLICATIONENTITY);
	if(interfaces==undefined)   return interfaces;
	else return interfaces[facadeName];
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

//interface for component core 
var node_makeObjectWithComponentInterface = function(entityType, rawEntity, debugMode){
	var loc_entityType = entityType;
	var loc_rawComponentCore = rawEntity;
	var loc_backupState;
	var loc_lifecycleEntity;
	
	var loc_configureValue = node_createConfigure(node_getBasicEntityObjectInterface(loc_rawComponentCore).getConfigure()).getConfigureValue();
//	var loc_configureValue = node_createConfigure(loc_rawComponentCore.getConfigure!=null?loc_rawComponentCore.getConfigure():undefined).getConfigureValue();
	
//	var loc_configureValue = node_createConfigure(loc_rawComponentCore.getConfigure!=null?loc_rawComponentCore.getConfigure():undefined).getConfigureValue();
	var loc_debugMode = false;
	var loc_debugView;
	
	var loc_init = function(rawComponentCore, debugMode){
		if(debugMode!=undefined)  loc_debugMode = debugMode;
		else{
			//get debug mode from configure
			var debugConf = loc_configureValue[node_basicUtility.buildNosliwFullName("debug")];
			if("true"==debugConf){
				//debug mode
				loc_debugMode = true;
			}
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
	
	var loc_interfaceEntity = {

		//execute command
		getExecuteCommandRequest : function(commandName, parm, handlers, requestInfo){},
		getExecuteNosliwCommandRequest : function(commandName, parm, handlers, requestInfo){   this.getExecuteCommandRequest(node_basicUtility.buildNosliwFullName(commandName), parm, handlers, requestInfo);    },

		//get part by id
		getPart : function(partId){ },
		
		//value by name
		getValue : function(name){},
		setValue : function(name, value){},
		
		getRawEntity : function(){   return loc_rawComponentCore.getRawEntity==undefined?loc_rawComponentCore:loc_rawComponentCore.getRawEntity();    },
		
		//************************* for debugging
		getDataType: function(){  return loc_entityType;    },
		
		//************************* interface exposed by the core external
		getAllInterfaceInfo : function(){  return loc_rawComponentCore.getAllInterfaceInfo!=undefined?loc_rawComponentCore.getAllInterfaceInfo():[];	},
		getInterfaceExecutable : function(interfaceName){   return loc_rawComponentCore.getInterfaceExecutable!=undefined?loc_rawComponentCore.getInterfaceExecutable(interfaceName):undefined;    },
		
		//*************************state
		getBackupState : function(){   return loc_backupState;    },

		getGetStateDataRequest : function(handlers, request){ 
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("WrapperGetStateData", {}), handlers, request);

			var stateData;
			if(loc_rawComponentCore.getGetStateDataRequest!=undefined){
				out.addRequest(loc_rawComponentCore.getGetStateDataRequest({
					success : function(request, data){
						stateData = data;
					}
				}));
			}

			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				if(loc_isDebugMode()){
					loc_getDebugView().logMethodCalled(node_CONSTANT.COMPONENT_INTERFACE_GETSTATE, {
						"stateData" : stateData
					});
				}
				return stateData;
			}));
			
			return out;
		},
		getRestoreStateDataRequest : function(stateData, handlers, request){ 
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("WrapperGetRestoreStateData", {}), handlers, request);

			if(loc_rawComponentCore.getRestoreStateDataRequest!=undefined){
				out.addRequest(loc_rawComponentCore.getRestoreStateDataRequest(stateData));
			}
			
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				if(loc_isDebugMode()){
					loc_getDebugView().logMethodCalled(node_CONSTANT.COMPONENT_INTERFACE_RESTORESTATE, {
						"stateData" : stateData
					});
				}
			}));
			
			return out;
		},

		//*************************event
		registerEventListener : function(listener, handler, thisContext){  },
		unregisterEventListener : function(listener){ },

		registerValueChangeEventListener : function(listener, handler, thisContext){   },
		unregisterValueChangeEventListener : function(listener){ },
		
		//***********************component interface

		getPreInitRequest : function(handlers, request){
			return node_componentUtility.getInterfaceCallRequest(loc_rawComponentCore, node_getComponentInterface, "getPreInitRequest", [], handlers, request);
		},
		
		getPostInitRequest : function(handlers, request){
			return node_componentUtility.getInterfaceCallRequest(loc_rawComponentCore, node_getComponentInterface, "getPostInitRequest", [], handlers, request);
		},
		
		getLifeCycleRequest : function(transitName, handlers, request){
			if(loc_isDebugMode()){
				loc_getDebugView().logMethodCalled(node_CONSTANT.COMPONENT_INTERFACE_LIFECYCLE, {
					"transitName" : transitName
				});
			}
			
			if(loc_rawComponentCore.getLifeCycleRequest!=undefined)		return loc_rawComponentCore.getLifeCycleRequest(transitName, handlers, request);
			else return node_createServiceRequestInfoSequence(undefined, handlers, request);
		},

		getLifecycleEntity : function(){   return loc_lifecycleEntity;    },
		
		updateBackupStateObject : function(backupStateObj){
			loc_backupState = backupStateObj;
			
			var treeNodeInterface = node_getEntityTreeNodeInterface(loc_rawComponentCore);
			if(treeNodeInterface!=undefined){
				var childrenName = treeNodeInterface.getChildrenName();
				_.each(childrenName, function(childName, i){
					var childValue = treeNodeInterface.getChild(childName).getChildValue();
					var childBackupStateObj = backupStateObj.createChildState(childName);
					if(node_getObjectType(childValue)==node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTRUNTIME){
						//for child is runtime					
						childValue.updateBackupStateObject(childBackupStateObj);
					}
					else{
						node_getComponentInterface(childValue).updateBackupStateObject(childBackupStateObj);
					}
				})
			}
		},
		
		updateLifecycleEntityObject : function(lifecycleEntityObj){
			loc_lifecycleEntity = lifecycleEntityObj;
			loc_lifecycleEntity.setComponentCore(this);
			
			var treeNodeInterface = node_getEntityTreeNodeInterface(loc_rawComponentCore);
			if(treeNodeInterface!=undefined){
				var childrenName = treeNodeInterface.getChildrenName();
				_.each(childrenName, function(childName, i){
					var childValue = treeNodeInterface.getChild(childName).getChildValue();
					var childLifecycleEntity = lifecycleEntityObj.createChild(childrenName);
					if(node_getObjectType(childValue)==node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTRUNTIME){
						//for child is runtime					
						childValue.updateLifecycleEntityObject(childLifecycleEntity);
					}
					else{
						node_getComponentInterface(childValue).updateLifecycleEntityObject(childLifecycleEntity);
					}
				})
			}
		},
		
		updateView : function(view){
			if(loc_rawComponentCore.updateView!=undefined){
				return loc_rawComponentCore.updateView(view);
			}
			else  return view;
		},

		
		
		
		
		setLifeCycleStatus : function(status){},
		
		startLifecycleTask : function(){},
		endLifecycleTask : function(){},
		
	};
	loc_init(rawEntity, debugMode);
	
	return node_buildInterface(rawEntity, node_CONSTANT.INTERFACE_COMPONENTENTITY, loc_interfaceEntity);
};

var node_getComponentInterface = function(baseObject){
	var out = node_getInterface(baseObject, node_CONSTANT.INTERFACE_COMPONENTENTITY);
	if(out!=undefined)   return out;
	return baseObject;
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
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.getBasicEntityObjectInterface", function(){node_getBasicEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});


//Register Node by Name
packageObj.createChildNode("buildDecorationPlugInObject", node_buildDecorationPlugInObject); 
packageObj.createChildNode("buildComponentEnv", node_buildComponentEnv); 
packageObj.createChildNode("makeObjectWithComponentInterface", node_makeObjectWithComponentInterface); 
packageObj.createChildNode("getComponentInterface", node_getComponentInterface); 

packageObj.createChildNode("makeObjectWithApplicationInterface", node_makeObjectWithApplicationInterface); 
packageObj.createChildNode("getApplicationInterface", node_getApplicationInterface); 

})(packageObj);
