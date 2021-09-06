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
	var node_ServiceInfo;
	var node_contextUtility;
	var node_requestServiceProcessor;
	var node_getLifecycleInterface;
	var node_makeObjectWithType;
	var node_makeObjectWithLifecycle;
	var node_destroyUtil;
	var node_createDataAssociation;
	var node_createDynamicIOData;
	var node_dataAssociationUtility;
	var node_basicUtility;
	var node_createUIDecorationRequest;
	var node_createEventObject;
	var node_createSystemData;
	var node_createEventSource;
	var node_createEventInfo;
	var node_eventUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_createModuleUIRequest = function(moduleUIDef, moduleContextIODataSet, externalUIDecorationInfos, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createModuleUI", {"moduleUIDef":moduleUIDef, "moduleContext":moduleContextIODataSet}), handlers, request);
	
	//generate page for module ui first
	out.addRequest(nosliw.runtime.getUIPageService().getGenerateUIPageRequest(moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_PAGE], undefined, {
		success :function(requestInfo, page){
			var moduleUI = loc_createModuleUI(moduleUIDef, page, moduleContextIODataSet);
			
			var uiDecorationInfos = [];
			//ui decoration from internal module ui
			_.each(moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_UIDECORATION], function(decInfo, i){
				uiDecorationInfos.push(new node_DecorationInfo(undefined, decInfo[node_COMMONATRIBUTECONSTANT.INFODECORATION_ID], undefined, undefined, node_createConfigure(decInfo[node_COMMONATRIBUTECONSTANT.INFODECORATION_CONFIGURE])));
			});

			//ui decoration from external module ui
			_.each(externalUIDecorationInfos, function(decInfo, i){
				uiDecorationInfos.push(decInfo);
			});
			
			//build ui decoration obj
			return loc_buildUIDecorationInfosRequest(uiDecorationInfos, {
				success : function(request, uiDecorationInfos){
					//add decoration to module ui
					_.each(uiDecorationInfos, function(uiDecorationInfo, index){
						moduleUI.prv_addDecoration(uiDecorationInfo.decoration);
					});
					
					return moduleUI.prv_postInitRequest({
						success : function(request){
							return moduleUI;
						}
					});
				}
			});
		}
	}));
	return out;
};

	
//build decoration object in decorationInfo.decoration
var loc_buildUIDecorationInfosRequest = function(decorationInfos, handlers, request){
	var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
	_.each(decorationInfos, function(decorationInfo, i){
		out.addRequest(loc_buildUIDecorationRequest(decorationInfo));
	});
	
	out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
		return decorationInfos;
	}));
	
	return out;
};	
	
var loc_buildUIDecorationRequest = function(decorationInfo, handlers, request){
	var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
	out.addRequest(node_createUIDecorationRequest(decorationInfo.id, decorationInfo.configure.getConfigureValue(), {
		success : function(request, decoration){
			decorationInfo.decoration = decoration;
			return decorationInfo;
		}
	}));
	return out;
};
	
