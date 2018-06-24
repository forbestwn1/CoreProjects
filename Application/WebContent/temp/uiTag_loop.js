
nosliw.runtime.getResourceService().importResource({"id":{"id":"loop",
"type":"uiTag"
},
"children":[],
"dependency":{},
"info":{}
}, {"name":"loop",
"context":{"inherit":true,
"public":{}
},
"attributes":{},
"script":
function (env) {
    var node_namingConvensionUtility = nosliw.getNodeData("common.namingconvension.namingConvensionUtility");
    var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
    var loc_env = env;
    var loc_containerVariable;
    var loc_childResourceViews = [];
    var loc_childVaraibles = [];
    var loc_id = 0;
    var loc_handleEachElementProcessor;
    var loc_generateId = function () {
        loc_id++;
        return loc_id + "";
    };
    var loc_getElementContextVariable = function (key) {
        var out = node_createContextVariable(loc_dataContextEleName);
        out.path = node_namingConvensionUtility.cascadePath(out.path, key + "");
        return out;
    };
    var loc_updateView = function (requestInfo) {
        _.each(loc_childResourceViews, function (resourceView, id) {
            resourceView.destroy();
        });
        loc_childResourceViews = [];
        var index = 0;
        loc_handleEachElementProcessor.executeLoopRequest(function (eleVar, indexVar) {
            loc_addEle(eleVar, indexVar, index);
            index++;
        });
    };
    var loc_updateView1 = function (requestInfo) {
        loc_containerVariable = undefined;
        var index = 0;
        loc_env.executeGetHandleEachElementRequest("internal_data", "", function (eleVar, indexVar) {
            loc_addEle(eleVar, indexVar, index);
            index++;
        }, {success: function (requestInfo, containerVar) {
            loc_containerVariable = containerVar;
            loc_containerVariable.registerDataOperationEventListener(undefined, function (event, eventData, requestInfo) {
                if (event == "EVENT_WRAPPER_SET") {
                    loc_out.destroy();
                    loc_updateView();
                }
                if (event == "EVENT_WRAPPER_NEWELEMENT") {
                    loc_addEle(eventData.getElement(), eventData.getIndex(), 0);
                }
                if (event == "WRAPPER_EVENT_DESTROY") {
                    loc_out.prv_deleteEle(loc_getElementContextVariable(eventData.index));
                }
            }, this);
        }});
    };
    var loc_addEle = function (eleVar, indexVar, index, requestInfo) {
        var eleContext = loc_env.createExtendedContext([loc_env.createContextElementInfo(loc_env.getAttributeValue("element"), eleVar)], requestInfo);
        var resourceView = loc_env.createUIResourceViewWithId(loc_env.getId() + "." + loc_generateId(), eleContext, requestInfo);
        if (index == 0) {
            resourceView.insertAfter(loc_env.getStartElement());
        } else {
            resourceView.insertAfter(loc_childResourceViews[index - 1].getEndElement());
        }
        loc_childResourceViews.splice(index, 0, resourceView);
        loc_childVaraibles.splice(index, 0, eleVar);
        eleVar.registerDataOperationEventListener(undefined, function (event, dataOperation, requestInfo) {
            if (event == "EVENT_WRAPPER_DELETE") {
                loc_out.prv_deleteEle(index);
            }
        }, this);
    };
    var loc_out = {prv_deleteEle: function (index, requestInfo) {
        var view = loc_childResourceViews[index];
        view.detachViews();
        view.destroy();
        loc_childResourceViews.splice(index, 1);
        loc_childVaraibles.splice(index, 1);
    }, postInit: function (requestInfo) {
        loc_handleEachElementProcessor = loc_env.createHandleEachElementProcessor("internal_data", "");
        loc_handleEachElementProcessor.registerEventListener(undefined, function (event) {
            loc_updateView();
        });
        loc_updateView();
    }, destroy: function () {
        loc_handleEachElementProcessor.destroy();
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

