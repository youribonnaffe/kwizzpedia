import com.google.appengine.tools.remoteapi.RemoteApiInstaller
import com.google.appengine.tools.remoteapi.RemoteApiOptions
import com.google.appengine.api.datastore.*

// ...
RemoteApiOptions options = new RemoteApiOptions()
    //.server("kwizzpedia.appspot.com", 443)
    .server("localhost", 8080)
    .credentials("", "");

RemoteApiInstaller installer = new RemoteApiInstaller();
installer.install(options);

DatastoreService ds = DatastoreServiceFactory.getDatastoreService();



Query q = new Query("question");
q.setKeysOnly();

PreparedQuery pq = ds.prepare(q);
List<Key> keys = new ArrayList<Key>();
for(Entity e : pq.asIterator()) {
    keys.add(e.getKey());
}
ds.delete(keys);


String[] lines = new File("questions.txt").text.split('\n')
List<String[]> rows = lines.collect {it.split(';')}
def random = new Random()
rows.each { row ->
    def entity = new Entity("question")
    entity.setProperty("subject", row[2])
    entity.setProperty("answer", row[6])
    entity.setProperty("category", row[7])
    entity.setProperty("phrase", row[3])
    entity.setProperty("random_number", random.nextFloat())

    def currents = rows.clone()
    currents.remove(row)
    Collections.shuffle(currents)
    def answers = currents.collect{it[6]}.toSet().toList().subList(0, 3)
    answers << row[6]
    Collections.shuffle(answers)

    entity.setProperty("choices", answers)
    ds.put(entity)

}
installer.uninstall();