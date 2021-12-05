package convivit.api
import java.time.LocalDate

class Invitation {
    static belongsTo = [role: UserRole]
    // Status: pending, validated, canceled, overdue, closed
    String code
    String dni
    String email
    String kind
    String status
    Boolean overDue
    LocalDate fromDate
    LocalDate toDate
    LocalDate validatedAt
    LocalDate closedAt
    LocalDate canceledAt

    static constraints = {
      validatedAt nullable: true
      closedAt nullable: true
      canceledAt nullable: true
      overDue nullable: true, default: false
    }

    Invitation canUseIt() {
      def today = LocalDate.now()
      if (this.status == 'validated') {
        throw new IllegalStateException("Invitacion ya utilizada")
      }
      if (this.fromDate > today) {
        throw new IllegalStateException("Invitacion no es valida para hoy")
      }
      if (this.fromDate < today) {
        this.status = 'overdue'
        this.closedAt = LocalDate.now()
        return this
      }

      this.status = 'validated'
      this.validatedAt = LocalDate.now()
      return this
    }

    Invitation canCloseIt() {
      def today = LocalDate.now()
      if (this.status == 'validated') {
        this.status = 'closed'
        this.closedAt = LocalDate.now()
        this.overDue =  this.fromDate.compareTo(today) < 1
        return this
      } else {
        throw new IllegalStateException("Invitacion no esta validada")
      }
    }

    Invitation canExtendIt(LocalDate newDate) {
      def today = LocalDate.now()
      if (this.status == "pending" && this.fromDate.compareTo(today) < 0) {
        throw new IllegalStateException("La invitacion ya se venció")
      }

      if (this.status == 'canceled') {
        throw new IllegalStateException("No se puede extender porque ya esta cancelada")
      }

      this.toDate = newDate
      return this
    }

    Invitation canCancelIt() {
      if (this.status == 'validated') {
        throw new IllegalStateException("No se puede cancelar una invitacion en uso")
      }

      if (this.status == 'canceled') {
        throw new IllegalStateException("No se puede cancelar una invitacion ya cancelada")
      }
      this.status = 'canceled'
      this.canceledAt = LocalDate.now()
      return this
    }
}
