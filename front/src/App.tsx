import { useState, useEffect, ChangeEvent, FormEvent } from 'react';
import './App.css';

interface Citation {
  idcitation: number;
  nomH: string;
  citationcol: string;
  anne: number;
}

interface FormDataWithStrings {
  idcitation: string;
  nomH: string;
  citationcol: string;
  anne: string;
}

function App() {
  const [citations, setCitations] = useState<Citation[]>([]);
  const [formData, setFormData] = useState<FormDataWithStrings>({
    idcitation: '',
    nomH: '',
    citationcol: '',
    anne: ''
  });
  const [isSubmitted, setIsSubmitted] = useState<boolean>(false);
  const [isEditing, setIsEditing] = useState<boolean>(false);

  // Fetch all citations on component mount
  useEffect(() => {
    fetchCitations();
  }, []);

  // Fetch all citations
  const fetchCitations = async () => {
    try {
      const response = await fetch('http://localhost:8080/citations');
      if (response.ok) {
        const data = await response.json();
        setCitations(data);
      } else {
        console.error('Failed to fetch citations');
      }
    } catch (error) {
      console.error('Error fetching citations:', error);
    }
  };

  // Handle form field change
  const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  // Handle form submission
  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    
    // Convert string values to numbers for idcitation and anne
    const citationData: Citation = {
      idcitation: parseInt(formData.idcitation) || 0,
      nomH: formData.nomH,
      citationcol: formData.citationcol,
      anne: parseInt(formData.anne) || 0
    };
    
    const url = isEditing 
      ? `http://localhost:8080/modifierCitation/${citationData.idcitation}`
      : 'http://localhost:8080/ajoutCitation';
    
    const method = isEditing ? 'PUT' : 'POST';
    
    try {
      const response = await fetch(url, {
        method: method,
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(citationData)
      });
      
      if (response.ok) {
        setIsSubmitted(true);
        fetchCitations(); // Refresh the list
        resetForm();
      } else {
        alert(`Error ${isEditing ? 'updating' : 'submitting'} the form`);
      }
    } catch (error) {
      console.error('Error submitting form:', error);
      alert('Error processing your request');
    }
  };

  // Delete a citation
  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this citation?')) {
      try {
        const response = await fetch(`http://localhost:8080/supprimerCitation/${id}`, {
          method: 'DELETE'
        });
        
        if (response.ok) {
          fetchCitations(); // Refresh the list
          alert('Citation deleted successfully');
        } else {
          alert('Error deleting the citation');
        }
      } catch (error) {
        console.error('Error deleting citation:', error);
        alert('Error processing your request');
      }
    }
  };

  // Edit a citation
  const handleEdit = (citation: Citation) => {
    setFormData({
      idcitation: citation.idcitation.toString(),
      nomH: citation.nomH,
      citationcol: citation.citationcol,
      anne: citation.anne.toString()
    });
    setIsEditing(true);
    setIsSubmitted(false);
  };

  // Reset form
  const resetForm = () => {
    setFormData({
      idcitation: '',
      nomH: '',
      citationcol: '',
      anne: ''
    });
    setIsEditing(false);
  };

  // Cancel editing
  const handleCancel = () => {
    resetForm();
  };

  return (
    <div className="app-container">
      <h1>Gestion des Citations</h1>
      
      <div className="citations-list">
        <h2>Liste des Citations</h2>
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Auteur</th>
              <th>Citation</th>
              <th>Année</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {citations.length > 0 ? (
              citations.map((citation) => (
                <tr key={citation.idcitation}>
                  <td>{citation.idcitation}</td>
                  <td>{citation.nomH}</td>
                  <td>{citation.citationcol}</td>
                  <td>{citation.anne}</td>
                  <td>
                    <button onClick={() => handleEdit(citation)}>Modifier</button>
                    <button onClick={() => handleDelete(citation.idcitation)}>Supprimer</button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={5}>Aucune citation disponible</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <div className="form-container">
        {!isSubmitted ? (
          <>
            <h2>{isEditing ? 'Modifier une Citation' : 'Ajouter une Citation'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>ID Citation:</label>
                <input
                  type="number"
                  name="idcitation"
                  value={formData.idcitation}
                  onChange={handleChange}
                  required
                  disabled={isEditing}
                />
              </div>
              
              <div className="form-group">
                <label>Auteur:</label>
                <input
                  type="text"
                  name="nomH"
                  value={formData.nomH}
                  onChange={handleChange}
                  required
                />
              </div>
              
              <div className="form-group">
                <label>Citation:</label>
                <textarea
                  name="citationcol"
                  value={formData.citationcol}
                  onChange={handleChange}
                  required
                ></textarea>
              </div>
              
              <div className="form-group">
                <label>Année:</label>
                <input
                  type="number"
                  name="anne"
                  value={formData.anne}
                  onChange={handleChange}
                  required
                />
              </div>
              
              <div className="form-actions">
                <input type="submit" value={isEditing ? 'Mettre à jour' : 'Ajouter'} />
                {isEditing && <button type="button" onClick={handleCancel}>Annuler</button>}
              </div>
            </form>
          </>
        ) : (
          <>
            <h2>Citation {isEditing ? 'modifiée' : 'ajoutée'} avec succès!</h2>
            <button onClick={() => setIsSubmitted(false)}>Ajouter une autre citation</button>
          </>
        )}
      </div>
    </div>
  );
}

export default App;