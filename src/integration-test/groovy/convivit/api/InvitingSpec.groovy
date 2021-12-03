package convivit.api

import org.springframework.beans.factory.annotation.*

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import java.time.LocalDate

@Integration
@Rollback
class InvitingSpec extends Specification {
    @Autowired
    InvitationService invitationService

    Long ownerId
    Long tenantId
    Long unitId

    def createOwner(Unit unit) {
      def user = new User(first_name: "Probando", last_name: "Apellido", email: "email@email.com").save()
      def role = new UserRole(role: "owner", user: user, unit: unit).save()
      unit.addToRoles(role)
      this.ownerId = role.id
    }

    def createTenant(Boolean authorized, Unit unit) {
      def user = new User(first_name: "Probando", last_name: "Apellido", email: "email@email.com").save()
      def role = new UserRole(role: "tenant", authorized: authorized, unit: unit, user: user).save()
      unit.addToRoles(role)
      this.tenantId = role.id
    }

    def setup() {
      def admin = new User(first_name: "Admin", last_name: "admin", email: "consorcio@email.com").save()
      def adminRole = new UserRole(role: 'admin', user: admin).save()

      def consorcio = new Consorcio(name: "Test", admin: adminRole, address: "My address", type: "Building").save()
      def unit = new Unit(consorcio: consorcio, address: consorcio.address, floor: 5, number: "B", kind: "Apartment").save()
      consorcio.addToUnits(unit)

      this.unitId = unit.id
    }

    def cleanup() {
    }

    void "Invitando un amigo"() {
      given:
        Unit unit = Unit.get(this.unitId)
        createOwner(unit)
      when:
        def now = LocalDate.now()
        Invitation invitation = invitationService.invite("23786234", "amigo@email.com", now, now.plusDays(2), this.ownerId)
      then:
        UserRole userRole = UserRole.get(this.ownerId)
        userRole.invitations.size() == 1
    }
}
