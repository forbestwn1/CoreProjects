//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createPatternMatcher;
	var node_Pattern;
	var node_createEventObject;
	var node_createIODataSet;
	var node_createServiceRequestInfoSequence;
	var node_ModuleEventData;
	var node_ModuleInfo;
	var node_ApplicationDataInfo;
	var node_createComponentState;
	
//*******************************************   Start Node Definition  **************************************

var node_createUIAppComponentCore = function(id, appDef, ioInput){
	var loc_ioInput = ioInput;
	
	var loc_partMatchers = node_createPatternMatcher([
		new node_Pattern(new RegExp("module\.(\\w+)$"), function(result){return loc_out.getCurrentModuleInfo(result[1]).module;}),
		new node_Pattern(new RegExp("module\.(\\w+)\.outputMapping\.(\\w+)$"), function(result){return loc_out.getCurrentModuleInfo(result[1]).outputMapping[result[2]];}),
		new node_Pattern(new RegExp("module\.(\\w+)\.inputMapping\.(\\w+)$"), function(result){	return loc_out.getCurrentModuleInfo(result[1]).inputMapping[result[2]];	}),
	]);
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();

	var loc_componentState = node_createComponentState(undefined); 

	var loc_moduleEventProcessor = function(eventName, eventData, request){
		loc_trigueEvent(node_CONSTANT.APP_EVENT_MODULEEVENT, new node_ModuleEventData(this, eventName, eventData), request);
	};

	var loc_moduleValueChangeEventProcessor = function(eventName, eventData, request){
		loc_trigueValueChangeEvent(node_CONSTANT.EVENT_COMPONENT_VALUECHANGE, new node_ModuleEventData(this, eventName, eventData), request);
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
	

	var loc_trigueEvent = function(eventName, eventData, requestInfo){
		if(node_componentUtility.isActive(loc_out.prv_componentData.lifecycleStatus)){
			//trigue event only in active status
			loc_eventSource.triggerEvent(eventName, eventData, requestInfo); 
		}
	};
	var loc_trigueValueChangeEvent = function(eventName, eventData, requestInfo){
		if(node_componentUtility.isActive(loc_out.prv_componentData.lifecycleStatus)){
			//trigue event only in active status
			loc_valueChangeEventSource.triggerEvent(eventName, eventData, requestInfo); 
		}
	};

	var loc_out = {

		prv_componentData : {
			id : id,
			componentDef : appDef,
			contextDataSet : node_createIODataSet(),
			lifecycleStatus : undefined,  //current lifecycle status
			valueByName : {},
			
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
			
			module.prv_registerEventListener(loc_eventListener, loc_moduleEventProcessor, moduleInfo);
			module.prv_registerValueChangeEventListener(loc_valueChangeEventListener, loc_moduleValueChangeEventProcessor, moduleInfo);
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
		
		getModuleInfo : function(role, id){
			if(id==undefined){
				id = loc_out.prv_componentData.currentModuleByRole[role];
			}
			var modules = loc_out.prv_componentData.modulesByRole[role];
			for(var i in modules){
				if(modules[i].id==id)  return modules[i];
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
	
		setState : function(state){  loc_componentState.setState(state);	},

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
		setLifeCycleStatus : function(status){
			//lifecycle transit finish
			loc_out.prv_componentData.lifecycleStatus = status;
		},
		
		startLifecycleTask : function(){	loc_componentState.initDataForRollBack();	},
		endLifecycleTask : function(){ 	loc_componentState.clearDataFroRollBack();	},
	};
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("common.patternmatcher.createPatternMatcher", function(){node_createPatternMatcher = this.getData();});
nosliw.registerSetNodeDataEvent("common.patternmatcher.Pattern", function(){node_Pattern = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("uiapp.ModuleEventData", function(){node_ModuleEventData = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ModuleInfo", function(){node_ModuleInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ApplicationDataInfo", function(){node_ApplicationDataInfo = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentState", function(){node_createComponentState = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIAppComponentCore", node_createUIAppComponentCore); 

})(packageObj);
