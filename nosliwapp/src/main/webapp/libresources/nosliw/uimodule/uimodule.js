//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoError;
	var node_ServiceInfo;
	var node_createEventObject;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_getLifecycleInterface;
	var node_createModuleUIRequest;
	var node_createUIDecorationsRequest;
	var node_createIODataSet;
	var node_objectOperationUtility;
	var node_uiEventData;
	var node_destroyUtil;
	var node_componentUtility;
	var node_createComponentState;
	var node_createSystemData;
	var node_createEventSource;
	var node_createEventInfo;
	var node_eventUtility;
	var node_basicUtility;
	var node_requestServiceProcessor;
	var node_createDataAssociation;

//*******************************************   Start Node Definition  ************************************** 	
//module entity store all the status information for module
var node_createUIModuleComponentCore = function(id, uiModuleDef, uiDecorationInfos, ioInput){
	//input io used to get input value to initiate module
	var loc_inputIO = ioInput;
	
	var loc_uiDecorationInfos = uiDecorationInfos;
	
	//extra information by domain that provided by system 
	var loc_systemData = node_createSystemData();

	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();
	
	var loc_valueChangeEventListener = node_createEventObject();
	var loc_valueChangeEventSource = node_createEventObject();

	var loc_runtimeEnv;
	
	var loc_getEventSourceInfo = function(){
		return node_createEventSource(node_CONSTANT.TYPEDOBJECT_TYPE_APPMODULE, loc_out.getId());
	};
	
	var loc_getGetStateDataRequest = function(handlers, request){
		out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UI module get state data"), handlers, request);
		
		var stateData = {};
		//get state data for ui data
		var uiDataRequest = node_createServiceRequestInfoSet(undefined, {
			success : function(request, resultSet){
				var uiData = {};
				_.each(resultSet.getResults(), function(uiDataEle, uiId){
					uiData[uiId] = uiDataEle;
				});
				stateData.uiData = uiData;
			}
		});
		_.each(loc_out.getUIs(), function(ui, index){	uiDataRequest.addRequest(ui.getId(), ui.getGetStateRequest());	});
		out.addRequest(uiDataRequest);
		
		//get state data for context data
		out.addRequest(loc_out.prv_componentData.contextDataSet.getGetDataSetValueRequest({
			success : function(request, contextDataSet){
				stateData.context = contextDataSet;
			}
		}));
		
		//return state data
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){return stateData;}));
		return out;
	};
	
	var loc_getRestoreStateDataRequest = function(stateData, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UI module restore state data"), handlers, request);

		//ui data
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			var uiData = stateData.uiData;
			var updateUIStateRequest = node_createServiceRequestInfoSequence(undefined);
			_.each(loc_out.getUIs(), function(ui, index){
				updateUIStateRequest.addRequest(ui.getSetStateRequest(uiData[ui.getId()]));	
			});
			return updateUIStateRequest;
		}));

		//context data
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			var backupContextData = stateData.context;
			var updateContextStateRequest = node_createServiceRequestInfoSequence(undefined);
			_.each(backupContextData, function(contextData, name){
				updateContextStateRequest.addRequest(loc_out.prv_componentData.contextDataSet.getSetDataValueRequest(name, contextData));
			});
			return updateContextStateRequest;
		}));
		return out;
	};
	
	var loc_trigueEvent = function(eventName, eventData, requestInfo){
		if(node_componentUtility.isActive(loc_runtimeEnv.getLifecycleStatus())){
			//trigue event only in active status
			node_eventUtility.triggerEventInfo(loc_eventSource, eventName, eventData, loc_getEventSourceInfo(), requestInfo);
		}
	};
	var loc_trigueValueChangeEvent = function(eventName, eventData, requestInfo){
		if(node_componentUtility.isActive(loc_runtimeEnv.getLifecycleStatus())){
			//trigue event only in active status
			node_eventUtility.triggerEventInfo(loc_valueChangeEventSource, eventName, eventData, loc_getEventSourceInfo(), requestInfo);
		}
	};

	//initiate context data set with input value
	var loc_initContextIODataSet = function(input, request){
		var data = loc_out.prv_componentData.componentDef[node_COMMONATRIBUTECONSTANT.EXECUTABLECOMPONENT_INITSCRIPT](input);
		loc_out.prv_componentData.contextDataSet.setData(undefined, data, request);
	};

	//initiate context data set with inputIO
	var loc_getInitIOContextRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		if(loc_inputIO!=undefined){
			out.addRequest(loc_inputIO.getGetDataValueRequest(undefined, {
				success : function(request, data){
					loc_initContextIODataSet(data, request);
				}
			}));
		}
		else{
			loc_initContextIODataSet(undefined, request);
		}
		return out;
	};
	
	var loc_destroy = function(request){
		node_destroyUtil(loc_out.prv_componentData.contextDataSet, request);
		
		_.each(loc_out.prv_componentData.uiArray, function(ui, i){
			node_destroyUtil(ui, request);
		});
		loc_out.prv_componentData.uiArray = undefined;
		loc_out.prv_componentData.ui = undefined;
		loc_out.prv_componentData.componentDef = undefined;
		loc_out.prv_componentData.lifecycle = undefined;
	};
	
	var loc_init = function(){
		loc_out.prv_componentData.contextDataSet.registerEventListener(loc_valueChangeEventListener, function(eventName, eventData, request){
			loc_trigueValueChangeEvent(node_CONSTANT.EVENT_COMPONENT_VALUECHANGE, undefined, request);
		});
	};

	var loc_getBuildModuleUIRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createModuleUI", undefined), handlers, request);

		//build module ui
		var buildModuleUIsRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("BuildModuleUIs", {}), {
			success : function(request, resultSet){
				_.each(resultSet.getResults(), function(moduleUI, index){
					loc_addUI(moduleUI);
				});
			}
		});

		// build uis
		_.each(loc_out.prv_componentData.componentDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_UI], function(moduleUIDef, index){
			var moduleUIId = moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_ID];
			buildModuleUIRequest = node_createModuleUIRequest(moduleUIDef, loc_out.getContextIODataSet(), node_componentUtility.cloneDecorationInfoArray(loc_uiDecorationInfos));
			buildModuleUIsRequest.addRequest(index, buildModuleUIRequest);
		});
		out.addRequest(buildModuleUIsRequest);
		return out;
	};
	
	var loc_addUI = function(ui){
		loc_out.prv_componentData.uiArray.push(ui);
		loc_out.prv_componentData.ui[ui.getId()] = ui;
		//register listener for module ui
		ui.registerEventListener(loc_eventListener, function(eventName, eventDataInfo, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("moduleUIEventHandler", undefined), undefined, requestInfo);
			
			var eventHandler = ui.getEventHandler(eventName);
			if(eventHandler!=undefined){
				//data association
				var inDataAssociation = eventHandler[node_COMMONATRIBUTECONSTANT.EXECUTABLEHANDLEREVENT_IN];
				out.addRequest(node_createDataAssociation(node_createIODataSet(eventDataInfo.getEventData()), inDataAssociation, loc_out.prv_componentData.contextDataSet).getExecuteRequest());
				
				//task
				var steps = eventHandler[node_COMMONATRIBUTECONSTANT.EXECUTABLEHANDLEREVENT_HANDLER][node_COMMONATRIBUTECONSTANT.EXECUTABLEHANDLER_STEPS];
				_.each(steps, function(step, i){
					out.addRequest(loc_getExecuteTaskByNameRequest(step));
				});
				
				node_requestServiceProcessor.processRequest(out);
			}
			
			loc_trigueEvent(eventName, eventDataInfo, requestInfo);
			
		}, ui);

