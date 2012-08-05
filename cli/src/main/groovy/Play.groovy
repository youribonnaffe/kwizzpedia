results.each {
    def currents = results.clone()
    currents.remove(it)
    Collections.shuffle(currents)
    def answers = currents*.noun.toSet().toList().subList(0, 3)
    answers << it.noun
    Collections.shuffle(answers)

    println "$it.verb of ${it.subject}?"
    println "Answers: $answers"
    println "Your choice?"
    def answer = reader.readLine()

    if (answers[answer.toInteger() - 1] == it.noun)
        println "Correct!"
    else
        System.err.println "Wrong!"

}