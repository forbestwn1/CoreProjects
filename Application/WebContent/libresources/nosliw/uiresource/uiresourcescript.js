/**
 * this object is used to handle all the function related with uiResourceView 
 * two type of functions: 
 * 		normal function
 * 		function for expression 
 */
var nosliwCreateUIResourceScriptObject = function(scriptFacName, uiResourceView){
	
	var loc_uiResourceView = uiResourceView;
	var loc_scriptObject = nosliwScriptFactory[scriptFacName].call(this, uiResourceView); 
	
	
	var loc_out = {
			/*
			 * get function object by function name defined in script block
			 * yes: return function object
			 * no : return undefined
			 */
			prv_getLocalFunction : function(name){
				if(loc_scriptObject.script==undefined)  return undefined;
				return loc_scriptObject.script[name];
			},
			
			/*
			 * invoke function 
			 * if function cannot find locally, then try to find out at parent resource view
			 */
			prv_callFunction : function(funName){
				var out = undefined;
				var funObj = loc_getLocalFunction(funName);
				if(funObj==undefined){
					var parentResourceView = loc_uiResourceView.prv_getParentResourceView();
					if(parentResourceView!=undefined){
						out = parentResourceView.getScriptObject().prv_callFunction.apply(loc_parentResourveView, arguments);
					}
				}
				else{
					out = this.prv_callLocalFunction.apply(this, arguments);
				}
				return out;
			},
			
			/*
			 * invoke function defined within js block 
			 */
			prv_callLocalFunction : function(funName){
				var out = undefined;
				var funObj = this.prv_getLocalFunction(funName);
				if(funObj!=undefined){
					var parms = [];
					for (var i=1; i < arguments.length; i++) {
						parms.push(arguments[i]);
					}
					out = funObj.apply(loc_scriptObject.script, parms);
				}
				return out;
			},
			
			
			/*
			 * execute expression according to context value
			 */
			executeUIExpression : function(expressionFunName, expressionResultsArray){
				if(loc_scriptObject.uiexpression==undefined)  return undefined;
				return loc_scriptObject.uiexpression[expressionFunName].call(loc_uiResourceView, expressionResultsArray);
			},
			
			/*
			 * invoke function related with event
			 * if function cannot find locally, then try to find out at parent resource view
			 */
			callEventFunction : function(funName, data, info){
				var funObj = this.prv_getLocalFunction(funName);
				if(funObj!=undefined){
					//if function can be found locally
					this.prv_callLocalFunction(funName, data, info);
				}
				else{
					//if function cannot be found within current resource view, then try to find at parent resource view
					if(loc_parentResourveView!=undefined)		this.prv_parentResourveView.prv_getScriptObject().callEventFunction(funName, data, info);
				}
			},
	};
	
	return loc_out;
};
