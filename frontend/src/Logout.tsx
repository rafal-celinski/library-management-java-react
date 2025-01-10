import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Logout = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const performLogout = async () => {
            try {
                const response = await fetch('/api/logout', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });

                if (response.ok) {
                    console.log("Logged out successfully");
                    navigate('/login');
                } else {
                    console.error("Failed to log out");
                }
            } catch (error) {
                console.error("Error during logout:", error);
            }
        };
        performLogout();
    }, [navigate]);

    return <p>Logging out...</p>;
};

export default Logout;
