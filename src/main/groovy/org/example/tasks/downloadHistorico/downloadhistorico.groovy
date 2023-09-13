package org.example.tasks.downloadHistorico

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import java.text.SimpleDateFormat

import static groovyx.net.http.HttpBuilder.configure

class downloadhistorico {
    static void main(String[] args) {
        String baseUrl = 'https://www.gov.br/ans/pt-br'
        String caminhoArquivo = 'downloads/historico/historico.txt'
        List<historico> historicoList = []

        Document page = configure {
            request.uri = baseUrl
        }.get()

        Elements firstLinkElement = page.select('a[href="https://www.gov.br/ans/pt-br/assuntos/prestadores"]')

        if (!firstLinkElement.isEmpty()) {
            String firstLinkHref = firstLinkElement.attr('href')

            println "Primeiro link href: ${firstLinkHref}"

            Document secondPage = configure {
                request.uri = firstLinkHref
            }.get()

            Elements secondLinkElement = secondPage.select('a.govbr-card-content')

            if (!secondLinkElement.isEmpty()) {
                String secondLinkHref = secondLinkElement.attr('href')

                println "Segundo link href: ${secondLinkHref}"

                Document thirdPage = configure {
                    request.uri = secondLinkHref
                }.get()

                Elements thirdLinkElement = thirdPage.select('a.alert-link.internal-link[data-tippreview-enabled="true"]')

                if (!thirdLinkElement.isEmpty()) {
                    String thirdLinkHref = thirdLinkElement.attr('href')

                    println "Terceiro link href: ${thirdLinkHref}"

                    String downloadPage = configure {
                        request.uri = thirdLinkHref
                    }.get()

                    Document document = Jsoup.parse(downloadPage)
                    Elements tabela = document.select("table")

                    tabela.select("tr").each { linha ->
                        Elements colunas = linha.select("td")


                        if (colunas.size() >= 3) {
                            String competencia = colunas[0].text().trim()
                            String publicacao = colunas[1].text().trim()
                            String inicioVigencia = colunas[2].text().trim()

                            SimpleDateFormat formatoData = new SimpleDateFormat("MMM/yyyy")
                            Date dataCompetencia = formatoData.parse(competencia)
                            Date dataInicio = formatoData.parse("jan/2016")

                            if (dataCompetencia >= dataInicio) {
                                historicoList.add(new historico(competencia, publicacao, inicioVigencia))
                            }

                        }
                    }

                } else {
                    println "Terceiro link não encontrado."
                }
            } else {
                println "Segundo link não encontrado."
            }
        } else {
            println "Primeiro link não encontrado."
        }



        File arquivo = new File(caminhoArquivo)
        arquivo.parentFile.mkdirs()

        arquivo.withWriter { writer ->
            historicoList.each { historico ->
                writer.write(historico.toText() + '\n')
            }
        }

        println("Dados salvos em $caminhoArquivo")
    }
}

