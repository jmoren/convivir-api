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
        if (invitation.canUseIt()) {
          invitation.save(flush:true, failOnError:true)
          println("After save(flush: true, failOnError: true): Status => ${invitation.status}")
          return invitation
        }
    }

    Invitation closeInvitation(Long invitationId) {
        Invitation invitation = Invitation.get(invitationId)
        if (invitation.canCloseIt()) {
          invitation.save(flush:true, failOnError:true)
          println("After save(flush: true, failOnError: true): Status => ${invitation.status}")
          return invitation
        }
    }

    Invitation cancelInvitation(Long invitationId) {
        Invitation invitation = Invitation.get(invitationId)
        if (invitation.canCancelIt()) {
          invitation.save(flush:true, failOnError:true)
          println("After save(flush: true, failOnError: true): Status => ${invitation.status}")
          return invitation
        }
    }

    Invitation extendInvitation(Long invitationId, LocalDate date) {
        Invitation invitation = Invitation.get(invitationId)
        if (invitation.canExtendIt(date)) {
          invitation.save(flush:true, failOnError:true)
          println("After save(flush: true, failOnError: true): Status => ${invitation.status}")
          println("After save(flush: true, failOnError: true): CanceledAt => ${invitation.canceledAt}")
          return invitation
        }
    }
}
