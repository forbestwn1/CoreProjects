//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_getComponentLifecycleInterface;
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_updateCandidateView = function(all, candidates, views){
	_.each(all, function(ele, i){
		if(candidates.includes(ele)){
			views[ele].css('color', 'green');
		}
		else{
			views[ele].css('color', 'red');
		}
	});		
};
	
var node_createComponentLifeCycleDebugView = function(component){

	var out = $('<div></div>');
	
	var loc_stateView = {};
	var loc_commandView = {};
	
	var lifecycle = node_getComponentLifecycleInterface(component);
	var stateMachine = lifecycle.getStateMachine();

	var allStatesView = $('<div>All States : </div>');
	_.each(stateMachine.getAllStates(), function(state, i){
		var stateView = $('<a>'+state+'</a>');
		allStatesView.append(stateView);
		allStatesView.append($('<span>&nbsp;&nbsp;</span>'));
		stateView.on('click', function(){
			event.preventDefault();
			lifecycle.command(state);
		});
		loc_stateView[state] = stateView;
	});
	out.append(allStatesView);

	var allCommandsView = $('<div>All Commands : </div>');
	_.each(stateMachine.getAllCommands(), function(command, i){
		var commandView = $('<a>'+command+'</a>');
		allCommandsView.append(commandView);
		allCommandsView.append($('<span>&nbsp;&nbsp;</span>'));
		commandView.on('click', function(){
			event.preventDefault();
			lifecycle.command("activate");
		});
		loc_commandView[command] = commandView;
	});
	out.append(allCommandsView);
	
	var stateHistoryBlockView = $('<div>State History : </div>');
	var currentStateBlockView = $('<div>Current State : </div>');
	var stateHistoryView = $('<span></span>');
	stateHistoryBlockView.append(stateHistoryView);
	var currentStateView = $('<span></span>');
	currentStateBlockView.append(currentStateView);
	out.append(stateHistoryBlockView);
	out.append(currentStateBlockView);
	stateHistoryView.text(stateMachine.getCurrentState());
	currentStateView.text(stateMachine.getCurrentState());
	loc_updateCandidateView(stateMachine.getAllStates(), stateMachine.getNextStateCandidates(), loc_stateView);
	loc_updateCandidateView(stateMachine.getAllCommands(), stateMachine.getCommandCandidates(), loc_commandView);
	lifecycle.registerEventListener(undefined, function(eventName, eventData, request){
		if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION){
			stateHistoryView.text(stateHistoryView.text() + " -- " + stateMachine.getCurrentState());
		}
		else if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION){
			stateHistoryView.text(stateHistoryView.text() + " XX " + stateMachine.getCurrentState());
		}
		else if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION){
			stateHistoryView.text(stateHistoryView.text() + " XX " + stateMachine.getCurrentState());
		}
		currentStateView.text(stateMachine.getCurrentState());
		loc_updateCandidateView(stateMachine.getAllStates(), stateMachine.getNextStateCandidates(), loc_stateView);
		loc_updateCandidateView(stateMachine.getAllCommands(), stateMachine.getCommandCandidates(), loc_commandView);
	});
	
	return out;
};
	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentLifeCycleDebugView", node_createComponentLifeCycleDebugView); 

})(packageObj);
