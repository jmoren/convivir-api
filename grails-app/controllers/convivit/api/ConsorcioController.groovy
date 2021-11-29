package convivit.api


import grails.rest.*
import grails.converters.*

class ConsorcioController extends RestfulController {
    static responseFormats = ['json', 'xml']
    ConsorcioController() {
        super(Consorcio)
    }
}
