package convivit.api

import grails.gorm.transactions.Transactional
import java.time.LocalDate
@Transactional
class InvitationService {

    Invitation invite(String dni, String email, LocalDate fromDate, LocalDate toDate, Long userRoleId) {
        UserRole userRole = UserRole.get(userRoleId)
        if (userRole == null) {
            throw new IllegalArgumentException("No existe el usuario")
        } else {
          Invitation invitation = userRole.inviteTo(fromDate, toDate, email, dni)
          return invitation
        }
    }

    Invitation useInvitation(Long invitationId) {
        Invitation invitation = Invitation.get(invitationId)
        if (invitation.useIt()) {
          invitation.save(flush:true, failOnError:true)
          return invitation
        }
    }

    Invitation closeInvitation(Long invitationId) {
        Invitation invitation = Invitation.get(invitationId)
        if (invitation.closeIt()) {
          invitation.save(flush:true, failOnError:true)
          return invitation
        }
    }

    Invitation cancelInvitation(Long invitationId) {
        Invitation invitation = Invitation.get(invitationId)
        if (invitation.cancelIt()) {
          invitation.save(flush:true, failOnError:true)
          return invitation
        }
    }

    Invitation extendInvitation(Long invitationId, LocalDate date) {
        Invitation invitation = Invitation.get(invitationId)
        if (invitation.extendIt(date)) {
          invitation.save(flush:true, failOnError:true)
          return invitation
        }
    }
}
