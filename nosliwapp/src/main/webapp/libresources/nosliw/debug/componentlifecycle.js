//get/create package
var packageObj = library.getChildPackage();    


(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_getComponentLifecycleInterface;
	var node_getComponentManagementInterface;
	var node_createServiceRequestInfoSimple;
	var node_requestServiceProcessor;
	var node_getComponentManagementInterface;
	var node_componentUtility;
	var node_getStateMachineDefinition;
	
//*******************************************   Start Node Definition  ************************************** 	

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
		loc_currentTask = comInterface.createLifecycleTask(next);
		
		loc_currentTask.registerTransitEventListener(undefined, function(eventName, eventData, eventRequest){
			var comInterface = node_getComponentManagementInterface(loc_component);
			if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION){
				loc_historyText = loc_historyText + " --> " + eventData.to;
			}
			else if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION){
				loc_historyText = loc_historyText + " XX " + eventData.to;
			}
			else if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_ROLLBACKTRANSITION){
				loc_historyText = loc_historyText + " <-- " + eventData.to;
			}
			loc_updateViewContent();
		});
		loc_currentTask.process();
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
				loc_processNextRequest(state);
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
				loc_processNextRequest(command);
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
	};
	
	var loc_updateViewContent = function(){
		var comInterface = node_getComponentManagementInterface(loc_component);
		
		loc_stateHistoryView.text(loc_historyText);
		loc_currentStateView.text(comInterface.getLifecycleState());
		
		var byTos = loc_stateMachineStateDef.getCandidateTransits(comInterface.getLifecycleState());
		var candidates = [];
		_.each(byTos, function(transitDef, to){
			if(transitDef.expose==true){
				candidates.push(to);
			}
		});
		loc_updateCandidateView(loc_stateMachineStateDef.getAllStates(), candidates, loc_stateView);

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
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentManagementInterface", function(){node_getComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.getStateMachineDefinition", function(){node_getStateMachineDefinition = this.getData();});


//Register Node by Name
packageObj.createChildNode("createComponentLifeCycleDebugView", node_createComponentLifeCycleDebugView); 

})(packageObj);
