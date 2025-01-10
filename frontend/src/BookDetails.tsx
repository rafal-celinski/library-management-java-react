import React, {useEffect, useState} from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import {Link, useNavigate, useParams} from "react-router-dom";

type Book = {
    id: number;
    title: string;
    author: string;
    publisher: string;
    releaseDate: string;
};


const BookDetails: React.FC = () => {
    const navigate = useNavigate();
    const { id } = useParams();
    const [book, setBook] = useState<Book | null>(null);
    const userRole = sessionStorage.getItem("userRole");


    const fetchBook = async (bookId: string) => {
        try {
            const response = await fetch(`/api/books/${bookId}`);

            if (response.status == 401) {
                navigate('/login');
                return;
            }

            if (!response.ok) {
                throw new Error("Failed to fetch book");
            }

            const data = await response.json()
            setBook(data);
        }
        catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        if (id) {
            fetchBook(id);
        }
    }, [id]);

    return (
        <>
            <h2>Book details</h2>
                {!book ? (
                    <p>Loading book...</p>
                ) : (
                    <div>
                        <div>
                            <hr/>
                            <dl className="row">
                                <dt className="col-sm-2">Title</dt>
                                <dd className="col-sm-10">{book.title}</dd>

                                <dt className="col-sm-2">Author</dt>
                                <dd className="col-sm-10">{book.author}</dd>

                                <dt className="col-sm-2">Publisher</dt>
                                <dd className="col-sm-10">{book.publisher}</dd>

                                <dt className="col-sm-2">Release date</dt>
                                <dd className="col-sm-10">{book.releaseDate}</dd>
                            </dl>
                        </div>
                        <div>
                            {userRole == "LIBRARIAN" && (<><Link to={`/book/${book.id}/edit`}>Edit</Link><span> | </span></>)}
                            {userRole == "LIBRARIAN" && (<><Link to={`/book/${book.id}/delete`}>Delete</Link><span> | </span></>)}
                            <Link to={`/books`}>Back to list</Link>
                        </div>
                    </div>
                )}
        </>
    );
};

export default BookDetails
