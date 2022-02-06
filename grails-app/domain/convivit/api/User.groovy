package convivit.api

class User {
    String firstName
    String lastName
    String email

    static hasMany = [
      roles: UserRole
    ]

    static mapping = {
        sort "email"
    }

    static constraints = {
      email nullable: false, email: true
      firstName nullable: false
      lastName nullable: false
    }
}
