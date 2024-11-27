package com.dicoding.escore.utils

import android.content.Context
import com.dicoding.escore.ml.FinalModel2
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import org.tensorflow.lite.DataType

class TextClassifierHelper(
    private val modelName: String = "final_model_2.tflite",
    private val context: Context,
    private val classifierListener: ClassifierListener? // Listener untuk callback hasil
) {

    // Fungsi utama untuk melakukan klasifikasi teks
    fun classifyText(inputText: String) {
        try {
            // Mengubah teks input menjadi vektor fitur (byteBuffer)
            val byteBuffer = preprocessText(inputText)

            // Memuat model
            val model = FinalModel2.newInstance(context)

            // Membuat input untuk model
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 100), DataType.FLOAT32)
            inputFeature0.loadBuffer(byteBuffer)

            // Melakukan inferensi model
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            // Mendapatkan hasil prediksi
            val result = interpretResults(outputFeature0.floatArray)

            // Callback hasil ke listener
            classifierListener?.onResult(result)

            // Menutup model untuk membebaskan resource
            model.close()
        } catch (e: Exception) {
            e.printStackTrace()
            classifierListener?.onError(e.message ?: "Unknown error occurred")
        }
    }

    // Fungsi untuk memproses teks menjadi ByteBuffer (contoh, sesuaikan dengan model Anda)
    private fun preprocessText(inputText: String): ByteBuffer {
        val maxInputLength = 100 // Panjang input yang diterima oleh model
        val byteBuffer = ByteBuffer.allocateDirect(maxInputLength * 4).apply {
            order(java.nio.ByteOrder.nativeOrder())
        }

        // Mengisi ByteBuffer dengan representasi numerik teks
        val tokenizedText = tokenizeText(inputText, maxInputLength)
        tokenizedText.forEach { value ->
            byteBuffer.putFloat(value)
        }

        return byteBuffer
    }

    // Contoh fungsi tokenisasi sederhana (sesuaikan dengan model Anda)
    private fun tokenizeText(text: String, maxLength: Int): FloatArray {
        val words = text.split("\\s+".toRegex()).map { it.hashCode().toFloat() }
        val paddedWords = FloatArray(maxLength) { 0f }

        for (i in words.indices) {
            if (i < maxLength) {
                paddedWords[i] = words[i]
            }
        }
        return paddedWords
    }

    // Fungsi untuk menginterpretasikan hasil keluaran model
    private fun interpretResults(outputArray: FloatArray): String {
        // Asumsikan model mengeluarkan probabilitas untuk beberapa kelas
        val maxIndex = outputArray.indices.maxByOrNull { outputArray[it] } ?: -1
        return "Predicted Class: $maxIndex with Confidence: ${outputArray[maxIndex]}"
    }
}

// Interface untuk callback
interface ClassifierListener {
    fun onResult(result: String)
    fun onError(error: String)
}
