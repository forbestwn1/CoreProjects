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
	var node_destroyUtil;
	var node_createDataAssociation;
	var node_createDynamicIOData;
	var node_dataAssociationUtility;
	var node_basicUtility;
	var node_createUIDecorationRequest;

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
				uiDecorationInfos.push(new node_DecorationInfo(undefined, decInfo[node_COMMONATRIBUTECONSTANT.INFODECORATION_ID], undefined, decInfo[node_COMMONATRIBUTECONSTANT.INFODECORATION_CONFIGURE]));
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
					return moduleUI;
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
	out.addRequest(node_createUIDecorationRequest(decorationInfo.id, decorationInfo.configureValue, {
		success : function(request, decoration){
			decorationInfo.decoration = decoration;
			return decorationInfo;
		}
	}));
	return out;
};
	
var loc_createModuleUI = function(moduleUIDef, page, moduleContextIODataSet){
	var loc_moduleUIDef = moduleUIDef;
	var loc_page = page;
	loc_page.setName(moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_PAGENAME]);  //page name

	//extra information by domain that provided by system and consumed by ui 
	var loc_extraContextData = {};

	var loc_getId = function(){   return loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_ID];   };
	var loc_getName = function(){   return loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME];   };
	var loc_getTitle = function(){   return loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME];   };
	
	var loc_setExtraContextData = function(domain, obj){
		var domainObj = loc_extraContextData[domain];
		if(domainObj==undefined){
			domainObj = {};
			loc_extraContextData[domain] = domainObj;
		}
		domainObj = _.extend(domainObj, obj);
	};
	
	var loc_buildExtraContext = function(domain, context){
		if(context==undefined)  context = {};
		_.each(loc_extraContextData[domain], function(value, prop){
			context[node_basicUtility.buildNosliwFullName(domain+"_"+prop)] = value;
		});
		return context;
	};
	
	//data association from module context to page context
	var loc_inputDataAssociation = node_createDataAssociation(moduleContextIODataSet, loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_INPUTMAPPING], node_createDynamicIOData(
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
			_.each(loc_extraContextData, function(data, domain){
				pageInput = loc_buildExtraContext(domain, pageInput);
			});
			//update page with data
			out.addRequest(loc_page.getUpdateContextRequest(pageInput));
			return out;
		}
	), node_dataAssociationUtility.buildDataAssociationName("MODULE", "CONTEXT", "PAGE", loc_page.getName()));
	
	//data association from page to module context
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

	var loc_getExecuteCommandRequest = function(commandName, parms, handlers, request){
		var coreCommandName = node_basicUtility.getNosliwCoreName(commandName);
		if(coreCommandName==undefined){
			//normal command
			return loc_page.getExecuteCommandRequest(commandName, parms, handlers, request);	
		}
		else{
			//system command
			if(coreCommandName=="syncInData"){
				return loc_out.getSynInDataRequest(handlers, request);
			}
			else{
				//process by page
				return loc_page.getExecuteCommandRequest(commandName, parms, handlers, request);	
			}
		}
	};
	
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(moduleUIDef, page){
		//build ui info extra data 
		loc_setExtraContextData("ui", {
			info :{
				id : loc_getId(),
				name : loc_getName(),
				title : loc_getTitle()
			}
		});
	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY]  = function(request){
		node_destroyUtil(loc_page, request);
	};

	var loc_out = {

		prv_addDecoration : function(decoration){		loc_page.addDecoration(decoration);	},	

		getPage : function(){		return loc_page;		},
		
		getId : function(){	return loc_getId();	},
		getName : function(){	return loc_getName();	},
		
		//process that handle event
		getEventHandler : function(eventName){   return loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_EVENTHANDLER][eventName];   },
		
		//handle state of ui
		getGetStateRequest : function(handlers, requestInfo){  return loc_page.getGetPageStateRequest(handlers, requestInfo);  },
		getSetStateRequest : function(stateData, handlers, requestInfo){  return loc_page.getUpdateContextRequest(stateData, handlers, requestInfo);  },
		
		getUpdateExtraContextDataRequest : function(domain, extraContextData){
			loc_setExtraContextData(domain, extraContextData);
			return loc_page.getUpdateContextRequest(loc_buildExtraContext(domain));
		},
		
		getUpdateContextRequest : function(parms, handlers, requestInfo){	return loc_page.getUpdateContextRequest(parms, handlers, requestInfo);	},
		executeUpdateContextRequest : function(parms, handlers, requestInfo){	node_requestServiceProcessor.processRequest(this.getUpdateContextRequest(parms, handlers, requestInfo));	},

		//take command
		getExecuteCommandRequest : function(commandName, parms, handlers, request){	return loc_getExecuteCommandRequest(commandName, parms, handlers, request);	},
		executeCommandRequest : function(commandName, parms, handlers, request){	node_requestServiceProcessor.processRequest(this.getExecuteCommandRequest(commandName, parms, handlers, request));	},

		registerEventListener : function(listener, handler, thisContext){		return loc_page.registerEventListener(listener, handler, thisContext);	},
		registerValueChangeEventListener : function(listener, handler, thisContext){	return	loc_page.registerValueChangeEventListener(listener, handler, thisContext);	},
		
		getSynInDataRequest : function(handlers, request){  return loc_inputDataAssociation.getExecuteRequest(handlers, request);  },
		executeSynInDataRequest : function(handlers, request){	node_requestServiceProcessor.processRequest(this.getSynInDataRequest(handlers, request));	},
		
		getSynOutDataRequest : function(name, handlers, request){	return loc_outputDataAssociation.getExecuteRequest(handlers, request);	},
		executeSynOutDataRequest : function(name, handlers, request){	node_requestServiceProcessor.processRequest(this.getSynOutDataRequest(name, handlers, request));	},
		
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
nosliw.registerSetNodeDataEvent("common.lifecycle.destroyUtil", function(){node_destroyUtil = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createDynamicData", function(){node_createDynamicIOData = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.dataAssociationUtility", function(){node_dataAssociationUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createUIDecorationRequest", function(){node_createUIDecorationRequest = this.getData();});


//Register Node by Name
packageObj.createChildNode("createModuleUIRequest", node_createModuleUIRequest); 

})(packageObj);
