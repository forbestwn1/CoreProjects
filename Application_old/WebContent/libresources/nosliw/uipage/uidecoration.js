//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_createEventObject;
	var node_makeObjectWithLifecycle;
	var node_destroyUtil;
	var node_makeObjectWithType;
	var node_getLifecycleInterface;
	var node_requestServiceProcessor;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_contextUtility;
	var node_getObjectType;
	var node_UICommonUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIDecorationRequest = function(decorationId, configureValue, handlers, request){
	var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
	out.addRequest(nosliw.runtime.getUIPageService().getCreateUIPageRequest(decorationId, undefined, {
		success :function(requestInfo, page){
			var decoration = loc_createDecoration(page.getUIView(), page.getStyle());
			if(configureValue==undefined)  return decoration; 
			//update decoration with configure
			return decoration.getUpdateContextByNameRequest(configureValue, {
				success : function(request){
					return decoration;
				}
			});
		}
	}));
	return out;
};

var node_createUIDecorationsRequest = function(decorationIds, handlers, request){
	var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
	var decs = [];
	if(decorationIds!=undefined){
		var decsRequest = node_createServiceRequestInfoSequence();
		_.each(decorationIds, function(decorationId, index){
			decsRequest.addRequest(nosliw.runtime.getUIPageService().getCreateUIPageRequest(decorationId, undefined, {
				success :function(requestInfo, page){
					var decoration = loc_createDecoration(page.getUIView(), page.getStyle());
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

var loc_createDecoration = function(uiView, style){
	var loc_eventSource = node_createEventObject();
	var loc_eventListenerForDec = node_createEventObject();
	var loc_eventListenerForParent = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListenerForDec = node_createEventObject();
	var loc_valueChangeEventListenerForParent = node_createEventObject();
	
	var loc_childUI;
	var loc_childView;
	
	var loc_uiView = uiView;
	var loc_style = style;
	var loc_id = loc_uiView.getId();

	//regular event
	loc_uiView.registerEventListener(loc_eventListenerForDec, function(event, eventData, requestInfo){
		loc_eventSource.triggerEvent(event, eventData, requestInfo);
	});

	//value change event
	loc_uiView.registerValueChangeEventListener(loc_valueChangeEventListenerForDec, function(event, eventData, requestInfo){
		loc_valueChangeEventSource.triggerEvent(event, eventData, requestInfo);
	});

	var loc_getPlaceHolderView = function(){
		var placeHolderAttrValue = loc_uiView.getAttribute(node_COMMONCONSTANT.NOSLIW_RESERVE_ATTRIBUTE_PLACEHOLDER);
		var segs = placeHolderAttrValue.split(":");
		return loc_uiView.getElementByAttributeValue(segs[0], segs[1]);
	};
	
	var loc_out = {
		
		getId : function(){   return loc_id;   },
			
		setChild : function(child){
			
			var childDataType = node_getObjectType(child);
			if(childDataType==node_CONSTANT.TYPEDOBJECT_TYPE_UIVIEW){
				loc_childUI = child;
				
				//insert parent view into decoration view
				loc_childUI.appendTo(loc_getPlaceHolderView());

				//listener to event from parent
				loc_childUI.registerEventListener(loc_eventListenerForParent, function(event, eventData, requestInfo){
					//process by decoration first
					var result = loc_uiView.command(node_COMMONCONSTANT.DECORATION_COMMAND_EVENTPROCESS, {}, requestInfo);
					if(result===false)  return false;   //if return false, then no pop up the event
					else loc_eventSource.triggerEvent(event, eventData, requestInfo);  //otherwise, pop up the event
				});

				//value change event, just pop up
				loc_childUI.registerValueChangeEventListener(loc_valueChangeEventListenerForParent, function(event, eventData, requestInfo){
					loc_valueChangeEventSource.triggerEvent(event, eventData, requestInfo);  
				});
			}
			else{
				loc_childView = $(child);
				loc_childView.appendTo(loc_getPlaceHolderView());
			}
		},	
		
		appendTo : function(ele){	loc_uiView.appendTo(ele);	},
		insertAfter : function(ele){	loc_uiView.insertAfter(ele);	},
		detachViews : function(){	loc_uiView.detachViews();  },
		getPlaceHolderView : function(){    return loc_getPlaceHolderView();  },
		
		attachStyle : function(){  node_UICommonUtility.attachStyle(loc_style, loc_id);      },
		detachStyle : function(){    node_UICommonUtility.detachStyle(loc_id);     },
		
		getContextElements : function(){
			var out = {};
			out = _.extend(out, loc_uiView.getContext().prv_elements, loc_childUI==undefined?undefined:loc_childUI.getContextElements());
			return out;
		},
		
		getUpdateContextRequest : function(parms, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
			out.addRequest(loc_uiView.getUpdateContextRequest(parms));
			if(loc_childUI!=undefined) out.addRequest(loc_childUI.getUpdateContextRequest(parms));
			return out;
		},
		
		getUpdateContextByNameRequest : function(parms, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
			out.addRequest(loc_uiView.getUpdateContextByNameRequest(parms));
			if(loc_childUI!=undefined) out.addRequest(loc_childUI.getUpdateContextByNameRequest(parms));
			return out;
		},

		command : function(command, parms, requestInfo){
			//decoration process command first
			var result = loc_uiView.command(node_COMMONCONSTANT.DECORATION_COMMAND_COMMANDPROCESS, 
				{
					commnad : command,
					parms : parms
				}, requestInfo);
			if(result===false)  return false;   //if command return false, then no pass to parent 
			else{
				if(loc_childUI!=undefined){
					return loc_childUI.command(command, parms, requestInfo);   //otherwise, let parent process this command
				}
			}
		},
		
		registerEventListener : function(listener, handler, thisContext){	return loc_eventSource.registerListener(undefined, listener, handler, thisContext);},
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener);},

		registerValueChangeEventListener : function(listener, handler, thisContext){	return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext);},
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener);},
		
		destroy : function(request){
			loc_eventSource.clearup();
			loc_eventListenerForDec.clearup();
			loc_eventListenerForParent.clearup();

			loc_valueChangeEventSource.clearup();
			loc_valueChangeEventListenerForDec.clearup();
			loc_valueChangeEventListenerForParent.clearup();
			
			node_destroyUtil(loc_uiView);
		}
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.destroyUtil", function(){node_destroyUtil = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){node_createServiceRequestInfoSequence = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("uicommon.utility", function(){node_UICommonUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIDecorationsRequest", node_createUIDecorationsRequest); 
packageObj.createChildNode("createUIDecorationRequest", node_createUIDecorationRequest); 

})(packageObj);
