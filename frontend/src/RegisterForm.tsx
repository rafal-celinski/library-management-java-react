import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import {Link, useNavigate} from "react-router-dom";

type FieldErrors = {
    username?: string;
    password?: string;
    email?: string;
    phoneNumber?: string;
    firstName?: string;
    lastName?: string;
};

type Errors = {
    globalError: string | null;
    fieldErrors: FieldErrors;
};


const RegisterForm: React.FC = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        username: "",
        password: "",
        email: "",
        phoneNumber: "",
        firstName: "",
        lastName: "",
    })

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
            const response = await fetch("/api/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(formData),
            });

            if (!response.ok) {
                const data = await response.json()

                const globalError = data?.message || null;
                const fieldErrors = data?.details || {};

                updateGlobalError(globalError)
                updateFieldErrors(fieldErrors)
            }

            navigate('/');
            return;
        }
        catch (error) {
            console.error("Login failed", error);
            updateGlobalError("An error occurred during register.");
        }
    }

    return (
        <div className="container-fluid d-flex justify-content-center align-items-center">
            <div className="card border rounded p-4">

                <h3 className="card-title text-center">Create your account</h3>
                <form onSubmit={handleSubmit}>
                    {errors.globalError && <div className="text-danger text-start">{errors.globalError}</div>}
                    <div className="mb-3">
                        <label className="form-label text-start d-block">
                            Username
                        </label>
                        {"username" in errors.fieldErrors && <div className="text-danger text-start">{errors.fieldErrors["username"]}</div>}
                        <input
                            type="text"
                            className="form-control"
                            id="username"
                            value={formData.username}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="mb-3">
                        <label className="form-label text-start d-block">
                            Password
                        </label>
                        {"password" in errors.fieldErrors && <div className="text-danger text-start">{errors.fieldErrors["password"]}</div>}
                        <input
                            type="password"
                            className="form-control"
                            id="password"
                            value={formData.password}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="mb-3">
                        <label className="form-label text-start d-block">
                            Email
                        </label>
                        {"email" in errors.fieldErrors && <div className="text-danger text-start">{errors.fieldErrors["email"]}</div>}
                        <input
                            type="text"
                            className="form-control"
                            id="email"
                            value={formData.email}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="mb-3">
                        <label className="form-label text-start d-block">
                            Phone number
                        </label>
                        {"phoneNumber" in errors.fieldErrors && <div className="text-danger text-start">{errors.fieldErrors["phoneNumber"]}</div>}
                        <input
                            type="text"
                            className="form-control"
                            id="phoneNumber"
                            value={formData.phoneNumber}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="mb-3">
                        <label className="form-label text-start d-block">
                            First name
                        </label>
                        {"firstName" in errors.fieldErrors && <div className="text-danger text-start">{errors.fieldErrors["firstName"]}</div>}
                        <input
                            type="text"
                            className="form-control"
                            id="firstName"
                            value={formData.firstName}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="mb-3">
                        <label className="form-label text-start d-block">
                            Last name
                        </label>
                        {"lastName" in errors.fieldErrors && <div className="text-danger text-start">{errors.fieldErrors["lastName"]}</div>}
                        <input
                            type="text"
                            className="form-control"
                            id="lastName"
                            value={formData.lastName}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="d-grid">
                        <button type="submit" className="btn btn-primary">
                            Create
                        </button>
                    </div>
                </form>
                <div className="mt-3 text-center">
                    <small>
                        Already have an account? <Link to={`/login`}>Sign in</Link>
                    </small>
                </div>
            </div>
        </div>
    );
};

export default RegisterForm;



