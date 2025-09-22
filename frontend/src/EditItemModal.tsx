import { useState } from "react";
import { api } from "./api";

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

interface EditItemModalProps {
  item: MenuItem;
  onClose: () => void;
  onSaved: () => void; // kaydet sonrası listeyi yenilemek için
}

export default function EditItemModal({ item, onClose, onSaved }: EditItemModalProps) {
  const [name, setName] = useState(item.name);
  const [description, setDescription] = useState(item.description);
  const [price, setPrice] = useState<number>(item.price);
  const [imagePath, setImagePath] = useState(item.imagePath);

  const handleSave = async () => {
    try {
      await api.put(`/menu/${item.id}?lang=${localStorage.getItem("lang") || "tr"}`, {
        id: item.id,
        name,
        description,
        imagePath,
        price,
        category: { id: item.category.id, name: item.category.name },
      });
      onSaved();
      onClose();
    } catch (err) {
      console.error("Ürün güncellenemedi", err);
      alert("Ürün güncellenemedi!");
    }
  };


  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-40 z-50">
      <div className="bg-white p-6 rounded-lg shadow-md w-96">
        <h2 className="text-xl font-bold mb-4">Ürünü Düzenle</h2>

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
          <button onClick={handleSave} className="px-4 py-2 bg-yellow-600 text-white rounded">
            Kaydet
          </button>
        </div>
      </div>
    </div>
  );
}
