package sv.edu.catolica.appscrumtutor.Learning;

public class QuizPreguntaDTO {
    private Integer id;
    private String pregunta;
    private String opcionA;
    private String opcionB;
    private String opcionC;
    private String opcionCorrecta;

    public QuizPreguntaDTO() {}

    public Integer getId() { return id; }
    public String getPregunta() { return pregunta; }
    public String getOpcionA() { return opcionA; }
    public String getOpcionB() { return opcionB; }
    public String getOpcionC() { return opcionC; }
    public String getOpcionCorrecta() { return opcionCorrecta; }

}
