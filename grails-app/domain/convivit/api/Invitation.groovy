package convivit.api
import java.time.*

class Invitation {

    static class InvitationException extends Exception { 
      public InvitationException(String errorMessage) {
          super(errorMessage);
      }
    }

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

    static mapping = {
        sort "fromDate"
    }

    Invitation useIt() {
      def today = LocalDate.now()
      if (this.status == 'canceled') {
        throw new InvitationException("No se puede validar porque ya esta cancelada")
      }

      if (this.status == 'closed') {
        throw new InvitationException("No se puede validar porque ya esta cerrada")
      }

      if (this.status == 'validated') {
        throw new InvitationException("Invitacion ya utilizada")
      }

      if (this.fromDate > today) {
        throw new InvitationException("Invitacion no es valida para hoy")
      }

      if (this.toDate < today) {
        throw new InvitationException("Invitacion esta vencida")
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
        def isOverdue = this.toDate < today
        setOverDue(isOverdue)
        return this
      } else {
        throw new InvitationException("Invitacion no esta validada")
      }
    }

    Invitation extendIt(LocalDate newDate) {
      def today = LocalDate.now()
      if (this.status == "pending" && this.toDate < today) {
        throw new InvitationException("La invitacion ya se venció")
      }

      if (this.status == 'canceled') {
        throw new InvitationException("No se puede extender porque ya esta cancelada")
      }

      if (this.toDate > newDate) {
        throw new InvitationException("La nueva fecha tiene que ser mayor a la de salida")
      }

      setToDate(newDate)
      return this
    }

    Invitation cancelIt() {
      if (this.status == 'validated') {
        throw new InvitationException("No se puede cancelar una invitacion en uso")
      }

      if (this.status == 'canceled') {
        throw new InvitationException("No se puede cancelar una invitacion ya cancelada")
      }
      setStatus('canceled')
      setCanceledAt(LocalDateTime.now())
      return this
    }
}
