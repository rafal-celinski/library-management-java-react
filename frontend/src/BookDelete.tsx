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

    const handleClick = async () => {
        try {
            const response = await fetch(`/api/books/${id}`, {
                method: "DELETE"
            });

            if (response.status == 401) {
                navigate('/login');
                return;
            }

            if (!response.ok) {
                throw new Error("Failed to delete book");
            }

            navigate('/books');
            return;
        }
        catch (error) {
            console.error(error);
        }
    }

    return (
        <>
            <h2>Delete book</h2>
            <h3>Are you sure you want to do this?</h3>

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
                        <button onClick={handleClick} className="btn btn-danger">
                            Delete
                        </button>
                    </div>
                </div>
            )}
            <div>
                <Link to={`/books`}>Back to list</Link>
            </div>
        </>
    );
};

export default BookDetails
