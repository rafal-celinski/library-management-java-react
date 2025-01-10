import React, {useEffect, useState} from "react";
import {Link, useNavigate, useParams} from "react-router-dom";

type Book = {
    id: number;
    title: string;
    author: string;
    publisher: string;
    releaseDate: string;
};

type FieldErrors = {
    title?: string;
    author?: string;
    publisher?: string;
    releaseDate?: string;
};

type Errors = {
    globalError: string | null;
    fieldErrors: FieldErrors;
};


const EditBookForm: React.FC = () => {
    const navigate = useNavigate();
    const { id } = useParams();
    const [formData, setFormData] = useState<Book | null>(null);

    const [errors, setErrors] = useState<Errors>({
        globalError: null,
        fieldErrors: {}
    });

    const resetErrors = () => {
        setErrors({
            globalError: null,
            fieldErrors: {}
        });
    };

    const updateFieldErrors = (newFieldErrors: { [key: string]: string }) => {
        setErrors(prevState => ({
            ...prevState,
            fieldErrors: newFieldErrors
        }));
    };

    const updateGlobalError = (newGlobalError: string) => {
        setErrors(prevState => ({
            ...prevState,
            globalError: newGlobalError
        }));
    };


    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { id, value } = e.target;
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        setFormData((prev) => ({
            ...prev,
            [id]: value,
        }));
    };

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
            setFormData(data);
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

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        resetErrors()

        try {
            const response = await fetch(`/api/books/${id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(formData),
            });

            if (response.status == 401) {
                navigate('/login');
                return;
            }

            if (!response.ok) {
                const data = await response.json()

                const globalError = data?.message || null;
                const fieldErrors = data?.details || {};

                updateGlobalError(globalError)
                updateFieldErrors(fieldErrors)
            }

            navigate('/books');
            return;

        } catch (error) {
            console.error(error);
            updateGlobalError("An error occurred");
        }
    };

    return (
        <>
            <h2>Edit book</h2>
            {!formData ? (
                <p>Loading book...</p>
                ) : (
            <div className="row">
                <div className="col-md-4">
                    <form onSubmit={handleSubmit}>
                        {errors.globalError && <div className="text-danger text-start">{errors.globalError}</div>}
                        <div className="form-group">
                            <label className="control-label">Title</label>
                            {"title" in errors.fieldErrors &&
                                <div className="text-danger text-start">{errors.fieldErrors["title"]}</div>}
                            <input
                                id="title"
                                type="text"
                                className="form-control"
                                value={formData.title}
                                onChange={handleChange}
                            />
                        </div>
                        <div className="form-group">
                            <label className="control-label">Author</label>
                            {"author" in errors.fieldErrors &&
                                <div className="text-danger text-start">{errors.fieldErrors["author"]}</div>}
                            <input
                                id="author"
                                type="text"
                                className="form-control"
                                value={formData.author}
                                onChange={handleChange}
                            />
                        </div>
                        <div className="form-group">
                            <label className="control-label">Publisher</label>
                            {"publisher" in errors.fieldErrors &&
                                <div className="text-danger text-start">{errors.fieldErrors["publisher"]}</div>}
                            <input
                                id="publisher"
                                type="text"
                                className="form-control"
                                value={formData.publisher}
                                onChange={handleChange}
                            />
                        </div>
                        <div className="form-group">
                            <label className="control-label">Release date</label>
                            {"releaseDate" in errors.fieldErrors &&
                                <div className="text-danger text-start">{errors.fieldErrors["releaseDate"]}</div>}
                            <input
                                id="releaseDate"
                                type="date"
                                className="form-control"
                                value={formData.releaseDate}
                                onChange={handleChange}
                            />
                        </div>
                        <button type="submit" className="btn btn-primary">
                            Edit
                        </button>
                    </form>
                </div>
            </div>
                )}
            <div>
                <Link to={`/books`}>Back to list</Link>
            </div>
        </>
    );
};

export default EditBookForm;
