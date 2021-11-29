package convivit.api
import java.time.LocalDate

class Invitation {
    static belongsTo = [role: UserRole]

    String dni
    String email
    String kind
    String status
    LocalDate fromDate
    LocalDate toDate

    static constraints = {
    }

    Boolean valid() {
        Boolean pending = this.status == 'pending'
        Boolean today = this.fromDate == LocalDate.now()
        return (pending && today)
    }

    Invitation useIt() {
        if (this.valid()) {
            this.status = 'validated'
            this.save()
            return this
        } else {
            throw new IllegalStateException("Invitacion invalida")
        }
    }

    Invitation closeIt() {
      Boolean overDue = this.toDate < LocalDate.now()
        if (!overDue) {
          if (this.status == 'validated') {
              this.status = 'done'
              this.save()
              return this
          } else {
            throw new IllegalStateException("Invitacion invalida")
          }
        } else {
          throw new IllegalStateException("Invitacion se paso del limite!")
        }
    }

    Invitation extendDate(LocalDate newDate) {
      Boolean overDue = this.toDate < LocalDate.now()
      if (!overDue) {
        if (this.status != 'canceled' && this.toDate > newDate) {
          this.date = newDate
          this.save()
          return this
        } else {
          throw new IllegalStateException("La nueva fecha no puede ser menor de la original")
        }
      } else {
        throw new IllegalStateException("La invitacion ya se venció")
      }
    }

    Invitation cancel() {
      if (this.status == 'pending') {
        this.status = 'canceled'
        this.save()
        return this
      } else {
        throw new IllegalStateException("Invitacion ya utilizada o cancelada")
      }
    }
}
