{
	"name": "page_minimum",
	"description": "page_minimum",
	"topicType": "uiResource",
	"info": {
		"director": "page_minimum"
	},
	"node": [{
			"type": "service",
			"id": "1",
			"status": {
				"editable": ["id", "name"],
				"visible": true
			},
			"entity": {
				"referenceId": "TestTemplateService",
				"name": "TestTemplateService",
				"service": {
					"id": "TestTemplateService",
					"name": "TestTemplateService",
					"implementation": "com.nosliw.service.test.template.HAPServiceImp",
					"description": "test service implemented by process",
					"interface": {
						"result": [{
								"name": "success",
								"output": [{
										"name": "outputInService",
										"criteria": "test.string;1.0.0"
									}
								]
							}
						],
						"parm": [{
								"name": "serviceParm1",
								"criteria": "test.string;1.0.0",
								"default": {
									"dataTypeId": "test.string;1.0.0",
									"value": "hello"
								}
							}, {
								"name": "serviceParm2",
								"criteria": "test.string;1.0.0"
							}
						]
					}
				}
			}
		}, {
			"type": "serviceInput",
			"id": "2",
			"status": {
				"editable": [],
				"visible": true
			},
			"entity": {
				"name": "input",
				"parm": [{
						"name": "serviceParm1",
						"criteria": "test.string;1.0.0",
						"default": {
							"dataTypeId": "test.string;1.0.0",
							"value": "hello"
						}
					}, {
						"name": "serviceParm2",
						"criteria": "test.string;1.0.0"
					}
				]
			}
		}, {
			"type": "serviceOutput",
			"id": "3",
			"status": {
				"editable": [],
				"visible": true
			},
			"entity": {
				"name": "output",
				"result": [{
						"name": "success",
						"output": [{
								"name": "outputInService",
								"criteria": "test.string;1.0.0"
							}
						]
					}
				]
			}
		}, {
			"type": "serviceInputParm",
			"id": "4",
			"status": {
				"editable": [],
				"visible": true
			},
			"entity": {
				"name": "serviceParm1",
				"criteria": "test.string;1.0.0",
				"default": {
					"dataTypeId": "test.string;1.0.0",
					"value": "hello"
				}
			}
		}, {
			"type": "serviceInputParm",
			"id": "5",
			"status": {
				"editable": [],
				"visible": true
			},
			"entity": {
				"name": "serviceParm2",
				"criteria": "test.string;1.0.0"
			}
		}, {
			"type": "serviceOutputItem",
			"id": "6",
			"status": {
				"editable": [],
				"visible": true
			},
			"entity": {
				"name": "outputInService",
				"criteria": "test.string;1.0.0"
			}
		}, {
			"type": "constant",
			"id": "7",
			"status": {
				"editable": ["data"],
				"visible": true,
				"invisible": ["data"]
			},
			"entity": {
				"name": "TestTemplateService_input_serviceParm1_constant",
				"data": {
					"dataTypeId": "test.string;1.0.0",
					"value": "helloaaaaa"
				}
			}
		}, {
			"type": "variable",
			"id": "8",
			"status": {
				"visible": false
			},
			"entity": {
				"name": "TestTemplateService_input_serviceParm1_variable",
				"definition": {
					"criteria": "test.string;1.0.0"
				}
			}
		}, {
			"type": "variable",
			"id": "10",
			"status": {
				"visible": false
			},
			"entity": {
				"name": "TestTemplateService_input_serviceParm2_variable",
				"definition": {
					"criteria": "test.string;1.0.0"
				}
			}
		}, {
			"type": "variable",
			"id": "11",
			"status": {
				"visible": false
			},
			"entity": {
				"name": "TestTemplateService_output_outputInService_variable",
				"definition": {
					"criteria": "test.string;1.0.0"
				}
			}
		}, {
			"type": "UI_page",
			"id": "20",
			"status": {
			},
			"entity": {
			}
		}, {
			"type": "UI_element",
			"id": "22",
			"status": {
			},
			"entity": {
				"type" : "tag",
				"tag": "textinput"
			}
		}
	],
	"connection": [{
			"id": "101",
			"description": "service to serviceInput",
			"type": "contain",
			"status": {},
			"end1": {
				"nodeId": "1",
				"profile": "container"
			},
			"end2": {
				"nodeId": "2"
			},
			"entity": {
				"child": "serviceInput"
			}
		}, {
			"id": "102",
			"description": "service to serviceOutput",
			"type": "contain",
			"status": {},
			"end1": {
				"nodeId": "1",
				"profile": "container"
			},
			"end2": {
				"nodeId": "3"
			},
			"entity": {
				"child": "serviceResult"
			}
		}, {
			"id": "103",
			"description": "serviceInput to parm1",
			"type": "contain",
			"status": {},
			"end1": {
				"nodeId": "2",
				"profile": "container"
			},
			"end2": {
				"nodeId": "4"
			},
			"entity": {
				"child": "serviceParm1"
			}
		}, {
			"id": "104",
			"type": "contain",
			"description": "serviceInput to parm2",
			"end1": {
				"nodeId": "2",
				"profile": "container"
			},
			"end2": {
				"nodeId": "5",
				"delete": true
			},
			"entity": {
				"child": "serviceParm2"
			}
		}, {
			"id": "105",
			"type": "contain",
			"description": "serviceResult to output1",
			"end1": {
				"nodeId": "3",
				"profile": "container"
			},
			"end2": {
				"nodeId": "6",
				"delete": true
			},
			"entity": {
				"child": "outputInService"
			}
		}, {
			"id": "107",
			"type": "dataIO",
			"description": "parm1 to var",
			"end1": {
				"nodeId": "4",
				"profile": "dataIn"
			},
			"end2": {
				"nodeId": "8",
				"profile": "dataIO",
				"delete": true
			},
			"entity": {
				"path": ""
			}
		}, {
			"id": "109",
			"type": "dataIO",
			"description": "parm2 to var",
			"end1": {
				"nodeId": "5",
				"profile": "dataIn"
			},
			"end2": {
				"nodeId": "10",
				"profile": "dataIO",
				"delete": true
			},
			"entity": {
				"path": ""
			}
		}, {
			"id": "110",
			"type": "dataIO",
			"end1": {
				"nodeId": "6",
				"profile": "dataOut"
			},
			"end2": {
				"nodeId": "11",
				"profile": "dataIO",
				"delete": true
			},
			"entity": {
				"path": ""
			}
		}, {
			"id": "201",
			"type": "contain",
			"end1": {
				"nodeId": "20",
				"profile": "container"
			},
			"end2": {
				"nodeId": "22",
			},
			"entity": {
				"child": 1
			}
		}, {
			"id": "202",
			"type": "dataIO",
			"end1": {
				"nodeId": "22",
				"profile": "dataIO"
			},
			"end2": {
				"nodeId": "10",
				"profile": "dataIO"
			},
			"entity": {
			}
		}
	]
}
