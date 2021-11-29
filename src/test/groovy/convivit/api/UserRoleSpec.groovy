package convivit.api

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import java.time.LocalDate
class UserRoleSpec extends Specification implements DomainUnitTest<UserRole> {

    def setup() {
    }

    def cleanup() {
    }

    def addTenant(Unit unit) {
      def tenant = new User(
          first_name: "Mauro",
          last_name: "Icardi",
          email: "inquilino@test.com"
      ).save()

      def role = new UserRole(role: "tenant")
      tenant.addToRoles(role)
      unit.addToUsers(role)
    }

    def setUpConsorcioWithMeet(Boolean votable) {
      def consorcio = new Consorcio(
        name: "Test",
        address: "Mi calle 123",
        type: "Building"
      ).save()

      def today = LocalDate.now()
      def meet = new Meet(
        date: today,
        dueDate: today.plusDays(7),
        title: "Asamblea I",
        content: "El acta va aca",
        votable: votable,
        approved: false
      ).save()
      consorcio.addToMeets(meet)

      return consorcio
    }

    def setUpUnitAsOwnerWithConsorcio(Consorcio consorcio, Boolean withTenant) {
      def unit = new Unit(street: "Melancolia", number: "34", type: "Apartment").save()

      consorcio.addUnit(unit)

      def owner = new User(
          first_name: "Jorge",
          last_name: "Moreno",
          email: "propietario@test.com"
      ).save()

      def role = new UserRole(role: "owner")
      owner.addToRoles(role)
      unit.addToUsers(role)

      if (withTenant) {
        addTenant(unit)
      }
    }

    def setUpUnitAsOwner(Boolean withTenant) {
      def unit = new Unit(street: "Melancolia", number: "34", type: "Apartment").save()
      def owner = new User(
          first_name: "Jorge",
          last_name: "Moreno",
          email: "propietario@test.com"
      ).save()

      def role = new UserRole(role: "owner")
      owner.addToRoles(role)
      unit.addToUsers(role)

      if (withTenant) {
        addTenant(unit)
      }
    }

    void "Invitacion personal valida"() {
      given:
        def withTenant = false
        setUpUnitAsOwner(withTenant)
      when:
          UserRole r = User.findByEmail("propietario@test.com").roles[0]
          LocalDate fromDate = LocalDate.now().plusDays(2)
          LocalDate toDate = LocalDate.now().plusDays(4)
          Invitation invitation = r.inviteTo(fromDate, toDate, "amigo@mail.com", "234234343")
      then:
          Invitation.count() == 1
          invitation.status == 'pending'
          invitation.kind == 'Personal'
          invitation.email == "amigo@mail.com"
          invitation.dni == "234234343"
    }

    void "Invitacion especial valida"() {
      given:
          def withTenant = true
          setUpUnitAsOwner(withTenant)
      when:
          UserRole r = User.findByEmail("propietario@test.com").roles[0]
          LocalDate fromDate = LocalDate.now().plusDays(5)
          Invitation invitation = r.inviteTo(fromDate, fromDate, "otro_amigo@mail.com", "234234343")
      then:
          Invitation.count() == 1
          invitation.status == 'pending'
          invitation.kind == 'Special'
          invitation.email == "otro_amigo@mail.com"
          invitation.dni == "234234343"
    }

    void "Invitacion espcial invalida"() {
      given:
          def withTenant = true
          setUpUnitAsOwner(withTenant)
      when:
          UserRole r = User.findByEmail("propietario@test.com").roles[0]
          LocalDate fromDate = LocalDate.now().plusDays(2)
          LocalDate toDate = LocalDate.now().plusDays(3)
          Invitation invitation = r.inviteTo(fromDate, toDate, "otro_amigo@mail.com", "234234343")
      then:
          Invitation.count() == 0
          Exception e = thrown()
          e.message == "Una invitacion especial solo puede ser por el dia"
    }

    void "Invitacion personal invalida"() {
      given:
          def withTenant = false
          setUpUnitAsOwner(withTenant)
      when:
          UserRole r = User.findByEmail("propietario@test.com").roles[0]
          LocalDate fromDate = LocalDate.now().plusDays(3)
          LocalDate toDate = LocalDate.now().plusDays(1)
          Invitation invitation = r.inviteTo(fromDate, toDate, "otro_amigo@mail.com", "234234343")
      then:
          Invitation.count() == 0
          Exception e = thrown()
          e.message == "La fecha desde no puede ser mayor a la fecha hasta"
    }

    void "Invitacion ya existe para la misma fecha y DNI"() {
      given:
          def withTenant = false
          setUpUnitAsOwner(withTenant)
      when:
          UserRole r = User.findByEmail("propietario@test.com").roles[0]
          LocalDate fromDate = LocalDate.now().plusDays(1)
          LocalDate toDate = LocalDate.now().plusDays(2)
          Invitation invitation1 = r.inviteTo(fromDate, toDate, "otro_amigo@mail.com", "234234343")
          Invitation invitation2 = r.inviteTo(fromDate, toDate, "otro_amigo@mail.com", "234234343")
      then:
          Invitation.count() == 1
          Exception e = thrown()
          e.message == "Ya existe una invitacion para el DNI 234234343"
    }

    void "Votar una asamblea habilitada"() {
      given:
        def votable = true
        def withTenant = false
        def consorcio = setUpConsorcioWithMeet(votable)
        setUpUnitAsOwnerWithConsorcio(consorcio, withTenant)
      when:
        UserRole role = User.findByEmail("propietario@test.com").roles[0]
        Meet meet = Meet.get(1)
        role.vote(meet, true)
      then:
        role.votes.size() == 1
        role.votes[0].value == true
        meet.votes.size() == 1
    }
}