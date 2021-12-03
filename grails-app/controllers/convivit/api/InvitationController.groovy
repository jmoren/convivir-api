package convivit.api


import grails.rest.*
import grails.converters.*

class InvitationController extends RestfulController {
    InvitationService invitationService
    static responseFormats = ['json', 'xml']
    InvitationController() {
        super(Invitation)
    }

    def updateState(Long invitationId) {
      Invitation invitation
      String message
      String currentStatus = request.JSON.status
      switch(currentStatus) {
        case 'use':
          iinvitationnv = invitationService.useInvitation(invitationId)
          break;
        case 'cancel':
          invitation = invitationService.cancelInvitation(invitationId)
          break;
        case 'close':
          invitation = invitationService.closeInvitation(invitationId)
          break;
        default:
          message = "Accion no permitida"
      }

      if (error) {
        render( status: 404, contentType: "text/json"){
            error message
        }
      } else {
        respond(invitation)
      }
    }
}
