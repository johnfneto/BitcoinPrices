package com.johnfneto.bitcoinprices.utils

import com.squareup.moshi.JsonQualifier

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class SingleToArray