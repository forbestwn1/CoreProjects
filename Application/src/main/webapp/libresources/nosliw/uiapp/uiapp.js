//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createPatternMatcher;
	var node_Pattern;
	var node_createEventObject;
	var node_createIODataSet;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_ModuleEventData;
	var node_ModuleInfo;
	var node_ApplicationDataInfo;
	var node_createComponentState;
	var node_appUtility;
	var node_componentUtility;
	var node_createEventSource;
	var node_createEventInfo;
	var node_eventUtility;
	var node_makeObjectWithType;

//*******************************************   Start Node Definition  **************************************

var node_createUIAppComponentCore = function(id, appDef, configure, ioInput){
	var loc_ioInput = ioInput;
	var loc_configure = configure;
	var loc_appDataService = loc_configure.getConfigureValue().__appDataService;
	var loc_ownerConfigure = loc_configure.getConfigureValue().__ownerConfigure;

	var loc_partMatchers = node_createPatternMatcher([
		new node_Pattern(new RegExp("module\.(\\w+)$"), function(result){return loc_out.getCurrentModuleInfo(result[1]).module;}),
		new node_Pattern(new RegExp("module\.(\\w+)\.outputMapping\.(\\w+)$"), function(result){return loc_out.getCurrentModuleInfo(result[1]).outputMapping[result[2]];}),
		new node_Pattern(new RegExp("module\.(\\w+)\.inputMapping\.(\\w+)$"), function(result){	return loc_out.getCurrentModuleInfo(result[1]).inputMapping[result[2]];	}),
	]);
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();

	var loc_componentState;
	
	var loc_initState = function(state){
		loc_componentState = node_createComponentState(state,
			function(handlers, request){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				return out;
			}, 
			function(stateData, handlers, request){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				return out;
			}
		);
	};

	var loc_initContextIODataSet = function(input){
		var data = loc_out.prv_componentData.componentDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_INITSCRIPT](input);
		loc_out.prv_componentData.contextDataSet.setData(undefined, data);
	};

	var loc_getInitIOContextRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		if(loc_ioInput!=undefined){
			out.addRequest(loc_ioInput.getGetDataValueRequest(undefined, {
				success : function(request, data){
					loc_initContextIODataSet(data);
				}
			}));
		}
		else{
			loc_initContextIODataSet();
		}
		return out;
	};

	var loc_getEventSourceInfo = function(){
		return node_createEventSource(node_CONSTANT.TYPEDOBJECT_TYPE_APP, loc_out.getId()); 
	};
	
	var loc_trigueEvent = function(eventName, eventData, requestInfo){
		if(node_componentUtility.isActive(loc_out.prv_componentData.lifecycleStatus)){
			//trigue event only in active status
			node_eventUtility.triggerEventInfo(loc_eventSource, eventName, eventData, loc_getEventSourceInfo(), requestInfo);
		}
	};
	var loc_trigueValueChangeEvent = function(eventName, eventData, requestInfo){
		if(node_componentUtility.isActive(loc_out.prv_componentData.lifecycleStatus)){
			//trigue event only in active status
			loc_valueChangeEventSource.triggerEvent(eventName, eventData, requestInfo); 
		}
	};

	var loc_destroy = function(request){
		
	};

	var loc_init = function(){
		loc_initAppDataOwnerInfo();
	};
	
	var loc_initAppDataOwnerInfo = function(){
		var appDataDefs = loc_out.prv_componentData.componentDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_APPLICATIONDATA];
		_.each(appDataDefs, function(appDataDef, appDataName){
			var info = appDataDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
			var categary = info.categary;
			if(categary==undefined)		categary = loc_ownerConfigure.defaultOwnerType;
			
			var ownerId;
			if(loc_ownerConfigure.ownerIdByType!=undefined)  ownerId = loc_ownerConfigure.ownerIdByType[categary];
			if(ownerId==undefined)  ownerId = loc_ownerConfigure.defaultOwnerId;
			loc_out.prv_componentData.appDataOwnerInfo[appDataName] = nosliw.runtime.getSecurityService().createOwnerInfo(categary, ownerId);
		});
	};
	

	var loc_out = {

		prv_componentData : {
			id : id,
			componentDef : appDef,
			contextDataSet : node_createIODataSet(),
			lifecycleStatus : undefined,  //current lifecycle status
			valueByName : {},
			appDataOwnerInfo : {},   //app data owner info
			
			modulesByRole : {},
			currentModuleByRole : {},
		},
			
		getId : function(){  return loc_out.prv_componentData.id;  },
		
		getContextIODataSet : function(){  return loc_out.prv_componentData.contextDataSet;  },
		
		getProcess : function(name){  return loc_out.prv_componentData.componentDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_PROCESS][name];  },

		getEventHandler : function(moduleName, eventName){
			var moduleDef = loc_out.prv_componentData.componentDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_MODULE][moduleName];
			return moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_EVENTHANDLER][eventName];
		},

		//build module instance info 
		buildModuleInfoRequest : function(moduleName, applicationDataInfo, configureData, handlers, request){
			var moduleDef = loc_out.prv_componentData.componentDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_MODULE][moduleName];
			return node_appUtility.buildModuleInfoRequest(moduleDef, loc_out, applicationDataInfo, configureData, loc_appDataService, loc_componentState.getState(), handlers, request);
		},
		
		addModuleInfo : function(moduleInfo){
			var role = moduleInfo.role;
			var module = moduleInfo.module;
			
			var modules = loc_out.prv_componentData.modulesByRole[role];
			if(modules==undefined){
				modules = [];
				loc_out.prv_componentData.modulesByRole[role] = modules;
			}
			modules.push(moduleInfo);
			loc_out.setCurrentModuleInfo(role, moduleInfo.id);
			
			module.registerEventListener(loc_eventListener, function(eventName, eventData, request){
				loc_trigueEvent(eventName, eventData, request);
			}, moduleInfo);
			module.registerValueChangeEventListener(loc_valueChangeEventListener, function(eventName, eventData, request){
				loc_trigueValueChangeEvent(node_CONSTANT.EVENT_COMPONENT_VALUECHANGE, new node_ModuleEventData(this, eventName, eventData), request);
			}, moduleInfo);
			return moduleInfo;
		},
		
		removeModuleInfo : function(role, moduleId){
			var modules = loc_out.prv_componentData.modulesByRole[role];
			var currentId = loc_out.prv_componentData.currentModuleByRole[role];
			for(var index in modules){
				if(moduleId==modules[index].id){
					break;
				}
			}
			
			if(currentId==moduleId){
				if(index-1>=0)		loc_out.prv_componentData.currentModuleByRole[role] = modules[index-1].id;
				else loc_out.prv_componentData.currentModuleByRole[role] = undefined;
			}
			
			modules.splice(index, 1);
		},

		//every role has only one current module
		getCurrentModuleInfo : function(role){	return loc_out.getModuleInfo(role);	},
		setCurrentModuleInfo : function(role, moduleId){	loc_out.prv_componentData.currentModuleByRole[role] = moduleId;	},
		
		getAllModuleInfo : function(){
			var out = [];
			_.each(loc_out.prv_componentData.modulesByRole, function(modulesByRole, role){
				_.each(modulesByRole, function(moduleInfo){
					out.push(moduleInfo);
				});
			});
			return out;
		},
		
		getModuleInfoByRole : function(role){
			return loc_out.prv_componentData.modulesByRole[role];
		},
		
		getModuleInfo : function(role, id){
			if(id==undefined)	id = loc_out.prv_componentData.currentModuleByRole[role];  //if id not provided, then use id for current component
			var modules = loc_out.prv_componentData.modulesByRole[role];
			for(var i in modules){
				if(modules[i].id==id)  return modules[i];
			}
		},

		getModuleInfoById : function(id){
			var modules = loc_out.prv_componentData.modulesByRole;
			for(var role in modules){
				if (modules.hasOwnProperty(role)) {
					var moduleInfo = loc_out.getModuleInfo(role, id);
					if(moduleInfo!=undefined)  return moduleInfo;
				}
			}
		},

		clearModuleInfo : function(){
			loc_out.prv_componentData.modulesByRole = {};
			loc_out.prv_componentData.currentModuleByRole = {};
		},
		
