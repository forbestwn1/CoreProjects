nosliwApplication.environments = {
	product_remote : {
		nosliw_address : 'http://www.lastkilometer.ca',
		nosliw_port : 8082,
		nosliw_context : 'nosliw',
		library : [],
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
	product_local : {
		nosliw_address : 'http://localhost',
		nosliw_port : 8082,
		nosliw_context : 'nosliw',
		library : [
		],
		logging : {
			module : ["process", "requestInfo", "requestManager"],
		}
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
};
