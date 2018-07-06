nosliw.getUITagManager().registerUITag("include", 
function(){
	var loc_out = {
			tag_preInit : function(){
				var that = this;
				
				this.tag_processExtraContext();				
				
				var resourceName = this.getAttribute('include');
				var uiResource = NosliwUIResourceManager.getUIResource(resourceName);
				this.m_processBodyResourceView(uiResource);
				
				this.bodyResourceView.on('all', function(eventName, data){
					var rootResourceView = that.m_getRootResourceView();
					rootResourceView.trigueEvent(eventName, data);
				});
				
			},

			tag_initViews : function(){
				return this.bodyResourceView.getViews();
			},

			tag_updateContext : function(){
				var contextNew = cloneContext(this.context);
				
				for (var name in this.m_extraContextConfigures) {
				    if (this.m_extraContextConfigures.hasOwnProperty(name)) {
				    	var path = this.m_extraContextConfigures[name];
				    	var c = createSubContextElement(this.context, path);
				    	contextNew[name] = c;
				    }
				}				
				this.bodyResourceView.setContext(contextNew);
			},
			
			prv_processExtraContext : function(){
				var value = this.getAttribute("context");
				if(value!=undefined){
					var contexts = value.split(';');
					for(var i in contexts){
						var context = contexts[i];
						var contextInfos = context.split('@');
						var contextName = contextInfos[0];
						var contextPath = contextInfos[1];
						var p = contextPath.split('.');
						this.m_extraContextConfigures[contextName] = {
							name : p[0],
							path : p[1],
						};
					}
				}
			},
		
	};

	return loc_out;
}
);	


var m_createIncludeUITag = function(id, uiTag, uiResourceView){
		
		
		var m_uiTag = new NosliwUITag(id, uiTag, uiResourceView, 
		{
			m_extraContextConfigures : {},

			tag_preInit : function(){
				var that = this;
				
				this.tag_processExtraContext();				
				
				var resourceName = this.getAttribute('include');
				var uiResource = NosliwUIResourceManager.getUIResource(resourceName);
				this.m_processBodyResourceView(uiResource);
				
				this.bodyResourceView.on('all', function(eventName, data){
					var rootResourceView = that.m_getRootResourceView();
					rootResourceView.trigueEvent(eventName, data);
				});
				
			},

			tag_initViews : function(){
				return this.bodyResourceView.getViews();
			},

			tag_updateContext : function(){
				var contextNew = cloneContext(this.context);
				
				for (var name in this.m_extraContextConfigures) {
				    if (this.m_extraContextConfigures.hasOwnProperty(name)) {
				    	var path = this.m_extraContextConfigures[name];
				    	var c = createSubContextElement(this.context, path);
				    	contextNew[name] = c;
				    }
				}				
				this.bodyResourceView.setContext(contextNew);
			},
			
			tag_processExtraContext : function(){
				var value = this.getAttribute("context");
				if(value!=undefined){
					var contexts = value.split(';');
					for(var i in contexts){
						var context = contexts[i];
						var contextInfos = context.split('@');
						var contextName = contextInfos[0];
						var contextPath = contextInfos[1];
						var p = contextPath.split('.');
						this.m_extraContextConfigures[contextName] = {
							name : p[0],
							path : p[1],
						};
					}
				}
			},
			
		});

		return m_uiTag;
	};
	
