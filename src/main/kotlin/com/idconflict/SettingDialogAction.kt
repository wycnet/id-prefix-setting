package com.idconflict

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class SettingDialogAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        SettingDialog().apply {
            isVisible = true
        }
    }
}