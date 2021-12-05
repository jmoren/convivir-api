package convivit.api


import grails.rest.*
import grails.converters.*
import java.time.LocalDate

class UserRoleController extends RestfulController {
    static responseFormats = ['json', 'xml']
    VotingService votingService
    InvitationService invitationService

    UserRoleController() {
        super(UserRole)
    }

    def invite(Long roleId) {
      try {
        def params = request.JSON.invitation
        def invitation = invitationService.invite(
          params.dni,
          params.email,
          LocalDate.parse(params.fromDate),
          LocalDate.parse(params.toDate),
          roleId
        )
        respond(invitation)
      } catch (Exception e) {
        render( status: 403, contentType: "text/json"){
            error e.message
        }
      }
    }

    def vote(Long meetId, Long roleId) {
      try {
        def myValue = request.JSON.vote
        def vote = votingService.voteMeet(meetId, roleId, myValue)
        respond(vote)
      } catch (Exception e) {
        render( status: 403, contentType: "text/json"){
            error e.message
        }
      }
    }
}
