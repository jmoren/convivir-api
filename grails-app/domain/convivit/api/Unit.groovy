package convivit.api

class Unit {
    String address
    String number
    String floor
    // Apartment, House
    String kind

    static belongsTo = [consorcio: Consorcio]
    static hasMany = [roles: UserRole]

    static constraints = {
      floor nullable: true
    }

    Boolean isApartment() {
        return this.kind == 'Apartment'
    }

    Boolean isHouse() {
        return this.kind == 'House'
    }

    User getOwner() {
      UserRole owner = this.roles.find { it.role == 'owner' }

      return owner?.user
    }

    User getTenant() {
      UserRole tenant = this.roles.find { it.role == 'tenant' }
      return tenant?.user
     }
}
