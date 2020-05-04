{
	"name": "page_minimum",
	"description": "page_minimum",
	"builderId" : "page_minimum",
	"node" : [
		{
			"type" : "service",
			"id" : "1",
			"entity" : {
				"service": "TestTemplateService",
				"name": "TestTemplateService",
			},
			"extra" : {
				"id": "TestTemplateService",
				"name": "TestTemplateService",
			    "implementation" : "com.nosliw.service.test.template.HAPServiceImp",
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
			}
		},	
		{
			"type" : "serviceInput",
			"id" : "2",
			"name" : "input",
			"entity" : {
				"children" : [
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
			"name" : "output",
			"entity" : {
				"children" : [
					{
						"name" : "outputInService"
						"criteria" : "test.string;1.0.0"
					}
				]
			}
		},	
		{
			"type" : "serviceInputParm",
			"id" : "4",
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
			"entity" : {
				name : "serviceParm2",
				criteria : "test.string;1.0.0",
			}
		},
		{
			"type" : "serviceOutputParm",
			"id" : "6",
			"entity" : {
				"name" : "outputInService"
				"criteria" : "test.string;1.0.0"
			}
		},
		{
			"type" : "constant",
			"id" : "7",
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
			"entity" : {
				"name" : "serviceParm2",
				"id" : "TestTemplateService_input_serviceParm2_variable",
				"criteria" : "test.string;1.0.0",
			}
		},
		{
			"type" : "variable",
			"id" : "9",
			"entity" : {
				"name" : "outputInService",
				"id" : "TestTemplateService_output_outputInService_variable",
				"criteria" : "test.string;1.0.0",
			}
		},
		{
			"type" : "UI",
			"id" : "10",
			"entity" : {
				"data" : "TestTemplateService_input_serviceParm2_variable",
			}
		},
		{
			"type" : "UI_title",
			"id" : "12",
			"entity" : {
				"title" : "serviceParm2",
			}
		},
		{
			"type" : "UI_tag",
			"id" : "13",
			"entity" : {
				"tag" : "textinput",
			}
		},
		
	],
	"connection: : [
		{
			"id" : "101",
			"type" : "contain",
			"from" : "0"
			"to" : "1"
			"entity" : {
				"element" : "1",
			},
		},
		{
			"id" : "102",
			"type" : "contain",
			"from" : "1"
			"to" : "2"
			"entity" : {
				"element" : "input",
			},
		},
		{
			"id" : "103",
			"type" : "contain",
			"from" : "1"
			"to" : "3"
			"entity" : {
				"element" : "output",
			},
		},
		{
			"id" : "104",
			"type" : "contain",
			"from" : "2"
			"to" : "4"
			"entity" : {
				"element" : "serviceParm1",
			},
		},
		{
			"id" : "105",
			"type" : "contain",
			"from" : "2"
			"to" : "5"
			"entity" : {
				"element" : "serviceParm2",
			},
		},
		{
			"id" : "106",
			"type" : "contain",
			"from" : "3"
			"to" : "6"
			"entity" : {
				"element" : "outputInService",
			},
		},
		{
			"id" : "107",
			"type" : "input",
			"from" : "4"
			"to" : "7"
			"entity" : {
				"path" : "",
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
