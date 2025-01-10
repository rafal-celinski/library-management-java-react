import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import {Link, useNavigate} from "react-router-dom";

type FieldErrors = {
    username?: string;
    password?: string;
};

type Errors = {
    globalError: string | null;
    fieldErrors: FieldErrors;
};


const LoginForm: React.FC = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        username: "",
        password: "",
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
            const response = await fetch("/api/login", {
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

            const data = await response.json()
            sessionStorage.setItem("userRole", data.userRole);

            navigate('/');
            return;
        }
        catch (error) {
            console.error("Login failed", error);
            updateGlobalError("An error occurred during login.");
        }
    }

    return (
        <div className="container-fluid d-flex justify-content-center align-items-center">
            <div className="card border rounded p-4">
                <h3 className="card-title text-center">Login</h3>
                <form onSubmit={handleSubmit}>
                    {errors.globalError && <div className="text-danger text-start">{errors.globalError}</div>}
                    <div className="mb-3">
                        <label className="form-label text-start d-block">
                            Username
                        </label>
                        {"username" in errors.fieldErrors && <div className="text-danger text-start">{errors.fieldErrors["username"]}</div>}
                        <input
                            className="form-control"
                            id="username"
                            placeholder="Enter your username"
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
                            placeholder="Enter your password"
                            value={formData.password}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="d-grid">
                        <button type="submit" className="btn btn-primary">
                            Login
                        </button>
                    </div>
                </form>
                <div className="mt-3 text-center">
                    <small>
                        Don't have an account? <Link to="/register">Create new account</Link>
                    </small>
                </div>
            </div>
        </div>
    );
};

export default LoginForm;
