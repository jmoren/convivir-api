package convivit.api


import grails.rest.*
import grails.converters.*

class UserController extends RestfulController {
    static responseFormats = ['json', 'xml']
    UserController() {
        super(User)
    }

    def login() {
      def e = request.JSON.email
      respond(User.findByEmail(e))
    }
}
