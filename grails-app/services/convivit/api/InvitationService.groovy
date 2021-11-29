package convivit.api

import grails.gorm.transactions.Transactional
import java.time.LocalDate
@Transactional
class InvitationService {

    def serviceMethod() {

    }

    Invitation invite(String dni, String email, LocalDate fromDate, LocalDate toDate, Long userId) {
        UserRole u = UserRole.get(userId)
        if (u == null) {
            throw new IllegalArgumentException("No existe el usuario")
        } else {
          Invitation invitation = u.inviteTo(fromDate, toDate, email, dni)
          return invitation
        }
    }
}
