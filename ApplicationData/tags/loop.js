{
	name : "test",
	description : "",
	attributes : [
		{
			name : "data"
		},
		{
			name : "element"
		},
		{
			name : "elename"
		}	
	],
	context: {
		inherit : true,
		public : {
			"<%=&(element)&%>" : {
				path : "<%=&(data)&%>.element"
			}		
		},
		private : {
			"internal_data": {
				path : "<%=&(data)&%>"
			}
		}
	},
	script : function(context, parentResourceView, uiTagResource, attributes, env){

		var node_namingConvensionUtility = nosliw.getNodeData("common.namingconvension.namingConvensionUtility");
		var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
		var node_createDataOperationRequest = nosliw.getNodeData("uidata.dataoperation.createDataOperationRequest");
		var node_DataOperationService = nosliw.getNodeData("uidata.dataoperation.DataOperationService");
		var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
		var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
		var node_createUIResourceViewFactory = nosliw.getNodeData("uiresource.createUIResourceViewFactory");
		var node_createContextElementInfo = nosliw.getNodeData("uidata.context.createContextElementInfo");
		var node_createContext = nosliw.getNodeData("uidata.context.createContext");
		var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");


		var loc_env = env;
		//container data variable
		var loc_dataVariable = env.createVariable("internal_data");
		//element data variable
		var loc_eleContextEleName = attributes.element;
		//element name
		var loc_eleNameContextEleName = attributes.elename;
		
		var loc_childResourceViews = {};
		
		var loc_id = 0;
		
		var loc_generateId = function(){
			loc_id++;
			return loc_id+"";
		};
		
		var loc_getElementContextVariable = function(key){
			var out = node_createContextVariable(loc_dataContextEleName);
			out.path = node_namingConvensionUtility.cascadePath(out.path, key+"");
			return out;
		};

		var loc_updateView = function(requestInfo){
			var index = 0;
			var request = loc_dataVariable.getWrapper().getHandleEachElementRequest(function(data, path){
				return node_createServiceRequestInfoSimple({}, function(){
					loc_addEle(data, index, path, requestInfo);
					index++;
				});
			});
			node_requestServiceProcessor.processRequest(request, false);
		};

		var loc_addEle = function(data, index, path, requestInfo){
			var eleContext = loc_env.createExtendedContext([
				loc_env.createContextElementInfo(loc_eleContextEleName, loc_dataVariable, path),
//				loc_env.createContextElementInfo(loc_eleNameContextEleName, index)
			], requestInfo);
			
			var resourceView = loc_env.createUIResourceViewWithId(loc_env.getId()+"."+loc_generateId(), eleContext, requestInfo);
	    	resourceView.insertAfter(loc_env.getStartElement());
				
	    	loc_childResourceViews[index] = resourceView;

/*			
			var that = this;
			eleContext.getContextElementVariable(loc_eleContextEleName).registerDataChangeEventListener(undefined, function(event, path, operationData, requestInfo){
				if(event=="EVENT_WRAPPER_DESTROY"){
					that.prv_deleteEle(key);
				}
			}, this);
*/			
		};
		
		var loc_out = 
		{
			
			prv_deleteEle : function(key, requestInfo){
				alert("ffff");
				var view = loc_childResourceViews[key];
				view.detachViews();
				delete loc_childResourceViews[key];
			},
			
			ovr_postInit : function(requestInfo){
				loc_updateView();
				var that = this;
				loc_dataVariable.registerDataChangeEventListener(undefined, function(event, dataOperation, requestInfo){
					if(event=="EVENT_WRAPPER_ADDELEMENT"){
						loc_addEle({
							key : dataOperation.index,
							value : dataOperation.value
						});
					}
					if(event=="WRAPPER_EVENT_DESTROY1"){
						that.prv_deleteEle(loc_getElementContextVariable(dataOperation.index));
					}
				}, this);
				
			},

			ovr_preInit : function(requestInfo){
			},
		
			ovr_initViews : function(startEle, endEle, requestInfo){
				loc_startEle = startEle;
				loc_endEle = endEle;
			},
			
			
		};
		
		return loc_out;
	}

}