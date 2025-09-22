import { Routes, Route } from "react-router-dom";
import { useEffect, useState } from "react";
import MenuList from "./MenuList";

function LoginPage() {
  return (
    <div style={{ textAlign: "center", marginTop: 50 }}>
      <h2>Login Sayfası</h2>
      <p>Spring Security’nin kendi login ekranı kullanılacaktır.</p>
    </div>
  );
}

function App() {
  const [isAdmin, setIsAdmin] = useState(false);

  useEffect(() => {
    // ➡️ Backend'e oturum sorgusu: kullanıcı giriş yapmış mı ve hangi rollere sahip?
    fetch("http://localhost:8080/api/auth/me", {
      credentials: "include", // 🍪 Session cookie taşınsın
    })
      .then((res) => {
        if (res.ok) return res.json();
        throw new Error("Giriş yapılmamış");
      })
      .then((data) => {
        // Eğer kullanıcı giriş yapmışsa ve ROLE_ADMIN rolüne sahipse admin modunu aç
        if (data.authenticated && data.roles?.includes("ROLE_ADMIN")) {
          setIsAdmin(true);
        } else {
          setIsAdmin(false);
        }
      })
      .catch(() => setIsAdmin(false)); // Login yoksa admin değil
  }, []);

  return (
    <Routes>
      {/* Ana sayfa: Menü */}
      <Route
        path="/"
        element={
          <div>

            <MenuList isAdmin={isAdmin} />
          </div>
        }
      />

      {/* İleride gerekirse özel login sayfası */}
      <Route path="/login" element={<LoginPage />} />
    </Routes>
  );
}

export default App;
