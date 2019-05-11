//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_StateTransitPath = function(from, to, path){
	this.from = from;
	this.to = to;
	this.path = path;
};
	
var node_CommandInfo = function(name, froms, nexts){
	this.name = name;
	this.froms = froms;
	this.nexts = nexts;
};
	
var node_NextStateInfo = function(name, callBack, reverseCallBack){
	this.name = name;
	this.callBack = callBack;
	this.reverseCallBack = reverseCallBack;
};	
	
var node_StateInfo = function(name, nextStates){
	this.name = name;
	this.nextStates = nextStates;
	if(this.nextStates==undefined)  this.nextStates = {};
};

node_StateInfo.prototype = {
	addNextState : function(name, callBack, reverseCallBack){
		this.nextStates[name] = new node_NextStateInfo(name, callBack, reverseCallBack);
	}
};

var node_TransitInfo = function(from, to){
	this.from = from;
	this.to = to;
};

var node_createStateMachineDef = function(){

	var loc_states = {};
	
	var loc_commands = {};

	var loc_nextCommandsByState;

	var loc_transitPath = {};
	
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
	

	var loc_buildNextCommandsByState = function(){
		loc_nextCommandsByState = {};
		_.each(loc_getAllStates(), function(state, i){
			var commands = [];
			var candidates = loc_getNextCandidateStates(state);
			_.each(loc_commands, function(commandInfo, command){
				if(candidates.includes(commandInfo.nexts[0])){
					if(commandInfo.froms==undefined || commandInfo.froms.includes(state)){
						commands.push(command);
					}
				}
			});
			loc_nextCommandsByState[state] = commands;
		});
	};
	
	var loc_discoverTransitPath = function(from, to){
		
	};
	
	var loc_out = {

		getStateInfo : function(state){		return loc_getStateInfo(state);	},	
			
		addStateInfo : function(transitInfo, callBack, reverseCallBack){
			var stateInfo = loc_getStateInfo(transitInfo.from);
			stateInfo.addNextState(transitInfo.to, callBack, reverseCallBack);
		},

		addCommand : function(commandInfo){		loc_commands[commandInfo.name] = commandInfo;		},
		
		getCandidateCommands : function(state){
			if(loc_nextCommandsByState==undefined){
				loc_buildNextCommandsByState();
			}
			return loc_nextCommandsByState[state];
		},
		
		getCandidateTransits : function(state){	return loc_getNextCandidateStates(state);	}, 
		
		getAllStates : function(){     
			var out = [];
			_.each(loc_states, function(stateInfo, stateName){ out.push(stateName);  });
			return out;
		},
		
		getAllCommands : function(){   
			var out = [];
			_.each(loc_commands, function(commandInfo, commandName){ out.push(commandName);  });
			return out;
		},
		getCommandInfo : function(command){		return loc_commands[command];	},
		
		addTransitPath : function(stateTransitPath){
			var fromTransit = loc_transitPath[stateTransitPath.from];
			if(fromTransit==undefined){
				fromTransit = {};
				loc_transitPath[stateTransitPath.from] = fromTransit;
			}
			fromTransit[stateTransitPath.to] = stateTransitPath;
		},
		
		getTransitPath : function(from, to){
			var fromTransit = loc_transitPath[from];
			if(fromTransit==undefined){
				fromTransit = {};
				loc_transitPath[from] = fromTransit;
			}
			var out = fromTransit[to];
			if(out==undefined){
				var out = loc_discoverTransitPath(from, to);
				if(out!=undefined){
					fromTransit[to] = out;
				}
			}
			return out;
		}
		
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("StateTransitPath", node_StateTransitPath); 
packageObj.createChildNode("TransitInfo", node_TransitInfo); 
packageObj.createChildNode("CommandInfo", node_CommandInfo); 
packageObj.createChildNode("NextStateInfo", node_NextStateInfo); 
packageObj.createChildNode("StateInfo", node_StateInfo);
packageObj.createChildNode("createStateMachineDef", node_createStateMachineDef); 

})(packageObj);
