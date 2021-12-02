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
    int afirmative = this.votes.find { it.value == true }.size()
    int negative = this.votes.find { it.value == false }.size()
    boolean half_votes = (total / 2) > this.votes.size()
    if (half_votes)
      this.approved = afirmative > negative
      this.save()
  }

  Boolean valid() {
      this.votable && this.dueDate <= LocalDate.now()
  }
}
