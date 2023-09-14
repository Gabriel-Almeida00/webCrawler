package org.webCrawler.config

import groovy.json.JsonSlurper

class Config {
    static String SENHA

    static {
        try {
            File configFile = new File("config.json")
            JsonSlurper jsonSlurper = new JsonSlurper()
            Object configData = jsonSlurper.parse(configFile)
            SENHA = configData.senha
        } catch (Exception e) {
            println("Erro ao ler o arquivo de configuração: ${e.message}")
        }
    }
}
