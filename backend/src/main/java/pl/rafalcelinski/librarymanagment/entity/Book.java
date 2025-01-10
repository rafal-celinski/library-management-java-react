package pl.rafalcelinski.librarymanagment.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String author;
    private String publisher;
    private LocalDate releaseDate;
    private boolean isPermanentlyUnavailable = false;

    public Book() {}

    public Book(String title, String author, String publisher, LocalDate releaseDate) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.releaseDate = releaseDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isPermanentlyUnavailable() {
        return isPermanentlyUnavailable;
    }

    public void setPermanentlyUnavailable(boolean permanentlyUnavailable) {
        isPermanentlyUnavailable = permanentlyUnavailable;
    }
}
