package sv.edu.catolica.appscrumtutor.Learning;

import java.util.List;

public class QuizWithPreguntasDTO {
    private Integer id;
    private String titulo;
    private String descripcion;
    private List<QuizPreguntaDTO> preguntas;

    public QuizWithPreguntasDTO() {}

    public Integer getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public List<QuizPreguntaDTO> getPreguntas() { return preguntas; }

}
