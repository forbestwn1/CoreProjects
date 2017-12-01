//get/create package
var packageObj = library.getChildPackage("loggingservice");    

(function(packageObj){
	//get used node
	var node_runtimeName;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_createLoggingService = function(){
	var loc_logging;

	var loc_buildMessage = function(arguments){
		var out = "";
		for(var i in arguments){
			out = out + " " + arguments[i];
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
			if(node_runtimeName=="rhino"){
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
	
	var loc_processArguments = function(args){
		var out = [];
		for(var i in args){
			try{
				out.push(JSON.stringify(args[i]==undefined?"undefined":args[i]));
			}
			catch(err){
			}
		}
		return out;
	}
	
	loc_out = {
		trace : function(){
			loc_getLogging().trace.apply(loc_logging, loc_processArguments(arguments));
		},
		debug : function(){			
			loc_getLogging().debug.apply(loc_logging, loc_processArguments(arguments));
		},
		info : function(){			
			loc_getLogging().info.apply(loc_logging, loc_processArguments(arguments));
		},
		warn : function(){			
			loc_getLogging().warn.apply(loc_logging, loc_processArguments(arguments));
		},
		error : function(){			
			loc_getLogging().error.apply(loc_logging, loc_processArguments(arguments));
		},
		fatal : function(){			
			loc_getLogging().fatal.apply(loc_logging, loc_processArguments(arguments));
		},
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("runtime.name", function(){node_runtimeName = this.getData();});

//Register Node by Name
packageObj.createChildNode("createLoggingService", node_createLoggingService); 

})(packageObj);
