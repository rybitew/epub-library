//package pl.app.epublibrary.entities;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
//import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
//import org.springframework.data.cassandra.core.mapping.Table;
//
//import java.util.UUID;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(value = "authors_by_book_id")
//public class AuthorByBookId {
//
//    @PrimaryKeyColumn(
//            type = PrimaryKeyType.PARTITIONED,
//            name = "book_id"
//    )
//    private UUID id;
//
//    private String author;
//}
