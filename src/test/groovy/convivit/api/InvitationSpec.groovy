package convivit.api

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import java.time.LocalDate
class InvitationSpec extends Specification implements DomainUnitTest<Invitation> {

    def setupInvitation(String status, LocalDate from, LocalDate to) {
      new Invitation(
        fromDate: from,
        toDate: to,
        dni: "23456432",
        kind: "Personal",
        status: status,
        email: "amigo@mail.com",
        role: new UserRole(unit: new Unit(), role: "owner")
      ).save(failOnError: true)
    }

    void "Utilizar una invitacion valida"() {
      given:
        LocalDate from = LocalDate.now()
        LocalDate to = LocalDate.now().plusDays(1)
        setupInvitation("pending", from, to)
      when:
        Invitation i = Invitation.get(1)
        i.useIt()
      then:
        i.status == "validated"
        i.validatedAt == LocalDate.now()
    }

    void "Utilizar una invitacion dos veces"() {
      given:
        LocalDate from = LocalDate.now()
        LocalDate to = LocalDate.now().plusDays(1)
        setupInvitation("validated", from, to)
      when:
        Invitation i = Invitation.get(1)
        i.useIt()
      then:
        Exception e = thrown()
        e.message == "Invitacion ya utilizada"
    }

    void "Utilizar una invitacion antes de la fecha"() {
      given:
        LocalDate from = LocalDate.now().plusDays(1)
        LocalDate to = LocalDate.now().plusDays(1)
        setupInvitation("pending", from, to)
      when:
        Invitation i = Invitation.get(1)
        i.useIt()
      then:
        Exception e = thrown()
        e.message == "Invitacion no es valida para hoy"
    }

    void "Utilizar una invitacion despues de la fecha"() {
      given:
        LocalDate from = LocalDate.now().minusDays(2)
        LocalDate to = LocalDate.now().plusDays(3)
        setupInvitation("pending", from, to)
      when:
        Invitation i = Invitation.get(1)
        i.useIt()
      then:
        Exception e = thrown()
        e.message == "Invitacion esta vencida"
        i.status == 'overdue'
    }

    void "Extender una invitacion valida"() {
      given:
        LocalDate from = LocalDate.now().plusDays(1)
        LocalDate to = LocalDate.now().plusDays(2)
        setupInvitation("pending", from, to)
      when:
        Invitation i = Invitation.get(1)
        LocalDate newDate = LocalDate.now().plusDays(5)
        i.extendIt(newDate)
      then:
        i.toDate == newDate
    }

    void "Extender una invitacion vencida"() {
      given:
        LocalDate from = LocalDate.now().minusDays(1)
        LocalDate to = LocalDate.now().plusDays(1)
        setupInvitation("pending", from, to)
      when:
        Invitation i = Invitation.get(1)
        LocalDate newDate = LocalDate.now().plusDays(5)
        i.extendIt(newDate)
      then:
        Exception e = thrown()
        e.message  == "La invitacion ya se venció"
    }

    void "Extender una invitacion cancelada"() {
      given:
        LocalDate from = LocalDate.now()
        LocalDate to = LocalDate.now().plusDays(1)
        setupInvitation("canceled", from, to)
      when:
        Invitation i = Invitation.get(1)
        LocalDate newDate = LocalDate.now().plusDays(5)
        i.extendIt(newDate)
      then:
        Exception e = thrown()
        e.message  == "No se puede extender porque ya esta cancelada"
    }

    void "Cerrar una invitacion validada"(){
      given:
        LocalDate from = LocalDate.now()
        LocalDate to = LocalDate.now().plusDays(1)
        setupInvitation("validated", from, to)
      when:
        Invitation i = Invitation.get(1)
        i.closeIt()
      then:
        i.status == "closed"
        i.closedAt == LocalDate.now()
    }

    void "Cerrar una invitacion cancelada"(){
      given:
        LocalDate from = LocalDate.now()
        LocalDate to = LocalDate.now().plusDays(1)
        setupInvitation("canceled", from, to)
      when:
        Invitation i = Invitation.get(1)
        i.closeIt()
      then:
        Exception e = thrown()
        e.message == "Invitacion no esta validada"
    }

    void "Cerrar una invitacion despues de la fecha limite"(){
      given:
        LocalDate from = LocalDate.now().minusDays(2)
        LocalDate to = LocalDate.now().minusDays(1)
        setupInvitation("validated", from, to)
      when:
        Invitation i = Invitation.get(1)
        i.closeIt()
      then:
        i.status == "closed"
        i.overDue == true
        i.closedAt == LocalDate.now()
    }

    void "Cancelar una invitacion en uso"(){
      given:
        LocalDate from = LocalDate.now().minusDays(2)
        LocalDate to = LocalDate.now().minusDays(1)
        setupInvitation("validated", from, to)
      when:
        Invitation i = Invitation.get(1)
        i.cancelIt()
      then:
        Exception e = thrown()
        e.message == "No se puede cancelar una invitacion en uso"
    }

    void "Cancelar una invitacion cancelada"(){
      given:
        LocalDate from = LocalDate.now().minusDays(2)
        LocalDate to = LocalDate.now().minusDays(1)
        setupInvitation("canceled", from, to)
      when:
        Invitation i = Invitation.get(1)
        i.cancelIt()
      then:
        Exception e = thrown()
        e.message == "No se puede cancelar una invitacion ya cancelada"
    }

    void "Cancelar una invitacion pendinte"(){
      given:
        LocalDate from = LocalDate.now().minusDays(2)
        LocalDate to = LocalDate.now().minusDays(1)
        setupInvitation("pending", from, to)
      when:
        Invitation i = Invitation.get(1)
        i.cancelIt()
      then:
        i.status == "canceled"
        i.canceledAt == LocalDate.now()
    }
 }
