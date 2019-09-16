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

//*******************************************   Start Node Definition  ************************************** 	
//module entity store all the status information for module
var node_createUIModuleComponentCoreRequest = function(id, uiModuleDef, uiDecorationConfigure, ioInput, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createUIModule", {"uiModule":uiModuleDef}), handlers, request);

	var module = loc_createUIModuleComponentCore(id, uiModuleDef, ioInput);

	//build module ui
	var buildModuleUIsRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("BuildModuleUIs", {}), {
		success : function(request, resultSet){
			_.each(resultSet.getResults(), function(moduleUI, index){
				module.prv_addUI(moduleUI);
			});
			return module;
		}
	});

	// build uis
	_.each(uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_UI], function(moduleUIDef, index){
		var moduleUIId = moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_ID];
		buildModuleUIRequest = node_createModuleUIRequest(moduleUIDef, module.getContextIODataSet(), uiDecorationConfigure);
		buildModuleUIsRequest.addRequest(index, buildModuleUIRequest);
	});
	out.addRequest(buildModuleUIsRequest);
	
	return out;
};	
	
var loc_createUIModuleComponentCore = function(id, uiModuleDef, ioInput){
	//input io used to get input value to initiate module
	var loc_inputIO = ioInput;
	
	var loc_state;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();
	
	var loc_valueChangeEventListener = node_createEventObject();
	var loc_valueChangeEventSource = node_createEventObject();
	
	var loc_trigueEvent = function(eventName, eventData, requestInfo){
		if(node_componentUtility.isActive(loc_out.prv_module.lifecycleStatus)){
			//trigue event only in active status
			loc_eventSource.triggerEvent(eventName, eventData, requestInfo);
		}
	};
	var loc_trigueValueChangeEvent = function(eventName, eventData, requestInfo){
		if(node_componentUtility.isActive(loc_out.prv_module.lifecycleStatus)){
			//trigue event only in active status
			loc_valueChangeEventSource.triggerEvent(eventName, eventData, requestInfo);
		}
	};

	//initiate context data set with input value
	var loc_initContextIODataSet = function(input){
		var data = loc_out.prv_module.uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_INITSCRIPT](input);
		loc_out.prv_module.contextDataSet.setData(undefined, data);
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
		node_destroyUtil(loc_out.prv_module.contextDataSet, request);
		
		_.each(loc_out.prv_module.uiArray, function(ui, i){
			node_destroyUtil(ui, request);
		});
		loc_out.prv_module.uiArray = undefined;
		loc_out.prv_module.ui = undefined;
		loc_out.prv_module.uiModuleDef = undefined;
		loc_out.prv_module.lifecycle = undefined;
	};
	
	var loc_init = function(){
		loc_out.prv_module.contextDataSet.registerEventListener(loc_valueChangeEventListener, function(eventName, eventData, request){
			loc_trigueValueChangeEvent(node_CONSTANT.EVENT_COMPONENT_VALUECHANGE, undefined, request);
		});
	};
	
	var loc_out = {
		prv_module : {
			id : id,              //
			uiModuleDef : uiModuleDef,   //
			contextDataSet : node_createIODataSet(),   //module context data set
			lifecycleStatus : undefined,  //current lifecycle status
			
			rootView : undefined,
			
			uiArray : [],    //
			ui : {},
			
			valueByName : {},
		},
		
		prv_addUI : function(ui){
			loc_out.prv_module.uiArray.push(ui);
			loc_out.prv_module.ui[ui.getId()] = ui;
			//register listener for module ui
			ui.registerEventListener(loc_eventListener, function(eventName, eventData, requestInfo){
				loc_trigueEvent(node_CONSTANT.MODULE_EVENT_UIEVENT, new node_uiEventData(this.getId(), eventName, eventData), requestInfo);
			}, ui);
			ui.registerValueChangeEventListener(loc_valueChangeEventListener, function(eventName, eventData, requestInfo){
				//handle ui value change, update value in module
				this.executeSynOutDataRequest(undefined, undefined, requestInfo);
			}, ui);
		},
	
		getId : function(){  return loc_out.prv_module.id;  },
		getVersion : function(){   return "1.0.0";   },
		
		getContextIODataSet : function(){  return loc_out.prv_module.contextDataSet;  },
		
		getUIs : function(){  return loc_out.prv_module.uiArray;  },
		getUI : function(id) {  return loc_out.prv_module.ui[id];   },
		
		getEventHandler : function(uiId, eventName){   return this.getUI(uiId).getEventHandler(eventName);   },
		
		getProcess : function(name){  return loc_out.prv_module.uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_PROCESS][name];  },

