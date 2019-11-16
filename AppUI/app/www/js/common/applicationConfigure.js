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

var buildApplicationConfigure = function(){

	var loc_dataInput = {};
	
	//get all parms from url
	var searchParams = new URLSearchParams(window.location.search);
	for (let p of searchParams) {
		loc_dataInput[p[0]] = p[1];
	}
	
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
		
		getParms : function(){   return loc_dataInput;  },
	};
	return loc_out;
};

