package com.project.plugins

import io.ktor.http.ContentType
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import io.ktor.websocket.WebSocketDeflateExtension.Companion.install
import org.slf4j.event.*
import java.util.Locale.filter



