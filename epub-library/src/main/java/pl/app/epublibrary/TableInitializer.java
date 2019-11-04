//package pl.app.epublibrary;
//
//import com.datastax.driver.core.DataType;
//import com.datastax.driver.core.Session;
//import com.datastax.driver.core.schemabuilder.SchemaBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TableInitializer {
//    Session session;
//
//    @Autowired
//    public TableInitializer(Session session) {
//        this.session = session;
//    }
//
//    public void initialize(){
//        initializeBooks();
//    }
//
//    private void initializeBooks() {
//        session.execute(
//                "CREATE TABLE IF NOT EXISTS books(" +
//                        "  title text," +
//                        "  authors frozen<map<text, text>>," +
//                        "  release_date timestamp," +
//                        "  id UUID," +
//                        "  age INT," +
//                        "  profession TEXT," +
//                        "  salary INT," +
//                        "  PRIMARY KEY((country), first_name, last_name, id)" +
//                        ");"
//        )
//    }
//}
