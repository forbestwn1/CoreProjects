var loc_environments = {
	product : {
		nosliw_address : 'http://www.lastkilometer.ca',
		nosliw_port : 8082,
		nosliw_context : 'nosliw',
		library : [],
	},
	local : {
		nosliw_address : 'http://localhost',
		nosliw_port : 8082,
		nosliw_context : 'nosliw',
		library : [
			'libresources/external/log4javascript/1.0.0/log4javascript.js'
		],
	},
	test : {
		nosliw_address : 'http://www.lastkilometer.ca',
		nosliw_port : 8082,
		nosliw_context : 'nosliwtest',
		library : [
			'libresources/external/log4javascript/1.0.0/log4javascript.js'
		],
	},
};

var buildApplicationConfigure = function(parms){

	var loc_dataInput = parms;
	
	var loc_getParm = function(name, defaultValue){
		var value = loc_dataInput[name];
		if(value==undefined)  value = defaultValue;
		return value;
	};
	
	var loc_out = {
		
		//application configure (decoration)
		getApplicationConfigureName : function(){
			return loc_getParm("configure", "normal");
		},
		
		//environment information (address, port, context, library)
		getEnvironment : function(){
			var envName =  loc_getParm("env", "product");
			return loc_environments[envName];
		},
		
		getVersion : function(){ return loc_getParm("version", "0.0.0");     },
		
		getParms : function(){   return loc_dataInput;  },
	};
	return loc_out;
};

