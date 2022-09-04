//get/create package
var packageObj = library.getChildPackage("debug");    


(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_getComponentLifecycleInterface;
	var node_getComponentManagementInterface;
	var node_createServiceRequestInfoSimple;
	var node_createIODataSet;
	var node_createDynamicIOData;
	var node_requestServiceProcessor;
	var node_createEventObject;
	var node_getComponentManagementInterface;
	var node_componentUtility;
	var node_getStateMachineDefinition;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createPackageDebugView = function(id, color){
	
	var loc_view = $('<div style="border-width:thick; border-style:solid; border-color:'+color+'">'+id+'</div>');
	var loc_wrapperView = $('<div style="border-style:dotted; border-color:red"></div>');
	loc_view.append(loc_wrapperView);
	
	var loc_content = "";

	var loc_out = {

		updateRuntimeContext : function(runtimeContext){   
			$(runtimeContext.view).append(loc_view);
			
			return node_componentUtility.makeNewRuntimeContext(runtimeContext, {
				view : loc_wrapperView.get()
			});
		}
	};
	return loc_out;
};
	
var node_createComponentDebugView = function(id){
	
	var loc_view = $('<div style="border-width:thick; border-style:solid; border-color:yellow">'+id+'</div>');
	var loc_outputValueView = $('<textarea rows="5" cols="150" style="resize: none;"></textarea>');
	var loc_wrapperView = $('<div style="border-style:dotted; border-color:red"></div>');
	loc_view.append(loc_outputValueView);
	loc_view.append(loc_wrapperView);
	
	var loc_content = "";

	var loc_println = function(content){
		loc_content = loc_content + "\n";
		loc_content = loc_content + content;
		loc_outputValueView.val(loc_content);
	};
	
	var loc_out = {

		getView : function(){   return loc_view.get();   },
			
		getWrapperView : function(){  return loc_wrapperView.get();   },

		logMethodCalled : function(functionName, args){
			loc_println("method called: " + functionName);
			loc_println("method args: " + JSON.stringify(args, null, 4));
		},

	};
	return loc_out;
};
	
	
//load component and init it with inputValue
var node_createComponentResetView = function(resetCallBack, resourceType, resourceId, inputValue, configureValue, runtimeContext){
	var loc_resetCallBack = resetCallBack;
	var loc_configureValue = configureValue;
	var loc_runtimeContext = runtimeContext;
	
	var loc_view = $('<div>Component Input: </div>');
	var loc_resourceTypeView = $('<textinput></textinput><br>');
	var loc_resourceIdView = $('<textinput></textinput><br>');
	var loc_configureValueView = $('<textinput></textinput><br>');
	var loc_inputValueView = $('<textarea rows="5" cols="150" style="resize: none;" data-role="none"></textarea>');
	var loc_submitView = $('<button>Reset</button>');
	loc_view.append(loc_resourceTypeView);
	loc_view.append(loc_resourceIdView);
	loc_view.append(loc_configureValueView);
	loc_view.append(loc_inputValueView);
	loc_view.append(loc_submitView);

	var loc_getResourceType = function(){  return loc_resourceTypeView.val();  };
	var loc_getResourceId = function(){  return loc_resourceIdView.val();  };
	var loc_getConfigureValue = function(){  return loc_configureValue;  };
	var loc_getInputValue = function(){
		var content = loc_inputValueView.val();
		if(content=='')  return;
		return JSON.parse(content); 
	};
	var loc_getRuntimeContext = function(){  return loc_runtimeContext;   };
	
	var loc_init = function(resourceType, resourceId, inputValue, configureValue, runtimeContext){
		if(resourceType!=undefined)   	loc_out.setResourceType(resourceType);
		if(resourceId!=undefined)  		loc_out.setResourceId(resourceId);
		if(inputValue!=undefined)		loc_out.setInputValue(inputValue);
		if(configureValue!=undefined)		loc_out.setConfigureValue(configureValue);
		if(runtimeContext!=undefined)   loc_out.setRuntimeContext(runtimeContext);

		loc_submitView.on('click', function(){
			var request = loc_resetCallBack(loc_getResourceType(), loc_getResourceId(), loc_getInputValue(), loc_getConfigureValue(), loc_getRuntimeContext());
			node_requestServiceProcessor.processRequest(request);
		});
	};
	
	var loc_out = {

		getView : function(){  return loc_view;   },
		
		setResourceType : function(type){   loc_resourceTypeView.val(type);   },
		setResourceId : function(id){  loc_resourceIdView.val(id);   },
		setConfigureValue : function(configureValue){
			loc_configureValue = configureValue;
			loc_configureValueView.val(JSON.stringify(loc_configureValue));   
		},
		setRuntimeContext : function(runtimeContext){  locruntimeContext = runtimeContext;  },
		setInputValue : function(inputValue){  loc_inputValueView.val(JSON.stringify(inputValue));  },
	};
	
	loc_init(resourceType, resourceId, inputValue, configureValue, runtimeContext);
	
	return loc_out;
};

//display component context value
var node_createComponentDataView = function(){
	var loc_comInterface;
	
	var loc_view = $('<div>Component Data: </div>');
	var loc_textView = $('<textarea rows="5" cols="150" style="resize: none;" data-role="none"></textarea>');
	loc_view.append(loc_textView);

	var loc_listener = node_createEventObject();

	var loc_clearup = function(){
		if(loc_comInterface!=undefined){
			loc_comInterface.unregisterContextDataChangeEventListener(loc_listener);
			loc_comInterface = undefined;
		}
	};

	var loc_showDataSet = function(dataSet){	loc_textView.val(JSON.stringify(dataSet, null, 4));	};
	
	var loc_refresh = function(request){
		node_requestServiceProcessor.processRequest(loc_comInterface.getContextDataSetValueRequest({
			success : function(request, dataSet){
				loc_showDataSet(dataSet);
			}
		}, request));
	};
	
	var loc_setup = function(component, request){
//		loc_comInterface = node_getComponentManagementInterface(component);
//		loc_comInterface.getContextIODataSet().registerEventListener(undefined, function(eventName, eventData, request){
//			loc_refresh(request);
//		});

//		loc_comInterface = node_getComponentManagementInterface(component);
//		loc_comInterface.registerContextDataChangeEventListener(loc_listener, function(eventName, dataSet){
//			loc_showDataSet(dataSet);
//		});

//		loc_refresh(request);
	};
	
	var loc_out = {
			
		getView : function(){  return loc_view;   },
		
		setComponent : function(component, request){
			loc_clearup();
			loc_setup(component, request);
		},
		
		refresh : function(){
			loc_refresh();
		},
	};
	
	return loc_out;
};

//display component event
var node_createComponentEventView = function(){
	var loc_comInterface;
	var loc_view = $('<div>Component Event: </div>');
	var loc_textView = $('<textarea rows="5" cols="150" style="resize: none;" data-role="none"></textarea>');
	loc_view.append(loc_textView);

	var loc_clearup = function(){};
	
	var loc_setup = function(component){
		loc_comInterface = node_getComponentManagementInterface(component);
		loc_comInterface.registerEventListener(undefined, function(eventName, eventData, request){
			var content = loc_textView.val();
			content = content + "\n\n*****************************************\n\n";
			content = content + JSON.stringify({
				eventName : eventName,
				eventData : eventData
			}, null, 4);
			
			loc_textView.val(content);
		});
	};
	
	var loc_out = {
		getView : function(){  return loc_view;   },
		
		setComponent : function(component){
			loc_clearup();
			loc_setup(component);
		}
	};
	return loc_out;
};

//component lifecycle 
var node_createComponentLifeCycleDebugView = function(){
	var loc_stateMachineStateDef;
	
	var loc_view = $('<div></div>');

	var loc_stateHistoryView;
	var loc_currentStateView;

	var loc_stateView = {};
	var loc_commandView = {};
	
	var loc_component;
	var loc_historyText;
	
	var loc_currentTask;
	
	var loc_updateCandidateView = function(all, candidates, views){
		if(candidates==undefined)  candidates = [];
		_.each(all, function(ele, i){
			if(candidates.includes(ele)){
				views[ele].css('color', 'green');
			}
			else{
				views[ele].css('color', 'red');
			}
		});		
	};

	var loc_processNextRequest = function(next){
		var comInterface = node_getComponentManagementInterface(loc_component);
		var loc_currentTask = comInterface.createLifecycleTask(next);
		loc_currentTask.getProcessRequest({
			success : function(){
				var comInterface = node_getComponentManagementInterface(loc_component);
				loc_historyText = loc_historyText + " -- " + comInterface.getLifecycleState()
			},
			error: function(){
				var comInterface = node_getComponentManagementInterface(loc_component);
				loc_historyText = loc_historyText + " XX " + comInterface.getLifecycleState()
			},
			exception : function(){
				var comInterface = node_getComponentManagementInterface(loc_component);
				loc_historyText = loc_historyText + " XX " + comInterface.getLifecycleState()
			}
		});
	};
	
	var loc_init = function(){
		loc_stateMachineStateDef = node_getStateMachineDefinition();
		
		var allStatesView = $('<div>All States : </div>');
		_.each(loc_stateMachineStateDef.getAllStates(), function(state, i){
			var stateView = $('<a>'+state+'</a>');
			allStatesView.append(stateView);
			allStatesView.append($('<span>&nbsp;&nbsp;</span>'));
			stateView.on('click', function(){
				event.preventDefault();
				var comInterface = node_getComponentManagementInterface(loc_component);
				loc_currentTask = comInterface.createLifecycleTask(state, loc_component);
				loc_currentTask.executeProcessRequest({
					success : function(){
						var comInterface = node_getComponentManagementInterface(loc_component);
						loc_historyText = loc_historyText + " -- " + comInterface.getLifecycleState()
						loc_updateViewContent();
					},
					error: function(){
						var comInterface = node_getComponentManagementInterface(loc_component);
						loc_historyText = loc_historyText + " XX " + comInterface.getLifecycleState()
						loc_updateViewContent();
					},
					exception : function(){
						var comInterface = node_getComponentManagementInterface(loc_component);
						loc_historyText = loc_historyText + " XX " + comInterface.getLifecycleState()
						loc_updateViewContent();
					}
				});
			});
			loc_stateView[state] = stateView;
		});
		loc_view.append(allStatesView);

		var allCommandsView = $('<div>All Commands : </div>');
		_.each(loc_stateMachineStateDef.getAllCommands(), function(command, i){
			var commandView = $('<br><a>'+command+'</a>');
			allCommandsView.append(commandView);
			allCommandsView.append($('<span>&nbsp;&nbsp;</span>'));
			commandView.on('click', function(){
				event.preventDefault();
//				loc_lifecycle.command(command);
				loc_lifecycle.executeTransitRequest(command, {
					success : function(request){
						console.log('aaa');
					}
				});
			});
			loc_commandView[command] = commandView;
		});
		loc_view.append(allCommandsView);

		var stateHistoryBlockView = $('<div>State History : </div>');
		var currentStateBlockView = $('<div>Current State : </div>');
		loc_stateHistoryView = $('<span></span>');
		stateHistoryBlockView.append(loc_stateHistoryView);
		loc_currentStateView = $('<span></span>');
		currentStateBlockView.append(loc_currentStateView);
		loc_view.append(stateHistoryBlockView);
		loc_view.append(currentStateBlockView);

	};
	
	var loc_setup = function(component){
		loc_component = component;
		loc_historyText = node_getComponentManagementInterface(loc_component).getLifecycleState();
		
		loc_updateViewContent();

//		loc_lifecycle.registerEventListener(undefined, function(eventName, eventData, request){
//			if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION){
//				stateHistoryView.text(stateHistoryView.text() + " -- " + loc_stateMachine.getCurrentState());
//			}
//			else if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION){
//				stateHistoryView.text(stateHistoryView.text() + " XX " + loc_stateMachine.getCurrentState());
//			}
//			else if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION){
//				stateHistoryView.text(stateHistoryView.text() + " XX " + loc_stateMachine.getCurrentState());
//			}
//			currentStateView.text(loc_comInterface.getLifecycleState());
//			loc_updateCandidateView(loc_stateMachineStateDef.getAllStates(), loc_stateMachineStateDef.getCandidateTransits(loc_comInterface.getLifecycleState()), loc_stateView);
//			loc_updateCandidateView(loc_stateMachineStateDef.getAllCommands(), loc_stateMachineStateDef.getCandidateCommands(loc_comInterface.getLifecycleState()), loc_commandView);
//		});
		
	};
	
	var loc_updateViewContent = function(){
		var comInterface = node_getComponentManagementInterface(loc_component);
		
		loc_stateHistoryView.text(loc_historyText);
		loc_currentStateView.text(comInterface.getLifecycleState());
		loc_updateCandidateView(loc_stateMachineStateDef.getAllStates(), loc_stateMachineStateDef.getCandidateTransits(comInterface.getLifecycleState()), loc_stateView);
		loc_updateCandidateView(loc_stateMachineStateDef.getAllCommands(), loc_stateMachineStateDef.getCandidateCommands(comInterface.getLifecycleState()), loc_commandView);
	};
	
	var loc_out = {
		
		getView : function(){   return loc_view;   },
		
		setComponent : function(component){
			loc_setup(component);
		}
	};
	
	loc_init();
	return loc_out;
};
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createDynamicData", function(){node_createDynamicIOData = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentManagementInterface", function(){node_getComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.getStateMachineDefinition", function(){node_getStateMachineDefinition = this.getData();});


//Register Node by Name
packageObj.createChildNode("createPackageDebugView", node_createPackageDebugView); 
packageObj.createChildNode("createComponentDebugView", node_createComponentDebugView); 
packageObj.createChildNode("createComponentLifeCycleDebugView", node_createComponentLifeCycleDebugView); 
packageObj.createChildNode("createComponentDataView", node_createComponentDataView); 
packageObj.createChildNode("createComponentEventView", node_createComponentEventView); 
packageObj.createChildNode("createComponentResetView", node_createComponentResetView); 

})(packageObj);
