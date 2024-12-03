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
	
	var loc_setCurrentByTagId = function(that, tagId){
		var index = -1;
		for(var i in that.tagInfos){
			if(that.tagInfos[i].id==tagId){
				index = i;
				break;
			}
		}
		loc_setCurrentByIndex(that, index);
	};
	
	var loc_vueComponent = {
		data : function(){
			return {
				tagInfos : [],
				dataInfo : {},
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
			onOpenPopup : function(){
				nosliwApplication.info.framework7.popup.open(this.$refs.popup);
			},
			onSelectChange : function(selected){
				this.popSelected = selected;
			},
			onSelectTag : function(){
				loc_setCurrentByTagId(this, this.popSelected);
				node_designUtility.applyPatchFromQuestion(this.story, this.question, node_COMMONATRIBUTECONSTANT.STORYNODEUITAG_TAGNAME, this.popSelected, this.question.answer);
				node_designUtility.applyPatchFromQuestion(this.story, this.question, node_COMMONATRIBUTECONSTANT.STORYNODEUITAGDATA_MATCHERS, this.currentTagInfo[node_COMMONATRIBUTECONSTANT.UITAGINFO_MATCHERS], this.question.answer);
				this.popSelected = undefined;
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
		created : function(){
			var element = node_storyUtility.getQuestionTargetElement(this.story, this.question);
			this.dataInfo = element[node_COMMONATRIBUTECONSTANT.STORYNODEUITAGDATA_DATAINFO][node_COMMONATRIBUTECONSTANT.UIDATAINFO_DATATYPE];
		},
		mounted: function () {
			var that = this;
			var request = node_createServiceRequestInfoSequence(undefined, {
				success : function(request, uiTag){
					
				}
			});
			var element = node_storyUtility.getQuestionTargetElement(this.story, this.question);
			request.addRequest(loc_storyService.getQueryUITagRequest(element[node_COMMONATRIBUTECONSTANT.STORYNODEUITAGDATA_DATAINFO][node_COMMONATRIBUTECONSTANT.UIDATAINFO_DATATYPE][node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_CRITERIA], {
				success : function(request, queryResultSet){
					var tagInfos = [];
					_.each(queryResultSet[node_COMMONATRIBUTECONSTANT.UITAGQUERYRESULTSET_ITEMS], function(item, i){
						var tagInfo = item[node_COMMONATRIBUTECONSTANT.UITAGQUERYRESULT_UITAGINFO];
						tagInfos.push(tagInfo);
					});
					that.tagInfos = tagInfos;
					
//					that.tagInfos.splice(0, that.tagInfos.length);
//					_.each(queryResultSet[node_COMMONATRIBUTECONSTANT.UITAGQUERYRESULTSET_ITEMS], function(item, i){
//						var tagInfo = item[node_COMMONATRIBUTECONSTANT.UITAGQUERYRESULT_UITAGINFO];
//						that.tagInfos.push(tagInfo);
//					});

					loc_setCurrentByIndex(that, 0);
				}
			}));
			node_requestServiceProcessor.processRequest(request);
		},	
		template : `
			<div>
				{{question.question}}:
				<uitag_data v-bind:uitaginfo="currentTagInfo" v-bind:datainfo="dataInfo"/>
<!--				<a class="popup-open" href="#" data-popup=".popup-about">Change UI</a>  -->
				<a href="#" v-on:click.prevent="onOpenPopup">Change UI</a>

				<div class="popup popup-about" ref="popup">
				    <div class="block">
				      <p>Please select control: </p>
				    	<uitag_select v-bind:uitaginfolist='this.tagInfos' v-bind:initselect='this.initSelect' v-bind:datainfo="dataInfo" v-on:selectChange="onSelectChange" />
				      <!-- Close Popup -->
				      <p><a class="link popup-close" href="#" v-on:click.prevent="onSelectTag">Ok</a></p>
				      <p><a class="link popup-close" href="#" v-on:click.prevent="onCancelSelectTag">Close</a></p>
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
nosliw.registerSetNodeDataEvent("variable.data.entity.createData", function(){node_createData = this.getData();});
nosliw.registerSetNodeDataEvent("variable.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("variable.context.createContext", function(){node_createContext = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItemUIData", node_createComponentQuestionItemUIData); 

})(packageObj);
