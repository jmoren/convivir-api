package convivit.api

class Unit {

    static class UnitException extends Exception { 
      public UnitException(String errorMessage) {
        super(errorMessage);
      }
    }
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

    static mapping = {
        sort "number"
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
        if (currentOwner) {
          throw new UnitException("Esta unidad ya tiene propietario")
        }
        if (currentTenant && currentTenant == user) {
          throw new UnitException("No se puede ser propietario e inquilino al mismo tiempo")
        }
      }

      if (role == 'tenant') {
        if (currentTenant) {
          throw new UnitException("Esta unidad ya tiene inquilino")
        }

        if (currentOwner && currentOwner == user) {
          throw new UnitException("No se puede ser propietario e inquilino al mismo tiempo")
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
