package com.idconflict

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.WordCompletionContributor
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import java.util.*


class IdCompletionContributor : CompletionContributor() {
    private val preString = "android:id=\"@+id/"
    private val userValue = UserSettingValue.getInstance()

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val offset = parameters.offset;
        val document = parameters.editor.document;
        val lineStartOffset = document.getLineStartOffset(document.getLineNumber(offset))
        val text = document.getText(TextRange.create(lineStartOffset, offset))

        val file: PsiFile = parameters.originalFile //当前文件对象
        val currentFileName: String = file.name //当前文件字符串名

        when {
            text.contains(preString) -> {
                var dictResult: CompletionResultSet = result
                // view id 属性的值
                val valueOfId = getValueOfId(text)
                dictResult = result.withPrefixMatcher(valueOfId)
                val preList = ArrayList(userValue.preStringList)
                preList.add(getPreNameWithFileName(currentFileName, userValue.isPre))
                var isUserPreString: Boolean = false
                var iscContainsPreString: Boolean = false
                preList.forEach {
                    if (it.startsWith(valueOfId, true)) {
                        isUserPreString = true
                    }
                    if (valueOfId.startsWith(it) || valueOfId.endsWith(it)) {
                        iscContainsPreString = true
                    }
                }
                if (!iscContainsPreString) {
                    for (s in preList) {
                        val completeText =
                            if (userValue.isPre) {
                                if (isUserPreString) {
                                    s
                                } else {
                                    s + valueOfId
                                }
                            } else {
                                valueOfId + s
                            }
                        dictResult.addElement(LookupElementBuilder.create(completeText))
                    }
                }
            }
            else -> {
                super.fillCompletionVariants(parameters, result);
                WordCompletionContributor.addWordCompletionVariants(result, parameters, Collections.emptySet())
            }
        }
    }


    /** 获取id属性值*/
    private fun getValueOfId(text: String): String {
        val index = text.indexOf(preString)
        if (index == -1) {
            return ""
        }
        return text.substring(index + preString.length, text.length)
    }


    private fun getPreNameWithFileName(fileName: String, isPre: Boolean): String {
        val name = fileName.substring(0, fileName.indexOfFirst { it -> it == '.' })
        return if (isPre) "${name}_" else "_$name"
    }
}