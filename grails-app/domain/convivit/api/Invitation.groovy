package convivit.api
import java.time.*

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
    LocalDateTime validatedAt
    LocalDateTime closedAt
    LocalDateTime canceledAt

    static constraints = {
      validatedAt nullable: true
      closedAt nullable: true
      canceledAt nullable: true
      overDue nullable: true, default: false
    }

    Invitation useIt() {
      def today = LocalDate.now()
      if (this.status == 'canceled') {
        throw new IllegalStateException("No se puede validar porque ya esta cancelada")
      }

      if (this.status == 'closed') {
        throw new IllegalStateException("No se puede validar porque ya esta cerrada")
      }

      if (this.status == 'validated') {
        throw new IllegalStateException("Invitacion ya utilizada")
      }

      if (this.fromDate > today) {
        throw new IllegalStateException("Invitacion no es valida para hoy")
      }

      if (this.fromDate < today) {
        setStatus('overdue')
        setClosedAt(LocalDateTime.now())
        return this
      }

      setStatus('validated')
      setValidatedAt(LocalDateTime.now())
      return this
    }

    Invitation closeIt() {
      def today = LocalDate.now()
      if (this.status == 'validated') {
        setStatus('closed')
        setClosedAt(LocalDateTime.now())
        def isOverdue = this.fromDate.compareTo(today) < 1
        setOverDue(isOverdue)
        return this
      } else {
        throw new IllegalStateException("Invitacion no esta validada")
      }
    }

    Invitation extendIt(LocalDate newDate) {
      def today = LocalDate.now()
      if (this.status == "pending" && this.fromDate.compareTo(today) < 0) {
        throw new IllegalStateException("La invitacion ya se venció")
      }

      if (this.status == 'canceled') {
        throw new IllegalStateException("No se puede extender porque ya esta cancelada")
      }

      setToDate(newDate)
      return this
    }

    Invitation cancelIt() {
      if (this.status == 'validated') {
        throw new IllegalStateException("No se puede cancelar una invitacion en uso")
      }

      if (this.status == 'canceled') {
        throw new IllegalStateException("No se puede cancelar una invitacion ya cancelada")
      }
      setStatus('canceled')
      setCanceledAt(LocalDateTime.now())
      return this
    }
}
