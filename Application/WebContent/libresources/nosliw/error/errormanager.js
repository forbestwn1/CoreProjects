//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_requestServiceProcessor;
//*******************************************   Start Node Definition  ************************************** 	

var node_createErrorManager = function(){
	
	
	var loc_init = function(){
	};
	
	var loc_addErrorToStorage = function(error){
		return error;  //kkkk
		
		if(typeof localStorage !== 'undefined'){
			var errorData = loc_getErrorFromStorage();
			if(errorData==undefined) errorData = [];

			errorData.push({
				time : new Date(),
				error : error,
			});
			localStorage.errorData = JSON.stringify(errorData);
			return errorData;
		}
	};
	
	var loc_buildErrorEle = function(error){
//		alert("kkk start process error");
		try{
			var stackStr = JSON.stringify(error.stack);
		}
		catch(e){
//			alert("kkkkkkkkkk catch"+stackStr);
		}

//		alert("kkk after catch error"+stackStr);

		var out = {
			error : stackStr,
//			line : error.lineNumber
		};
//		alert("kkk before exit process error");

		return stackStr;
	};
	
	var loc_clearErrorInStorage = function(){
		if(typeof localStorage !== 'undefined') localStorage.removeItem("errorData");
	};
	
	var loc_getErrorFromStorage = function(){
		if(typeof localStorage !== 'undefined'){
			var errorData;
			var errorDataStr = localStorage.errorData;
			if(errorDataStr!=undefined)  	errorData = JSON.parse(errorDataStr);
			return errorData;
		}
	};
	
	
	var kkkkk = 0;
	
	var loc_logError = function(errorData){
		//gateway request
		alert("kkk log error "+errorData);
		
		if(kkkkk==0){
			var el = document.createElement('textarea');
			  el.value = errorData;
			  document.body.appendChild(el);
			  el.select();
			  document.execCommand('copy');
			  document.body.removeChild(el);
			  kkkkk++;
		}
		
		
		
		var gatewayId = node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_ERRORLOG;
		var command = node_COMMONATRIBUTECONSTANT.GATEWAYERRORLOGGER_COMMAND_LOGERRRO;
		var parms = {};
		parms[node_COMMONATRIBUTECONSTANT.GATEWAYERRORLOGGER_PARMS_ERROR] = errorData;
		var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms, {
			success : function(request, data){
				loc_clearErrorInStorage();
			},
			error : function(request, serviceData){
			},
			exception : function(request, serviceData){
			},
		});
		node_requestServiceProcessor.processRequest(gatewayRequest);
	};
/*
	window.onerror = function(msg, url, line, col, error) {
		   // Note that col & error are new to the HTML 5 spec and may not be 
		   // supported in every browser.  It worked for me in Chrome.
		   var extra = !col ? '' : '\ncolumn: ' + col;
		   extra += !error ? '' : '\nerror: ' + error;

		   // You can view the information in an alert to see things working like this:
		   var error = "Error: " + msg + "\nurl: " + url + "\nline: " + line + extra;
//		   alert(error);

		   // TODO: Report this error via ajax so you can keep track
		   //       of what pages have JS issues

		   var suppressErrorAlert = true;
		   // If you return true, then error alerts (like in older versions of 
		   // Internet Explorer) will be suppressed.
			var errorData = loc_addErrorToStorage(loc_buildErrorEle(error));
			loc_logError(errorData);

		   return suppressErrorAlert;
		};
*/
	
	var loc_out = {
		logError : function(error){
			if (typeof console != "undefined") console.log(error);
			var errorData = loc_addErrorToStorage(loc_buildErrorEle(error));
			loc_logError(errorData);
		},
		logErrorIfHasAny : function(){
			var errorData = loc_getErrorFromStorage();
			if(errorData!=undefined){
				//if have previous error, then log it
				loc_logError(errorData);
			}
		}
	};
	
	loc_init();
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});

//Register Node by Name
packageObj.createChildNode("createErrorManager", node_createErrorManager); 

})(packageObj);
