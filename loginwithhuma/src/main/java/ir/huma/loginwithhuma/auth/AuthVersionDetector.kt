package ir.huma.loginwithhuma.auth

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.pm.PackageInfoCompat

internal object AuthVersionDetector {

    private const val MIN_DONE_UI_VERSION_CODE = 349
    private const val MIN_DONE_APPS_VERSION_CODE = 111
    private const val MIN_HUMA_STORE_VERSION_CODE = 45

    fun getAuthVersion(context: Context): AuthVersion =
        when {
            isDoneUiInstalled(context) -> AuthVersion.V3
            isDoneAppV2Installed(context) -> AuthVersion.V2
            isHumaWizardInstalled(context) || isHumaStoreInstalled(context) -> AuthVersion.V1
            else -> AuthVersion.NONE
        }

    private fun isDoneUiInstalled(context: Context): Boolean =
        try {
            val pm = context.packageManager
            val info = pm.getPackageInfo("ir.huma.android.launcher", 0)
            PackageInfoCompat.getLongVersionCode(info) >= MIN_DONE_UI_VERSION_CODE
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }

    private fun isDoneAppV2Installed(context: Context): Boolean =
        try {
            val pm = context.packageManager
            val info = pm.getPackageInfo("ir.huma.humastore", 0)
            PackageInfoCompat.getLongVersionCode(info) >= MIN_DONE_APPS_VERSION_CODE
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }

    private fun isHumaStoreInstalled(context: Context): Boolean =
        try {
            val pm = context.packageManager
            val info = pm.getPackageInfo("ir.huma.humastore", 0)
            PackageInfoCompat.getLongVersionCode(info) >= MIN_HUMA_STORE_VERSION_CODE
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }

    private fun isHumaWizardInstalled(context: Context): Boolean =
        try {
            val pm = context.packageManager
            pm.getPackageInfo("ir.huma.humawizard", 0)
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
}