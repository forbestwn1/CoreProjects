//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
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
	var node_createDataAssociation;
	var node_createDynamicData;

//*******************************************   Start Node Definition  ************************************** 	

var node_createModuleUIRequest = function(moduleUIDef, moduleIOContext, decorations, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createModuleUI", {"moduleUIDef":moduleUIDef, "moduleContext":moduleIOContext}), handlers, request);
	
	//generate page
	out.addRequest(nosliw.runtime.getUIPageService().getGenerateUIPageRequest(moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_PAGE], undefined, {
		success :function(requestInfo, page){
			var moduleUI = node_createModuleUI(moduleUIDef, page, moduleIOContext);
			
			//append decorations
			if(decorations!=null){
				_.each(decorations, function(decoration, index){
					moduleUI.addDecoration(decoration);
				});
			}
			return moduleUI;
		}
	}));
	return out;
};

var node_createModuleUI = function(moduleUIDef, page, moduleIOContext){
	var loc_moduleUIDef = moduleUIDef;
	var loc_page = page;
	//io between module context and page context
	var loc_inputDataAssociation = node_createDataAssociation(moduleIOContext, loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_INPUTMAPPING], node_createDynamicData(
		function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_page.getContextEleValueAsParmsRequest());
			return out;
		}, 
		function(value, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var pageInput = {};
			pageInput = _.extend(pageInput, value);
			//combine data from module with extra data in ui
			_.each(loc_extraContextData, function(data, name){
				pageInput = _.extend(pageInput, data);
			});
			//update page with data
			out.addRequest(loc_page.getUpdateContextRequest(pageInput));
			return out;
		}
	));
	
	var loc_outputDataAssociation = node_createDataAssociation(node_createDynamicData(
		function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_page.getBuildContextGroupRequest());
			return out;
		}
	), loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_OUTPUTMAPPING], moduleIOContext);

	var loc_extraContextData = {};

	var loc_getRefreshRequest = function(handlers, request){
		return loc_getRefreshPageDataRequest(handlers, request);
	};

	var loc_getRefreshPageDataRequest = function(handlers, request){
		return loc_inputDataAssociation.getExecuteRequest(handlers, request);
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(moduleUIDef, page){
		loc_out.setExtraContextData("nosliw_ui_uiInfo", {
			nosliw_uiInfo :{
				id : loc_out.getName(),
				title : loc_out.getName()
			}
		});
	};
	
	var loc_out = {
		
		getPage : function(){		return loc_page;		},
		
		getName : function(){	return loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_ID];	},
		
		getEventHandler : function(eventName){   return loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_EVENTHANDLER][eventName];   },
			
		addDecoration : function(decoration){		loc_page.addDecoration(decoration);	},	

		getGetStateRequest : function(handlers, requestInfo){  return loc_page.getGetPageStateRequest(handlers, requestInfo);  },
		getSetStateRequest : function(stateData, handlers, requestInfo){  return loc_page.getUpdateContextRequest(stateData, handlers, requestInfo);  },
		
		setExtraContextData : function(name, extraContextData){  loc_extraContextData[name] = extraContextData;  },
		getExtraContextData : function(name){  return loc_extraContextData[name];  },
		getUpdateExtraContextDataRequest : function(name, extraContextData){
			this.setExtraContextData(name, extraContextData);
			return loc_page.getUpdateContextRequest(extraContextData);
		},
		
		getUpdateContextRequest : function(parms, handlers, requestInfo){	return loc_page.getUpdateContextRequest(parms, handlers, requestInfo);	},
		executeUpdateContextRequest : function(parms, handlers, requestInfo){	node_requestServiceProcessor.processRequest(this.getUpdateContextRequest(parms, handlers, requestInfo));	},

		//take command
		getExecuteCommandRequest : function(commandName, parms, handlers, request){		return loc_page.getExecuteCommandRequest(commandName, parms, handlers, request);	},
		executeCommandRequest : function(commandName, parms, handlers, request){	node_requestServiceProcessor.processRequest(this.getExecuteCommandRequest(commandName, parms, handlers, request));	},

		registerEventListener : function(listener, handler, thisContext){		return loc_page.registerEventListener(listener, handler, thisContext);	},
		registerValueChangeEventListener : function(listener, handler, thisContext){	return	loc_page.registerValueChangeEventListener(listener, handler, thisContext);	},
		
		getSynInDataRequest : function(name, handlers, request){  return loc_inputDataAssociation.getExecuteRequest(handlers, request);  },
		
		getSynOutDataRequest : function(name, handlers, request){	return loc_outputDataAssociation.getExecuteRequest(handlers, request);	},
		executeSynOutDataRequest : function(name, handlers, request){	node_requestServiceProcessor.processRequest(this.getSynOutDataRequest(name, handlers, request));	}
		
	};
	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_APPMODULEUI);

	node_getLifecycleInterface(loc_out).init(moduleUIDef, page);

	return loc_out;
	
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createDynamicData", function(){node_createDynamicData = this.getData();});

//Register Node by Name
packageObj.createChildNode("createModuleUIRequest", node_createModuleUIRequest); 

})(packageObj);
