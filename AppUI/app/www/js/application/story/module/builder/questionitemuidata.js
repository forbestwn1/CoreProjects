/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_storyChangeUtility;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoCommon;
	var node_createStoryService;
	var node_requestServiceProcessor;
	var node_storyUtility;
	var node_storyUIUtility;
	var node_designUtility;
	var node_CONSTANT;
	var node_createData;
	var node_createContextElementInfo;
	var node_createContext;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionItemUIData = function(){

	var loc_storyService = node_createStoryService();

	var loc_setCurrentByIndex = function(that, index){
		that.current = index;
		var initSelect = undefined;
		var currentTagInfo = that.tagInfos[that.current];
		if(currentTagInfo!=null)    initSelect = currentTagInfo.id;
		that.initSelect = initSelect;
	};
	
	var loc_setCurrentByTagId = function(that, index){
		
	};
	
	var loc_vueComponent = {
		data : function(){
			return {
				tagInfos : [],
				current : -1,
				initSelect : undefined,
				popSelected : undefined
			};
		},
		props : ['question', 'story'],
		components : {
		},
		methods : {
			selectUI : function(event){
				
			},
			onSelectChange : function(selected){
				this.popSelected = selected;
			},
			onSelectTag : function(){
				loc_setCurrentByTagId(this, this.popSelected);
			},
			onCancelSelectTag : function(){
				this.popSelected = undefined;
			},
			
		},
		computed: {
			currentTagInfo : {
				get : function(){
					if(this.current==-1)  return;
					return this.tagInfos[this.current];
				}
			}
		},
		mounted: function () {
			var that = this;
			var request = node_createServiceRequestInfoSequence(undefined, {
				success : function(request, uiTag){
					
				}
			});
			var element = node_storyUtility.getQuestionTargetElement(this.story, this.question);
			request.addRequest(loc_storyService.getQueryUITagRequest(element[node_COMMONATRIBUTECONSTANT.STORYNODEUIDATA_DATAINFO][node_COMMONATRIBUTECONSTANT.UIDATAINFO_DATATYPE], {
				success : function(request, queryResultSet){
					that.tagInfos.splice(0, that.tagInfos.length);
					_.each(queryResultSet[node_COMMONATRIBUTECONSTANT.UITAGQUERYRESULTSET_ITEMS], function(item, i){
						that.tagInfos.push(item[node_COMMONATRIBUTECONSTANT.UITAGQUERYRESULT_UITAGINFO]);
					});
					loc_setCurrentByIndex(that, 0);
				}
			}));
			node_requestServiceProcessor.processRequest(request);
		},	
		template : `
			<div>
				{{question.question}}:
				dataTag: <uitag_data v-bind:uitaginfo="currentTagInfo"/>
				<a v-on:click="selectUI">Select</a>
				<a class="button popup-open" href="#" data-popup=".popup-about">Open About Popup</a>

				<div class="popup popup-about">
				    <div class="block">
				    	<uitag_select v-bind:uitaginfolist='this.tagInfos' v-bind:initselect='this.initSelect' v-selectChange="onSelectChange"/>
				      <p>About</p>
				      <!-- Close Popup -->
				      <p><a class="link popup-close" href="#" v-on:click.prevent="onSelectTag">Ok</a></p>
				      <p><a class="link popup-close" href="#" v-on:click.prevent="onCancelSelectTag">Close</a></p>
				      <p>Lorem ipsum dolor sit amet...</p>
				    </div>
				  </div>
			
			</div>
		`
	};
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyChangeUtility", function(){node_storyChangeUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){	node_createServiceRequestInfoCommon = this.getData();	});
nosliw.registerSetNodeDataEvent("application.instance.story.service.createStoryService", function(){node_createStoryService = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUIUtility", function(){node_storyUIUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.designUtility", function(){node_designUtility = this.getData();});

nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.entity.createData", function(){node_createData = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItemUIData", node_createComponentQuestionItemUIData); 

})(packageObj);
