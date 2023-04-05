//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_getComponentLifecycleInterface;
	var node_createIODataSet;
	var node_requestServiceProcessor;
	var node_createServiceRequestInfoSequence;
	var node_createAppConfigure;
	var node_createModuleConfigure;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ResourceId;
	var node_createConfigure;
	var node_resourceUtility;

	var node_createComponentLifeCycleDebugView;
	var node_createComponentDataView;
	var node_createComponentEventView;
	var node_createComponentResetView;
	
//*******************************************   Start Node Definition  ************************************** 	
var node_createComponentDebugTool = function(views, resourceType, resourceId, inputValue, configure, configureParms, handlers, request){
	
	//changable
	var loc_resourceType;
	var loc_resourceId;
	var loc_inputValue;
	var loc_configure;
	var loc_runtimeContext;
	
	//won't change
	var loc_configureParms = configureParms;

	var loc_componentObj;
	
	var loc_lifecycleView;
	var loc_dataView;
	var loc_eventView;
	var loc_resetView;
	
	var loc_getConfigureRequest = function(configure, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		if(typeof configure === 'object'){
			loc_configure = node_createConfigure(configure, undefined, loc_configureParms);
		}
		else if(typeof configure === 'string'){
			var configureName = configure;
			var settingName;
			var index = configure.indexOf("_");
			if(index!=-1){
				configureName = configure.subString(0, index);
				settingName = configure.subString(index+1);
			}
			
			var configureResourceId = new node_ResourceId(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CONFIGURE, configureName);
			out.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(configureResourceId, {
				success : function(requestInfo, resourceTree){
					loc_configure = node_resourceUtility.getResourceFromTree(resourceTree, configureResourceId).resourceData[node_COMMONATRIBUTECONSTANT.EXECUTABLECONFIGURE_SCRIPT];
					if(settingName!=undefined)   loc_configure = loc_configure[settingName];
				}
			}));
		}
		return out;
	};
	
	//
	var loc_setComponent = function(requestInfo, componentObj){
		if(loc_componentObj!=undefined){
			//if old component exists, destroy old component first
			var lifecycle = node_getComponentLifecycleInterface(loc_componentObj);
			if(lifecycle.getComponentStatus()!=node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD){
				lifecycle.transit(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD);
			}
			loc_componentObj = undefined;
		}
		
		//set new component
		loc_componentObj = componentObj;
		loc_lifecycleView.setComponent(loc_componentObj, requestInfo);
		loc_dataView.setComponent(loc_componentObj, requestInfo);
		loc_eventView.setComponent(loc_componentObj, requestInfo);
	};
	
	var loc_getResetComponentRequest = function(resourceType, resourctId, inputValue, configure, runtimeContext, handlers, request){
		loc_resourceType = resourceType;
		loc_resourceId = resourceId;
		loc_inputValue = inputValue;
		loc_configure = configure;
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		out.addRequest(nosliw.runtime.getComplexEntityService().executeCreateApplicationRequest(new node_ResourceId(resourceType, resourctId), configure, runtimeContext, undefined, {
			success : function(request, application){
				loc_setComponent(request, application);
				return application;
			}
		}));

		return out;
	};
	
	var loc_init = function(views, resourceType, resourceId, inputValue, configure, configureParms, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(loc_getConfigureRequest(configure, {
			success : function(request){
				var lifecycleView = views.lifecycleView;
				if(lifecycleView!=undefined){
					loc_lifecycleView = node_createComponentLifeCycleDebugView();
					$(lifecycleView).append(loc_lifecycleView.getView());
				}
				
				var dataView = views.dataView;
				if(dataView!=undefined){
					loc_dataView = node_createComponentDataView(); 
					$(dataView).append(loc_dataView.getView());
				}

				var eventView = views.eventView;
				if(eventView!=undefined){
					loc_eventView = node_createComponentEventView();
					$(eventView).append(loc_eventView.getView());
				}

				var resetView = views.resetView;
				if(resetView!=undefined){
					loc_runtimeContext = {
						view : views.mainView
					};
					loc_resetView = node_createComponentResetView(function(resourceId, resourceType, inputValue, configure, runtimeContext, handlers, request){
						return loc_getResetComponentRequest(resourceType, resourceId, inputValue, configure, runtimeContext, handlers, request);
					}, resourceType, resourceId, inputValue, configure, loc_runtimeContext);
					$(resetView).append(loc_resetView.getView());
				}
				
				return loc_getResetComponentRequest(resourceType, resourceId, inputValue, loc_configure, loc_runtimeContext);
			}
		}));
		node_requestServiceProcessor.processRequest(out);
	};
	
	var loc_out = {
		getComponentObj : function(){	return loc_componentObj;	},
	};

	loc_init(views, resourceType, resourceId, inputValue, configure, configureParms, handlers, request);
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.createStateBackupService", function(){node_createStateBackupService = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.createModuleConfigure", function(){node_createModuleConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppConfigure", function(){node_createAppConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){	node_ResourceId = this.getData();	});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});


nosliw.registerSetNodeDataEvent("debug.createComponentLifeCycleDebugView", function(){node_createComponentLifeCycleDebugView = this.getData();});
nosliw.registerSetNodeDataEvent("debug.createComponentDataView", function(){node_createComponentDataView = this.getData();});
nosliw.registerSetNodeDataEvent("debug.createComponentEventView", function(){node_createComponentEventView = this.getData();});
nosliw.registerSetNodeDataEvent("debug.createComponentResetView", function(){node_createComponentResetView = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentDebugTool", node_createComponentDebugTool); 

})(packageObj);
