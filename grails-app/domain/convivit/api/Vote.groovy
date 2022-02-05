package convivit.api
import java.time.LocalDate

class Vote {

    static class VoteException extends Exception { 
      public VoteException(String errorMessage) {
          super(errorMessage);
      }
    }

    static belongsTo = [meet: Meet, role: UserRole]
    Boolean value
    LocalDate date

    static constraints = {
    }

    static mapping = {
        sort date: "asc"
    }
}
