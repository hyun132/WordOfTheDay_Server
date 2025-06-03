package com.hyun.demo.util

import com.google.api.gax.rpc.ClientStream
import com.google.api.gax.rpc.ResponseObserver
import com.google.api.gax.rpc.StreamController
import com.google.cloud.speech.v1.*
import com.google.protobuf.ByteString
import org.springframework.stereotype.Component
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.BinaryWebSocketHandler

@Component
class SttWebSocketHandler : BinaryWebSocketHandler() {
    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
        val audioData = message.payload.array()
        val clientStream = activeClients[session.id] ?: return

        try {
            clientStream.send(StreamingRecognizeRequest.newBuilder()
                .setAudioContent(ByteString.copyFrom(audioData))
                .build())
        } catch (e: Exception) {
            println("Error sending audio data: ${e.message}")
        }
    }

    private val activeClients = mutableMapOf<String, ClientStream<StreamingRecognizeRequest>>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        SpeechClient.create().use { client ->
            val responseObserver = object : ResponseObserver<StreamingRecognizeResponse> {
                override fun onStart(controller: StreamController) {}
                override fun onResponse(response: StreamingRecognizeResponse) {
                    val transcript = response.resultsList.firstOrNull()?.alternativesList?.firstOrNull()?.transcript
                    if (transcript != null && session.isOpen) {
                        session.sendMessage(TextMessage(transcript))
                    }
                }

                override fun onError(t: Throwable) {
                    println("STT error: ${t.message}")
                    t.printStackTrace()
                }

                override fun onComplete() {
                    println("STT stream complete.")
                }
            }

            val clientStream = client.streamingRecognizeCallable().splitCall(responseObserver)

            val config = RecognitionConfig.newBuilder()
            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
            .setLanguageCode("en-US")
            .setSampleRateHertz(16000) // 정확한 샘플레이트 설정 (마이크와 동일해야 함)
            .setEnableAutomaticPunctuation(true) // 자동 구두점
            .setAudioChannelCount(1) // 채널 수 명시
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
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val closed = activeClients.remove(session.id)
        closed?.closeSend() ?: println("Session ${session.id} not found during close.")
    }
}