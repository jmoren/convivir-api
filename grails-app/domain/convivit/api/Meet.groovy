package convivit.api
import java.time.LocalDate

class Meet {
  LocalDate date
  String title
  String content
  Boolean votable
  Boolean approved
  LocalDate dueDate

  static belongsTo = [
    consorcio: Consorcio
  ]
  static hasMany = [
    votes: Vote
  ]

  static constraints = {
    dueDate nullable: true
  }

  void updateStatus() {
    int total = this.consorcio.units.size()
    int afirmative = this.votes.findAll { it.value == true }.size()
    int negative = this.votes.findAll { it.value == false }.size()
    int totalVotes = this.votes.size()
    boolean half_votes = (total / 2) > totalVotes
    if (half_votes)
      this.approved = afirmative > negative
      this.save()
  }

  Boolean valid() {
      this.votable && this.dueDate <= LocalDate.now()
  }
}
