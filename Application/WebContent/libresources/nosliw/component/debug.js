//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_getComponentLifecycleInterface;
	var node_getComponentInterface;
	var node_createServiceRequestInfoSimple;
	var node_createIODataSet;
	var node_createDynamicData;
	var node_requestServiceProcessor;
	var node_createEventObject;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentResetView = function(resetCallBack, restartCallBack){
	var loc_view = $('<div>Component Input: </div>');
	var loc_textView = $('<textarea rows="5" cols="150" style="resize: none;" data-role="none"></textarea>');
	var loc_submitView = $('<button>Reset</button>')
	loc_view.append(loc_textView);
	loc_view.append(loc_submitView);
	
	loc_submitView.on('click', function(){
		resetCallBack();
	});

	var loc_inputIODataSet = node_createIODataSet();
	var loc_viewIO = node_createDynamicData(
		function(handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				var content = loc_textView.val();
				if(content=='')  return;
				return JSON.parse(content); 
			}, handlers, request); 
		} 
	);
	loc_inputIODataSet.setData(undefined, loc_viewIO);
	
	var loc_out = {
		getView : function(){  return loc_view;   },
		
		getInputIODataSet : function(){
			return loc_inputIODataSet;
		}
	}
	
	return loc_out;
};

var node_createComponentDataView = function(){
	var loc_component;
	
	var loc_view = $('<div>Component Data: </div>');
	var loc_textView = $('<textarea rows="5" cols="150" style="resize: none;" data-role="none"></textarea>');
	loc_view.append(loc_textView);

	var loc_listener = node_createEventObject();

	var loc_clearup = function(){
		if(loc_component!=undefined){
			var comInterface = node_getComponentInterface(loc_component);
			comInterface.unregisterDataChangeEventListener(loc_listener);
			loc_component = undefined;
		}
	};

	var loc_showDataSet = function(dataSet){	loc_textView.val(JSON.stringify(dataSet, null, 4));	};
	
	var loc_setup = function(request){
		var comInterface = node_getComponentInterface(loc_component);
		comInterface.registerDataChangeEventListener(loc_listener, function(eventName, dataSet){
			loc_showDataSet(dataSet);
		});
		node_requestServiceProcessor.processRequest(comInterface.getContextDataSetRequest({
			success : function(request, dataSet){
				loc_showDataSet(dataSet);
			}
		}, request));
	};
	
	var loc_out = {
		getView : function(){  return loc_view;   },
		
		setComponent : function(component, request){
			loc_clearup();
			loc_component = component;
			loc_setup(request);
		}
	};
	
	return loc_out;
};

var node_createComponentEventView = function(){
	var loc_component;
	var loc_view = $('<div>Component Event: </div>');
	var loc_textView = $('<textarea rows="5" cols="150" style="resize: none;" data-role="none"></textarea>');
	loc_view.append(loc_textView);

	var loc_clearup = function(){};
	
	var loc_setup = function(){
		var comInterface = node_getComponentInterface(loc_component);
		comInterface.registerEventListener(undefined, function(eventName, eventData, request){
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
			loc_component = component;
			loc_setup();
		}
	};
	return loc_out;
};


var node_createComponentLifeCycleDebugView = function(){

	var loc_view = $('<div></div>');
	
	var loc_component;
	
	var loc_stateView = {};
	var loc_commandView = {};
	
	var loc_lifecycle;
	var loc_stateMachine;

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

	var loc_setup = function(){
		loc_view.empty();
		loc_stateView = {};
		loc_commandView = {};
		
		loc_lifecycle = node_getComponentLifecycleInterface(loc_component);
		loc_stateMachine = loc_lifecycle.getStateMachine();
		
		var allStatesView = $('<div>All States : </div>');
		_.each(loc_stateMachine.getAllStates(), function(state, i){
			var stateView = $('<a>'+state+'</a>');
			allStatesView.append(stateView);
			allStatesView.append($('<span>&nbsp;&nbsp;</span>'));
			stateView.on('click', function(){
				event.preventDefault();
				loc_lifecycle.transit([state]);
			});
			loc_stateView[state] = stateView;
		});
		loc_view.append(allStatesView);

		var allCommandsView = $('<div>All Commands : </div>');
		_.each(loc_stateMachine.getAllCommands(), function(command, i){
			var commandView = $('<a>'+command+'</a>');
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
		var stateHistoryView = $('<span></span>');
		stateHistoryBlockView.append(stateHistoryView);
		var currentStateView = $('<span></span>');
		currentStateBlockView.append(currentStateView);
		loc_view.append(stateHistoryBlockView);
		loc_view.append(currentStateBlockView);
		stateHistoryView.text(loc_stateMachine.getCurrentState());
		currentStateView.text(loc_stateMachine.getCurrentState());
		loc_updateCandidateView(loc_stateMachine.getAllStates(), loc_stateMachine.getNextStateCandidates(), loc_stateView);
		loc_updateCandidateView(loc_stateMachine.getAllCommands(), loc_stateMachine.getCommandCandidates(), loc_commandView);
		loc_lifecycle.registerEventListener(undefined, function(eventName, eventData, request){
			if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION){
				stateHistoryView.text(stateHistoryView.text() + " -- " + loc_stateMachine.getCurrentState());
			}
			else if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION){
				stateHistoryView.text(stateHistoryView.text() + " XX " + loc_stateMachine.getCurrentState());
			}
			else if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION){
				stateHistoryView.text(stateHistoryView.text() + " XX " + loc_stateMachine.getCurrentState());
			}
			currentStateView.text(loc_stateMachine.getCurrentState());
			loc_updateCandidateView(loc_stateMachine.getAllStates(), loc_stateMachine.getNextStateCandidates(), loc_stateView);
			loc_updateCandidateView(loc_stateMachine.getAllCommands(), loc_stateMachine.getCommandCandidates(), loc_commandView);
		});
		
	};
	
	var loc_out = {
		
		getView : function(){   return loc_view;   },
		
		setComponent : function(component){
			loc_component = component;
			loc_setup();
		}
	};
	
	return loc_out;
};
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createDynamicData", function(){node_createDynamicData = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});


//Register Node by Name
packageObj.createChildNode("createComponentLifeCycleDebugView", node_createComponentLifeCycleDebugView); 
packageObj.createChildNode("createComponentDataView", node_createComponentDataView); 
packageObj.createChildNode("createComponentEventView", node_createComponentEventView); 
packageObj.createChildNode("createComponentResetView", node_createComponentResetView); 

})(packageObj);
