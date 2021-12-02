package convivit.api
import java.time.LocalDate
class BootStrap {

    def init = { servletContext ->
      def role = new UserRole(role: 'admin')
      def admin = new User(
        first_name: "Admin",
        last_name: "Admin",
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
        first_name: "Jorge",
        last_name: "Moreno",
        email: "jmoren@gmail.com"
      ).save(failOnError: true)

      def owner2 = new User(
        first_name: "Armando",
        last_name: "Lio",
        email: "armando@lio.com"
      ).save(failOnError: true)

      def owner3 = new User(
        first_name: "Pablo",
        last_name: "Ramirez",
        email: "pablo@ramirez.com"
      ).save(failOnError: true)

      def tenant1 = new User(
        first_name: "Juan",
        last_name: "Moreira",
        email: "juan@moreira.com"
      ).save(failOnError: true)

      def tenant2 = new User(
        first_name: "Pepe",
        last_name: "Argento",
        email: "pepe@argento.com"
      ).save(failOnError: true)

      def ownerRole1  = new UserRole(user: owner1, unit: unit1, role: 'owner').save(failOnError: true)
      def ownerRole4  = new UserRole(user: owner1, unit: unit4, role: 'owner').save(failOnError: true)
      def ownerRole2  = new UserRole(user: owner2, unit: unit2, role: 'owner').save(failOnError: true)
      def ownerRole3  = new UserRole(user: owner3, unit: unit3, role: 'owner').save(failOnError: true)
      def tenantRole1 = new UserRole(user: tenant1, unit: unit3, role: 'tenant', authorized: false).save(failOnError: true)
      def tenantRole2 = new UserRole(user: tenant2, unit: unit4, role: 'tenant', authorized: true).save(failOnError: true)
    }

    def destroy = {
    }
}
