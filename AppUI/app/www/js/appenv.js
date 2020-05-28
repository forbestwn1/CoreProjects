nosliwApplication.environments = {
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
		logging : {
			module : ["process", "requestInfo", "requestManager"],
		}
	},
	test : {
		nosliw_address : 'http://www.lastkilometer.ca',
		nosliw_port : 8082,
		nosliw_context : 'nosliw',
		library : [
			'libresources/external/log4javascript/1.0.0/log4javascript.js'
		],
		logging : {
			module : ["process", "requestInfo", "requestManager"],
		}
	},
};