// kkkk
		ui.registerValueChangeEventListener(loc_valueChangeEventListener, function(eventName, eventData, requestInfo){
			if(eventName==node_CONSTANT.CONTEXT_EVENT_VALUECHANGE){
				//only handle ui value change, not context update
				if(this.getSynOutMode()==node_CONSTANT.CONFIGURE_VALUE_SYNCOUT_AUTO){
					//update value in module
					this.executeSynOutDataRequest(undefined, undefined, requestInfo);
				}
				
				//trigue new event
				loc_trigueEvent(node_basicUtility.buildNosliwFullName(node_CONSTANT.EVENT_UIMODULE_UI_VALUE_CHANGE), eventData, requestInfo);
			}
		}, ui);
	};

	var loc_getTaskNameByLifecycle = function(lifecycleName){ return node_basicUtility.buildNosliwFullName(lifecycleName);	};

	var loc_getExecuteLifecycleTaskRequest = function(lifecycleName, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("loc_getUIModuleNormalLiefCycleCallBackRequestRequest", {}), handlers, request);
		out.addRequest(loc_getExecuteTaskByNameRequest(loc_getTaskNameByLifecycle(lifecycleName)));
		return out;
	};
	
	var loc_getExecuteTaskRequest = function(task, extraInput, handlers, request){
		return nosliw.runtime.getTaskRuntimeFactory().createTaskRuntime(loc_runtimeEnv.getTaskEnv()).getExecuteEmbededTaskRequest(task, loc_runtimeEnv.getContextIODataSet(), handlers, request);
	};

	var loc_getExecuteTaskByNameRequest = function(taskName, extraInput, handlers, request){
		var taskSuite = loc_out.getTaskSuite();
		return nosliw.runtime.getTaskRuntimeFactory().createTaskRuntime(loc_runtimeEnv.getTaskEnv()).getExecuteEmbededTaskInSuiteRequest(taskSuite, taskName, loc_runtimeEnv.getContextIODataSet(), handlers, request);
	};

	
	var loc_out = {
		prv_componentData : {
			id : id,              //
			componentDef : uiModuleDef,   //
			contextDataSet : node_createIODataSet(),   //module context data set
			valueByName : {},
			
			rootView : undefined,
			
			uiArray : [],    //
			ui : {},
		},
		
		getId : function(){  return loc_out.prv_componentData.id;  },
		
		getContextIODataSet : function(){  return loc_out.prv_componentData.contextDataSet;  },
		
		getUIs : function(){  return loc_out.prv_componentData.uiArray;  },
		getUI : function(id) {  return loc_out.prv_componentData.ui[id];   },
		
		getEventHandler : function(uiId, eventName){   return this.getUI(uiId).getEventHandler(eventName);   },
		
		getTaskSuite : function(){   return loc_out.prv_componentData.componentDef[node_COMMONATRIBUTECONSTANT.EXECUTABLECOMPONENT_TASK];  },
		
		getProcess : function(name){  return loc_out.prv_componentData.componentDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_PROCESS][name];  },
		getLifecycleProcess : function(name){  return loc_out.prv_componentData.componentDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_LIFECYCLE][name];  },

		setRootView : function(rootView){   loc_out.prv_componentData.rootView = rootView;     },
		getRootView : function(){   return loc_out.prv_componentData.rootView;     },

		getSystemData : function(domain){  return loc_systemData.getSystemData(domain);   },
		getUpdateSystemDataRequest : function(domain, systemData, handlers, request){
			loc_systemData.updateSystemData(domain, systemData);
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			_.each(loc_out.prv_componentData.uiArray, function(ui, i){
				out.addRequest(ui.getUpdateSystemDataRequest(domain, systemData));
			});
			return out;
		},
		
