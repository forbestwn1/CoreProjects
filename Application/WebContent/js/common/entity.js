/*
 * service information object 
 * this structor can be used in different senario: romote task, service request 
 * 		command: service name
 * 		data:    input data
 */
var NosliwServiceInfo = function(command, parms){
	this.command = command;
	this.parms = parms;
};


var createParms = function(){
	var loc_parmsObj = {};
	
	var loc_out = {
		addParm : function(name, value){
			loc_parmsObj[name] = value;
			return this;
		},
		
		getParmObj : function(){
			return loc_parmsObj;
		},
	};
	
	return loc_out;
};

