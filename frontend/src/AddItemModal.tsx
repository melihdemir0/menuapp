import { useState } from "react";
import { api } from "./api";

interface AddItemModalProps {
  categoryId: number;
  onClose: () => void;
  onSaved: () => void; // kaydetmeden sonra listeyi yenilemek için
}

export default function AddItemModal({ categoryId, onClose, onSaved }: AddItemModalProps) {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [price, setPrice] = useState<number>(0);
  const [imagePath, setImagePath] = useState("");

  const handleSave = async () => {
    try {
      await api.post(`/menu?lang=${localStorage.getItem("lang") || "tr"}`, {
        name,
        description,
        imagePath,
        price,
        category: { id: categoryId, name: "" }, // backend CategoryDTO bekliyor
      });
      onSaved();
      onClose();
    } catch (err) {
      console.error("Ürün eklenemedi", err);
      alert("Ürün eklenemedi!");
    }
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-40 z-50">
      <div className="bg-white p-6 rounded-lg shadow-md w-96">
        <h2 className="text-xl font-bold mb-4">Yeni Ürün Ekle</h2>

        <input
          type="text"
          placeholder="Ürün adı"
          value={name}
          onChange={(e) => setName(e.target.value)}
          className="w-full border p-2 rounded mb-3"
        />
        <textarea
          placeholder="Açıklama"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          className="w-full border p-2 rounded mb-3"
        />
        <input
          type="number"
          placeholder="Fiyat"
          value={price}
          onChange={(e) => setPrice(parseFloat(e.target.value))}
          className="w-full border p-2 rounded mb-3"
        />
        <input
          type="text"
          placeholder="Resim yolu (/images/...jpg)"
          value={imagePath}
          onChange={(e) => setImagePath(e.target.value)}
          className="w-full border p-2 rounded mb-3"
        />

        <div className="flex justify-end gap-2">
          <button onClick={onClose} className="px-4 py-2 bg-gray-400 text-white rounded">
            İptal
          </button>
          <button onClick={handleSave} className="px-4 py-2 bg-green-600 text-white rounded">
            Kaydet
          </button>
        </div>
      </div>
    </div>
  );
}
