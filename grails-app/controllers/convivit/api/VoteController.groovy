package convivit.api


import grails.rest.*
import grails.converters.*

class VoteController extends RestfulController {
    static responseFormats = ['json', 'xml']
    VoteController() {
        super(Vote)
    }
}
