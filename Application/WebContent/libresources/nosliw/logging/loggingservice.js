//get/create package
var packageObj = library.getChildPackage("loggingservice");    

(function(packageObj){
	//get used node
	var node_NOSLIWCOMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_createLoggingService = function(){
	var loc_logging;

	var loc_buildMessage = function(arguments){
		var out = "";
		for(var i in arguments){
			out = out + " " + JSON.stringify(arguments[i]);
		}
		return out;
	}
	
	var loc_rhinoLogFun = function(){
		java.lang.System.out.println(loc_buildMessage(arguments));
	}

	var loc_rhinoErrorFun = function(){
		java.lang.System.err.println(loc_buildMessage(arguments));
	}
	
	var loc_getLogging = function(){
		if(loc_logging==undefined){
			var runtimeName = nosliw.runtimeName;
			if(runtimeName=="rhino"){
				loc_logging = {
					trace : loc_rhinoLogFun,
					debug : loc_rhinoLogFun,
					info : loc_rhinoLogFun,
					warn : loc_rhinoLogFun,
					error : loc_rhinoLogFun,
					fatal : loc_rhinoLogFun
				};
			}
			else{
				if (typeof log4javascript !== 'undefined') {
					loc_logging = log4javascript.getDefaultLogger();
				}
			}
		}
		return loc_logging;
	}
	
	loc_out = {
		trace : function(){
			loc_getLogging().trace.apply(loc_logging, arguments);
		},
		debug : function(){			
			loc_getLogging().debug.apply(loc_logging, arguments);
		},
		info : function(){			
			loc_getLogging().info.apply(loc_logging, arguments);
		},
		warn : function(){			
			loc_getLogging().warn.apply(loc_logging, arguments);
		},
		error : function(){			
			loc_getLogging().error.apply(loc_logging, arguments);
		},
		fatal : function(){			
			loc_getLogging().fatal.apply(loc_logging, arguments);
		},
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createLoggingService", node_createLoggingService); 

	var module = {
		start : function(packageObj){
			node_NOSLIWCOMMONCONSTANT = packageObj.getNodeData("constant.NOSLIWCOMMONCONSTANT");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
