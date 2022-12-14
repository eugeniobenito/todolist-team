package madstodolist.controller;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class TareaData {
    private String titulo;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fechaLimite;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaLimite() {
        return this.fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }
}