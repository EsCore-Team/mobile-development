package com.dicoding.escore.data.remote.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("predictions")
	val predictions: List<PredictionsItem?>? = null
)

data class PredictionsItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("essay")
	val essay: String? = null,

	@field:SerializedName("predicted_result")
	val predictedResult: PredictedResult2? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PredictedResult2(

	@field:SerializedName("score")
	val score: String? = null,

	@field:SerializedName("suggestion")
	val suggestion: String? = null,

	@field:SerializedName("raw_score")
	val rawScore: Any? = null
)
