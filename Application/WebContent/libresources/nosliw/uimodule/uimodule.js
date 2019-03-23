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
	var node_objectOperationUtility;
	var node_IOTaskResult;
	var node_NormalActivityOutput;
	var node_EndActivityOutput;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_contextUtility;
	var node_createEventObject;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_getLifecycleInterface;
	var node_createModuleUIRequest;
	var node_createUIDecorationsRequest;
	
//*******************************************   Start Node Definition  ************************************** 	
//module entity store all the status information for module
var node_createUIModuleRequest = function(uiModuleDef, input, statelessData, decorations, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createUIModule", {"uiModule":uiModuleDef}), handlers, request);

	var module = loc_createUIModule(uiModuleDef, uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_INITSCRIPT](input), statelessData);

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
			success : function(request, data){
				return node_createModuleUIRequest(ui, module.getContext(), data);
			}
		}));

		buildModuleUIsRequest.addRequest(index, buildModuleUIRequest);
	});
	out.addRequest(buildModuleUIsRequest);
	
	return out;
};	
	
var loc_createUIModule = function(uiModuleDef, context, statelessData){

	var loc_uiModuleDef = uiModuleDef;
	
	var loc_context = context;
	
	var loc_uis = [];
	var loc_uisByName = {};

	var loc_uiEventHandler;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventListener = node_createEventObject();

	//hold module state data, so that when restart the module, we can return to the right state
	var loc_stateData = {};
	
	var loc_statelessData = statelessData==undefined?{}:statelessData;

	var loc_setContext = function(context) {  loc_context = context;  };
	
	var loc_out = {
		
		prv_addUI : function(ui){
			loc_uis.push(ui);
			loc_uisByName[ui.getName()] = ui;
			//register listener for module ui
			ui.registerEventListener(loc_eventListener, function(eventName, eventData, requestInfo){
				if(loc_uiEventHandler!=undefined){
					loc_uiEventHandler(eventName, ui.getName(), eventData, requestInfo);
				}
			});
			ui.registerValueChangeEventListener(loc_valueChangeEventListener, function(eventName, eventData, requestInfo){
				//handle ui value change, update value in module
				ui.executeSynOutUIDataRequest(loc_context, {
					success : function(requestInfo, moduleData){
						loc_setContext(moduleData);
					}
				}, requestInfo);
			});
		},
	
		getContext : function(){  return loc_context;  },
		setContext : function(context){ loc_setContext(context);  },
		
		getStateData : function(name){   return loc_stateData[name];  },
		setStateData : function(name, state){  loc_stateData[name] = state;   },

		getStatelessData : function(name){   return loc_statelessData;  },
		setStatelessData : function(name, data){  loc_statelessData[name] = data;   },

		getUIs : function(){  return loc_uis;  },
		getUI : function(name) {  return loc_uisByName[name];   },
		getRefreshUIRequest : function(uiName, handlers, request){	return this.getUI(uiName).getRefreshRequest(loc_context, handlers, request);	},
		
		getProcess : function(name){  return loc_uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_PROCESS][name];  },
		
		getEventHandler : function(uiName, eventName){  return this.getUI(uiName).getEventHandler(eventName);     },
		
		registerUIEventListener : function(handler){  loc_uiEventHandler = handler; },

	};

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
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityResult", function(){node_IOTaskResult = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityOutput", function(){node_NormalActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.EndActivityOutput", function(){node_EndActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.createModuleUIRequest", function(){node_createModuleUIRequest = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createUIDecorationsRequest", function(){node_createUIDecorationsRequest = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIModuleRequest", node_createUIModuleRequest); 

})(packageObj);
