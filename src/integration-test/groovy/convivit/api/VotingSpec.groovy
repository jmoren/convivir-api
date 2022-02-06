package convivit.api

import org.springframework.beans.factory.annotation.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import java.time.LocalDate
@Integration
@Rollback
class VotingSpec extends Specification {
    @Autowired
    VotingService votingService

    Long meetId
    Long roleId
    Long unitId

    def createOwner(Unit unit) {
      def user = new User(firstName: "Probando", lastName: "Apellido", email: "email@email.com").save(failOnError: true)
      def role = new UserRole(role: "owner", user: user, unit: unit).save(failOnError: true)
      unit.addToRoles(role)
      this.roleId = role.id
    }

    def createTenant(Boolean authorized, Unit unit) {
      def user = new User(firstName: "Probando", lastName: "Apellido", email: "email@email.com").save(failOnError: true)
      def role = new UserRole(role: "tenant", authorized: authorized, user: user).save(failOnError: true)
      unit.addToRoles(role)
      this.roleId = role.id
    }

    def setup() {
      def admin = new User(firstName: "Admin", lastName: "admin", email: "consorcio@email.com").save()
      def adminRole = new UserRole(role: 'admin', user: admin).save()

      def consorcio = new Consorcio(name: "Test", admin: adminRole, address: "My address", type: "Building").save()
      def unit = new Unit(consorcio: consorcio, address: consorcio.address, floor: 5, number: "B", kind: "Apartment").save()
      consorcio.addToUnits(unit)
      def meet = new Meet(
        title: "Asamblea II",
        date: LocalDate.now(),
        dueDate: LocalDate.now().plusDays(10),
        approved: false,
        consorcio: consorcio,
        votable: true,
        content: "El acta va aca."
      ).save()

      this.unitId = unit.id
      this.meetId = meet.id
    }

    def cleanup() {
    }

    void "Votar como propietario"() {
        given:
          Unit unit = Unit.get(this.unitId)
          createOwner(unit)
        when:
          Vote vote = votingService.voteMeet(this.meetId, this.roleId, true)
        then:
          Meet meet = Meet.get(this.meetId)
          meet.votes.size() == 1
    }

    void "Votar como inquilino no autorizado"() {
        given:
          Unit unit = Unit.get(this.unitId)
          createTenant(false, unit)
        when:
          Vote vote = votingService.voteMeet(this.meetId, this.roleId, true)
        then:
          Exception e = thrown()
          e.message == "No tienes permisos para votar"
    }

    void "Votar como inquilino autorizado"() {
        given:
          Unit unit = Unit.get(this.unitId)
          createTenant(true, unit)
        when:
          Vote vote = votingService.voteMeet(this.meetId, this.roleId, true)
        then:
          Meet meet = Meet.get(this.meetId)
          meet.votes.size() == 1
    }
}
