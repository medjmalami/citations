package com.example.demo.controller;

import com.example.demo.model.Citation;
import com.example.demo.dao.CitationDao;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Enable CORS for all origins
public class CitationController {
    private CitationDao citationDao = new CitationDao();
    
    // Original endpoint for adding a citation
    @PostMapping("/ajoutCitation")
    public boolean ajouterCitation(@RequestBody Citation citation) {
        System.out.println("Ajouter citation: " + citation.getNomH());
        return citationDao.insertCitation(citation);
    }
    
    // New endpoint to get all citations
    @GetMapping("/citations")
    public List<Citation> getAllCitations() {
        System.out.println("Get all citations");
        return citationDao.getAllCitations();
    }
    
    // New endpoint to get a specific citation by ID
    @GetMapping("/citation/{id}")
    public Citation getCitationById(@PathVariable int id) {
        System.out.println("Get citation with ID: " + id);
        return citationDao.getCitationById(id);
    }
    
    // New endpoint to update a citation
    @PutMapping("/modifierCitation/{id}")
    public boolean modifierCitation(@PathVariable int id, @RequestBody Citation citation) {
        System.out.println("Modifier citation with ID: " + id);
        // Ensure the ID in the path matches the ID in the citation object
        if (id != citation.getIdcitation()) {
            System.out.println("ID mismatch in update request");
            return false;
        }
        return citationDao.updateCitation(citation);
    }
    
    // New endpoint to delete a citation
    @DeleteMapping("/supprimerCitation/{id}")
    public boolean supprimerCitation(@PathVariable int id) {
        System.out.println("Supprimer citation with ID: " + id);
        return citationDao.deleteCitation(id);
    }
}