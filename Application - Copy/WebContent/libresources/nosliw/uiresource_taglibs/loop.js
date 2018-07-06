nosliw.getUITagManager().registerUITag("loop", 
function(){
	var loc_childResourceViews = {};
	var loc_elementName = undefined;
	var loc_keyName = undefined;
	
	var loc_out = {
			prv_addEle : function(dataWrapper, key, requestInfo){
		    	var eleResourceView = this.prv_createUIResourceView(this.prv_getParentResourceView().getIdNameSpace()+"."+key, {eleWrpper:dataWrapper, key:key}); 
		    	eleResourceView.key = key;
		    	loc_childResourceViews[key] = eleResourceView;
		    	eleResourceView.insertAfter(this.prv_getStartElement());
		    	var key1 = key;
		    	
		    	var eleVariable = eleResourceView.getContext().getContextElementVariable(loc_elementName);
		    	eleVariable.registerDataChangeEvent(function(event, path, data, requestInfo){
					if(event==NOSLIWCONSTANT.WRAPPER_EVENT_DESTROY){
						var view = loc_childResourceViews[key1];
						view.destroy();
						delete loc_childResourceViews[key1];
					}
				}, this);
			},
			
			ovr_preInit : function(requestInfo){
				loc_elementName = this.prv_getAttribute("data", "element");
				loc_keyName = this.prv_getAttribute("key", "key");
			},

			ovr_getContextElementInfoArray : function(data){
				var out = this.prv_getContextElementInfoArrayFromParent();
				out.push(nosliwCreateContextElementInfo(loc_elementName, {}, data.eleWrpper));
				out.push(nosliwCreateContextElementInfo(loc_keyName, {}, nosliwCreateWraper(data.key)));
				return out;
			},
			
			prv_updateView : function(){
				var that = this;
				var dataBinding = this.prv_getDataBinding("data");
				dataBinding.handleEachElement(function(dataWrapper, key){
					that.prv_addEle(dataWrapper, key);
				}, this);
			},

			ovr_postInit : function(requestInfo){
				var that = this;
				this.prv_updateView();
				var dataBinding = this.prv_getDataBinding("data");
				dataBinding.registerLifecycleEvent(function(){
					that.prv_updateView();
				}, that);
				
				dataBinding.registerDataChangeEvent(function(event, path, data, requestInfo){
					if(event==NOSLIWCONSTANT.WRAPPER_EVENT_ADDELEMENT){
						var dataWrapper = dataBinding.getWrapper();
						var eleWrapper = dataWrapper.createChildWrapper(data.index);
						that.prv_addEle(eleWrapper, data.index, requestInfo);
					}
				});
			},
	};
	return loc_out;
}
);	


var m_createContainerUITag = function(id, uiTag, uiResourceView){
		
		var m_childResourceViews = {};
		
		var m_uiTag = new NosliwUITag(id, uiTag, uiResourceView, 
		{
			tag_clearOldContext : function(){
				for (var key in m_childResourceViews) {
				    if (m_childResourceViews.hasOwnProperty(key)) {
				    	m_removeEle(key);
				    }
				}
			},
			
			tag_updateContext : function(){
				var context1 = this.context;
				handleContextContainerEachElement(this.context, this.dataBindings.data, function(key, eleWraper, eleContext){
					m_addEle.call(this, key, eleWraper, eleContext);
				}, this);
			},

			tag_handleDataEvent : function(code, contextPath, eventData){
				if(code==EVENT_ELEMENT_ADD){
			    	var contextEle = createContextElement(eventData.data); 
					m_addEle.call(this, eventData.ID, eventData.data, contextEle);
				}
				else if(code==EVENT_ELEMENT_REMOVE){
					m_removeEle.call(this, eventData.ID);
				}
				else if(code==EVENT_DATA_CHANGE){
					handleContextContainerEachElement(this.context, this.dataBindings.data, function(key, eleWraper, eleContext){
						m_addEle.call(this, key, eleWraper, eleContext);
					}, this);
				}
			},
		});


		var m_addEle = function(key, dataWraper, eleContext){
			
	    	var eleResourceView = this.m_createUIResourceView(m_uiTag.parentResourceView.getIdNameSpace()+"."+key); 
//	    		createUIResourceView(m_uiTag.uiTag, m_uiTag.parentResourceView.getIdNameSpace()+"."+key, m_uiTag.parentResourceView);
	    	eleResourceView.key = key;
	    	m_childResourceViews[key] = eleResourceView;
	    	
	 		m_uiTag.startEle.after(eleResourceView.getViews());
	    	
	    	var eContext;
	    	
	 		var contextEleName = m_uiTag.getAttribute('context');
	 		var context1 = {};
	 		context1[contextEleName] = eleContext;

	 		context1['index'] = createContextElementObject(createObjectWraper(key+''));

	 		var otherContextInfosAttr = m_uiTag.getAttribute('othercontext');
	 		if(!isStringEmpty(otherContextInfosAttr)){
		 		var otherContextInfos = otherContextInfosAttr.split(";");
		 		for(var i in otherContextInfos){
		 			var infoSegs = otherContextInfos[i].split("@");
		 			var contextName = infoSegs[0];
		 			var contextPath = contextName;
		 			var clone = false;
		 			if(!isStringEmpty(infoSegs[1])){
		 				contextPath = infoSegs[1];
		 			}
		 			if(!isStringEmpty(infoSegs[2])){
		 				var type = infoSegs[2];
		 				if(type=="clone")  clone=true;
		 				else clone = false;
		 			}
			 		context1[contextName] = createSubContextElement(this.context, contextPath, clone);
		 		}
		 		eContext = context1;
	 		}
	 		else{
		 		eContext = mergeContext(this.context, context1);
	 		}
	 		eleResourceView.setContext(eContext);
	 		
			this.m_triggerEvent("addElement", {
				key : key,
				context : eContext,
			});
		};
		
		
		var m_removeEle = function(key){
			m_childResourceViews[key].destroy();
			delete m_childResourceViews[key];
		};

		return m_uiTag;
	};
	