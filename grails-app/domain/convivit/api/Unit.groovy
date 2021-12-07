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

    UserRole addRole(User user, String role, Boolean authorization) {
      def currentOwner = this.getOwner()
      def currentTenant = this.getTenant()

      if (role == 'owner') {
        if (currentOwner != null) {
          throw new IllegalArgumentException("Esta unidad ya tiene propietario")
        }
        if (currentTenant.id == user.id) {
          throw new IllegalArgumentException("No se puede ser propietario e inquilino a la vez")
        }
      }

      if (role == 'tenant') {
        if (currentTenant != null) {
          throw new IllegalArgumentException("Esta unidad ya tiene inquilino")
        }

        if (currentOwner.id == user.id) {
          throw new IllegalArgumentException("No se puede ser propietario e inquilino a la vez")
        }
      }

      UserRole userRole = new UserRole(
        unit: this,
        user: user,
        role: role,
        authorized: authorization
      ).save(flush: true)

      this.addToRoles(userRole)
      user.addToRoles(userRole)

      return userRole
    }
}
