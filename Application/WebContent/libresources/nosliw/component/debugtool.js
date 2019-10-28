//get/create package
var packageObj = library.getChildPackage("debug");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_getComponentLifecycleInterface;
	var node_createIODataSet;
	var node_createComponentLifeCycleDebugView;
	var node_createComponentDataView;
	var node_createComponentEventView;
	var node_createComponentResetView;
	
//*******************************************   Start Node Definition  ************************************** 	
node_createDebugTool = function(views, configureParms, resourceType, resourceId, inputValue, settingName){
	
	//changable
	var loc_resourceType;
	var loc_resourceId;
	var loc_inputValue;
	var loc_settingName;
	
	//won't change
	var loc_configureParms = configureParms;

	var loc_componentObj;
	
	var loc_lifecycleView;
	var loc_dataView;
	var loc_eventView;
	var loc_resetView;
	
	//
	var loc_resetComponent = function(requestInfo, componentObj){
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
	
	var loc_loadComponent = function(resourceType, resourctId, inputValue, settingName){
		loc_resourceType = resourceType;
		loc_resourceId = resourceId;
		loc_inputValue = inputValue;
		loc_settingName = settingName;
		if(resourceType==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIMODULE){
			var configure = node_createModuleConfigure(settingName, loc_configureParms);
			var stateBackupService = node_createStateBackupService(resourceType, resourctId, "1.0.0", configure.getConfigureValue().__storeService);
			nosliw.runtime.getUIModuleService().executeGetUIModuleRuntimeRequest(100, resourctId, configure, node_createIODataSet(inputValue), stateBackupService, {
				success : loc_resetComponent
			});
		}
		else if(resourceType==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIAPPENTRY){
			var configure = node_createAppConfigure(settingName, loc_configureParms);
			var stateBackupService = node_createStateBackupService(resourceType, resourctId, "1.0.0", configure.getConfigureValue().__storeService);
			nosliw.runtime.getUIAppService().executeGetUIAppEntryRuntimeRequest(100, resourctId, configure, node_createIODataSet(inputValue), stateBackupService, {
				success : loc_resetComponent
			});
		}
	};
	
	var loc_init = function(views, configureParms, resourceType, resourceId, inputValue, settingName){
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
			loc_resetView = node_createComponentResetView(function(resourceId, resourceType, inputValue, settingName){
				loc_loadComponent(resourceType, resourctId, inputValue, settingName);
			}, resourceType, resourceId, inputValue, settingName);
			$(resetView).append(loc_resetView.getView());
		}

		loc_loadComponent(resourceType, resourceId, inputValue, settingName);
	};
	
	var loc_out = {
		getComponentObj : function(){	return loc_componentObj;	},
	};

	loc_init(views, configureParms, resourceType, resourceId, inputValue, settingName);
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
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppConfigure", function(){node_createAppConfigure = this.getData();});


nosliw.registerSetNodeDataEvent("component.debug.createComponentLifeCycleDebugView", function(){node_createComponentLifeCycleDebugView = this.getData();});
nosliw.registerSetNodeDataEvent("component.debug.createComponentDataView", function(){node_createComponentDataView = this.getData();});
nosliw.registerSetNodeDataEvent("component.debug.createComponentEventView", function(){node_createComponentEventView = this.getData();});
nosliw.registerSetNodeDataEvent("component.debug.createComponentResetView", function(){node_createComponentResetView = this.getData();});

//Register Node by Name
packageObj.createChildNode("createDebugTool", node_createDebugTool); 

})(packageObj);
