//get/create package
var packageObj = library.getChildPackage("loggingservice");    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var createLoggingService = function(){

	var loc_log;
	if(log4javascript!==undefined)  loc_log = log4javascript.getDefaultLogger();

	loc_logging = function(arguments){
		var out = "";
		for(var i in arguments){
			out = out + "  " + JSON.stringify(arguments[i]);
		}
		console.log(out);
	};
	
	loc_out = {
		trace : function(){
			if(loc_log!=undefined)			loc_log.trace.apply(loc_log, arguments);
			else   loc_logging(arguments[0]);
		},
		debug : function(){			
			if(loc_log!=undefined)			loc_log.debug.apply(loc_log, arguments);
			else   loc_logging(arguments[0]);
		},
		info : function(){			
			if(loc_log!=undefined)			loc_log.info.apply(loc_log, arguments);
			else   loc_logging(arguments[0]);
		},
		warn : function(){			
			if(loc_log!=undefined)			loc_log.warn.apply(loc_log, arguments);
			else   loc_logging(arguments[0]);
		},
		error : function(){			
			if(loc_log!=undefined)			loc_log.error.apply(loc_log, arguments);
			else   loc_logging(arguments[0]);
		},
		fatal : function(){			
			if(loc_log!=undefined)			loc_log.fatal.apply(loc_log, arguments);
			else   loc_logging(arguments[0]);
		},
	};
	
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createLoggingService", createLoggingService); 

})(packageObj);
