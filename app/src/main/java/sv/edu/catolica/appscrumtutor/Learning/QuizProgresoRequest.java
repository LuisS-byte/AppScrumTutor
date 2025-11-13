package sv.edu.catolica.appscrumtutor.Learning;

import java.math.BigDecimal;

public class QuizProgresoRequest {

    private BigDecimal puntaje;

    public QuizProgresoRequest(BigDecimal puntaje) {
        this.puntaje = puntaje;
    }

    public BigDecimal getPuntaje() {
        return puntaje;
    }
}

