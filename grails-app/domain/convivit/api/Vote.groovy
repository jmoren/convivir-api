package convivit.api
import java.time.LocalDate

class Vote {
    static belongsTo = [meet: Meet, role: UserRole]
    Boolean value
    LocalDate date

    static constraints = {
    }
}
