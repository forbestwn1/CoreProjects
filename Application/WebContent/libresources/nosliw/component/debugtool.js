//get/create package
var packageObj = library.getChildPackage("debug");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_getComponentLifecycleInterface;
	var node_getComponentInterface;
	
//*******************************************   Start Node Definition  ************************************** 	
node_createDebugTool = function(views, resourceType, resourceId, ioInput, configure){
	
	var loc_configure = configure;
	var loc_componentObj;
	
	var loc_lifecycleView;
	var loc_dataView;
	var loc_eventView;
	var loc_resetView;
	
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
	
	var loc_init = function(views, resourceId, resourceType, ioInput){
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
			loc_resetView = node_createComponentResetView(function(){
				var resourceType = loc_resetView.getResourceType();
				if(resourceType==node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIMODULE){
					nosliw.runtime.getUIModuleService().executeGetUIModuleRuntimeRequest(100, loc_resetView.getResourceId(), loc_configure, loc_resetView.getInputIODataSet(), {
						success : loc_resetComponent
					});
				}
			}, resourceType, resourceId, ioInput);
			$(resetView).append(loc_resetView.getView());
		}
	};
	
	var loc_out = {
			
	};

	
	loc_init(views, resourceId, resourceType, ioInput);
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentInterface = this.getData();});

nosliw.registerSetNodeDataEvent("component.debug.createComponentLifeCycleDebugView", function(){node_createComponentLifeCycleDebugView = this.getData();});
nosliw.registerSetNodeDataEvent("component.debug.createComponentDataView", function(){node_createComponentDataView = this.getData();});
nosliw.registerSetNodeDataEvent("component.debug.createComponentEventView", function(){node_createComponentEventView = this.getData();});
nosliw.registerSetNodeDataEvent("component.debug.createComponentResetView", function(){node_createComponentResetView = this.getData();});

//Register Node by Name
packageObj.createChildNode("createDebugTool", node_createDebugTool); 

})(packageObj);
