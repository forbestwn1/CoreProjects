//get/create package
var packageObj = library.getChildPackage("ui");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

// indicator ui to indicate the start of request and end of request
var node_createRequestStatusIndicatorUI = function(framework7App){
	var loc_framework7App = framework7App;
	
	var loc_init = function(){
		var requestProcessor = nosliw.runtime.getRequestProcessor();
		requestProcessor.registerEventListener(undefined, function(eventName, eventData){
			if(eventName==node_CONSTANT.EVENT_REQUESTPROCESS_START){
				loc_framework7App.preloader.show();
			}
			else if(eventName==node_CONSTANT.EVENT_REQUESTPROCESS_DONE){
				loc_framework7App.preloader.hide();
				
				if(eventData.result.type==node_CONSTANT.REQUEST_FINISHTYPE_ERROR || eventData.result.type==node_CONSTANT.REQUEST_FINISHTYPE_EXCEPTION){
					var toastBottom = loc_framework7App.toast.create({
					  text: eventData.result.data.message,
					  closeTimeout: 2000,
					});
					toastBottom.open();
				}
			}
		});
	}
	
	var loc_out = {
			
	};
	
	loc_init();
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createRequestStatusIndicatorUI", node_createRequestStatusIndicatorUI); 

})(packageObj);
