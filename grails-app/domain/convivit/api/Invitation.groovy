package convivit.api
import java.time.LocalDate

class Invitation {
    static belongsTo = [role: UserRole]
    // Status: pending, validated, canceled, overdue, closed
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

    Invitation useIt() {
      def today = LocalDate.now()
      if (this.status == 'validated') {
        throw new IllegalStateException("Invitacion ya utilizada")
      }
      if (this.fromDate > today) {
        throw new IllegalStateException("Invitacion no es valida para hoy")
      }
      if (this.fromDate < today) {
        this.status = 'overdue'
        this.save()
        throw new IllegalStateException("Invitacion esta vencida")
      }

      this.status = 'validated'
      this.validatedAt = LocalDate.now()
      this.save()
      return this
    }

    Invitation closeIt() {
      def today = LocalDate.now()
      if (this.status == 'validated') {
        this.status = 'closed'
        this.closedAt = LocalDate.now()
        this.overDue =  this.fromDate.compareTo(today) < 1
        this.save()
        return this
      } else {
        throw new IllegalStateException("Invitacion no esta validada")
      }
    }

    Invitation extend(LocalDate newDate) {
      def today = LocalDate.now()
      if (this.status == "pending" && this.fromDate.compareTo(today) < 0) {
        throw new IllegalStateException("La invitacion ya se venció")
      }

      if (this.status == 'canceled') {
        throw new IllegalStateException("No se puede extender porque ya esta cancelada")
      }

      this.toDate = newDate
      this.save()
      return this
    }

    Invitation cancel() {
      if (this.status == 'pending') {
        this.status = 'canceled'
        this.canceledAt = LocalDate.now()
        this.save()
        return this
      } else {
        throw new IllegalStateException("Invitacion ya utilizada o cancelada")
      }
    }
}
