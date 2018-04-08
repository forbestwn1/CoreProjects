//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_resourceUtility;
	var node_buildServiceProvider;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_createUIResourceViewFactory;
	var node_createContextElementInfo;
	var node_dataUtility;
	var node_createContext;
	var node_createContextVariableInfo;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = {

		/*
		 * create place holder html with special ui id 
		 */
		createPlaceHolderWithId : function(id){
			return "<nosliw style=\"display:none;\" nosliwid=\"" + id + "\"></nosliw>";
		},
		
		/*
		 * build context
		 * 		1. read context information for this resource from uiResource
		 * 		2. add extra element infos
		 */
		buildUIResourceContext : function(uiResource, contextElementInfoArray){
			var contextElementsInf = [];
			
			//get element info from resource definition
			var resourceAttrs = uiResource[node_COMMONATRIBUTECONSTANT.ATTR_UIRESOURCE_ATTRIBUTES];
			if(resourceAttrs!=undefined){
				var contextStr = resourceAttrs.contexts;
				var contextSegs = nosliwCreateSegmentParser(contextStr, node_COMMONCONSTANT.SEPERATOR_ELEMENT);
				while(contextSegs.hasNext()){
					var name = undefined;
					var element = undefined;
					var contextSeg = contextSegs.next();
					var index = contextSeg.indexOf("@");
					if(index==-1){
						name = contextSeg;
						info = {};
					}
					else{
						name = contextSeg.substring(0, index);
						var type = contextSeg.substring(index+1);
						info = {wrapperType:type};
					}
					contextElementsInf.push(nosliwCreateContextElementInfo(name, info));
				}
			}

			//add extra element info
			_.each(contextElementInfoArray, function(contextElementInfo, key){
				contextElementsInf.push(contextElementInfo);
			}, this);
			
			return nosliwCreateContext(contextElementsInf);
		},
		
		/*
		 * update all the ui id within html by adding space name ahead of them
		 */
		updateHtmlUIId : function(html, idNameSpace){
			var find = node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"=\"";
			return html.replace(new RegExp(find, 'g'), node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"=\""+idNameSpace+node_COMMONCONSTANT.SEPERATOR_FULLNAME);
		},
		
		createTagResourceId : function(name){
			var out = {};
			out[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = name; 
			out[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UITAG; 
			return out;
		},
		
		getContextTypes : function(){
			return [ node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PUBLIC, node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_INTERNAL, node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PRIVATE ];
		},
		
		//build context according to context definition and parent context
		buildContext : function(contextGroupDef, parentContext){
			//build context element first
			var contextElementInfosArray = [];
			
			_.each(this.getContextTypes(), function(contextType, index){
				var contextDef = contextGroupDef[contextType];
				
				_.each(contextDef, function(contextDefRootObj, eleName, list){
					var info = {
						contextType : contextType,
						matchers : contextDefRootObj[node_COMMONATRIBUTECONSTANT.CONTEXTNODE_MATCHERS],
						reverseMatchers : contextDefRootObj[node_COMMONATRIBUTECONSTANT.CONTEXTNODE_REVERSEMATCHERS]
					};
					var type = contextDefRootObj[node_COMMONATRIBUTECONSTANT.CONTEXTNODEROOT_TYPE];
					if(type==node_COMMONCONSTANT.UIRESOURCE_ROOTTYPE_ABSOLUTE){
						var defaultValue = contextDefRootObj[node_COMMONATRIBUTECONSTANT.CONTEXTNODE_DEFAULT];
						if(contextDefRootObj[node_COMMONATRIBUTECONSTANT.CONTEXTNODE_DEFINITION]!=undefined){
							//app data
							var defaultValueData = defaultValue;
							if(defaultValueData!=undefined){
								defaultValueData = node_dataUtility.createDataOfAppData(defaultValue);
							}
							contextElementInfosArray.push(node_createContextElementInfo(eleName, defaultValueData, "", undefined, info));
						}
						else{
							//object
							contextElementInfosArray.push(node_createContextElementInfo(eleName, defaultValue, "", undefined, info));
						}
					}
					else if(type==node_COMMONCONSTANT.UIRESOURCE_ROOTTYPE_RELATIVE){
						var pathObj = contextDefRootObj[node_COMMONATRIBUTECONSTANT.CONTEXTNODE_PATH];
						var rootName = pathObj[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_ROOTNAME];
						var path = pathObj[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_PATH];
						contextElementInfosArray.push(node_createContextElementInfo(eleName, parentContext, node_createContextVariableInfo(rootName, path), undefined, info));
					}
				});
			});
			
			var context = node_createContext(contextElementInfosArray);
			return context;
		}
		
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uiresource.createUIResourceViewFactory", function(){node_createUIResourceViewFactory = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextVariableInfo", function(){node_createContextVariableInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
