{
	"extra": {
		"name": "parent complex entity",
		"description": "parent complex entity"
	},
	"entity": {
		"ignore_variable" : "parent_public",
		"simplized1_none_test.complex.script": {
			"extra": {
				"status": "disabled1"
			},
			"scriptName": "complexscript_test_exist"
		},
		"testparm_none_test.complex.script": {
			"extra": {
				"status": "disabled1"
			},
			"entity":{
				"scriptName": "complexscript_test_printparms",
				"parm" : {
					"name" : "simplized1_none_test.complex.script"
				}
			}
		},
		"testconfigure_none_test.complex.script": {
			"extra": {
				"status": "disabled1"
			},
			"entity":{
				"scriptName": "complexscript_test_printconfigure",
				"parm" : {
					"name" : "simplized1_none_test.complex.script"
				}
			}
		},
		"teststate_none_test.complex.script": {
			"extra": {
				"status": "disabled1"
			},
			"entity":{
				"scriptName": "complexscript_test_state",
				"parm" : {
					"state" : "state1"
				}
			}
		},
		"testlifecycle.log_none_test.complex.script": {
			"extra": {
				"status": "disabled1"
			},
			"entity":{
				"scriptName": "complexscript_test_lifecycle_log",
				"parm" : {
				}
			}
		},
		"testlifecycle.rollback_none_test.complex.script": {
			"extra": {
				"status": "disabled1"
			},
			"entity":{
				"scriptName": "complexscript_test_lifecycle_rollback",
				"parm" : {
				}
			}
		},
		"entity1_none_test.complex.script": {
			"extra": {
				"status": "disabled"
			},
			"entity":{
				"scriptName": "testcomplexscript1",
				"parm" : {
					"name" : "simplized1_none_test.complex.script",
					"state" : "state1"
				}
			}
		},
		"localresource1_none_testcomplex1": {
			"extra": {
				"status": "disabled"
			},
			"resourceId": "testcomplex1|#localchild"
		},
		"globalresource1_none_testsimple1": {
			"extra": {
				"status": "disabled"
			},
			"resourceId": "testsimple1|*test1"
		},
		"entity2_none_test.complex.1": {
			"extra": {
				"status": "disabled"
			},
			"entity":{
			}
		},
		"reference1_none_testsimple1": { 
			"extra": {
				"status": "disabled"
			},
			"reference": "samename"
		},
		"normal1_set_test.complex.script": {
			"extra": {
				"status": "disabled"
			},
			"element": [
				{
					"extra": {
						"name": "element1",
						"description": "element1"
					},
					"entity":{
						"scriptName": "testcomplexscript1",
						"parm" : {
							"name" : "normal1_set_test.complex.script_element1",
							"state" : "element_set_1"
						}
					}
				},
				{
					"extra": {
						"name": "element2",
						"description": "element2"
					},
					"entity":{
						"scriptName": "testcomplexscript1",
						"parm" : {
							"name" : "normal1_set_test.complex.script_element2",
							"state" : "element_set_2"
						}
					}
				}
			]
		},
		"normal1_list_test.complex.script": {
			"extra": {
				"status": "disabled"
			},
			"element": [
				{
					"extra": {
						"name": "element1",
						"description": "element1"
					},
					"entity":{
						"scriptName": "testcomplexscript1",
						"parm" : {
							"name" : "normal1_list_test.complex.script_element1",
							"state" : "element_list_1"
						}
					}
				},
				{
					"extra": {
						"name": "element2",
						"description": "element2"
					},
					"entity":{
						"scriptName": "testcomplexscript1",
						"parm" : {
							"name" : "normal1_list_test.complex.script_element2",
							"state" : "element_list_2"
						}
					}
				}
			]
		},
		"attachment": {
			"extra": {
				"status": "disabled",
				"name": "parent attachment" 
			},
			"entity": {
				"valuestructure" : [
					{
						"name" : "valuestructure1",
						"entity": {
							"fromAttachment1": {
								"definition": {
									"criteria": "test.string;1.0.0"
								},
								"defaultValue": {
									"dataTypeId": "test.string;1.0.0",
									"value": "Default value for forlistservice_1_ex_parm1"
								}
							},
							"fromAttachment2": {
								"definition": {
									"criteria": "test.float;1.0.0"
								}
							}
						}
					}
				],
				"testsimple1": [
					{
						"name": "samename",
						"entity": {
							"parent_none_testsimple1": {
								"entity":{
								}
							}
						}
					},
					{
						"name": "inparentonly",
						"entity": {
							"parent_none_testsimple1": {
								"entity":{
								}
							}
						}
					}
				],
				"test.complex.1": [
				
				]
			}
		},
		"valueStructure": {
			"extra": {
				"status": "disabled"
			},
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"parent_public": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "9876543210"
							}
						}
					}
				},
				{
					"groupType" : "protected",
					"valueStructure" : {
						"parent_protected": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "9876543210"
							}
						}
					}
				},
				{
					"groupType" : "internal",
					"valueStructure" : {
						"parent_internal": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "9876543210"
							}
						}
					}
				},
				{
					"groupType" : "private",
					"valueStructure" : {
						"parent_private": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "9876543210"
							}
						}
					}
				},
				{
					"groupType": "public",
					"groupName" : "fromResource",
					"valueStructure" : {
						"resourceId" : "valuestructure|*test1"
					}
				},
				{
					"groupType": "public",
					"groupName" : "fromAttachment",
					"valueStructure" : {
						"reference": "valuestructure1;valuestructure"
					}
				}
			]
		}
	}
}
