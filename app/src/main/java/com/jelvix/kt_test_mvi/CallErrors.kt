package com.jelvix.kt_test_mvi

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize


/**
 * Created by Rim Gazzah on 8/28/20.
 **/
@Keep
sealed class CallErrors(): Parcelable {
    @Keep
    @Parcelize
    object ErrorEmptyData : CallErrors(), Parcelable
    @Keep
    @Parcelize
    object ErrorServer: CallErrors(), Parcelable
    @Keep
    @Parcelize
    data class ErrorException(val throwable: Throwable) : CallErrors(), Parcelable
}