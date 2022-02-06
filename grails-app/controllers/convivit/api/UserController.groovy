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
      def user = User.findByEmail(e)
      if (user) {
        respond(User.findByEmail(e))
      } else {
        render( status: 404, contentType: "text/json"){
          error 'Usuario no encontrado'
        }
      }
    }
}
