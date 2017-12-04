
nosliw.runtime.getResourceService().importResource({"id":{"id":"loop",
"type":"uiTag"
},
"children":[],
"dependency":{},
"info":{}
}, {"name":"test",
"context":{"inherit":true,
"public":{"internal_data":{"children":{},
"type":"relative",
"path":"<%=&(data)&%>"
}
}
},
"attributes":{},
"script":
function (context, parentResourceView, uiTagResource, attributes, tagEnv) {
    var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
    var node_createDataOperationRequest = nosliw.getNodeData("uidata.dataoperation.createDataOperationRequest");
    var node_DataOperationService = nosliw.getNodeData("uidata.dataoperation.DataOperationService");
    var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
    var node_requestServiceProcessor = nosliw.getNodeData("request.requestServiceProcessor");
    var node_createUIResourceViewFactory = nosliw.getNodeData("uiresource.createUIResourceViewFactory");
    var node_createContextElementInfo = nosliw.getNodeData("uidata.context.createContextElementInfo");
    var node_createContext = nosliw.getNodeData("uidata.context.createContext");
    var loc_context = context;
    var loc_uiTagResource = uiTagResource;
    var loc_parentResourceView = parentResourceView;
    var loc_tagEnv = tagEnv;
    var loc_dataContextEleName = "internal_data";
    var loc_dataVariable = loc_context.createVariable(node_createContextVariable(loc_dataContextEleName));
    var loc_eleContextEleName = attributes.element;
    var loc_view;
    var loc_startEle;
    var loc_endEle;
    var loc_getViewData = function (name) {
        var value = loc_view.val();
        return {dataTypeId: "test.string;1.0.0", value: value};
    };
    var loc_out = {prv_addEle: function (dataWrapper, key, requestInfo) {
        var contextEleInfo = [];
        _.each(context.getContext(), function (contextEle, name) {
            contextEleInfo.push(node_createContextElementInfo(name, context, new node_createContextVariable(name, "")));
        });
        contextEleInfo.push(node_createContextElementInfo(loc_eleContextEleName, dataWrapper));
        var eleContext = node_createContext(contextEleInfo, requestInfo);
        var resourceView = node_createUIResourceViewFactory().createUIResourceView(loc_uiTagResource, loc_tagEnv.getId() + "." + key, loc_parentResourceView, eleContext, requestInfo);
        resourceView.insertAfter(loc_startEle);
    }, ovr_postInit: function (requestInfo) {
        this.prv_updateView();
    }, ovr_preInit: function (requestInfo) {
    }, ovr_initViews: function (startEle, endEle, requestInfo) {
        loc_startEle = startEle;
        loc_endEle = endEle;
    }, prv_updateView: function () {
        var that = this;
        loc_dataVariable.getWrapper().handleEachElement(function (dataWrapper, key) {
            that.prv_addEle(dataWrapper, key);
        }, this);
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

