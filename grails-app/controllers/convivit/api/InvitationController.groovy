package convivit.api


import grails.rest.*
import grails.converters.*
import java.time.LocalDate

class InvitationController extends RestfulController {
    InvitationService invitationService
    static responseFormats = ['json', 'xml']
    InvitationController() {
        super(Invitation)
    }

    def index(Long roleId) {
      def role = UserRole.get(roleId)
      def invitations = role.invitations
      respond(invitations)
    }

    def extend(Long invitationId) {
      LocalDate extendDate = LocalDate.parse(request.JSON.date)
      def invitation = invitationService.extendInvitation(invitationId, extendDate)
      respond(invitation)
    }

    def move(Long invitationId) {
      try {
        String currentStatus = request.JSON.status
        switch(currentStatus) {
          case 'validate':
            def invitation = invitationService.useInvitation(invitationId)
            respond(invitation)
            break;
          case 'cancel':
            def invitation = invitationService.cancelInvitation(invitationId)
            respond(invitation)
            break;
          case 'close':
            def invitation = invitationService.closeInvitation(invitationId)
            respond(invitation)
            break;
          default:
            render( status: 404, contentType: "text/json"){
                error "Estado no permitido"
            }
        }
      } catch (Exception e) {
        render( status: 400, contentType: "text/json"){
            error e.message
        }
      }
    }
}
