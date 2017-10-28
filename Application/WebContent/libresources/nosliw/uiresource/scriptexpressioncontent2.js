//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
//*******************************************   Start Node Definition  ************************************** 	

	/*
	 * create expression content object
	 * type: 
	 * 		text, attribute, tagAttribute
	 */
	var node_createUIResourceExpressionContent = function(expressionContent, type, uiResourceView, requestInfo){
		
		//script expression definition
		var loc_scriptExpression = expressionContent[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_SCRIPTEXPRESSION];
		
		//parent resource view
		var loc_uiResourceView = uiResourceView;
		//ui id for content
		var loc_uiId = loc_uiResourceView.prv_getUpdateUIId(expressionContent[node_COMMONATRIBUTECONSTANT.UIEXPRESSIONCONTENT_UIID]);
		//element
		var loc_ele = loc_uiResourceView.prv_getLocalElementByUIId(loc_uiId);
		//type: text, attribute, tagAttribute
		var loc_type = type;

		var loc_expressionGroup = undefined;
		
		var loc_uiExpressionElements = undefined;
		
		var loc_listeners = [];
		
		//attribute, for tag attribute only
		var loc_attribute = expressionContent[node_COMMONATRIBUTECONSTANT.UIEXPRESSIONCONTENT_ATTRIBUTE];
		
		var loc_update = function(result){
			if(loc_type=="text"){
				loc_ele.text(result);
			}
			else if(loc_type=="attribute"){
				loc_ele.attr(loc_attribute, result); 
			}
			else if(loc_type=="tagAttribute"){
				var uiTag = loc_uiResourceView.prv_getTagByUIId(loc_uiId); 
				uiTag.setAttribute(loc_attribute, result);
			}
		};
		
		var lifecycleCallback = {};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(expressionContent, type, uiResourceView, requestInfo){
			loc_eventSource = nosliwCreateRequestEventSource();
			
			loc_uiExpressionElements = expressionContent[node_COMMONATRIBUTECONSTANT.UIEXPRESSIONCONTENT_UIEXPRESSIONELEMENTS];
			
			//create group which contain all expressions
			loc_expressionGroup = nosliwCreateRequestEventGroupHandler(
					function(requestInfo){
						var expressionGroupExecuteRequest = nosliwCreateRequestSet(new NosliwServiceInfo("expressionGroupExecute", {}), 
								{
									success : function(requestInfo, result){
										loc_update(result);
									}
								}, requestInfo);
						
						for(var eleIndex in loc_uiExpressionElements){
							var uiExpressionElement = loc_uiExpressionElements[eleIndex];
							if(!_.isString(uiExpressionElement)){
								var expressionUnitsArray = uiExpressionElement[node_COMMONATRIBUTECONSTANT.UIRESOURCEEXPRESSION_EXPRESSIONUNITS];
								for(var unitIndex in expressionUnitsArray){
									var element = loc_expressionGroup.getElement(""+eleIndex+"_"+unitIndex);
									expressionGroupExecuteRequest.addRequest(""+eleIndex+"_"+unitIndex, element.getRequestInfoExecuteExpression({}));
								}								
							}
							else{
								expressionGroupExecuteRequest.addRequest(""+eleIndex+"_"+unitIndex, nosliwCreateRequestSimple(new NosliwServiceInfo("", {data:uiExpressionElement}), function(request){
									return request.getService().parms.data;
								}, {}));
							}
						}
						
						expressionGroupExecuteRequest.setRequestProcessors({
							success : function(reqInfo, results){
								var out = "";
								for(var eleIndex in loc_uiExpressionElements){
									var uiExpressionElement = loc_uiExpressionElements[eleIndex];
									if(!_.isString(uiExpressionElement)){
										var expressionUnitsArray = uiExpressionElement[node_COMMONATRIBUTECONSTANT.UIRESOURCEEXPRESSION_EXPRESSIONUNITS];
										var expressionResults = [];
										for(var unitIndex in expressionUnitsArray){
											var result = results.getResult(""+eleIndex+"_"+unitIndex);
											if(result==undefined)  result = nosliwCreateData();
											expressionResults.push(result);
										}								
										var expressionFunName = uiExpressionElement[node_COMMONATRIBUTECONSTANT.UIRESOURCEEXPRESSION_EXPRESSIONID];
										out = out + loc_uiResourceView.prv_getScriptObject().executeUIExpression(expressionFunName, expressionResults);
									}
									else{
										out = out+results.getResult(""+eleIndex+"_"+unitIndex);
									}
								}
								
								return out;
							}, 
							error : function(){
								alert('error');
							},
							exception : function(){
								alert('exception');
							},
						});
						
						nosliw.getRequestServiceManager().processRequest(expressionGroupExecuteRequest);
					},
					function(expression, requestEventHandler){
						//add expression to group and register event handler
						var listener = expression.registerEvent(requestEventHandler);
						loc_listeners.push(listener);
						return listener;
					}, this);
			
			//create expression object and add them to group
			for(var p in loc_uiExpressionElements){
				var uiExpressionElement = loc_uiExpressionElements[p];
				if(!_.isString(uiExpressionElement)){
					var context = loc_uiResourceView.getContext();
					var expressionUnitsArray = uiExpressionElement[node_COMMONATRIBUTECONSTANT.UIRESOURCEEXPRESSION_EXPRESSIONUNITS];
					for(var i in expressionUnitsArray){
						var expression = expressionUnitsArray[i][node_COMMONATRIBUTECONSTANT.UIRESOURCEEXPRESSION_EXPRESSIONOBJECT];
						
						var contextVariablesArray = [];
						var contextVars = expressionUnitsArray[i][node_COMMONATRIBUTECONSTANT.UIRESOURCEEXPRESSION_CONTEXTVARIABLES];
						for(var j in contextVars){
							var contextVar = contextVars[j];
							if(context.getContextElement(contextVar.name)!=undefined){
								//valid context variable
								contextVariablesArray.push(nosliwCreateContextVariable(contextVar.name, contextVar.path));
							}
						}
						var expressionObj = nosliwCreateExpression(expression, contextVariablesArray, context);
						loc_expressionGroup.addElement(expressionObj, ""+p+"_"+i);
					}
				}
			}
			
			loc_expressionGroup.triggerEvent(requestInfo);
		};
			
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){
				loc_expressionGroup.destroy();
				loc_uiId = undefined;
				loc_ele = undefined;
				loc_uiExpression = undefined;
				loc_attribute = undefined;
				loc_context = undefined;
		};

		var loc_out = {
			ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},
			
		};

		//append resource and object life cycle method to out obj
		loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
		loc_out.init(expressionContent, type, uiResourceView, requestInfo);
		return loc_out;
	};

	//*******************************************   End Node Definition  ************************************** 	

	//populate dependency node data
	nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});


	//Register Node by Name
	packageObj.createChildNode("createUIResourceExpressionContent", node_createUIResourceExpressionContent); 

	})(packageObj);
