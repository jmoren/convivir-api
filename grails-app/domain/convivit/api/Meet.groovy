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

  static mapping = {
      sort date: "desc"
  }
  static constraints = {
    dueDate nullable: true
    finishedAt nullable: true
  }

  void registerVote(Vote vote) {
    this.addToVotes(vote)
    int total = this.consorcio.units.size()
    int afirmative = this.votes.findAll { it.value == true }.size()
    int negative = this.votes.findAll { it.value == false || it.value == null }.size()
    int totalVotes = this.votes.size()

    if (totalVotes == total) {
      setApproved(afirmative > negative)
      setFinishedAt(LocalDate.now())
      this.save()
    }
  }

  Boolean valid() {
      this.votable && this.dueDate <= LocalDate.now()
  }
}
