/* Copyright (C) 2019-2023 Julian Valentin, LTeX Development Community
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package org.bsplines.ltexls.parsing.typst

import org.bsplines.ltexls.parsing.CharacterBasedCodeAnnotatedTextBuilder

class TypstAnnotatedTextBuilder(
  codeLanguageId: String,
) : CharacterBasedCodeAnnotatedTextBuilder(codeLanguageId) {
  @Suppress("ReturnCount")
  override fun processCharacter() {
    if (this.isStartOfLine) {
      if (addMarkupInternal(LIST_REGEX)) return
      if (addMarkupInternal(LEADING_WHITESPACE_REGEX)) return
      if (addMarkupInternal(HEADING_REGEX)) return
    }
    if (addMarkupInternal(LINE_COMMENT_REGEX, "\n")) return
    if (addMarkupInternal(MULTILINELINE_COMMENT_REGEX, "\n")) return
    if (addMarkupInternal(MATH_REGEX)) return
    if (addMarkupInternal(LET_REGEX)) return
    if (addMarkupInternal(IMPORT_REGEX, "\n")) return
    if (addMarkupInternal(SHOW_REGEX)) return
    if (addMarkupInternal(CODE_REGEX)) return
    if (addMarkupInternal(CLOSING_PARENTHESIS_REGEX, "\n")) return

    addText(this.curString)
  }

  private fun addMarkupInternal(
    regex: Regex,
    interpretAs: String = "",
  ): Boolean {
    var matchResult: MatchResult?
    matchResult = matchFromPosition(regex)
    if (matchResult != null) {
      addMarkup(matchResult.value, interpretAs)
      return true
    }
    return false
  }

  companion object {
    private val LIST_REGEX = Regex("^\\s*[+|\\-|\\/]\\s")
    private val LEADING_WHITESPACE_REGEX = Regex("^\\s*")
    private val HEADING_REGEX = Regex("^=+\\s")

    private val LINE_COMMENT_REGEX = Regex("^\\/\\/.*(\r?\n|$)")
    private val MULTILINELINE_COMMENT_REGEX = Regex("^\\/\\*(.|\r?\n)*\\*\\/")
    private val MATH_REGEX = Regex("^\\$.*\\$")
    private val LET_REGEX = Regex("^#let\\s*[-a-z]*\\s*\\w*\\s*\\=")
    private val IMPORT_REGEX = Regex("^\\s*(#import|include).*\r?\n")
    private val SHOW_REGEX = Regex("^#show:\\s\\w*.with\\(")
    private val CODE_REGEX = Regex("^#\\w*\\(")
    private val CLOSING_PARENTHESIS_REGEX = Regex("^,\r?\n\\)(\r?\n|$)")
  }
}
