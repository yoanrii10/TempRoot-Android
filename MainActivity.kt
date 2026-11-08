package com.temproot.app

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.io.File

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pad = (20 * resources.displayMetrics.density).toInt()
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)
            setBackgroundColor(0xFF101A15.toInt())
        }
        fun text(value: String, size: Float, color: Int, bold: Boolean = false) =
            TextView(this).apply {
                this.text = value
                textSize = size
                setTextColor(color)
                if (bold) setTypeface(null, android.graphics.Typeface.BOLD)
                setPadding(0, 8, 0, 8)
            }

        root.addView(text("TEMP ROOT", 28f, 0xFF65E6A0.toInt(), true))
        root.addView(text("Diagnóstico de compatibilidad del dispositivo", 15f, 0xFFFFFFFF.toInt()))

        val info = DeviceDiagnostics.collect()
        val rows = listOf(
            "Resultado" to info.compatibility,
            "Android" to info.android,
            "Fabricante / modelo" to info.device,
            "Kernel" to info.kernel,
            "ABI" to info.abi,
            "SELinux" to info.selinux,
            "ROOT detectado" to info.rootStatus,
            "Bootloader" to info.bootloader
        )
        rows.forEach { (label, value) ->
            root.addView(text(label, 13f, 0xFF9DB5A7.toInt(), true))
            root.addView(text(value, 16f, 0xFFFFFFFF.toInt()))
            val line = android.view.View(this).apply {
                setBackgroundColor(0xFF294436.toInt())
                layoutParams = LinearLayout.LayoutParams(-1, 1)
            }
            root.addView(line)
        }

        root.addView(text(
            "Importante: esta versión solo diagnostica. Android no permite que una app normal se conceda ROOT. " +
            "La revocación global del ROOT al reiniciar requiere soporte del mecanismo privilegiado y del arranque del dispositivo.",
            13f, 0xFFFFD18A.toInt()
        ))
        setContentView(ScrollView(this).apply { addView(root) })
    }
}

data class Diagnostic(
    val compatibility: String,
    val android: String,
    val device: String,
    val kernel: String,
    val abi: String,
    val selinux: String,
    val rootStatus: String,
    val bootloader: String
)

object DeviceDiagnostics {
    fun collect(): Diagnostic {
        val api = Build.VERSION.SDK_INT
        val kernel = System.getProperty("os.version") ?: "No disponible"
        val abi = Build.SUPPORTED_ABIS.joinToString()
        val selinux = try {
            val enforcing = File("/sys/fs/selinux/enforce").readText().trim()
            if (enforcing == "1") "Enforcing" else "Permissive"
        } catch (_: Exception) {
            "No se pudo consultar"
        }
        val root = listOf(
            "/system/bin/su", "/system/xbin/su", "/sbin/su",
            "/system/bin/.ext/.su"
        ).any { File(it).exists() }
        val bootloader = try {
            val value = android.os.Build::class.java
                .getField("TAGS").get(null) as? String
            if (value?.contains("test-keys", true) == true)
                "Posible compilación de pruebas (no concluyente)"
            else "No verificable de forma fiable sin privilegios"
        } catch (_: Exception) {
            "No verificable"
        }
        val compatibility = when {
            api < 29 -> "No compatible: requiere Android 10 o posterior"
            else -> "Diagnóstico disponible; método ROOT aún no validado"
        }
        return Diagnostic(
            compatibility,
            "Android ${Build.VERSION.RELEASE} (API $api)",
            "${Build.MANUFACTURER} ${Build.MODEL}",
            kernel,
            abi,
            selinux,
            if (root) "Posibles rastros encontrados (no concluyente)" else "No detectado por comprobación básica",
            bootloader
        )
    }
}
