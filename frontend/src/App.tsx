
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';

import RegisterForm from "./RegisterForm.tsx";
import LoginForm from "./LoginForm.tsx";
import BookList from "./BookList.tsx";
import BookDetails from "./BookDetails.tsx";
import AddBookForm from "./AddBookForm.tsx";
import EditBookForm from "./EditBookForm.tsx";
import BookDelete from "./BookDelete.tsx";
import Logout from "./Logout.tsx";


const App = () => {
    return (
        <Router>
              <Routes>
                  <Route path="/" element={<Navigate to="/books" replace />} />
                  <Route path="/register" element={<RegisterForm/>}/>
                  <Route path="/login" element={<LoginForm/>}/>
                  <Route path="/logout" element={<Logout/>}/>
                  <Route path="/books" element={<BookList/>}/>
                  <Route path="/book/:id" element={<BookDetails/>}/>
                  <Route path="/book/new" element={<AddBookForm/>}/>
                  <Route path="/book/:id/edit" element={<EditBookForm/>}/>
                  <Route path="/book/:id/delete" element={<BookDelete/>}/>
              </Routes>
        </Router>
);
};

export default App;