//		getRefreshUIRequest : function(uiName, handlers, request){	return this.getUI(uiName).getSynInDataRequest(handlers, request);	},

		setRootView : function(rootView){   loc_out.prv_module.rootView = rootView;     },
		getRootView : function(){   return loc_out.prv_module.rootView;     },
		
//component core interface method		
		getInterface : function(){
			return {
				getPart : function(partId){  return loc_out.getPart(partId);	},

				getExecutePartCommandRequest : function(partId, commandName, commandData, handlers, requestInfo){
					return loc_out.getPart(partId).getExecuteCommandRequest(commandName, commandData, handlers, requestInfo);
				},
			};
		},
		
		getValue : function(name){  return loc_out.prv_module.valueByName[name];    },
		setValue : function(name, value){   loc_out.prv_module.valueByName[name] = value;   },
		
		setState : function(state){  loc_state = state;  },
		
		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){  return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener); },

		getExecuteCommandRequest : function(commandName, parm, handlers, requestInfo){},
		getPart : function(partId){ 	return node_objectOperationUtility.getObjectAttributeByPath(loc_out.prv_module, partId); },

		getLifeCycleRequest : function(transitName, handlers, request){
			var out;
			if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT){
				//reset context io
				out = loc_getInitIOContextRequest(handlers, request);
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE){
				if(loc_out.getValue(node_CONSTANT.COMPONENT_VALUE_BACKUP)==true){
					//active through back up
					out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UI module resume"), handlers, request);

					//ui data
					out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
						var uiData = loc_state.getValue("uiData", request);
						var updateUIStateRequest = node_createServiceRequestInfoSequence(undefined);
						_.each(loc_out.getUIs(), function(ui, index){
							updateUIStateRequest.addRequest(ui.getSetStateRequest(uiData[ui.getId()]));	
						});
						return updateUIStateRequest;
					}));

					//context data
					out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
						var backupContextData = loc_state.getValue("context", request);
						var updateContextStateRequest = node_createServiceRequestInfoSequence(undefined);
						_.each(backupContextData, function(contextData, name){
							updateContextStateRequest.addRequest(loc_out.prv_module.contextDataSet.getSetDataValueRequest(name, contextData));
						});
						return updateContextStateRequest;
					}));
				}
				else{
					//active through normal way
				}
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE){
				//reset context io
				out = loc_getInitIOContextRequest(handlers, request);
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND){
				out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UI module suspend"), handlers, request);

				//back up ui data
				var uiDataRequest = node_createServiceRequestInfoSet(undefined, {
					success : function(request, resultSet){
						var uiData = {};
						_.each(resultSet.getResults(), function(uiDataEle, uiId){
							uiData[uiId] = uiDataEle;
						});
						loc_state.setValue("uiData", uiData, request);
					}
				}, handlers, request);
				_.each(loc_out.getUIs(), function(ui, index){	uiDataRequest.addRequest(ui.getId(), ui.getGetStateRequest());	});
				out.addRequest(uiDataRequest);
				
				//backup context data
				out.addRequest(loc_out.prv_module.contextDataSet.getGetDataSetValueRequest({
					success : function(request, contextDataSet){
						loc_state.setValue("context", contextDataSet, request);
					}
				}));
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME){
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY){
				out = node_createServiceRequestInfoSimple(undefined, function(request){loc_destroy(request);}, handlers, request);
			}
			return out;
		},
		setLifeCycleStatus : function(status){   loc_out.prv_module.lifecycleStatus = status;    },
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


//Register Node by Name
packageObj.createChildNode("createUIModuleComponentCoreRequest", node_createUIModuleComponentCoreRequest); 

})(packageObj);
