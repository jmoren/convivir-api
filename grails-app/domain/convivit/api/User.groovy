package convivit.api

class User {
    String first_name
    String last_name
    String email

    static hasMany = [
      roles: UserRole
    ]

    static constraints = {
    }
}
