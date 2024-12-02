package com.dicoding.escore.data.remote.response

import com.google.gson.annotations.SerializedName

data class ModelMachineLearningResponse(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("user_email")
	val userEmail: String? = null,

	@field:SerializedName("essay")
	val essay: String? = null,

	@field:SerializedName("predicted_result")
	val predictedResult: PredictedResult? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class PredictedResult(

	@field:SerializedName("score")
	val score: String? = null,

	@field:SerializedName("suggestion")
	val suggestion: String? = null,

	@field:SerializedName("raw_score")
	val rawScore: Any? = null
)
