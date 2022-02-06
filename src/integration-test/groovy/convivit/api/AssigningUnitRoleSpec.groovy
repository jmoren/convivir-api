package convivit.api


import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification

@Integration
@Rollback
class AssigningUnitRoleSpec extends Specification {
    Long unitId
    Long ownerId
    Long tenantId

    @Autowired
    UnitService unitService
  
    def createOwner() {
      def user = new User(firstName: "First", lastName: "Owner", email: "email@email.com").save(failOnError: true)
      this.ownerId = user.id
    }
    
    def createTenant() {
      def tenant = new User(firstName: "Inquilino", lastName: "Nuevo", email: "inquilino@email.com").save(failOnError: true)
      this.tenantId = tenant.id
    }

    def createOwnerWithUnit(Unit unit) {
      def user = new User(firstName: "Other", lastName: "Owner", email: "email2@email.com").save(failOnError: true)
      def role = new UserRole(role: "owner", user: user, unit: unit).save(failOnError: true)
      unit.addToRoles(role)
      this.ownerId = user.id
    }

    def setup() {
      def admin = new User(firstName: "Admin", lastName: "admin", email: "consorcio@email.com").save()
      def adminRole = new UserRole(role: 'admin', user: admin).save()

      def consorcio = new Consorcio(name: "Test", admin: adminRole, address: "My address", type: "Building").save()
      def unit = new Unit(consorcio: consorcio, address: consorcio.address, floor: 5, number: "B", kind: "Apartment").save()
      consorcio.addToUnits(unit)
      this.unitId = unit.id
    }


    void "Agregar propietario a una unidad"() {
      given:
        Unit unit = Unit.get(this.unitId)
        createOwner()
      when:
        UserRole newOwner = unitService.addRole(this.unitId, this.ownerId, "owner", true)
      then:
        newOwner.user == User.get(this.ownerId)
        newOwner.unit == unit
        newOwner.role == 'owner'
     }

    void "Agregar un inquilino a una unidad" () {
      given:
        Unit unit = Unit.get(this.unitId)
        createOwnerWithUnit(unit)
        createTenant()
      when:
        UserRole newTenant = unitService.addRole(this.unitId, this.tenantId, "tenant", false)
      then:
        unit.getOwner() != null
        newTenant.user == User.get(this.tenantId)
        newTenant.unit == unit
        newTenant.role == 'tenant'
    }

    void "Agregar como inquilino al mismo propietario" () {
      given:
        Unit unit = Unit.get(this.unitId)
        createOwnerWithUnit(unit)
      when:
        UserRole newOwner = unitService.addRole(this.unitId, this.ownerId, "tenant", false)
      then:
        Exception e = thrown()
        e.message == "No se puede ser propietario e inquilino al mismo tiempo"
    }
}
