package convivit.api

class Unit {
    String street
    String number
    String floor
    // Appartment, House
    String kind

    static belongsTo = Consorcio
    static hasMany = [users: UserRole]

    static constraints = {
    }

    Boolean isApartment() {
        return this.kind == 'Apartment'
    }

    Boolean isHouse() {
        return this.kind == 'House'
    }

    User getOwner() {
      UserRole p = this.users.find { it.role == 'owner' }

      return p?.user
    }

    User getTenant() {
      UserRole i = this.users.find { it.role == 'tenant' }
      println "Role found: " + i.getClass().toString()
      return i?.user
     }
}
