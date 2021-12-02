package convivit.api
import java.time.LocalDate

class Meet {
  LocalDate date
  String title
  String content
  Boolean votable
  Boolean approved
  LocalDate dueDate
  LocalDate finishedAt
  static belongsTo = [
    consorcio: Consorcio
  ]
  static hasMany = [
    votes: Vote
  ]

  static constraints = {
    dueDate nullable: true
    finishedAt nullable: true
  }

  void addVote(Vote vote) {
    this.addToVotes(vote)
    int total = this.consorcio.units.size()
    int afirmative = this.votes.findAll { it.value == true }.size()
    int negative = this.votes.findAll { it.value == false || it.value == null }.size()
    int totalVotes = this.votes.size()
    boolean half = (total/2) < totalVotes
    if (totalVotes == total) {
      this.approved = (afirmative > negative)
      this.finishedAt = LocalDate.now()
      this.save(flush: true)
    }
  }

  Boolean valid() {
      this.votable && this.dueDate <= LocalDate.now()
  }
}
