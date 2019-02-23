//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_createEventObject;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_getLifecycleInterface;
	var node_requestServiceProcessor;
	var node_createServiceRequestInfoSimple;
	var node_contextUtility;
//*******************************************   Start Node Definition  ************************************** 	
/**
var node_createUIDecorationRequest = function(resourceId, ){
	
	var processUIRequest = node_createServiceRequestInfoSequence();
	processUIRequest.addRequest(nosliw.runtime.getUIPageService().getCreateUIPageRequest("Decoration_application", undefined, {
		success :function(requestInfo, page){
			var decoration = loc_createDecoration(page.getUIView());
			ui.addDecoration(decoration);
			ui.getPage().appendTo($('#testDiv'));
			
			var contextValue = {
				ui :{
					id : ui.getName(),
					title : ui.getName()
				}
			};
			return page.getUpdateContextRequest(contextValue);								
		}
	}));
	out.addRequest(processUIRequest);
	return loc_out;
};

var loc_createDecoration = function(uiView, appendId){
	var loc_eventSource = node_createEventObject();
	var loc_eventListenerForDec = node_createEventObject();
	var loc_eventListenerForParent = node_createEventObject();

	var loc_parent;
	
	var loc_uiView = uiView;

	loc_uiView.registerEventListener(loc_eventListenerForDec, function(event, eventData, requestInfo){
		loc_eventSource.triggerEvent(event, eventData, requestInfo);
	});

	var loc_out = {
		
		getName : function(){	return "HEADER___";	},
			
		setParent : function(parent){
			loc_parent = parent;
			loc_parent.appendTo(loc_uiView.getElementByAttributeValue('id', 'pleaseEmbed'));
			loc_parent.registerEventListener(loc_eventListenerForParent, function(event, eventData, requestInfo){
				loc_eventSource.triggerEvent(event, eventData, requestInfo);
			});
		},	
		appendTo : function(ele){
			loc_uiView.appendTo(ele);
		},
		insertAfter : function(ele){
			loc_uiView.insertAfter(ele);
		},
		detachViews : function(){
			
		},
		getUpdateContextRequest : function(parms, handlers, requestInfo){
			var parmsForDec = {};
			var parmsUpdated = {};
			var parmsForDecSum = 0;
			var parmsUpdatedSum = 0;
			_.each(parms, function(parm, name){
				if(name.startsWith(loc_out.getName())){
					parmsForDec[name.substring(loc_out.getName().length)] = parm;
					parmsForDecSum++;
				}
				else{
					parmsUpdated[name] = parm;
					parmsUpdatedSum++;
				}
			});
			out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
			if(parmsForDecSum>0) out.addRequest(loc_uiView.getUpdateContextRequest(parmsForDec));
			else if(parmsUpdatedSum>0)  out.addRequest(loc_parent.getUpdateContextRequest(parmsUpdated));
			return out;
		},
		command : function(command, parms, requestInfo){
			return loc_parent.command(command, parms, requestInfo);
		},
		getContext : function(){
			return loc_parent.getContext();
		},
		registerEventListener : function(listener, handler, thisContext){	return loc_eventSource.registerListener(undefined, listener, handler, thisContext);},
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener);},

	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIPage", node_createUIPage); 
*/
})(packageObj);
