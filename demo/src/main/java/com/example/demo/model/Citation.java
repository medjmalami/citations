package com.example.demo.model;
public class Citation {
    private int idcitation;
    private String nomH;
    private String citationcol;
    private int anne;

    public Citation() {}
    public Citation(int idcitation, String nomH, String citationcol,
    int anne) {

        this.idcitation = idcitation;
        this.nomH = nomH;
        this.citationcol = citationcol;
        this.anne = anne;
    }
    public int getIdcitation() {
        return idcitation;
    }
    public void setIdcitation(int idcitation) {
        this.idcitation = idcitation;
    }
    public String getNomH() {
        return nomH;
    }
    public void setNomH(String nomH) {
        this.nomH = nomH;
    }
    public String getCitationcol() {
        return citationcol;
    }
    public void setCitationcol(String citationcol) {
        this.citationcol = citationcol;
    }
    public int getAnne() {

        return anne;}
    public void setAnne(int anne) {
        this.anne = anne;
    }
}
