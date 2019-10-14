function(gate){
	var node_createServiceRequestInfoCommon = nosliw.getNodeData("request.request.createServiceRequestInfoCommon");
	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
	var node_ServiceRequestExecuteInfo = nosliw.getNodeData("request.entity.ServiceRequestExecuteInfo");
	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
	var node_createUIDecorationRequest = nosliw.getNodeData("uipage.createUIDecorationRequest");
	var node_basicUtility = nosliw.getNodeData("common.utility.basicUtility");
	
	var CONSTANT_UISTACK_DATANAME = "module_uiStack";
	
	var loc_gate = gate;
	var loc_uiModule = loc_gate.getComponentCore();
	var loc_configure = loc_gate.getConfigure();
	var loc_configureValue = loc_configure.getConfigureValue();
	
	var loc_framework7App = loc_configureValue.app;
	
	var loc_parentView;
	var loc_applicationContainerView;
	var loc_framework7View;
	
	var loc_getCurrentUIId = function(){ return  loc_getUIStack()[loc_getUIStack().length-1];  };
	
	var loc_getUIStack = function(){ 
		var out = loc_gate.getStateValue(CONSTANT_UISTACK_DATANAME);  
		if(out==undefined){
			out = [];
			loc_gate.setStateValue(CONSTANT_UISTACK_DATANAME, out);
		}
		return out;
	};
	
	var loc_clearUIStack = function(){
		loc_gate.setStateValue(CONSTANT_UISTACK_DATANAME, []);
	};
	
	var loc_updatePageStatus = function(){
		_.each(loc_getUIStack(), function(uiId, index){
			//update ui status data
			loc_uiModule.getUI(uiId).setExtraContextData("module_application", {
				uiStatus : {
					index : index,
				}
			});
		});
	};

	var loc_getRoutePathByUiId = function(uiId){	return "/"+uiId+"/";  };
	
	var loc_currentUIChangeRequest = function(handlers, request){
		loc_updatePageStatus();
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(loc_uiModule.getUI(loc_getCurrentUIId()).getSynInDataRequest());
		return out;
	};
	
	var loc_getTransferToRequest = function(uiId, mode, handlers, requestInfo){
		loc_framework7View.router.navigate(loc_getRoutePathByUiId(uiId));
		loc_getUIStack().push(uiId);
		return loc_currentUIChangeRequest(handlers, requestInfo);
	};
	
	var loc_getTransferBackRequest = function(handlers, requestInfo){
		loc_getUIStack().pop();
		loc_framework7View.router.back();
		return loc_currentUIChangeRequest(handlers, requestInfo);
	};

	var loc_processUIEvent = function(eventName, uiId, eventData, request){
		var coreEventName = node_basicUtility.getNosliwCoreName(eventName);
		if(coreEventName=="module_application_transferBack"){
			loc_gate.processRequest(loc_getTransferBackRequest(undefined, request));
		}
		else if(coreEventName=="module_application_refresh"){
			loc_gate.processRequest(loc_uiModule.getUI(uiId).getExecuteNosliwCommandRequest(node_CONSTANT.COMMAND_PAGE_REFRESH, undefined, undefined, request));
		}
	};

	var loc_getRestoreStateDataRequest = function(stateData, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			loc_gate.setState(stateData);
			var uiStack = loc_getUIStack();
			loc_clearUIStack();
			_.each(uiStack, function(stackEle, index){
				loc_framework7View.router.navigate(loc_getRoutePathByUiId(stackEle));
				loc_getUIStack().push(stackEle);
			});
			
			loc_updatePageStatus();
		}));
		return out;
	};


	var loc_getRestoreStateDataForRollBackRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(loc_gate.getRestoreStateDataForRollBackRequest({
			success : function(request, stateData){
				return loc_getRestoreStateDataRequest(stateData);
			} 
		}));
		return out;
	};

	var loc_out = {
			
		processComponentCoreEvent : function(eventName, eventData, request){
			loc_processUIEvent(eventData.eventName, eventData.uiId, eventData.eventData, request);
		},
		
		getUpdateViewRequest : function(view, handlers, request){
			loc_parentView = $(view);
			if(loc_framework7App==undefined){
				return node_createServiceRequestInfoSimple(undefined, function(request){
					loc_parentView = $(view);
					var loc_appView;
					loc_appView = $('<div></div>');
					loc_applicationContainerView = $('<div class="view view-main" style="height1:1200px;overflow-y1: scroll; "></div>');
					loc_appView.append(loc_applicationContainerView);
					loc_parentView.append(loc_appView);
					loc_framework7App = new Framework7({
						  // App root element
						  root: loc_appView.get(),
						  name: 'My App',
						  id: 'com.myapp.test',
						  panel: {
							  swipe: 'both',
						  },		
					});			
					return loc_applicationContainerView.get();
				}, handlers, request);
			}
			else{
				var containerConfigureValue = loc_configureValue.uiResource.container;
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				out.addRequest(node_createUIDecorationRequest(containerConfigureValue.id, containerConfigureValue, {
					success : function(request, uiDecoration){
						loc_applicationContainerView = uiDecoration.getPlaceHolderView();
						uiDecoration.appendTo(loc_parentView);
					}
				}));
				return out;
			}
		},

		getInterface : function(){
			return {
				getPresentUIRequest : function(uiName, mode, handlers, requestInfo){
					return loc_getTransferToRequest(uiName, mode, handlers, requestInfo);
				},
			}
		},
		
		getLifeCycleRequest : function(transitName, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT){
				var initRequest = node_createServiceRequestInfoCommon(undefined, handlers, request);
				initRequest.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(requestInfo){
					//append ui view to container
					_.each(loc_uiModule.getUIs(), function(ui, index){
						var uiPageContainer = $("<div class='page stacked' data-name="+ui.getName()+"/>"); 
						ui.getPage().appendTo(uiPageContainer);
						uiPageContainer.appendTo(loc_applicationContainerView);
					});
					
					//view configure
					var viewConfigure = {
						stackPages : true,
						routes : [],
						routesBeforeEnter : function(to, from, resolve, reject){
							resolve();
						}
					};
					_.each(loc_uiModule.getUIs(), function(ui, index){
						var route = {};
						route.name = ui.getName();
						route.path = loc_getRoutePathByUiId(ui.getName());
						route.pageName = ui.getName();
						viewConfigure.routes.push(route);
					});

					loc_framework7View = loc_framework7App.views.create(loc_applicationContainerView, viewConfigure);

					initRequest.successFinish();
				}));
				out.addRequest(initRequest);
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE){
				out.addRequest(loc_gate.getSaveStateDataForRollBackRequest());
				if(loc_gate.getValueFromCore(node_CONSTANT.COMPONENT_VALUE_BACKUP)==true){
					//get state data from store
					out.addRequest(loc_gate.getComponentState().getRestoreStateRequest());
				}
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY){ 
				out.addRequest(loc_gate.getSaveStateDataForRollBackRequest());
				loc_framework7View.destroy();
				loc_applicationContainerView.remove();
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND){  
				out.addRequest(loc_gate.getSaveStateDataForRollBackRequest());
				//save state data to store
				out.addRequest(loc_gate.getComponentState().getBackupStateRequest());
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE){
				out.addRequest(loc_gate.getSaveStateDataForRollBackRequest());
				loc_framework7View.router.clearPreviousHistory();
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE_REVERSE){
				out.addRequest(loc_getRestoreStateDataForRollBackRequest());
			}
			return out;
		},
		
		getRestoreStateDataRequest : function(handlers, request){
			return loc_getRestoreStateDataRequest(loc_gate.getState(), handlers, request)
		},

	};
	return loc_out;
}
