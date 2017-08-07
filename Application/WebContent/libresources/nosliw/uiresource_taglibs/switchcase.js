nosliw.getUITagManager().registerUITag("switch", 
function(){
	
	var loc_out = {
			prv_update : function(requestInfo){
				var switchVar = this.prv_getBodyResourceViewContext().getContextElementVariable("switch_variables");
				var value = this.getAttribute("evaluate");
				switchVar.requestDataOperation(new NosliwServiceInfo(NOSLIWCONSTANT.WRAPPER_OPERATION_SET, {path:"matched", data:false}), requestInfo);
				switchVar.requestDataOperation(new NosliwServiceInfo(NOSLIWCONSTANT.WRAPPER_OPERATION_SET, {path:"evaluate", data:value}), requestInfo);
			},

			ovr_preInit : function(requestInfo){
				this.prv_processBodyResourceView(undefined, undefined, requestInfo);
			},

			ovr_initViews : function(requestInfo){
				return this.prv_getBodyResourceView().getViews();
			},
			
			ovr_getContextElementInfoArray : function(data){
				var out = this.prv_getContextElementInfoArrayFromParent();
				out.push(nosliwCreateContextElementInfo("switch_variables", {}, nosliwCreateWraper({matched:false, evaluate:undefined})));
				return out;
			},
			
			ovr_processAttribute : function(name, value, requestInfo){
				if(name=='evaluate'){
					this.prv_update(requestInfo);
				}
			},

			ovr_postInit : function(requestInfo){
				this.prv_update(requestInfo);
			},
	};
	return loc_out;
}
);	

nosliw.getUITagManager().registerUITag("case", 
function(){
	var loc_out = {
			
			prv_isEqualValue : function(){
				var caseValue = this.getAttribute('value');
				var evalValue = this.prv_getDataBindingData('evaluate').value;
				if(caseValue==evalValue)  return true;
				else return false;
			},
			
			ovr_preInit : function(requestInfo){
				this.prv_addDataBinding("evaluate", nosliwCreateContextVariable('switch_variables', 'evaluate'), requestInfo);
				this.prv_addDataBinding("matched", nosliwCreateContextVariable('switch_variables', 'matched'), requestInfo);
			},

			prv_updateView : function(requestInfo){
				var bodyResourceView = this.prv_getBodyResourceView();
				if(this.prv_isEqualValue()){
					if(bodyResourceView==undefined){
						this.prv_processBodyResourceView(undefined, undefined, requestInfo);
					}
					this.prv_getBodyResourceView().insertAfter(this.prv_getStartElement());
				}
				else{
					if(bodyResourceView!=undefined)		bodyResourceView.destroy();
					this.bodyResourceView = this.prv_setBodyResourceView();
				}
			},

			ovr_handleDataEvent : function(name, event, path, data, requestInfo){
				if(name=="evaluate"){
					if(this.prv_isEqualValue()){
						var matchedVar = this.prv_getDataBinding('matched');
						matchedVar.requestDataOperation(new NosliwServiceInfo(NOSLIWCONSTANT.WRAPPER_OPERATION_SET, {path:"", data:true}), requestInfo);
					}
					this.prv_updateView(requestInfo);
				}
			},
	};
	return loc_out;
}
);	

nosliw.getUITagManager().registerUITag("default", 
function(){
	var loc_out = {
			prv_updateView : function(){
				if(this.prv_isMatched()){
					this.prv_getBodyResourceView().detachViews();
				}
				else{
					this.prv_getBodyResourceView().insertAfter(this.prv_getStartElement());
				}
			},

			ovr_preInit : function(requestInfo){
				this.prv_processBodyResourceView(undefined, undefined, requestInfo);
				this.prv_addDataBinding("matched", nosliwCreateContextVariable('switch_variables', 'matched'), requestInfo);
			},

			ovr_handleDataEvent : function(name, event, path, data, requestInfo){
				this.prv_updateView();
			},
			
			prv_isMatched : function(){
				var matched = this.prv_getDataBindingData('matched').value;
				if(matched==true)  return true;
				else return false;
			},
	};
	return loc_out;
}
);	

