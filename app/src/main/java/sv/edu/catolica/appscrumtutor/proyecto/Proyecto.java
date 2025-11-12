package sv.edu.catolica.appscrumtutor.proyecto;

public class Proyecto {



        private Long id;
        private String nombre;
        private String descripcion;
        private String fechaInicio; // <-- agregado
        private String fechaFin;    // <-- agregado
        public Long getId() { return id; }
        public String getNombre() { return nombre; }

        public String getDescripcion() { return descripcion; }

        public String getFechaInicio() { return fechaInicio; } // <-- agregado
        public String getFechaFin() { return fechaFin; }

}
