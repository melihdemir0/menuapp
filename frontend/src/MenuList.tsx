import { useEffect, useState } from "react";
import { api } from "./api";
import CategoryList from "./CategoryList";

interface Category {
  id: number;
  name: string;
}

interface MenuItem {
  id: number;
  name: string;
  description: string;
  imagePath: string;
  price: number;
  category: Category;
}

interface MenuListProps {
  isAdmin: boolean;
}

export default function MenuList({ isAdmin }: MenuListProps) {
  const [categories, setCategories] = useState<Category[]>([]);
  const [items, setItems] = useState<MenuItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [openCategory, setOpenCategory] = useState<number | null>(null);
  const [lang, setLang] = useState<"tr" | "en" | "de" | "ru" | "ar">("tr");
  const [openDropdown, setOpenDropdown] = useState(false);

  // 🔹 Modal state
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<MenuItem | null>(null);
  const [formData, setFormData] = useState({
    name: "",
    description: "",
    price: 0,
    imagePath: "",
    categoryId: 0,
  });
  const [file, setFile] = useState<File | null>(null);

  const fetchData = () => {
    setLoading(true);
    Promise.all([api.get(`/categories?lang=${lang}`), api.get(`/menu?lang=${lang}`)])
      .then(([catsRes, itemsRes]) => {
        setCategories(catsRes.data);
        setItems(itemsRes.data);
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchData();
  }, [lang]);

  if (loading) {
    return (
      <div
        style={{
          padding: 40,
          textAlign: "center",
          fontSize: 18,
          fontWeight: "bold",
          fontFamily: "Inter, Segoe UI, sans-serif",
        }}
      >
        Yükleniyor...
      </div>
    );
  }

  // ➕ Yeni ürün
  const handleAddItem = (categoryId: number) => {
    setEditingItem(null);
    setFormData({
      name: "",
      description: "",
      price: 0,
      imagePath: "",
      categoryId,
    });
    setFile(null);
    setIsModalOpen(true);
  };

  // ✏️ Düzenleme
  const handleEditItem = (item: MenuItem) => {
    setEditingItem(item);
    setFormData({
      name: item.name,
      description: item.description,
      price: item.price,
      imagePath: item.imagePath,
      categoryId: item.category.id,
    });
    setFile(null);
    setIsModalOpen(true);
  };

  // 🗑️ Silme
  const handleDeleteItem = async (id: number) => {
    if (!window.confirm("Bu ürünü silmek istediğinize emin misiniz?")) return;
    try {
      await api.delete(`/menu/${id}`);
      fetchData();
    } catch (err) {
      console.error(err);
      alert("Silme sırasında hata oluştu!");
    }
  };

  // Form input değişimi
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === "price" ? parseFloat(value) : value,
    }));
  };

  // Backend'e kaydet
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      let imagePath = formData.imagePath;

      // Eğer dosya seçildiyse önce yükle
      if (file) {
        const data = new FormData();
        data.append("file", file);

        const res = await api.post("/files/upload", data, {
          headers: { "Content-Type": "multipart/form-data" },
        });

        imagePath = res.data; // örn: "/images/tiramisu.jpg"
      }

      const dto = {
        name: formData.name,
        description: formData.description,
        imagePath: imagePath,
        price: formData.price,
        category: { id: formData.categoryId },
      };

      if (editingItem) {
        await api.put(`/menu/${editingItem.id}?lang=${lang}`, dto);
      } else {
        await api.post(`/menu?lang=${lang}`, dto);
      }

      setIsModalOpen(false);
      fetchData();
    } catch (err) {
      console.error(err);
      alert("Bir hata oluştu!");
    }
  };

  const flags: Record<string, string> = {
    tr: "https://flagcdn.com/w20/tr.png",
    en: "https://flagcdn.com/w20/gb.png",
    de: "https://flagcdn.com/w20/de.png",
    ru: "https://flagcdn.com/w20/ru.png",
    ar: "https://flagcdn.com/w20/sa.png",
  };

  return (
    <div
      style={{
        background: "#fafafa",
        minHeight: "100vh",
        padding: "40px 20px",
        fontFamily: "Inter, Segoe UI, sans-serif",
        width: "100%",
      }}
    >
      <div style={{ width: "100%" }}>
        {/* ✅ Sağ üst: Giriş/Çıkış butonu + dil menüsü */}
        <div
          style={{
            position: "fixed",
            top: 16,
            right: 16,
            zIndex: 1000,
            display: "flex",
            alignItems: "center",
            gap: "12px",
          }}
        >
          {!isAdmin && (
            <a
              href="http://localhost:8080/login"
              style={{
                padding: "8px 14px",
                background: "#007bff",
                color: "white",
                borderRadius: 6,
                fontWeight: 600,
                textDecoration: "none",
              }}
            >
              Giriş
            </a>
          )}

          {isAdmin && (
            <a
              href="http://localhost:8080/logout"
              style={{
                padding: "8px 14px",
                background: "#c62828",
                color: "white",
                borderRadius: 6,
                fontWeight: 600,
                textDecoration: "none",
              }}
            >
              Çıkış
            </a>
          )}

          {/* Bayrak menüsü */}
          <div style={{ position: "relative" }}>
            <div
              style={{
                background: "#fff",
                border: "1px solid #ccc",
                borderRadius: 8,
                boxShadow: "0 2px 6px rgba(0,0,0,0.1)",
                cursor: "pointer",
                padding: "6px 10px",
                display: "flex",
                alignItems: "center",
                gap: 6,
                minWidth: 70,
              }}
              onClick={() => setOpenDropdown(!openDropdown)}
            >
              <img
                src={flags[lang]}
                alt={lang}
                style={{ width: 20, height: 14, borderRadius: 2 }}
              />
              <span style={{ fontSize: "0.9rem", color: "#000" }}>
                {lang.toUpperCase()}
              </span>
            </div>

            {openDropdown && (
              <div
                style={{
                  position: "absolute",
                  top: "100%",
                  right: 0,
                  marginTop: 4,
                  background: "#fff",
                  border: "1px solid #ccc",
                  borderRadius: 8,
                  boxShadow: "0 2px 6px rgba(0,0,0,0.1)",
                  padding: "6px 10px",
                  display: "flex",
                  flexDirection: "column",
                  gap: 4,
                  zIndex: 2000,
                }}
              >
                {Object.keys(flags)
                  .filter((code) => code !== lang)
                  .map((code) => (
                    <div
                      key={code}
                      style={{
                        display: "flex",
                        alignItems: "center",
                        gap: 6,
                        cursor: "pointer",
                        padding: "4px 6px",
                      }}
                      onClick={() => {
                        setLang(code as typeof lang);
                        setOpenDropdown(false);
                      }}
                    >
                      <img
                        src={flags[code]}
                        alt={code}
                        style={{ width: 20, height: 14, borderRadius: 2 }}
                      />
                      <span style={{ fontSize: "0.9rem" }}>
                        {code.toUpperCase()}
                      </span>
                    </div>
                  ))}
              </div>
            )}
          </div>
        </div>

        {/* Menü kategorileri */}
        {categories.map((cat) => {
          const filtered = items.filter((i) => i.category?.id === cat.id);
          const isOpen = openCategory === cat.id;

          return (
            <div key={cat.id} style={{ marginBottom: 32 }}>
              <button
                onClick={() => setOpenCategory(isOpen ? null : cat.id)}
                style={{
                  width: "100%",
                  padding: "18px 20px",
                  marginTop: 12,
                  border: "none",
                  borderRadius: 12,
                  background: isOpen
                    ? "linear-gradient(90deg, #c62828, #e53935)"
                    : "#fff",
                  color: isOpen ? "#fff" : "#333",
                  fontSize: "1.2rem",
                  fontWeight: 700,
                  boxShadow: "0 2px 6px rgba(0,0,0,0.08)",
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                  cursor: "pointer",
                  transition: "all 0.2s ease",
                }}
              >
                {cat.name}
                <span>{isOpen ? "▲" : "▼"}</span>
              </button>

              {isAdmin && isOpen && (
                <button
                  onClick={() => handleAddItem(cat.id)}
                  style={{
                    marginTop: 8,
                    padding: "8px 14px",
                    background: "#2e7d32",
                    color: "white",
                    borderRadius: 6,
                    fontWeight: 600,
                    cursor: "pointer",
                  }}
                >
                  ➕ Yeni Ürün Ekle
                </button>
              )}

              {isOpen && (
                <div style={{ marginTop: 20 }}>
                  {filtered.length === 0 ? (
                    <p style={{ textAlign: "center", color: "#111" }}>
                      Bu kategoride ürün bulunmamaktadır.
                    </p>
                  ) : (
                    <div
                      style={{
                        display: "grid",
                        gridTemplateColumns:
                          "repeat(auto-fit, minmax(300px, 1fr))",
                        gap: 32,
                        marginTop: 20,
                        width: "100%",
                      }}
                    >
                      {filtered.map((item) => (
                        <div
                          key={item.id}
                          style={{
                            borderRadius: 16,
                            background: "#fff",
                            overflow: "hidden",
                            boxShadow: "0 4px 14px rgba(0,0,0,0.1)",
                            display: "flex",
                            flexDirection: "column",
                            transition:
                              "transform 0.2s ease, box-shadow 0.2s ease",
                          }}
                        >
                          {item.imagePath && (
                            <img
                              src={`${import.meta.env.VITE_API_BASE}${item.imagePath}`}
                              alt={item.name}
                              style={{
                                width: "100%",
                                height: "220px",       // sabit yükseklik
                                objectFit: "cover",    // taşıyorsa kırpar
                                borderTopLeftRadius: "16px",
                                borderTopRightRadius: "16px",
                                display: "block",
                              }}
                            />

                          )}

                          <div style={{ padding: "16px 20px" }}>
                            <h3
                              style={{
                                margin: "0 0 8px 0",
                                fontSize: "1.1rem",
                                fontWeight: 700,
                                color: "#222",
                              }}
                            >
                              {item.name}
                            </h3>
                            <span
                              style={{
                                display: "inline-block",
                                background: "#c62828",
                                color: "#fff",
                                fontWeight: 600,
                                padding: "4px 10px",
                                borderRadius: 6,
                                marginBottom: 10,
                                fontSize: "0.95rem",
                              }}
                            >
                              {item.price.toFixed(2)} ₺
                            </span>
                            <p
                              style={{
                                fontSize: "0.9rem",
                                color: "#555",
                                lineHeight: 1.4,
                              }}
                            >
                              {item.description}
                            </p>

                            {isAdmin && (
                              <div
                                style={{
                                  display: "flex",
                                  gap: 8,
                                  marginTop: 12,
                                }}
                              >
                                <button
                                  onClick={() => handleEditItem(item)}
                                  style={{
                                    flex: 1,
                                    padding: "8px 12px",
                                    background: "#f9a825",
                                    color: "#fff",
                                    borderRadius: 8,
                                    fontWeight: 600,
                                    cursor: "pointer",
                                  }}
                                >
                                  ✏ Düzenle
                                </button>
                                <button
                                  onClick={() => handleDeleteItem(item.id)}
                                  style={{
                                    flex: 1,
                                    padding: "8px 12px",
                                    background: "#d32f2f",
                                    color: "#fff",
                                    borderRadius: 8,
                                    fontWeight: 600,
                                    cursor: "pointer",
                                  }}
                                >
                                  🗑 Sil
                                </button>
                              </div>
                            )}
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}
            </div>
          );
        })}

        {/* 🔹 Admin için kategori yönetimi */}
        {isAdmin && (
          <div style={{ marginTop: 40 }}>
            <CategoryList lang={lang} onChanged={fetchData} />
          </div>
        )}
      </div>

      {/* 🔹 Ürün Ekle/Düzenle Modal */}
      {isModalOpen && (
        <div
          style={{
            position: "fixed",
            top: 0,
            left: 0,
            width: "100%",
            height: "100%",
            background: "rgba(0,0,0,0.5)",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            zIndex: 2000,
          }}
        >
          <div
            style={{
              background: "#fff",
              borderRadius: 12,
              padding: 20,
              width: "90%",
              maxWidth: 400,
            }}
          >
            <h2 style={{ marginBottom: 12 }}>
              {editingItem ? "Ürün Düzenle" : "Yeni Ürün Ekle"}
            </h2>
            <form onSubmit={handleSubmit}>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                placeholder="Ürün adı"
                style={{
                  width: "100%",
                  padding: 8,
                  marginBottom: 8,
                  border: "1px solid #ccc",
                  borderRadius: 6,
                }}
              />
              <textarea
                name="description"
                value={formData.description}
                onChange={handleChange}
                placeholder="Açıklama"
                style={{
                  width: "100%",
                  padding: 8,
                  marginBottom: 8,
                  border: "1px solid #ccc",
                  borderRadius: 6,
                  minHeight: 60,
                }}
              />
              <input
                type="number"
                name="price"
                value={formData.price}
                onChange={handleChange}
                placeholder="Fiyat"
                min={0}
                step="0.01"
                style={{
                  width: "100%",
                  padding: 8,
                  marginBottom: 8,
                  border: "1px solid #ccc",
                  borderRadius: 6,
                }}
              />

              {/* Dosya seçme */}
              <div style={{ marginBottom: 12 }}>
                <input
                  type="file"
                  accept="image/*"
                  onChange={(e) => {
                    const f = e.target.files?.[0];
                    if (f) {
                      setFile(f);
                      setFormData((prev) => ({
                        ...prev,
                        imagePath: f.name,
                      }));
                    }
                  }}
                />
              </div>

              <div style={{ display: "flex", justifyContent: "flex-end", gap: 8 }}>
                <button
                  type="button"
                  onClick={() => setIsModalOpen(false)}
                  style={{
                    padding: "6px 12px",
                    background: "#ccc",
                    borderRadius: 6,
                  }}
                >
                  İptal
                </button>
                <button
                  type="submit"
                  style={{
                    padding: "6px 12px",
                    background: "#4caf50",
                    color: "#fff",
                    borderRadius: 6,
                  }}
                >
                  Kaydet
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
