package org.webCrawler.tasks.downloadHistorico

class Historico {

    private String competencia
    private String puplicacao
    private String inicioVigencia

    Historico(String competencia, String puplicacao, String inicioVigencia) {
        this.competencia = competencia
        this.puplicacao = puplicacao
        this.inicioVigencia = inicioVigencia
    }


    String toText() {
        return "Competencia: $competencia\tPublicação: $puplicacao\tInicio Vigencia: $inicioVigencia"
    }
}
