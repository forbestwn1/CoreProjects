{
	"extra": {
		"name": "parent complex entity",
		"description": "parent complex entity"
	},
	"entity": {
		"entity_none_testsimple1": {
			"extra": {
				"status": "disabled"
			},
			"entity":{
				"attribute2_none_testsimple1": {
				
				}
			}
		},
		"simplized1_none_testsimple1": {
			"attribute2_none_testsimple1": {
			
			}
		},
		"resource1_none_testsimple1": {
			"extra": {
				"status": "disabled"
			},
			"resourceId": "testsimple1|*test1"
		},
		"reference1_none_testsimple1": { 
			"extra": {
				"status": "disabled"
			},
			"reference": "samename"
		},
		"entity1_none_testcomplex1": {
			"extra": {
				"status": "disabled"
			},
			"entity":{
				"attribute2_none_testcomplex1": {
				
				}
			}
		},
		"globalresource_none_testcomplex1": {
			"extra": {
				"status": "disabled1"
			},
			"resourceId": "testcomplex1|*child",
			"parent": {
				"attachment": {
					"mode": "none"
				}
			}
		},
		"reference_none_testcomplex1": { 
			"extra": {
				"status": "disabled"
			},
			"reference": "data"
		},
		"normal1_set_testsimple1": {
			"extra": {
				"status": "disabled"
			},
			"element": [
				{
					"extra": {
						"name": "element1",
						"description": "element1"
					},
					"entity": {
						"attribute2_none_testsimple1": {
						
						}
					}					
				}
			]
		},
		"normal1_list_testsimple1": {
			"extra": {
				"status": "disabled"
			},
			"element": [
				{
					"extra": {
						"name": "element1",
						"description": "element1"
					},
					"entity": {
						"attribute2_none_testsimple1": {
						
						}
					}					
				}
			]
		},
		"attachment": {
			"extra": {
				"status": "disabled1",
				"name": "parent complex entity" 
			},
			"entity": {
				"valuestructure" : [
				
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
				"testcomplex1": [
				
				]
			}
		}
	}
}
