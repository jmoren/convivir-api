package convivit.api

class User {
    String first_name
    String last_name
    String email

    static hasMany = [
      roles: UserRole
    ]

    static mapping = {
        sort "email"
    }

    static constraints = {
      email nullable: false, unique: true, email: true
      first_name nullable: false
      last_name nullable: false
    }
}
