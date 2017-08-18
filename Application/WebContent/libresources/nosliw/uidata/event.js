//get/create package
var packageObj = library.getChildPackage("event");    

(function(packageObj){
//get used node
var node_getEventInterface;
	
//*******************************************   Start Node Definition  ************************************** 	
var node_createDataOperationEventObject = function(){
	
	var loc_out = {
			
		prv_getEventObj : function(){
			return node_getEventInterface(this);
		},
			
		/*
		 * listener : any other event object  
		 * handler : function(event, path, data, requestInfo)
		 */
		registerListener : function(event, listener, handler, thisContext){
			prv_getEventObj().registerListener(event, listener, function(event, data){
				handler.call(this, event, data.path, data.value, data.requestInfo);
			}, thisContext);
		},
		
		unregister : function(listener){
			prv_getEventObj().unregister(listener);
		},
		
		/*
		 * event : event name
		 * path : path on data
		 * data : operation value
		 * requestInfo : original request info
		 */
		triggerEvent : function(event, path, value, requestInfo){
			prv_getEventObj().triggerEvent(event, {
				path : nosliwCommonUtility.emptyStringIfUndefined(path),
				value : value,
				requestInfo : requestInfo,
			});		
		},
		
		clearup : function(){
			prv_getEventObj().clearup();
		},
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.event.getEventInterface", function(){node_getEventInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("createDataOperationEventObject", node_createDataOperationEventObject); 

})(packageObj);
