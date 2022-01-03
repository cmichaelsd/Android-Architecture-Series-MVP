package com.colemichaels.mvp_demo.model

import org.mockito.ArgumentCaptor

open class BaseTest {
    open fun <T> captureArg(argumentCapture: ArgumentCaptor<T>): T =
        argumentCapture.capture()
}