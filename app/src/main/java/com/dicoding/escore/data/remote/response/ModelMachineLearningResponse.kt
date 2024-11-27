package com.dicoding.escore.data.remote.response

import com.google.gson.annotations.SerializedName

data class ModelMachineLearningResponse(

	@field:SerializedName("essay")
	val essay: String? = null,

	@field:SerializedName("predicted_result")
	val predictedResult: PredictedResult? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PredictedResult(

	@field:SerializedName("score")
	val score: Int? = null,

	@field:SerializedName("suggestion")
	val suggestion: String? = null
)
