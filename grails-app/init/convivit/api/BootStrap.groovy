package convivit.api

class BootStrap {

    def init = { servletContext ->
      def c1 = new Consorcio(name: "Esparta", address: "Mi calle 123", type: "Building").save()
      def c2 = new Consorcio(name: "Nordelta", address: "Ruta 197 km 34", type: "Country").save()
    }
    def destroy = {
    }
}
