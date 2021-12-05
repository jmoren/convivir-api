package convivit.api


import grails.rest.*
import grails.converters.*

class MeetController extends RestfulController {
    static responseFormats = ['json', 'xml']

    MeetController() {
        super(Meet)
    }

    def index(Long consorcioId) {
      def consorcio = Consorcio.get(consorcioId)
      def meets = consorcio.meets
      respond([meetList: meets])
    }
}
