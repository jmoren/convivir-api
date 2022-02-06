package convivit.api


import grails.rest.*
import grails.converters.*

class UnitController extends RestfulController {
    static responseFormats = ['json', 'xml']
    UnitController() {
        super(Unit)
    }

    def listByConsorcio(Long consorcioId) {
        def consorcio = Consorcio.get(consorcioId)
        respond(consorcio.units)
    }
}
