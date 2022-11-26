package com.example.cinepool.model;

public class Pelicula {
    String nombrePeli, directorPeli, duracionPeli, generoPeli, idiomaPeli, resumenPeli, photo;
    public Pelicula(){}

    public Pelicula(String nombrePeli, String directorPeli, String duracionPeli, String generoPeli, String idiomaPeli, String resumenPeli, String photo) {
        this.nombrePeli = nombrePeli;
        this.directorPeli = directorPeli;
        this.duracionPeli = duracionPeli;
        this.generoPeli = generoPeli;
        this.idiomaPeli = idiomaPeli;
        this.resumenPeli = resumenPeli;
        this.photo = photo;
    }

    public String getNombrePeli() {
        return nombrePeli;
    }

    public void setNombrePeli(String nombrePeli) {
        this.nombrePeli = nombrePeli;
    }

    public String getDirectorPeli() {
        return directorPeli;
    }

    public void setDirectorPeli(String directorPeli) {
        this.directorPeli = directorPeli;
    }

    public String getDuracionPeli() {
        return duracionPeli;
    }

    public void setDuracionPeli(String duracionPeli) {
        this.duracionPeli = duracionPeli;
    }

    public String getGeneroPeli() {
        return generoPeli;
    }

    public void setGeneroPeli(String generoPeli) {
        this.generoPeli = generoPeli;
    }

    public String getIdiomaPeli() {
        return idiomaPeli;
    }

    public void setIdiomaPeli(String idiomaPeli) {
        this.idiomaPeli = idiomaPeli;
    }

    public String getResumenPeli() {
        return resumenPeli;
    }

    public void setResumenPeli(String resumenPeli) {
        this.resumenPeli = resumenPeli;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
