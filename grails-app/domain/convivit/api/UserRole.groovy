package convivit.api

import java.time.LocalDate
class UserRole {
    String role
    Boolean authorized
    Set votes = []
    static belongsTo = [
      user: User,
      unit: Unit
    ]

    static hasMany = [
      invitations: Invitation,
      votes: Vote
    ]

    static constraints = {
        unit nullable: true
        authorized nullable: true, default: false
    }

    Invitation inviteTo(LocalDate fromDate, LocalDate toDate, String email, String dni) {
        String kind = this.getInvitationKind()

        this.stopIfAlreadyInvited(fromDate, dni)
        this.stopIfHasInValidDates(kind, fromDate, toDate)

        String code = UUID.randomUUID().toString()
        def invitation = new Invitation(
            code: code,
            dni: dni,
            email: email,
            fromDate: fromDate,
            toDate: toDate,
            kind: kind,
            role: this,
            status: 'pending'
        ).save()

        this.addToInvitations(invitation)
        return invitation
    }

    Vote vote(Meet meet, Boolean value) {
      if (this.unit.consorcio != meet.consorcio) {
        throw new IllegalStateException("Esta asamblea no es de tu consorcio")
      }

      this.allowedToVoteIn(meet)

      def vote = new Vote(value: value, meet: meet, role: this, date: LocalDate.now()).save()
      this.addToVotes(vote)
      meet.registerVote(vote)
      return vote
    }

    private Boolean stopIfAlreadyInvited(LocalDate from, String dni) {
      Invitation exists = this.invitations.find {
        it.dni == dni && it.status == 'pending' && it.fromDate == from
      }

      if (exists) {
        throw new IllegalStateException('Ya existe una invitacion para el DNI ' + dni)
      }

      return true
    }

    private Boolean stopIfHasInValidDates(String kind, LocalDate from, LocalDate to) {
      if (kind == 'Personal' && to.isBefore(from)) {
        throw new IllegalStateException("La fecha desde no puede ser mayor a la fecha hasta")
      }

      if (kind == 'Special' && from.compareTo(to) != 0) {
        throw new IllegalStateException("Una invitacion especial solo puede ser por el dia")
      }

      return true
    }

    private String getInvitationKind() {
        if (this.role == "owner" && this.unit.getTenant()) {
            return "Special"
        } else {
            return "Personal"
        }
    }

    private Boolean allowedToVoteIn(Meet meet) {
      def vote = meet.votes.find {
        it.role.unit == this.unit
      }
      if (vote != null) {
        def role_name = vote.role.role == 'tenant' ? 'Inquilino' : 'Propietario'
        def user_name = "${vote.role.user.first_name} ${vote.role.user.last_name}"
        def message = "Ya hay un voto de esta unidad: ${user_name} (${role_name})"
        throw new IllegalStateException(message)
      }

      if ((this.role == 'tenant' && !this.authorized) || this.role == 'admin') {
          throw new IllegalStateException("No tienes permisos para votar")
      }

      if (meet.valid()) {
          throw new IllegalStateException("Ya pasó la fecha limite")
      }

      return true
    }
}
