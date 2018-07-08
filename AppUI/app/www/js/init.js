/**
 * 
 */
var init = function(baseServer, callBackFunction){
	
	var libs = [
        "libresources/external/Underscore/1.6.0/underscore.js",
        "libresources/external/Backbone/1.1.2/backbone.js",
        "libresources/external/log4javascript/1.0.0/log4javascript.js",
        "libresources/external/handlebars/handlebars-v4.0.11.js",
        "libresources/nosliw/runtimebrowserinit/init.js"
	];
	
	var count = 0;
	var fileNumber = libs.length;

	var loadScriptInOrder = function(){
		var url = libs[count];
		
		var script = document.createElement('script');
		script.setAttribute('src', baseServer+url);
		script.setAttribute('defer', "defer");
		script.setAttribute('type', 'text/javascript');

		script.onload = callBack;
		document.getElementsByTagName("head")[0].appendChild(script);
	};
	
	var callBack = function(){
		count++;
		if(count==fileNumber){
			nosliw.init(baseServer);
			if(callBackFunction!=undefined)		callBackFunction.call();
		}
		else{
			loadScriptInOrder();
		}
	};

	loadScriptInOrder();
};



