import React, { useState } from "react";
import {Link, useNavigate} from "react-router-dom";

type Book = {
    id?: number;
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


const AddBookForm: React.FC = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState<Book>({
        title: "",
        author: "",
        publisher: "",
        releaseDate: "",
    });

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
        setFormData((prev) => ({
            ...prev,
            [id]: value,
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        resetErrors()

        try {
            const response = await fetch("/api/books", {
                method: "POST",
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
            <h3>Add book</h3>
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
                            Add
                        </button>
                    </form>
                </div>
            </div>
            <div>
                <Link to={`/books`}>Back to list</Link>
            </div>
        </>
    );
};

export default AddBookForm;
