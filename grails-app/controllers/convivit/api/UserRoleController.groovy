package convivit.api


import grails.rest.*
import grails.converters.*
import java.time.LocalDate

class UserRoleController extends RestfulController {
    static responseFormats = ['json', 'xml']
    VotingService votingService
    InvitationService invitationService
    UnitService unitService
    UserRoleController() {
        super(UserRole)
    }

    def save(Long unitId, Long userId, Long roleId) {
      try {
        def params = request.JSON.role
        println "params: " + params
        UserRole userRole = unitService.addRole(params.unitId, params.userId, params.role, params.authorized)
        respond(userRole)
      } catch (Exception e) {
        render( status: 422, contentType: "text/json"){
            error e.message
        }
      }
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
