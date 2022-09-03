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
var node_createStateMachineDef = function(transitInfos, commandInfos){
	var loc_transitInfos = transitInfos;
	
	//all transits by from -- tos
	var loc_transits = {};

	//all command info by command name -- from -- commandInfo
	var loc_commands = {};

	var loc_allStates = [];
	
	//calculated  from -- to -- nexts
	var loc_nextsByTranistInfo;
	
	var loc_addCommand = function(commandInfo){
		var byName = loc_commands[commandInfo.name];
		if(byName==undefined){
			byName = {};
			loc_commands[commandInfo.name] = byName;
		}
		byName[commandInfo.from] = commandInfo;
	};
	
	var loc_addTransit = function(transitInfo){
		var byFrom = loc_transits[transitInfo.from];
		if(byFrom==undefined){
			byFrom = [];
			loc_transits[transitInfo.from] = byFrom;
		}
		byFrom.push(transitInfo.to);
	};

	var loc_getNextCandidateStates = function(state){
		return loc_transits[state];
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
		_.each(loc_commands, function(commandInfoByFrom, commandName){
			_.each(commandInfoByFrom, function(commandInfo, from){
				var to = commandInfo.nexts[commandInfo.nexts.length-1];
				loc_addNextsByTransit(from, to, commandInfo.nexts);
			});
		});
		
		//by state
		_.each(loc_transits, function(tos, from){
			_.each(tos, function(to){
				loc_addNextsByTransit(from, to, [to]);
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
	
	var loc_init = function(transitInfos, commandInfos){
		_.each(commandInfos, function(commandInfo){
			loc_addCommand(commandInfo);
		});
		
		var allStates = {};
		_.each(transitInfos, function(transitInfo){
			allStates[transitInfo.from] = transitInfo.from;  
			allStates[transitInfo.to] = transitInfo.to;  
			loc_addTransit(transitInfo);
		});

		_.each(allStates, function(state, name){
			loc_allStates.push(state);
		});
	};
	
	var loc_out = {
		
		isTransitValid : function(from, to){
			var byTo = loc_transits[from];
			if(byTo==undefined)  return false;
			if(byTo[to==undefined])   return false;
			return true;
		},
		
		//get all possible next state
		getCandidateTransits : function(state){	return loc_getNextCandidateStates(state);	}, 

		//get all possible command
		getCandidateCommands : function(state){		return loc_getNextCandidateCommands(state);	},
		
		getAllCommands : function(){   
			var out = [];
			_.each(loc_commands, function(commandInfo, commandName){ out.push(commandName);  });
			return out;
		},
		
		getAllTransits : function(){   return loc_transitInfos;   },
		
		getAllStates : function(){  return loc_allStates;  },
		
		getCommandInfo : function(command, from){
			var commands = loc_commands[command];
			return commands==undefined?undefined:commands[from];
		},

		//get trasit path
		getNextsByTransitInfo : function(transitInfo){
			if(loc_nextsByTranistInfo==undefined)  loc_buildNextsByTransitInfo();
			return loc_nextsByTranistInfo[transitInfo.from][transitInfo.to];
		},
		
		processNext : function(currentState, next){
			var nexts;
			if(typeof next === 'string' || next instanceof String){
				//if nexts parm is command string
				var commandInfo = this.getCommandInfo(next, currentState);
				if(commandInfo==undefined){
					//nexts parm is target state
					nexts = this.getNextsByTransitInfo(new node_SMTransitInfo(currentState, next));
				}
				else{
					//command
					nexts = commandInfo.nexts;
				}
			}
			
			return nexts;
		},
	};
	
	loc_init(transitInfos, commandInfos);
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("SMTransitInfo", node_SMTransitInfo); 
packageObj.createChildNode("SMCommandInfo", node_SMCommandInfo); 
packageObj.createChildNode("createStateMachineDef", node_createStateMachineDef);
packageObj.createChildNode("StateMachineTaskInfo", node_StateMachineTaskInfo);

})(packageObj);
