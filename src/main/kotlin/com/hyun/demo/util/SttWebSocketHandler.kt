package com.hyun.demo.util

import com.google.api.gax.rpc.ClientStream
import com.google.api.gax.rpc.ResponseObserver
import com.google.api.gax.rpc.StreamController
import com.google.cloud.speech.v1.*
import com.google.protobuf.ByteString
import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Component
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.BinaryWebSocketHandler
import java.util.concurrent.TimeUnit

@Component
class SttWebSocketHandler : BinaryWebSocketHandler() {

    private val client: SpeechClient = SpeechClient.create()
    private val activeClients = mutableMapOf<String, ClientStream<StreamingRecognizeRequest>>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val responseObserver = object : ResponseObserver<StreamingRecognizeResponse> {
            override fun onStart(controller: StreamController) {}

            override fun onResponse(response: StreamingRecognizeResponse) {
                val transcript = response.resultsList.firstOrNull()
                    ?.alternativesList?.firstOrNull()?.transcript
                if (!transcript.isNullOrBlank() && session.isOpen) {
                    session.sendMessage(TextMessage(transcript))
                    println("STT transcript: $transcript")
                }
            }

            override fun onError(t: Throwable) {
                if (t.message?.contains("GOAWAY") == true) {
                    println("STT stream closed by server (GOAWAY). Treating as complete.")
                } else {
                    println("STT error: ${t.message}")
                    t.printStackTrace()
                }
            }

            override fun onComplete() {
                println("STT stream complete.")
            }
        }

        val clientStream = client
            .streamingRecognizeCallable()
            .splitCall(responseObserver)

        // 초기 설정 요청
        val config = RecognitionConfig.newBuilder()
            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
            .setLanguageCode("en-US")
            .setSampleRateHertz(16000)
            .setEnableAutomaticPunctuation(true)
            .setAudioChannelCount(1)
            .build()

        val streamingConfig = StreamingRecognitionConfig.newBuilder()
            .setConfig(config)
            .build()

        clientStream.send(
            StreamingRecognizeRequest.newBuilder()
                .setStreamingConfig(streamingConfig)
                .build()
        )

        activeClients[session.id] = clientStream
        println("WebSocket connected: ${session.id}")
    }

    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
        val audioData = message.payload.array()
        val clientStream = activeClients[session.id] ?: return

        try {
            clientStream.send(
                StreamingRecognizeRequest.newBuilder()
                    .setAudioContent(ByteString.copyFrom(audioData))
                    .build()
            )
        } catch (e: Exception) {
            println("Error sending audio data: ${e.message}")
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        if (message.payload == "STOP") {
            activeClients[session.id]?.let {
                it.closeSend()
                println("STT stream completed by STOP command.")
            }

        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        activeClients.remove(session.id)?.let {
            it.closeSend()
            activeClients.remove(session.id)
            println("Session ${session.id} closed and stream ended.")
        } ?: println("Session ${session.id} not found during close.")
    }

    @PreDestroy
    fun shutdown() {
        println("Shutting down SpeechClient.")
        client.shutdown()
        client.awaitTermination(5, TimeUnit.SECONDS)
    }
}