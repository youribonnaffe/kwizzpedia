import groovy.sparql.Sparql

sparql = new Sparql("http://dbpedia.org/sparql")

def query = """
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX dc: <http://purl.org/dc/elements/1.1/>
PREFIX : <http://dbpedia.org/resource/>
PREFIX dbpedia2: <http://dbpedia.org/property/>
PREFIX dbpedia: <http://dbpedia.org/>
PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
PREFIX category: <http://dbpedia.org/resource/Category:>
PREFIX dcterms: <http://purl.org/dc/terms/>

SELECT ?subject ?noun ("Capital of" as ?verb)  ?subject_fr  ?subject_en ?noun_fr ?noun_en
where { ?subject a <http://dbpedia.org/ontology/Country> ;
dbpedia2:capital ?noun .
?noun a <http://dbpedia.org/ontology/PopulatedPlace> .
?subject dcterms:subject category:European_countries ;
rdfs:label ?subject_fr ;
rdfs:label ?subject_en .
?noun rdfs:label ?noun_fr .
?noun rdfs:label ?noun_en .
FILTER langMatches( lang(?subject_fr), 'fr') .
FILTER langMatches( lang(?subject_en), 'en') .
FILTER langMatches( lang(?noun_fr), 'fr') .
FILTER langMatches( lang(?noun_en), 'en')
}
ORDER BY(?subject)

"""

def results = []
sparql.eachRow query, { row ->
    results << row.collectEntries {key, value -> [key, new String(value.getBytes("iso-8859-1"), "utf-8")] }
}

println "Found ${results.size()} results"

def category = "geography"
new File("questions.txt").withWriter { out ->
    results.each {
        println "${it.subject};${it.subject_fr};${it.subject_en};${it.verb};${it.noun};${it.noun_fr};${it.noun_en}"
        out.writeLine("${it.subject};${it.subject_fr};${it.subject_en};${it.verb};${it.noun};${it.noun_fr};${it.noun_en};$category")
    }
}

println "Done!"

