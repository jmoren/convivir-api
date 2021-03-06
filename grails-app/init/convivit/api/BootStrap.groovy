package convivit.api
import java.time.*
import convivit.api.Invitation.Status
class BootStrap {

    def init = { servletContext ->
      def role = new UserRole(role: 'admin')
      def admin = new User(
        firstName: "Admin",
        lastName: "Admin",
        email: "consorcio@admin.com"
      ).addToRoles(role).save()

      def consorcio = new Consorcio(
        name: "Esparta",
        address: "Mi calle 123",
        type: "Building",
        admin: role
      ).save()

      def meet1 = new Meet(
          date: LocalDate.now().minusDays(20),
          votable: false,
          title: "Asamblea I",
          content: "El acta de la asamblea va aca.",
          approved: false,
          consorcio: consorcio
      ).save(failOnError: true)
      def meet2 = new Meet(
          date: LocalDate.now().minusDays(20),
          dueDate: LocalDate.now().minusDays(10),
          votable: true,
          title: "Asamblea II",
          content: "El acta de la asamblea va aca.",
          approved: true,
          finishedAt: LocalDate.now().minusDays(10),
          consorcio: consorcio
      ).save(failOnError: true)
      def meet3 = new Meet(
          date: LocalDate.now(),
          dueDate: LocalDate.now().plusDays(10),
          votable: true,
          title: "Asamblea III",
          content: "El acta de la asamblea va aca.",
          approved: false,
          consorcio: consorcio
      ).save(failOnError: true)

      def unit1 = new Unit(
        address: consorcio.address,
        floor: "4",
        number: "A",
        kind: "Apartment",
        consorcio: consorcio
      ).save(failOnError: true)
      def unit2 = new Unit(
        address: consorcio.address,
        floor: "4",
        number: "B",
        kind: "Apartment",
        consorcio: consorcio
      ).save(failOnError: true)
      def unit3 = new Unit(
        address: consorcio.address,
        floor: "5",
        number: "A",
        kind: "Apartment",
        consorcio: consorcio
      ).save(failOnError: true)
      def unit4 = new Unit(
        address: consorcio.address,
        floor: "5",
        number: "B",
        kind: "Apartment",
        consorcio: consorcio
      ).save(failOnError: true)

      def owner1 = new User(
        firstName: "Jorge",
        lastName: "Moreno",
        email: "jmoren@gmail.com"
      ).save(failOnError: true)

      def owner2 = new User(
        firstName: "Armando",
        lastName: "Lio",
        email: "armando@lio.com"
      ).save(failOnError: true)

      def owner3 = new User(
        firstName: "Pablo",
        lastName: "Ramirez",
        email: "pablo@ramirez.com"
      ).save(failOnError: true)

      def tenant1 = new User(
        firstName: "Juan",
        lastName: "Moreira",
        email: "juan@moreira.com"
      ).save(failOnError: true)

      def tenant2 = new User(
        firstName: "Pepe",
        lastName: "Argento",
        email: "pepe@argento.com"
      ).save(failOnError: true)

      def ownerRole1  = new UserRole(user: owner1, unit: unit1, role: 'owner').save(failOnError: true)
      def ownerRole4  = new UserRole(user: owner1, unit: unit4, role: 'owner').save(failOnError: true)
      def ownerRole2  = new UserRole(user: owner2, unit: unit2, role: 'owner').save(failOnError: true)
      def ownerRole3  = new UserRole(user: owner3, unit: unit3, role: 'owner').save(failOnError: true)
      def tenantRole1 = new UserRole(user: tenant1, unit: unit3, role: 'tenant', authorized: false).save(failOnError: true)
      def tenantRole2 = new UserRole(user: tenant2, unit: unit4, role: 'tenant', authorized: true).save(failOnError: true)

      def invitation1 = new Invitation(
          code: UUID.randomUUID().toString(),
          dni: "28023787",
          email: "paco@email.com",
          kind: "Personal",
          fromDate: LocalDate.now().plusDays(2),
          toDate: LocalDate.now().plusDays(3),
          status: Status.PENDING,
          role: ownerRole1
      ).save()

      def invitation2 = new Invitation(
          code: UUID.randomUUID().toString(),
          dni: "23023787",
          email: "amanda@email.com",
          kind: "Personal",
          fromDate: LocalDate.now().minusDays(4),
          toDate: LocalDate.now().minusDays(1),
          status: Status.VALIDATED,
          validatedAt: LocalDateTime.now().minusDays(3),
          role: ownerRole1
      ).save()

      ownerRole1.addToInvitations(invitation1)
      ownerRole1.addToInvitations(invitation2)
      println "Bootstraped data"
    }

    def destroy = {
    }
}
