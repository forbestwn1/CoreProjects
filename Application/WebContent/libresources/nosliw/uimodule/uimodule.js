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

//*******************************************   Start Node Definition  ************************************** 	
//module entity store all the status information for module
var node_createUIModuleRequest = function(id, uiModuleDef, decorations, ioInput, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createUIModule", {"uiModule":uiModuleDef}), handlers, request);

	var module = loc_createUIModule(id, uiModuleDef, ioInput);

	//prepare decoration first
	var decorationInfo = {};
	if(uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_DECORATION]!=null)  decorationInfo = uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_DECORATION];
	if(decorations!=null){
		if(decorations[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_GLOBAL]!=undefined)   decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_GLOBAL]=decorations[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_GLOBAL];
		if(decorations[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_UI]!=undefined)   decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_UI]=decorations[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_UI];
	}
	
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
	_.each(uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_UI], function(ui, index){
		
		var buildModuleUIRequest = node_createServiceRequestInfoSequence();

		var uiName = uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_ID];
		var decs;
		if(decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_UI]!=undefined)  decs = decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_UI][uiName]; 
		if(decs==undefined) decs = decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_GLOBAL];
		buildModuleUIRequest.addRequest(node_createUIDecorationsRequest(decs, {
			success : function(request, decorations){
				return node_createModuleUIRequest(ui, module.getIOContext(), decorations);
			}
		}));

		buildModuleUIsRequest.addRequest(index, buildModuleUIRequest);
	});
	out.addRequest(buildModuleUIsRequest);
	
	return out;
};	
	
var loc_createUIModule = function(id, uiModuleDef, ioInput){
	var loc_ioInput = ioInput;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();
	
	var loc_valueChangeEventListener = node_createEventObject();
	var loc_valueChangeEventSource = node_createEventObject();
	
	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };
	var loc_trigueValueChangeEvent = function(eventName, eventData, requestInfo){loc_valueChangeEventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_updateIOContext = function(input){
		var data = loc_out.prv_module.uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_INITSCRIPT](input);
		loc_out.prv_module.ioContext.setData(undefined, data);
	};
	
	var loc_init = function(){
		loc_out.prv_module.ioContext.registerEventListener(loc_valueChangeEventListener, function(eventName, eventData, request){
			if(loc_out.prv_module.lifecycle.isActive()==true){
				loc_trigueValueChangeEvent(node_CONSTANT.EVENT_COMPONENT_VALUECHANGE, undefined, request);
			}
		});
	};
	
	var loc_out = {
		prv_module : {
			id : id,
			uiModuleDef : uiModuleDef,
			
			ioContext : node_createIODataSet(),
			
			uiArray : [],
			ui : {},

			lifecycle : undefined    //module's lifecycle obj
		},
		
		prv_addUI : function(ui){
			loc_out.prv_module.uiArray.push(ui);
			loc_out.prv_module.ui[ui.getName()] = ui;
			//register listener for module ui
			ui.registerEventListener(loc_out.prv_module.eventListener, function(eventName, eventData, requestInfo){
				loc_trigueEvent(node_CONSTANT.MODULE_EVENT_UIEVENT, new node_uiEventData(this.getName(), eventName, eventData), requestInfo);
			}, ui);
			ui.registerValueChangeEventListener(loc_valueChangeEventListener, function(eventName, eventData, requestInfo){
				//handle ui value change, update value in module
				this.executeSynOutDataRequest(undefined, undefined, requestInfo);
			}, ui);
		},
	
		setLifecycle : function(lifecycle){  this.prv_module.lifecycle = lifecycle;   },
		
		getId : function(){  return loc_out.prv_module.id;  },
		getVersion : function(){   return "1.0.0";   },
		
		getIOContext : function(){  return loc_out.prv_module.ioContext;  },
		
		getUIs : function(){  return loc_out.prv_module.uiArray;  },
		getUI : function(name) {  return loc_out.prv_module.ui[name];   },
		getRefreshUIRequest : function(uiName, handlers, request){	return this.getUI(uiName).getSynInDataRequest(handlers, request);	},
		
		getEventHandler : function(uiName, eventName){   return this.getUI(uiName).getEventHandler(eventName);   },
		
		getProcess : function(name){  return loc_out.prv_module.uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_PROCESS][name];  },
		
		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){  return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener); },

		getInitIOContextRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(loc_ioInput!=undefined){
				out.addRequest(loc_ioInput.getGetDataValueRequest(undefined, {
					success : function(request, data){
						loc_updateIOContext(data);
					}
				}));
			}
			else{
				loc_updateIOContext();
			}
			return out;
		},
		
		getExecuteCommandRequest : function(commandName, parm, handlers, requestInfo){},
		getPart : function(partId){ 	return node_objectOperationUtility.getObjectAttributeByPath(loc_out.prv_module, partId); },
		
		getDestroyRequest : function(handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				node_destroyUtil(loc_out.prv_module.ioContext, request);
				
				_.each(loc_out.prv_module.uiArray, function(ui, i){
					node_destroyUtil(ui, request);
				});
				loc_out.prv_module.uiArray = undefined;
				loc_out.prv_module.ui = undefined;
				loc_out.prv_module.uiModuleDef = undefined;
				loc_out.prv_module.lifecycle = undefined;
			}, handlers, request);
		}
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


//Register Node by Name
packageObj.createChildNode("createUIModuleRequest", node_createUIModuleRequest); 

})(packageObj);
