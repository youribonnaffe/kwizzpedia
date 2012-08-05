import com.google.appengine.labs.repackaged.org.json.JSONObject

log.info "Question asked"

def question = null
while (question == null) {
    def random = new Random().nextFloat()
    question = datastore.execute {
        select all from 'question'
        where category == params.category
        and random_number >= random
        sort asc by random_number
        limit 1
    }
}
log.info "question = " + question

response.renderJson question[0].properties