var loc_createModuleUI = function(moduleUIDef, page, moduleContextIODataSet){
	var loc_moduleUIDef = moduleUIDef;
	var loc_info = moduleUIDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
	var loc_page = page;
	loc_page.setName(moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_PAGENAME]);  //page name

	//extra information by domain that provided by system and consumed by ui 
	var loc_systemData = node_createSystemData();
	
	//event source used to register and trigger event
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();
	
	var loc_valueChangeEventListener = node_createEventObject();
	var loc_valueChangeEventSource = node_createEventObject();

	var loc_valueChangeEventEnabled = true;
	
	var loc_getEventSourceInfo = function(){
		return node_createEventSource(node_CONSTANT.TYPEDOBJECT_TYPE_APPMODULEUI, loc_getId(), loc_out); 
	};
	
	var loc_trigueEvent = function(eventName, eventData, request){
		node_eventUtility.triggerEventInfo(loc_eventSource, eventName, eventData, loc_getEventSourceInfo(), request);
	};
	
	var loc_trigueValueChangeEvent = function(eventName, eventData, request){  
		if(loc_valueChangeEventEnabled==true)  node_eventUtility.triggerEventInfo(loc_valueChangeEventSource, eventName, eventData, loc_getEventSourceInfo(), request);  
	};

	
	var loc_getId = function(){   return loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];   };
	var loc_getName = function(){   return loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME];   };
	var loc_getTitle = function(){   return loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME];   };
	
	//update page context, during this request, value change event should not be trigued
	var loc_updatePageContextRequest = function(contextData, handlers, request){
		loc_valueChangeEventEnabled = false;
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(loc_page.getUpdateContextRequest(contextData, {
			success : function(request){  loc_valueChangeEventEnabled = true;  }
		}));
		return out;
	};
	
	var loc_getUpdateSystemDataRequest = function(domain, systemData, handlers, request){
		//update system data
		loc_systemData.updateSystemData(domain, systemData);
		//update page context
		return loc_updatePageContextRequest(loc_systemData.buildSystemDataContextByDomain(domain), handlers, request);
	};
	
	//data association from module context to page context
	var loc_inputDataAssociations = {};
	_.each(loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEEMBEDEDCOMPONENT_IN], function(dataAssociationDef, name){
		var loc_inputDataAssociation = node_createDataAssociation(moduleContextIODataSet, dataAssociationDef, node_createDynamicIOData(
				function(handlers, request){
					var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
					out.addRequest(loc_page.getContextEleValueAsParmsRequest());
					return out;
				}, 
				function(value, handlers, request){
					var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
					out.addRequest(loc_updatePageContextRequest(value));
					return out;
				}
			), node_dataAssociationUtility.buildDataAssociationName("MODULE", "CONTEXT", "PAGE", loc_page.getName()));
		loc_inputDataAssociations[name] = loc_inputDataAssociation;
	});
		
	var loc_outputDataAssociations = {};
	_.each(loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEEMBEDEDCOMPONENT_OUT], function(dataAssociationDef, name){
		var loc_outputDataAssociation = node_createDataAssociation(node_createDynamicIOData(
				function(handlers, request){
					var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
					out.addRequest(loc_page.getBuildContextGroupRequest());
					return out;
				}
			), 
			loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_OUTPUTMAPPING], 
			moduleContextIODataSet,
			node_dataAssociationUtility.buildDataAssociationName("PAGE", loc_page.getName(), "MODULE", "CONTEXT"));
		loc_outputDataAssociations[name] = loc_outputDataAssociation;
	});
	
	
	var loc_getExecuteCommandRequest = function(commandName, parms, handlers, request){
		var coreCommandName = node_basicUtility.getNosliwCoreName(commandName);
		if(coreCommandName!=undefined){
			//system command
			if(coreCommandName==node_CONSTANT.COMMAND_MODULEUI_SYNCIN_DATA){
				return loc_out.getSynInDataRequest(handlers, request);
			}
			else if(coreCommandName==node_CONSTANT.COMMAND_MODULEUI_REFRESH){
				return loc_out.getSynInDataRequest(handlers, request);
			}
			else if(coreCommandName==node_CONSTANT.COMMAND_MODULEUI_UPDATE_DATA){
				return loc_page.getExecuteCommandRequest(node_basicUtility.buildNosliwFullName(node_CONSTANT.COMMAND_PAGE_REFRESH), parms, handlers, request);
			}
		}
		else{
			//normal command or unrecoganized command
			return loc_page.getExecuteCommandRequest(commandName, parms, handlers, request);	
		}
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(moduleUIDef, page, moduleContextIODataSet){
		
		loc_page.registerEventListener(loc_eventListener, function(eventName, eventData, request){
			loc_trigueEvent(eventName, eventData, request);
		}, loc_out);	

		loc_page.registerValueChangeEventListener(loc_valueChangeEventListener, function(eventName, eventData, request){
			loc_trigueValueChangeEvent(eventName, eventData, request);
		}, loc_out);	
	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY]  = function(request){
		node_destroyUtil(loc_page, request);
	};

	var loc_out = {

		prv_addDecoration : function(decoration){		loc_page.addDecoration(decoration);	},	

		prv_postInitRequest : function(handlers, request){
			//build ui info extra data 
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_getUpdateSystemDataRequest("ui", {
				info :{
					id : loc_getId(),
					name : loc_getName(),
					title : loc_getTitle()
				}
			}));
			return out;
		},
		
		getPage : function(){		return loc_page;		},
		
		getId : function(){	return loc_getId();	},
		getName : function(){	return loc_getName();	},
		
		//process that handle event
		getEventHandler : function(eventName){   return loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_EVENTHANDLER][eventName];   },
		
		//handle state of ui
		getGetStateRequest : function(handlers, requestInfo){  return loc_page.getGetPageStateRequest(handlers, requestInfo);  },
		getSetStateRequest : function(stateData, handlers, requestInfo){  return loc_updatePageContextRequest(stateData, handlers, requestInfo);  },
		
		getUpdateSystemDataRequest : function(domain, systemData, handlers, request){
			return loc_getUpdateSystemDataRequest(domain, systemData, handlers, request);
		},
		
		getUpdateContextRequest : function(parms, handlers, requestInfo){	return loc_updatePageContextRequest(parms, handlers, requestInfo);	},
		executeUpdateContextRequest : function(parms, handlers, requestInfo){	node_requestServiceProcessor.processRequest(this.getUpdateContextRequest(parms, handlers, requestInfo));	},

		getSynInMode : function(){  return loc_info[node_CONSTANT.CONFIGURE_KEY_SYNCIN] || node_CONSTANT.CONFIGURE_VALUE_SYNCIN_MANUAL;	},
		getSynInDataRequest : function(dataAssociationName, handlers, request){  return loc_inputDataAssociations[dataAssociationName].getExecuteRequest(handlers, request);  },
		executeSynInDataRequest : function(dataAssociationName, handlers, request){	node_requestServiceProcessor.processRequest(this.getSynInDataRequest(dataAssociationName, handlers, request));	},
		
		getSynOutMode : function(){  return loc_info[node_CONSTANT.CONFIGURE_KEY_SYNCOUT] || node_CONSTANT.CONFIGURE_VALUE_SYNCOUT_MANUAL;	},
		getSynOutDataRequest : function(name, handlers, request){	return loc_outputDataAssociation.getExecuteRequest(handlers, request);	},
		executeSynOutDataRequest : function(name, handlers, request){	node_requestServiceProcessor.processRequest(this.getSynOutDataRequest(name, handlers, request));	},
		
		//take command
		getExecuteCommandRequest : function(commandName, parms, handlers, request){	return loc_getExecuteCommandRequest(commandName, parms, handlers, request);	},
		executeCommandRequest : function(commandName, parms, handlers, request){	node_requestServiceProcessor.processRequest(this.getExecuteCommandRequest(commandName, parms, handlers, request));	},

		getExecuteNosliwCommandRequest : function(commandName, parms, handlers, request){	return this.getExecuteCommandRequest(node_basicUtility.buildNosliwFullName(commandName), parms, handlers, request);	},
		executeNosliwCommandRequest : function(commandName, parms, handlers, request){	node_requestServiceProcessor.processRequest(this.getExecuteNosliwCommandRequest(commandName, parms, handlers, request));	},

		//event
		registerEventListener : function(listener, handler, thisContext){	return loc_eventSource.registerListener(undefined, listener, handler, thisContext);	},
		registerValueChangeEventListener : function(listener, handler, thisContext){	return	loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext);	},
	};
	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_APPMODULEUI);

	node_getLifecycleInterface(loc_out).init(moduleUIDef, page, moduleContextIODataSet);

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
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.destroyUtil", function(){node_destroyUtil = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createDynamicData", function(){node_createDynamicIOData = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.dataAssociationUtility", function(){node_dataAssociationUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createUIDecorationRequest", function(){node_createUIDecorationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.createSystemData", function(){node_createSystemData = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventSource", function(){node_createEventSource = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventInfo", function(){node_createEventInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createModuleUIRequest", node_createModuleUIRequest); 

})(packageObj);