//component core interface method		
		getInterface : function(){
			return {
				getPart : function(partId){  return loc_out.getPart(partId);	},
	
				getExecutePartCommandRequest : function(partId, commandName, commandData, handlers, requestInfo){
					return this.getPart(partId).getExecuteCommandRequest(commandName, commandData, handlers, requestInfo);
				},
			}
		},

		getValue : function(name){  return loc_out.prv_componentData.valueByName[name];    },
		setValue : function(name, value){   loc_out.prv_componentData.valueByName[name] = value;   },
	
		setState : function(state){  loc_initState(state);	},

		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){  return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener); },
		
		getPart : function(partId){		return loc_partMatchers.match(partId);	},
		
		getLifeCycleRequest : function(transitName, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT){
				//reset context io
				out.addRequest(loc_getInitIOContextRequest());
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE){
				out.addRequest(loc_componentState.getSaveStateDataForRollBackRequest());
				if(loc_out.getValue(node_CONSTANT.COMPONENT_VALUE_BACKUP)==true){
					//active through back up
					out.addRequest(loc_componentState.getRestoreStateRequest());
				}
				else{
					//active through normal way
				}
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE){
				out.addRequest(loc_componentState.getSaveStateDataForRollBackRequest());
				//reset context io
				out.addRequest(loc_getInitIOContextRequest(handlers, request));
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND){
				out.addRequest(loc_componentState.getSaveStateDataForRollBackRequest());
				out.addRequest(loc_componentState.getBackupStateRequest(handlers, request));
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME){
				out.addRequest(loc_componentState.getSaveStateDataForRollBackRequest());
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY){
				out.addRequest(loc_componentState.getSaveStateDataForRollBackRequest());
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){loc_destroy(request);}, handlers, request));
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE_REVERSE){
				out.addRequest(loc_componentState.getRestoreStateDataForRollBackRequest());
			}
			return out;
		},
		setLifeCycleStatus : function(status){		loc_out.prv_componentData.lifecycleStatus = status; },   //lifecycle transit finish
		
		startLifecycleTask : function(){	loc_componentState.initDataForRollBack();	},
		endLifecycleTask : function(){ 	loc_componentState.clearDataFroRollBack();	},
	};
	
	loc_init();
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UIMODULE);
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("common.patternmatcher.createPatternMatcher", function(){node_createPatternMatcher = this.getData();});
nosliw.registerSetNodeDataEvent("common.patternmatcher.Pattern", function(){node_Pattern = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("uiapp.ModuleEventData", function(){node_ModuleEventData = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ModuleInfo", function(){node_ModuleInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ApplicationDataInfo", function(){node_ApplicationDataInfo = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentState", function(){node_createComponentState = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.utility", function(){node_appUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventSource", function(){node_createEventSource = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventInfo", function(){node_createEventInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIAppComponentCore", node_createUIAppComponentCore); 

})(packageObj);
