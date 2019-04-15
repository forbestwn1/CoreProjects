function(gate){
	var node_createServiceRequestInfoCommon = nosliw.getNodeData("request.request.createServiceRequestInfoCommon");
	var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
	var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
	var node_ServiceRequestExecuteInfo = nosliw.getNodeData("request.entity.ServiceRequestExecuteInfo");
	var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
	var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
	var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
	var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
	
	var CONSTANT_UISTACK_DATANAME = "module_uiStack";
	
	var loc_gate = gate;
	var loc_uiModule = loc_gate.getComponent();
	
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
	
	var loc_getUpdatePageStatusRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSet(undefined, handlers, request);
		_.each(loc_getUIStack(), function(uiName, index){
			//update ui status data
			out.addRequest(uiName, loc_uiModule.getUI(uiName).getUpdateExtraContextDataRequest("nosliw_module_state", {
				nosliw_uiStatus : {
					index : index,
				}
			}));
		});
		return out;
	};

	var loc_getRoutePathByUiName = function(uiName){
		return "/"+uiName+"/";
	}
	
	var loc_getTransferToRequest = function(uiName, mode, handlers, requestInfo){
		loc_view.router.navigate(loc_getRoutePathByUiName(uiName));
		loc_getUIStack().push(uiName);
		return loc_getUpdatePageStatusRequest(handlers, requestInfo);
	};
	
	var loc_transferBack = function(){
		loc_getUIStack().pop();
		loc_view.router.back();
	};

	var loc_processUIEvent = function(eventName, uiName, eventData, request){
		if(eventName=="nosliw_transferBack"){
			loc_transferBack();
		}
		else if(eventName=="nosliw_refresh"){
			loc_processRequest(loc_uiModule.getRefreshUIRequest(uiName, undefined, request));
		}
	};

	//runtime execute request through this method, so that ui can do something (for instance, spinning circle)
	loc_processRequest = function(request){     node_requestServiceProcessor.processRequest(request);   };
	
	var loc_out = {
			
		processComponentEvent : function(eventName, eventData, request){
			loc_processUIEvent(eventData.eventName, eventData.uiName, eventData.eventData, request);
		},
		
		getExecuteCommandRequest : function(command, parms, handlers, request){
			
		},
		
		getInterface : function(){
			return {
				getPresentUIRequest : function(uiName, mode, handlers, requestInfo){
					return loc_getTransferToRequest(uiName, mode, handlers, requestInfo);
				},
			}
		},
		
		getDeactiveRequest :function(handlers, request){
			loc_view.router.clearPreviousHistory();
		},
		
		getSuspendRequest :function(handlers, request){
		},
		
		getResumeRequest :function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var uiStack = loc_getUIStack();
			loc_clearUIStack();
			_.each(uiStack, function(stackEle, index){
				loc_view.router.navigate(loc_getRoutePathByUiName(stackEle));
				loc_getUIStack().push(stackEle);
			});
			return out;	
		},

		getInitRequest :function(handlers, requestInfo){
			var out = node_createServiceRequestInfoCommon(undefined, handlers, requestInfo);
			out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(requestInfo){
				//put ui to root
				_.each(loc_uiModule.getUIs(), function(ui, index){
					var uiPageContainer = $("<div class='page stacked' data-name="+ui.getName()+"/>"); 
					ui.getPage().appendTo(uiPageContainer);
					uiPageContainer.appendTo(loc_gate.getConfigure().root);
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
					route.path = loc_getRoutePathByUiName(ui.getName());
					route.pageName = ui.getName();
					viewConfigure.routes.push(route);
				});

				loc_view = loc_gate.getConfigure().app.views.create(loc_gate.getConfigure().root, viewConfigure);

				out.executeSuccessHandler();
			}));
			return out;
		},
	};
	return loc_out;
}
