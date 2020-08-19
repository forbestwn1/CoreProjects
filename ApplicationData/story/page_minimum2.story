{
	"name": "page_minimum",
	"description": "page_minimum",
	"info" : {
		"director" : "page_minimum",
	},
	"node" : [
		{
			"type" : "service",
			"id" : "1",
			"configure" : {
				"editable" : ["id", "name"],
				"visible" : true, 
			},
			"entity" : {
				"referenceId": "TestTemplateService",
				"name": "TestTemplateService",
				"service": {
					"id": "TestTemplateService",
					"name": "TestTemplateService",
				    "implementation" : "com.nosliw.service.test.template1.HAPServiceImp",
					"description" : "test service implemented by process",
					"interface" : {
						"result" : [
							{
								"name" : "success",
								"output" : [
									{
										"name" : "outputInService"
										"criteria" : "test.string;1.0.0"
									}
								],
							}
						],
						"parm" : [
							{
								name : "serviceParm1",
								criteria : "test.string;1.0.0",
								default :{
									dataTypeId: "test.string;1.0.0",
									value: "hello"
								},
							},
							{
								name : "serviceParm2",
								criteria : "test.string;1.0.0",
							}
						]
					}
				},
			},
		},	
		{
			"type" : "serviceInput",
			"id" : "2",
			"configure" : {
				"editable" : [],
				"visible" : true, 
			},
			"entity" : {
				"name" : "input",
				"parm" : [
					{
						name : "serviceParm1",
						criteria : "test.string;1.0.0",
						default :{
							dataTypeId: "test.string;1.0.0",
							value: "hello"
						},
					},
					{
						name : "serviceParm2",
						criteria : "test.string;1.0.0",
					}
				]
			}
		},	
		{
			"type" : "serviceOutput",
			"id" : "3",
			"configure" : {
				"editable" : [],
				"visible" : true,
			},
			"entity" : {
				"name" : "output",
				"result" : [
					{
						"name" : "success",
						"output" : [
							{
								"name" : "outputInService"
								"criteria" : "test.string;1.0.0"
							}
						],
					}
				],
			}
		},	
		{
			"type" : "serviceInputParm",
			"id" : "4",
			"configure" : {
				"editable" : [],
				"visible" : true,
			},
			"entity" : {
				name : "serviceParm1",
				criteria : "test.string;1.0.0",
				default :{
					dataTypeId: "test.string;1.0.0",
					value: "hello"
				},
			}
		},	
		{
			"type" : "serviceInputParm",
			"id" : "5",
			"configure" : {
				"editable" : [],
				"visible" : true,
			},
			"entity" : {
				name : "serviceParm2",
				criteria : "test.string;1.0.0",
			}
		},
		{
			"type" : "serviceOutputParm",
			"id" : "6",
			"configure" : {
				"editable" : [],
				"visible" : true,
			},
			"entity" : {
				"name" : "outputInService"
				"criteria" : "test.string;1.0.0"
			}
		},
		{
			"type" : "constant",
			"id" : "7",
			"configure" : {
				"editable" : ["data"],
				"visible" : true,
				"invisible" : ["data"],
			},
			"entity" : {
				"name" : "TestTemplateService_input_serviceParm1_constant",
				"data" : {
					dataTypeId: "test.string;1.0.0",
					value: "helloaaaaa"
				}
			}
		},
		{
			"type" : "variable",
			"id" : "8",
			"configure" : {
				"visible" : false, 
			},
			"entity" : {
				"name" : "TestTemplateService_input_serviceParm2_variable",
				"definition" : {
					"criteria" : "test.string;1.0.0",
				}
			}
		},
		{
			"type" : "variable",
			"id" : "9",
			"configure" : {
				"visible" : false, 
			},
			"entity" : {
				"name" : "TestTemplateService_output_outputInService_variable",
				"criteria" : "test.string;1.0.0",
			}
		},
		{
			"type" : "UI",
			"id" : "10",
			"configure" : {
				"editable" : [],
				"visible" : true, 
				"invisible" : [],
			},
			"entity" : {
				"complexity" : "simple"
				"title" : true
			}
		},
		{
			"type" : "UI_title",
			"id" : "12",
			"configure" : {
				"editable" : ["title"],
				"visible" : true, 
				"invisible" : [],
			},
			"entity" : {
				"title" : "serviceParm2",
			}
		},
		{
			"type" : "UI_tag",
			"id" : "13",
			"configure" : {
				"editable" : ["tag"],
				"visible" : true, 
				"invisible" : [],
			},
			"entity" : {
				"tag" : "textinput",
			}
		},
		
	],
	"connection: : [
		{
			"id" : "101",
			"type" : "contain",
			"from" : "0",
			"to" : "1"
			"entity" : {
				"element" : "1",
			},
		},
		{
			"id" : "102",
			"type" : "contain",
			"node1" : {
				"id" : "1",
				"profile" : "container"
			},
			"node2" : {
				"id" : "2",
				"delete" : true
			},
			"entity" : {
				"element" : "input",
			},
		},
		{
			"id" : "103",
			"type" : "contain",
			"node1" : {
				"id" : "1",
				"profile" : "container"
			},
			"node2" : {
				"id" : "3",
				"delete" : true
			},
			"entity" : {
				"element" : "output",
			},
		},
		{
			"id" : "104",
			"type" : "contain",
			"node1" : {
				"id" : "2",
				"profile" : "container"
			},
			"node2" : {
				"id" : "4",
				"delete" : true
			},
			"entity" : {
				"element" : "serviceParm1",
			},
		},
		{
			"id" : "105",
			"type" : "contain",
			"node1" : {
				"id" : "2",
				"profile" : "container"
			},
			"node2" : {
				"id" : "5",
				"delete" : true
			},
			"entity" : {
				"element" : "serviceParm2",
			},
		},
		{
			"id" : "106",
			"type" : "contain",
			"node1" : {
				"id" : "3",
				"profile" : "container"
			},
			"node2" : {
				"id" : "6",
				"delete" : true
			},
			"entity" : {
				"element" : "outputInService",
			},
		},
		{
			"id" : "107",
			"type" : "input",
			"node1" : {
				"id" : "4",
				"profile" : "input",
			},
			"node2" : {
				"id" : "7",
				"profile" : "output",
				"delete" : true
			},
			"entity" : {
				"path" : "",
			},
		},
		{
			"id" : "108",
			"type" : "input",
			"node1" : {
				"id" : "4",
				"profile" : "input",
			},
			"node2" : {
				"id" : "17",
				"profile" : "output",
				"delete" : true
			},
			"entity" : {
				"path" : "",
			},
		},
		{
			"id" : "109",
			"type" : "input",
			"node1" : {
				"id" : "17",
				"profile" : "in",
			},
			"node2" : {
				"id" : "18",
				"profile" : "io",
				"delete" : true
			},
			"entity" : {
				"path" : "",
			},
		},
		{
			"id" : "110",
			"type" : "contain",
			"node1" : {
				"id" : "18",
				"profile" : "container",
			},
			"node2" : {
				"id" : "19",
				"delete" : true
			},
			"entity" : {
				"path" : "lable",
			},
		},
		{
			"id" : "111",
			"type" : "contain",
			"node1" : {
				"id" : "18",
				"profile" : "container",
			},
			"node2" : {
				"id" : "20",
				"delete" : true
			},
			"entity" : {
				"path" : "control",
			},
		},
		{
			"id" : "108",
			"type" : "input",
			"from" : "5"
			"to" : "8"
			"entity" : {
				"path" : "",
			},
		},
		{
			"id" : "109",
			"type" : "output",
			"from" : "6"
			"to" : "9"
			"entity" : {
				"path" : "",
			},
		},
		{
			"id" : "110",
			"type" : "present",
			"from" : "8"
			"to" : "10"
			"entity" : {
				"path" : "",
			},
		},
		{
			"id" : "111",
			"type" : "contain",
			"from" : "10"
			"to" : "12"
			"entity" : {
				"element" : "title",
			},
		},
		{
			"id" : "112",
			"type" : "contain",
			"from" : "10"
			"to" : "13"
			"entity" : {
				"element" : "tag",
			},
		},
	
	]
}
