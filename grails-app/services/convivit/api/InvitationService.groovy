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
}
