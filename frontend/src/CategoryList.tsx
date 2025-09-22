import { useEffect, useState } from "react";
import { api } from "./api";

type Lang = "tr" | "en" | "de" | "ru" | "ar";

interface Category {
  id: number;
  name: string;
}

export default function CategoryList({
  lang = "tr",
  onChanged,
}: {
  lang: Lang;
  onChanged?: () => void;
}) {
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [newName, setNewName] = useState("");
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editingName, setEditingName] = useState("");

  useEffect(() => {
    fetchCategories();
  }, [lang]);

  const fetchCategories = async () => {
    setLoading(true);
    try {
      const res = await api.get(`/categories?lang=${lang}`);
      setCategories(res.data);
    } catch (err) {
      console.error("Kategori çekilemedi", err);
      alert("Kategoriler alınırken hata oluştu.");
    } finally {
      setLoading(false);
    }
  };

  const addCategory = async () => {
    if (!newName.trim()) return alert("Kategori adı boş olamaz!");
    await api.post(`/categories?lang=${lang}`, { name: newName.trim() });
    setNewName("");
    fetchCategories();
    onChanged?.();
  };

  const saveEdit = async () => {
    if (!editingName.trim()) return alert("Kategori adı boş olamaz!");
    if (editingId == null) return;
    await api.put(`/categories/${editingId}?lang=${lang}`, { name: editingName.trim() });
    setEditingId(null);
    setEditingName("");
    fetchCategories();
    onChanged?.();
  };

  const deleteCategory = async (id: number) => {
    if (!confirm("Silmek istediğine emin misin?")) return;
    await api.delete(`/categories/${id}`);
    fetchCategories();
    onChanged?.();
  };

  return (
    <div
      style={{
        marginTop: 20,
        padding: 20,
        border: "1px solid #ddd",
        borderRadius: 12,
        background: "#fafafa",
      }}
    >
      <h2 style={{ color: "#111", marginBottom: 16 }}>📂 Kategori Yönetimi</h2>

      {loading ? (
        <p style={{ color: "#111" }}>Yükleniyor...</p>
      ) : (
        <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
          {categories.map((cat, index) => (
            <div
              key={cat.id}
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                padding: "10px 14px",
                borderRadius: 8,
                background: index % 2 === 0 ? "#fff" : "#f3f3f3",
                boxShadow: "0 1px 3px rgba(0,0,0,0.08)",
              }}
            >
              {editingId === cat.id ? (
                <>
                  <input
                    value={editingName}
                    onChange={(e) => setEditingName(e.target.value)}
                    style={{
                      flex: 1,
                      padding: 6,
                      border: "1px solid #ccc",
                      borderRadius: 6,
                      marginRight: 8,
                      color: "#000",
                    }}
                  />
                  <button
                    onClick={saveEdit}
                    style={{
                      background: "#2e7d32",
                      color: "#fff",
                      border: "none",
                      borderRadius: 6,
                      padding: "6px 12px",
                      marginRight: 6,
                      cursor: "pointer",
                    }}
                  >
                    ✔ Kaydet
                  </button>
                  <button
                    onClick={() => setEditingId(null)}
                    style={{
                      background: "#9e9e9e",
                      color: "#fff",
                      border: "none",
                      borderRadius: 6,
                      padding: "6px 12px",
                      cursor: "pointer",
                    }}
                  >
                    ✖ İptal
                  </button>
                </>
              ) : (
                <>
                  <span style={{ fontSize: "1rem", fontWeight: 500, color: "#111" }}>
                    {cat.name}
                  </span>
                  <div style={{ display: "flex", gap: 8 }}>
                    <button
                      onClick={() => {
                        setEditingId(cat.id);
                        setEditingName(cat.name);
                      }}
                      style={{
                        background: "#f9a825",
                        color: "#fff",
                        border: "none",
                        borderRadius: 6,
                        padding: "6px 12px",
                        cursor: "pointer",
                      }}
                    >
                      ✏ Düzenle
                    </button>
                    <button
                      onClick={() => deleteCategory(cat.id)}
                      style={{
                        background: "#d32f2f",
                        color: "#fff",
                        border: "none",
                        borderRadius: 6,
                        padding: "6px 12px",
                        cursor: "pointer",
                      }}
                    >
                      🗑 Sil
                    </button>
                  </div>
                </>
              )}
            </div>
          ))}
        </div>
      )}

      {/* Yeni kategori ekleme */}
      <div style={{ marginTop: 20, display: "flex", gap: 8 }}>
        <input
          value={newName}
          onChange={(e) => setNewName(e.target.value)}
          placeholder="Yeni kategori adı"
          style={{
            flex: 1,
            padding: 8,
            border: "1px solid #ccc",
            borderRadius: 6,
            color: "#000",
          }}
        />
        <button
          onClick={addCategory}
          style={{
            padding: "8px 14px",
            background: "#1976d2",
            color: "#fff",
            border: "none",
            borderRadius: 6,
            cursor: "pointer",
          }}
        >
          ➕ Ekle
        </button>
      </div>
    </div>
  );
}
