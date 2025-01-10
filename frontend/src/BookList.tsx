import React, {useEffect, useState} from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import {Link, useNavigate} from "react-router-dom";

type Book = {
    id: number;
    title: string;
    author: string;
    publisher: string;
    releaseDate: string;
};

const BookList: React.FC = () => {
    const navigate = useNavigate();
    const [books, setBooks] = useState<Book[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const userRole = sessionStorage.getItem("userRole");

    const fetchBooks = async () => {
        try {
            const response = await fetch("/api/books");

            if (response.status == 401) {
                navigate('/login');
                return;
            }

            if (!response.ok) {
                throw new Error("Failed to fetch books");
            }

            const data = await response.json()
            setBooks(data.content);
            setLoading(false);
        }
        catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        fetchBooks();
    }, []);

    return (
        <>
            <h2>Books</h2>
            <p>
                {userRole == "LIBRARIAN" && (<Link to={`/book/new`}>Create new</Link>)}
            </p>

            {loading ? (
                <p>Loading books...</p>
            ) : (
                <table className="table">
                    <thead>
                    <tr>
                        <th>Title</th>
                        <th>Author</th>
                        <th>Publisher</th>
                        <th>ReleaseDate</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {books.map((book) => (
                        <tr key={book.id}>
                            <td>{book.title}</td>
                            <td>{book.author}</td>
                            <td>{book.publisher}</td>
                            <td>{book.releaseDate}</td>
                            <td>
                                <Link to={`/book/${book.id}`}>Details</Link>
                                {userRole == "LIBRARIAN" && (<><span> | </span><Link to={`/book/${book.id}/edit`}>Edit</Link></>)}
                                {userRole == "LIBRARIAN" && (<><span> | </span><Link to={`/book/${book.id}/delete`}>Delete</Link></>)}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </>
    );
};

export default BookList