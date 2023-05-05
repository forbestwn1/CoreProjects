{
	"extra": {
		"name": "localchild complex entity",
		"description": "localchild complex entity"
	},
	"entity": {
		"locallocalresource_none_testcomplex1": {
			"extra": {
				"status": "disabled1"
			},
			"resourceId": "testcomplex1|#localgrandchild",
			"parent": {
				"attachment": {
					"mode": "parent"
				}
			}
		},
		"attachment": {
			"extra": {
				"status": "localchild complex entity",
				"name": "localchild complex entity" 
			},
			"entity": {
				"valuestructure" : [
				],
				"testsimple1": [
					{
						"name": "samename",
						"entity": {
							"localchild_none_testsimple1": {
								"entity":{
								}
							}
						}
					},
					{
						"name": "inlocalchildonly",
						"entity": {
							"localchild_none_testsimple1": {
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
