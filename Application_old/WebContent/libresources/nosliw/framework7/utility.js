//get/create package
var packageObj = library;    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

// indicator ui to indicate the start of request and end of request
var node_utility = function(){
	
	var loc_out = {
		
		createTypicalFramework7App : function(root){
			var out = new Framework7({
				  // App root element
				  root: root,
				  name: 'My App',
				  id: 'com.myapp.test',
				  panel: {
					  swipe: 'both',
				  },		
			});
			return out;
		},
		
		createRequestStatusIndicatorUI : function(framework7App){
			var requestProcessor = nosliw.runtime.getRequestProcessor();
			requestProcessor.registerEventListener(undefined, function(eventName, eventData){
				if(eventName==node_CONSTANT.EVENT_REQUESTPROCESS_START){
					framework7App.preloader.show();
				}
				else if(eventName==node_CONSTANT.EVENT_REQUESTPROCESS_DONE){
					framework7App.preloader.hide();
					
					if(eventData.result.type==node_CONSTANT.REQUEST_FINISHTYPE_ERROR || eventData.result.type==node_CONSTANT.REQUEST_FINISHTYPE_EXCEPTION){
						var toastBottom = framework7App.toast.create({
						  text: eventData.result.data.message,
						  closeTimeout: 2000,
						});
						toastBottom.open();
					}
				}
			});
		}
			
	};
	return loc_out;
}();
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
