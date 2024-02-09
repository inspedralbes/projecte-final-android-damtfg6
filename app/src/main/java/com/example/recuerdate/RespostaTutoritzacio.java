package com.example.recuerdate;

public class RespostaTutoritzacio {
        private boolean autoritzacio;
        private Usuari usuariTutoritzatData; // Campo para los datos del usuario tutorizado

        public RespostaTutoritzacio(boolean autoritzacio, Usuari usuariTutoritzatData) {
            this.autoritzacio = autoritzacio;
            this.usuariTutoritzatData = usuariTutoritzatData;
        }

        public boolean isAutoritzacio() {
            return autoritzacio;
        }

        public void setAutoritzacio(boolean autoritzacio) {
            this.autoritzacio = autoritzacio;
        }


        public Usuari getUsuariTutoritzatData() {
            return usuariTutoritzatData;
        }

        public void setUsuariTutoritzatData(Usuari usuariTutoritzatData) {
            this.usuariTutoritzatData = usuariTutoritzatData;
        }

        @Override
        public String toString() {
            return "RespostaLogin{" +
                    "autoritzacio=" + autoritzacio +
                    ", usuariTutoritzatData=" + usuariTutoritzatData +
                    '}';
        }
    }
