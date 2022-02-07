package convivit.api
import java.time.*

class Invitation {

   public enum Status {
        PENDING, VALIDATED, CANCELED, OVERDUE, CLOSED
    }

    static class InvitationException extends Exception { 
      public InvitationException(String errorMessage) {
          super(errorMessage);
      }
    }

    static belongsTo = [role: UserRole]
    // Invitation.Status: pending, validated, canceled, overdue, closed
    String code
    String dni
    String email
    String kind
    Status status
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
      if (this.status == Status.CANCELED) {
        throw new InvitationException("No se puede validar porque ya esta cancelada")
      }

      if (this.status == Status.CLOSED) {
        throw new InvitationException("No se puede validar porque ya esta cerrada")
      }

      if (this.status == Status.VALIDATED) {
        throw new InvitationException("Invitacion ya utilizada")
      }

      if (this.fromDate > today) {
        throw new InvitationException("Invitacion no es valida para hoy")
      }

      if (this.toDate < today) {
        setStatus(Status.OVERDUE)
        this.save()
        throw new InvitationException("Invitacion esta vencida")
      }

      setStatus(Status.VALIDATED)
      setValidatedAt(LocalDateTime.now())
      return this
    }

    Invitation closeIt() {
      def today = LocalDate.now()
      if (this.status == Status.VALIDATED) {
        setStatus(Status.CLOSED)
        setClosedAt(LocalDateTime.now())
        setOverDue(this.toDate < today)
        return this
      } else {
        throw new InvitationException("Invitacion no esta validada")
      }
    }

    Invitation extendIt(LocalDate newDate) {
      def today = LocalDate.now()
      if (this.status == Status.PENDING && this.toDate < today) {
        setStatus(Status.OVERDUE)
        this.save()
        throw new InvitationException("La invitacion ya se venció")
      }

      if (this.status == Status.CANCELED) {
        throw new InvitationException("No se puede extender porque ya esta cancelada")
      }

      if (this.toDate > newDate) {
        throw new InvitationException("La nueva fecha tiene que ser mayor a la de salida")
      }

      setToDate(newDate)
      return this
    }

    Invitation cancelIt() {
      if (this.status == Status.VALIDATED) {
        throw new InvitationException("No se puede cancelar una invitacion en uso")
      }

      if (this.status == Status.CANCELED) {
        throw new InvitationException("No se puede cancelar una invitacion ya cancelada")
      }
      setStatus(Status.CANCELED)
      setCanceledAt(LocalDateTime.now())
      return this
    }
}
