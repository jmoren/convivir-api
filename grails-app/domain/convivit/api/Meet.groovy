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
    println "Unidades: " + total + ", Votos + " + afirmative + ", Votos - " + negative
    println "Votos emitidos: " + totalVotes
    boolean half_votes = (total / 2) < totalVotes
    println "Se llego a la mitad?: " + half_votes

    if (half_votes) {
      println "Vamos a actualizar la meet"
      this.approved = (afirmative > negative)
      println "Mas afirmativos que negativos: " + this.approved
      this.save(flush: true, failureOnError: true)
      println "Meet: " + Meet.get(this.id).approved
    }
  }

  Boolean valid() {
      this.votable && this.dueDate <= LocalDate.now()
  }
}
