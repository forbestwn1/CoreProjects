//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
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

//*******************************************   Start Node Definition  ************************************** 	
//module entity store all the status information for module
var node_createUIModuleComponentCore = function(id, uiModuleDef, uiDecorationConfigure, ioInput){
	//input io used to get input value to initiate module
	var loc_inputIO = ioInput;
	
	var loc_uiDecorationConfigure = uiDecorationConfigure;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();
	
	var loc_valueChangeEventListener = node_createEventObject();
	var loc_valueChangeEventSource = node_createEventObject();

	var loc_componentState = node_createComponentState(undefined, 
		function(handlers, request){
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
			}, handlers, request);
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
		}, 
		function(stateData, handlers, request){
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
		});

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

	//initiate context data set with input value
	var loc_initContextIODataSet = function(input){
		var data = loc_out.prv_componentData.componentDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_INITSCRIPT](input);
		loc_out.prv_componentData.contextDataSet.setData(undefined, data);
	};

	//initiate context data set with inputIO
	var loc_getInitIOContextRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		if(loc_inputIO!=undefined){
			out.addRequest(loc_inputIO.getGetDataValueRequest(undefined, {
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
			buildModuleUIRequest = node_createModuleUIRequest(moduleUIDef, loc_out.getContextIODataSet(), loc_uiDecorationConfigure);
			buildModuleUIsRequest.addRequest(index, buildModuleUIRequest);
		});
		out.addRequest(buildModuleUIsRequest);
		return out;
	};
	
	var loc_addUI = function(ui){
		loc_out.prv_componentData.uiArray.push(ui);
		loc_out.prv_componentData.ui[ui.getId()] = ui;
		//register listener for module ui
		ui.registerEventListener(loc_eventListener, function(eventName, eventData, requestInfo){
			loc_trigueEvent(node_CONSTANT.MODULE_EVENT_UIEVENT, new node_uiEventData(this.getId(), eventName, eventData), requestInfo);
		}, ui);
		ui.registerValueChangeEventListener(loc_valueChangeEventListener, function(eventName, eventData, requestInfo){
			//handle ui value change, update value in module
			this.executeSynOutDataRequest(undefined, undefined, requestInfo);
		}, ui);
	};

	var loc_out = {
		prv_componentData : {
			id : id,              //
			componentDef : uiModuleDef,   //
			contextDataSet : node_createIODataSet(),   //module context data set
			lifecycleStatus : undefined,  //current lifecycle status
			valueByName : {},
			stateDataForRollBack : [],
			
			rootView : undefined,
			
			uiArray : [],    //
			ui : {},
		},
		
		getId : function(){  return loc_out.prv_componentData.id;  },
		
		getContextIODataSet : function(){  return loc_out.prv_componentData.contextDataSet;  },
		
		getUIs : function(){  return loc_out.prv_componentData.uiArray;  },
		getUI : function(id) {  return loc_out.prv_componentData.ui[id];   },
		
		getEventHandler : function(uiId, eventName){   return this.getUI(uiId).getEventHandler(eventName);   },
		
		getProcess : function(name){  return loc_out.prv_componentData.componentDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_PROCESS][name];  },

		setRootView : function(rootView){   loc_out.prv_componentData.rootView = rootView;     },
		getRootView : function(){   return loc_out.prv_componentData.rootView;     },
		
//component core interface method		
		getInterface : function(){
			return {
				getPart : function(partId){  return loc_out.getPart(partId);	},

				getExecutePartCommandRequest : function(partId, commandName, commandData, handlers, requestInfo){
					return loc_out.getPart(partId).getExecuteCommandRequest(commandName, commandData, handlers, requestInfo);
				},
			};
		},
		
		getValue : function(name){  return loc_out.prv_componentData.valueByName[name];    },
		setValue : function(name, value){   loc_out.prv_componentData.valueByName[name] = value;   },
		
		setState : function(state){  loc_componentState.setState(state);	},
		
		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){  return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener); },

		getExecuteCommandRequest : function(commandName, parm, handlers, requestInfo){},
		getPart : function(partId){ 	return node_objectOperationUtility.getObjectAttributeByPath(loc_out.prv_componentData, partId); },

		getLifeCycleRequest : function(transitName, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT){
				//reset context io
				out.addRequest(loc_getBuildModuleUIRequest());
				out.addRequest(loc_getInitIOContextRequest());
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE){
				out.addRequest(loc_componentState.getSaveStateDataForRollBackRequest());
				if(loc_out.getValue(node_CONSTANT.COMPONENT_VALUE_BACKUP)==true){
					//active through back up
					out.addRequest(loc_componentState.getRestoreStateRequest());
//					var kkkk = bbbb++;
//					out.addRequest(node_createServiceRequestInfoError());
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

	loc_init();
	
	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UIMODULE);

	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
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
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.uiEventData", function(){node_uiEventData = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.destroyUtil", function(){node_destroyUtil = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentState", function(){node_createComponentState = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUIModuleComponentCore", node_createUIModuleComponentCore); 

})(packageObj);
