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
	var node_createServiceRequestInfoSequence;
	var node_contextUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIDecorationsRequest = function(resourceIds, handlers, request){
	var out = node_createServiceRequestInfoSequence(handlers, request);
	var decs = [];
	if(resourceIds!=undefined){
		var decsRequest = node_createServiceRequestInfoSequence();
		_.each(resourceIds, function(resourceId, index){
			decsRequest.addRequest(nosliw.runtime.getUIPageService().getCreateUIPageRequest(resourceId, undefined, {
				success :function(requestInfo, page){
					var decoration = loc_createDecoration(page.getUIView());
					decs.push(decoration);
				}
			}));
		});
		out.addRequest(decsRequest);
	}
	out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
		return decs;
	}));
	
	return out;
};

var loc_createDecoration = function(uiView){
	var loc_eventSource = node_createEventObject();
	var loc_eventListenerForDec = node_createEventObject();
	var loc_eventListenerForParent = node_createEventObject();

	var loc_parent;
	
	var loc_uiView = uiView;

	loc_uiView.registerEventListener(loc_eventListenerForDec, function(event, eventData, requestInfo){
		loc_eventSource.triggerEvent(event, eventData, requestInfo);
	});

	var loc_out = {
		
		setParent : function(parent){
			loc_parent = parent;
			
			//insert parent view into decoration view
			var placeHolderAttrValue = loc_uiView.getAttribute(node_COMMONCONSTANT[NOSLIW_RESERVE_ATTRIBUTE_PLACEHOLDER]);
			var segs = placeHolderAttrValue.split(":");
			loc_parent.appendTo(loc_uiView.getElementByAttributeValue(segs[0], segs[1]));

			//listener to event from parent
			loc_parent.registerEventListener(loc_eventListenerForParent, function(event, eventData, requestInfo){
				//process by decoration first
				var result = loc_uiView.command(node_COMMONCONSTANT[DECORATION_COMMAND_EVENTPROCESS], {}, requestInfo);
				if(result===false)  return false;   //if return false, then no pop up the event
				else loc_eventSource.triggerEvent(event, eventData, requestInfo);  //otherwise, pop up the event
			});
		},	
		
		appendTo : function(ele){	loc_uiView.appendTo(ele);	},
		insertAfter : function(ele){	loc_uiView.insertAfter(ele);	},
		detachViews : function(){	loc_uiView.detachViews();  },
		
		getUpdateContextRequest : function(parms, handlers, requestInfo){
/*			
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
			var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
			if(parmsForDecSum>0) out.addRequest(loc_uiView.getUpdateContextRequest(parmsForDec));
			else if(parmsUpdatedSum>0)  out.addRequest(loc_parent.getUpdateContextRequest(parmsUpdated));
*/
			
			var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
			out.addRequest(loc_uiView.getUpdateContextRequest(parms));
			out.addRequest(loc_parent.getUpdateContextRequest(parms));
			return out;
		},
		
		command : function(command, parms, requestInfo){
			//decoration process command first
			var result = loc_uiView.command(node_COMMONCONSTANT[DECORATION_COMMAND_COMMANDPROCESS], 
				{
					commnad : command,
					parms : parms
				}, requestInfo);
			if(result===false)  return false;   //if command return false, then no pass to parent 
			else return loc_parent.command(command, parms, requestInfo);   //otherwise, let parent process this command
		},
		getContext1 : function(){
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){node_createServiceRequestInfoSequence = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIDecorationsRequest", node_createUIDecorationsRequest); 

})(packageObj);
