package convivit.api


import grails.rest.*
import grails.converters.*

class InvitationController extends RestfulController {
    static responseFormats = ['json', 'xml']
    InvitationController() {
        super(Invitation)
    }
}
