package com.example.demo.dao;

import com.example.demo.model.Citation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitationDao {
    private Connection connection;

    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/testb",
                "root", "root");
            return connection;
        } catch (Exception e) {
            System.out.println("Erreur de connexion à la base de données");
            e.printStackTrace(); // debug info
            return null;
        }
    }

    public boolean insertCitation(Citation citation) {
        try {
            // Initialize connection before using it
            Connection conn = getConnection();
            if (conn == null) return false;
            
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO citations (id, auteur, citation, annee) VALUES (?, ?, ?, ?)");
            ps.setInt(1, citation.getIdcitation());
            ps.setString(2, citation.getNomH());
            ps.setString(3, citation.getCitationcol());
            ps.setInt(4, citation.getAnne());
            ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (Exception e) {
            System.out.println("Erreur lors de l'insertion dans la base de données");
            e.printStackTrace(); // print actual error
            return false;
        }
    }
    
    public List<Citation> getAllCitations() {
        List<Citation> citations = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            if (conn == null) return citations;
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id as idcitation, auteur as nomH, citation as citationcol, annee as anne FROM citations");
            
            while (rs.next()) {
                Citation citation = new Citation();
                citation.setIdcitation(rs.getInt("idcitation"));
                citation.setNomH(rs.getString("nomH"));
                citation.setCitationcol(rs.getString("citationcol"));
                citation.setAnne(rs.getInt("anne"));
                citations.add(citation);
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des citations");
            e.printStackTrace();
        }
        
        return citations;
    }
    
    public boolean updateCitation(Citation citation) {
        try {
            Connection conn = getConnection();
            if (conn == null) return false;
            
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE citations SET auteur = ?, citation = ?, annee = ? WHERE id = ?");
            ps.setString(1, citation.getNomH());
            ps.setString(2, citation.getCitationcol());
            ps.setInt(3, citation.getAnne());
            ps.setInt(4, citation.getIdcitation());
            
            int rowsAffected = ps.executeUpdate();
            ps.close();
            conn.close();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour de la citation");
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteCitation(int id) {
        try {
            Connection conn = getConnection();
            if (conn == null) return false;
            
            PreparedStatement ps = conn.prepareStatement("DELETE FROM citations WHERE id = ?");
            ps.setInt(1, id);
            
            int rowsAffected = ps.executeUpdate();
            ps.close();
            conn.close();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de la citation");
            e.printStackTrace();
            return false;
        }
    }
    
    public Citation getCitationById(int id) {
        Citation citation = null;
        
        try {
            Connection conn = getConnection();
            if (conn == null) return null;
            
            PreparedStatement ps = conn.prepareStatement(
                "SELECT id as idcitation, auteur as nomH, citation as citationcol, annee as anne FROM citations WHERE id = ?");
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                citation = new Citation();
                citation.setIdcitation(rs.getInt("idcitation"));
                citation.setNomH(rs.getString("nomH"));
                citation.setCitationcol(rs.getString("citationcol"));
                citation.setAnne(rs.getInt("anne"));
            }
            
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération de la citation");
            e.printStackTrace();
        }
        
        return citation;
    }
}