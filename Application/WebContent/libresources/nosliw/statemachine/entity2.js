//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	
//*******************************************   Start Node Definition  **************************************
var node_StateMachineTaskInfo = function(task, handlers, request){
	this.task = task;
	this.handlers = handlers;
	this.request = request;
};	

//state definition
var node_StateInfo = function(name, nextStates){
	this.name = name;   //name of state
	this.nextStates = nextStates;  //all possible next states info
	if(this.nextStates==undefined)  this.nextStates = {};
};

node_StateInfo.prototype = {
	addNextState : function(name, callBack, reverseCallBack){
		this.nextStates[name] = new node_NextStateInfo(name, callBack, reverseCallBack);
	}
};

//next state
var node_NextStateInfo = function(name, callBack, reverseCallBack){
	this.name = name;    						//name: name of next state
	this.callBack = callBack;    				//callBack: callBack during transit
	this.reverseCallBack = reverseCallBack;		//reverseCallBack : callBack during reverse trainsit
};	

//
var node_createStateMachineStateConfigure = function(transits, commands){
	
	var loc_transits = transits;

	//all command info by command name -- from -- commandInfo
	var loc_commands = {};
	
	var loc_init = function(transits, commands){
		
	};
	
	var loc_out = {
		getTransits : function(){   return loc_transits;    },
		getCommands : function(){   return loc_commands;    },
	};
	
	loc_init(transits, commands);
	return loc_out;
};

//
var node_SMCommandInfo = function(name, from, nexts){
	this.name = name;		//name of command
	this.from = from;		//start state
	this.nexts = nexts;		//state transit path
};

//transit from --- to
var node_SMTransitInfo = function(from, to){
	this.from = from;   //from state
	this.to = to;  		//to state
};

//state machine state definition
var node_createStateMachineDef = function(){

	//all state by name
	var loc_states = {};
	
	//all command info by command name -- from -- commandInfo
	var loc_commands = {};

	//from -- to -- nexts
	var loc_nextsByTranistInfo;

	var loc_addState = function(stateInfo){   loc_states[stateInfo.name] = stateInfo;    };
	
	var loc_getStateInfo = function(state){
		var stateInfo = loc_states[state];
		if(stateInfo==undefined){
			stateInfo = new node_StateInfo(state);
			loc_addState(stateInfo);
		}
		return stateInfo;
	};

	var loc_getAllStates = function(){
		var out = [];
		_.each(loc_states, function(state, name){   out.push(name);   });
		return out;
	};

	var loc_getNextCandidateStates = function(state){
		var out = [];
		_.each(loc_getStateInfo(state).nextStates, function(state, name){   out.push(name);   });
		return out;
	};

	var loc_getNextCandidateCommands = function(state){
		var out = [];
		_.each(loc_commands, function(commandInfoByFrom, command){
			_.each(commandInfoByFrom, function(commandInfo, from){
				if(from==state){
					out.push(command);
				}
			});
		});
		return out;
	};

	var loc_buildNextsByTransitInfo = function(){
		//by command first
		loc_nextsByTranistInfo = {};
		_.each(loc_commands, function(commandInfoByFrom, command){
			_.each(commandInfoByFrom, function(commandInfo, from){
				var to = commandInfo.nexts[commandInfo.nexts.length-1];
				loc_addNextsByTransit(from, to, commandInfo.nexts);
			});
		});
		
		//by state
		_.each(loc_states, function(stateInfo, state){
			_.each(stateInfo.nextStates, function(nextInfo, next){
				loc_addNextsByTransit(state, next, [next]);
			});
		});
	};

	var loc_addNextsByTransit = function(from, to, nexts){
		var outByTo = loc_nextsByTranistInfo[from];
		if(outByTo==undefined){
			outByTo = {};
			loc_nextsByTranistInfo[from] = outByTo;
		}
		outByTo[to] = nexts;
	};
	
	var loc_out = {

		getStateInfo : function(state){		return loc_getStateInfo(state);	},	
			
		addStateInfo : function(transitInfo, callBack, reverseCallBack){
			var stateInfoFrom = loc_getStateInfo(transitInfo.from);
			var stateInfoTo = loc_getStateInfo(transitInfo.to);
			stateInfoFrom.addNextState(transitInfo.to, callBack, reverseCallBack);
		},

		getCandidateTransits : function(state){	return loc_getNextCandidateStates(state);	}, 
		
		getAllStates : function(){     
			var out = [];
			_.each(loc_states, function(stateInfo, stateName){ out.push(stateName);  });
			return out;
		},
		
		
		addCommand : function(commandInfo){
			var byName = loc_commands[commandInfo.name];
			if(byName==undefined){
				byName = {};
				loc_commands[commandInfo.name] = byName;
			}
			byName[commandInfo.from] = commandInfo;
		},
		
		getCandidateCommands : function(state){		return loc_getNextCandidateCommands(state);	},
		
		
		getAllCommands : function(){   
			var out = [];
			_.each(loc_commands, function(commandInfo, commandName){ out.push(commandName);  });
			return out;
		},
		
		getCommandInfo : function(command, from){
			var commands = loc_commands[command];
			return commands==undefined?undefined:commands[from];
		},
		
		getNextsByTransitInfo : function(transitInfo){
			if(loc_nextsByTranistInfo==undefined)  loc_buildNextsByTransitInfo();
			return loc_nextsByTranistInfo[transitInfo.from][transitInfo.to];
		},
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("SMTransitInfo", node_SMTransitInfo); 
packageObj.createChildNode("SMCommandInfo", node_SMCommandInfo); 
packageObj.createChildNode("NextStateInfo", node_NextStateInfo); 
packageObj.createChildNode("StateInfo", node_StateInfo);
packageObj.createChildNode("createStateMachineDef", node_createStateMachineDef);
packageObj.createChildNode("StateMachineTaskInfo", node_StateMachineTaskInfo);


})(packageObj);
