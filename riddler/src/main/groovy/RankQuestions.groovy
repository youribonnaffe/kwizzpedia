import groovy.json.JsonSlurper

String[] lines = new File("questions.txt").text.split('\n')
List<String[]> rows = lines.collect {it.split(';')}

def nouns = []
rows.collect {it[0]}.toSet().each { it ->
    def url = it.replace("http://dbpedia.org/resource/", "http://stats.grok.se/json/en/latest90/")
    def json = new JsonSlurper().parseText(new URL(url).text)
    def sum = json.daily_views.values().sum()
    nouns << [subject: it, rank: json.rank, score: sum]
    println "${it} scores ${json.rank} $sum"
}

// TODO why twice ?
def res = []
rows.each { question ->
    def url = question[0].replace("http://dbpedia.org/resource/", "http://stats.grok.se/json/en/latest90/")
    def json = new JsonSlurper().parseText(new URL(url).text)
    def sum = json.daily_views.values().sum()
    res << [subject: question[0], rank: json.rank, score: sum]
    println "${question[0]} scores ${json.rank} $sum"
}

println "Done!"
