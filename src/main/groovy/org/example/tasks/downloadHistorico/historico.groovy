package org.example.tasks.downloadHistorico

class historico {

    private String competencia
    private String puplicacao
    private String inicioVigencia

    historico(String competencia, String puplicacao, String inicioVigencia) {
        this.competencia = competencia
        this.puplicacao = puplicacao
        this.inicioVigencia = inicioVigencia
    }

    String getCompetencia() {
        return competencia
    }

    void setCompetencia(String competencia) {
        this.competencia = competencia
    }

    String getPuplicacao() {
        return puplicacao
    }

    void setPuplicacao(String puplicacao) {
        this.puplicacao = puplicacao
    }

    String getInicioVigencia() {
        return inicioVigencia
    }

    void setInicioVigencia(String inicioVigencia) {
        this.inicioVigencia = inicioVigencia
    }

    String toText() {
        return "Competencia: $competencia\tPublicação: $puplicacao\tInicio Vigencia: $inicioVigencia"
    }
}
