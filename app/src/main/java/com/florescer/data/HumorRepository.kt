package com.florescer.data

import com.florescer.data.model.*
import com.florescer.data.network.*

class HumorRepository(
    private val humorApi: HumorApi,
    private val afirmacoesApi: AfirmacoesApi,
    private val analiseSintomasApi: AnaliseSintomasApi,
    private val recomendacoesApi: RecomendacoesApi,
    private val evolucaoApi: EvolucaoApi,
    private val recursosApi: RecursosApi,
    private val trilhasApi: TrilhasApi,
    private val videosApi: VideosApi,
    private val sonsApi: SonsApi,
    private val dao: HumorDao,
    private val authRepository: AuthRepository
) {


    suspend fun saveHumorLocal(humor: HumorEntry) = dao.insert(humor)
    suspend fun getHumoresLocais(): List<HumorEntry> = dao.getAll()


    suspend fun sendHumorToBackend(humor: HumorEntry): Boolean {
        return try {
            val userId = authRepository.getUserId()

            val symptomsList = if (humor.symptoms.isBlank()) emptyList()
            else humor.symptoms.split(",").map { it.trim() }

            val dto = HumorDTO(
                mood = humor.mood.uppercase(),
                note = humor.comment,
                physicalSymptoms = symptomsList,
                heartRate = humor.heartRate.toIntOrNull()
            )

            val response = humorApi.postHumor(userId, dto)
            response.mood.isNotBlank()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun syncHumoresLocais(): SyncResult {
        return try {
            val humoresLocais = getHumoresLocais()
            var successCount = 0
            var errorCount = 0

            for (humor in humoresLocais) {
                val success = sendHumorToBackend(humor)
                if (success) successCount++ else errorCount++
            }

            SyncResult(
                total = humoresLocais.size,
                success = successCount,
                errors = errorCount
            )
        } catch (e: Exception) {
            SyncResult(error = e.message ?: "Erro na sincronização")
        }
    }

    suspend fun getHumores(): List<Humor> {
        return try {
            humorApi.getHumores()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getHistoricoEvolucao(
        dateFrom: String? = null,
        dateTo: String? = null,
        limit: Int? = null
    ): List<EvolucaoHistorico> {
        return try {
            val userId = authRepository.getUserId()
            evolucaoApi.getHistoricos(userId, dateFrom, dateTo, limit)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getRecursos(limit: Int? = 10): List<Recurso> {
        return try {
            val userId = authRepository.getUserId()
            recursosApi.getRecursos(userId, limit)
        } catch (e: Exception) {
            e.printStackTrace()
            getRecursosFallback()
        }
    }

    suspend fun getTrilhas(limit: Int? = 10): List<Trilha> {
        return try {
            val userId = authRepository.getUserId()
            trilhasApi.getTrilhas(userId, limit)
        } catch (e: Exception) {
            e.printStackTrace()
            getTrilhasFallback()
        }
    }

    suspend fun getVideos(limit: Int? = 10): List<Video> {
        return try {
            val userId = authRepository.getUserId()
            videosApi.getVideos(userId, limit)
        } catch (e: Exception) {
            e.printStackTrace()
            getVideosFallback()
        }
    }

    suspend fun getSons(limit: Int? = 10): List<Som> {
        return try {
            val userId = authRepository.getUserId()
            sonsApi.getSons(userId, limit)
        } catch (e: Exception) {
            e.printStackTrace()
            getSonsFallback()
        }
    }

    suspend fun getAfirmacoesPorHumor(
        mood: String,
        limit: Int? = 3
    ): List<Afirmacao> {
        return try {
            val userId = authRepository.getUserId()
            val all = afirmacoesApi.getAfirmacoesPorHumor(userId, limit, mood)

            when {
                all.isNotEmpty() && all.first().affirmations.isNotEmpty() ->
                    all.first().affirmations
                all.isNotEmpty() ->
                    all.map { Afirmacao(it.id ?: "afirmacao_${all.indexOf(it)}") }
                else -> getAfirmacoesFallback(mood)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            getAfirmacoesFallback(mood)
        }
    }

    suspend fun getRecomendacoes(limit: Int? = 5): List<Recomendacao> {
        return try {
            val userId = authRepository.getUserId()
            recomendacoesApi.get(userId, limit)
        } catch (e: Exception) {
            e.printStackTrace()
            getRecomendacoesFallback()
        }
    }

    suspend fun postSintomas(sintomas: SintomasEntry): List<Recomendacao> {
        return try {
            val userId = authRepository.getUserId()
            analiseSintomasApi.postSintomas(userId, sintomas)
                .filter { it.descricao != null }
        } catch (e: Exception) {
            e.printStackTrace()
            getRecomendacoesFallback() // Fallback para recomendações genéricas
        }
    }

    private fun getAfirmacoesFallback(mood: String): List<Afirmacao> {
        return when (mood.lowercase()) {
            "feliz" -> listOf(
                Afirmacao("Alegria é um estado de espírito que cultivo todos os dias"),
                Afirmacao("Mereço toda a felicidade que sinto"),
                Afirmacao("Minha positividade contagia o mundo ao meu redor")
            )
            "triste" -> listOf(
                Afirmacao("Este momento difícil vai passar"),
                Afirmacao("Sou forte o suficiente para superar qualquer desafio"),
                Afirmacao("Permito-me sentir minhas emoções com compaixão")
            )
            "ansioso", "enjoado" -> listOf(
                Afirmacao("Estou seguro neste momento presente"),
                Afirmacao("Confio na minha capacidade de lidar com qualquer situação"),
                Afirmacao("Respirar é minha âncora em meio à tempestade")
            )
            "bravo" -> listOf(
                Afirmacao("Minha raiva é válida, mas não me controla"),
                Afirmacao("Escolho responder com calma e clareza"),
                Afirmacao("Transformo minha energia em ações positivas")
            )
            "amoroso" -> listOf(
                Afirmacao("Meu coração está aberto para dar e receber amor"),
                Afirmacao("Sou digno de afeto e conexão"),
                Afirmacao("Compartilho amor comigo e com os outros")
            )
            else -> listOf(
                Afirmacao("Sou digno de amor e cuidado"),
                Afirmacao("Cada dia é uma nova oportunidade de crescimento"),
                Afirmacao("Me aceito exatamente como estou neste momento")
            )
        }
    }

    private fun getRecomendacoesFallback(): List<Recomendacao> {
        return listOf(
            Recomendacao("Faça uma pausa de 5 minutos para respirar profundamente"),
            Recomendacao("Beba um copo de água e observe seu corpo"),
            Recomendacao("Anote três coisas pelas quais você é grato hoje"),
            Recomendacao("Alongue-se suavemente por 2 minutos"),
            Recomendacao("Lembre-se: é okay não estar okay o tempo todo")
        )
    }

    private fun getRecursosFallback(): List<Recurso> {
        return listOf(
            Recurso(
                id = "1",
                titulo = "Guia de Autocuidado",
                descricao = "Dicas simples para cuidar de si mesmo no dia a dia",
                tipo = "guia"
            )
        )
    }

    private fun getTrilhasFallback(): List<Trilha> {
        return listOf(
            Trilha(
                id = "1",
                nome = "Trilha do Bem-Estar",
                descricao = "Uma jornada de autocuidado e autoconhecimento"
            )
        )
    }

    private fun getVideosFallback(): List<Video> {
        return listOf(
            Video(
                id = "1",
                titulo = "Meditação Guiada para Iniciantes",
                url = "https://www.youtube.com/embed/inpok4MKVLM"
            )
        )
    }

    private fun getSonsFallback(): List<Som> {
        return listOf(
            Som(
                id = "1",
                nome = "Som da Chuva Relaxante",
                url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
            )
        )
    }
}

data class SyncResult(
    val total: Int = 0,
    val success: Int = 0,
    val errors: Int = 0,
    val error: String? = null
) {
    val isSuccess: Boolean get() = error == null && errors == 0
    val hasErrors: Boolean get() = errors > 0 || error != null
}