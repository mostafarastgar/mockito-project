import org.springframework.cloud.contract.spec.Contract
Contract.make {
    description "should authenticate the requested user"
    request{
        method 'POST'
        url '/user/authenticate'
        body([
        			   "username": "name1",
        			   "password": "123"
        		])
        headers {
        			contentType('application/json')
        		}

    }
    response {
        body 'true'
        status 200
    }
}