//component core interface method		
		getInterface : function(){
			return {
				getPart : function(partId){  return loc_out.getPart(partId);	},

				getExecutePartCommandRequest : function(partId, commandName, commandData, handlers, requestInfo){
					return loc_out.getPart(partId).getExecuteCommandRequest(commandName, commandData, handlers, requestInfo);
				},
				
				getElement : function(elementPath){
					var out = loc_out;
					for(var i in elementPath){
						if(out!=undefined)		out = out.getChild(elementPath[i]);
					}
					return out;
				},

			};
		},
		
		//component runtime env
		getRuntimeEnv : function(){   return loc_runtimeEnv;    },
		setRuntimeEnv : function(componentEnv){   loc_runtimeEnv = componentEnv;     },

		getValue : function(name){  return loc_out.prv_componentData.valueByName[name];    },
		setValue : function(name, value){   loc_out.prv_componentData.valueByName[name] = value;   },
		
		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){  return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener); },

		getExecuteCommandRequest : function(commandName, parm, handlers, requestInfo){},
		getPart : function(partId){ 	return node_objectOperationUtility.getObjectAttributeByPath(loc_out.prv_componentData, partId); },

		getLifeCycleRequest : function(transitName, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY){
				out.addRequest(loc_getExecuteLifecycleTaskRequest(transitName));
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){loc_destroy(request);}, handlers, request));
			}
			else{
				if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT){
					//reset context io
					out.addRequest(loc_getBuildModuleUIRequest());
					out.addRequest(loc_getInitIOContextRequest());
				}
				else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE){
					//reset context io
					out.addRequest(loc_getInitIOContextRequest(handlers, request));
				}
				out.addRequest(loc_getExecuteLifecycleTaskRequest(transitName));
			}
			return out;
		},
		
		getChild : function(childId){
			var type = childId[node_COMMONATRIBUTECONSTANT.PATHTOCHILDELEMENT_ELEMENTTYPE];
			if(type==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UI){
				var eleId = childId[node_COMMONATRIBUTECONSTANT.PATHTOCHILDELEMENT_ELEMENTID];
				return loc_out.prv_componentData.ui[eleId];
			}
		},
		
		getGetStateDataRequest : function(handlers, request){  return loc_getGetStateDataRequest(handlers, request);  },
		
		getRestoreStateDataRequest : function(stateData, handlers, request){   return loc_getRestoreStateDataRequest(stateData, handlers, request);   },

		
	};

	loc_init();
	
	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UIMODULE);

	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoError", function(){node_createServiceRequestInfoError = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.createModuleUIRequest", function(){node_createModuleUIRequest = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createUIDecorationsRequest", function(){node_createUIDecorationsRequest = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.uiEventData", function(){node_uiEventData = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.destroyUtil", function(){node_destroyUtil = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentState", function(){node_createComponentState = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.createSystemData", function(){node_createSystemData = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventSource", function(){node_createEventSource = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventInfo", function(){node_createEventInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociation", function(){node_createDataAssociation = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIModuleComponentCore", node_createUIModuleComponentCore); 

})(packageObj);
