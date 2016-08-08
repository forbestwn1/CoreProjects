/**
 * 
 */
var nosliwLogging = function(){

	var loc_log = log4javascript.getDefaultLogger();
	
	loc_out = {
		trace : function(){			loc_log.trace.apply(loc_log, arguments);		},
		debug : function(){			loc_log.debug.apply(loc_log, arguments);		},
		info : function(){			loc_log.info.apply(loc_log, arguments);		},
		warn : function(){			loc_log.warn.apply(loc_log, arguments);		},
		error : function(){			loc_log.error.apply(loc_log, arguments);		},
		fatal : function(){			loc_log.fatal.apply(loc_log, arguments);		},
		
	};
	
	return loc_out;
}();

