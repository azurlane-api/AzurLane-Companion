package info.kurozeropb.alcompanion.responses

data class Response<out V : Any?, out E : Exception?>(val value: V, val exception: E)