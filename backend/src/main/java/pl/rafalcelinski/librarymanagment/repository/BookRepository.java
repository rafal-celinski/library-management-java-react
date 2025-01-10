package pl.rafalcelinski.librarymanagment.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.rafalcelinski.librarymanagment.entity.Book;


public interface BookRepository extends JpaRepository<Book, Long> {


}
