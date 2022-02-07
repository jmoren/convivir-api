package convivit.api.commands

import grails.validation.Validateable
import java.time.LocalDate

class InvitationParams implements Validateable {
    String fromDate
    String toDate
    String dni
    String email

    LocalDate parsedFromDate(){
      return LocalDate.parse(fromDate)
    }

    LocalDate parsedToDate(){
      return LocalDate.parse(toDate)
    }

    static constraints = {
        dni nullable: false
        email nullable: false
        fromDate nullable: false
        toDate nullable: false
    }
}