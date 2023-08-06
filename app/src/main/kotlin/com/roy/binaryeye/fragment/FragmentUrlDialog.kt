package com.roy.binaryeye.fragment

import android.os.Bundle
import android.support.v7.preference.PreferenceDialogFragmentCompat
import android.view.View
import android.widget.TextView
import com.roy.binaryeye.R
import com.roy.binaryeye.app.prefs
import com.roy.binaryeye.database.Scan
import com.roy.binaryeye.net.sendAsync
import com.roy.binaryeye.preference.UrlPreference

class FragmentUrlDialog : PreferenceDialogFragmentCompat() {
    private var urlView: TextView? = null
    private var testButton: TextView? = null

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)

        urlView = view?.findViewById(R.id.url)
        testButton = view?.findViewById(R.id.testUrl)
        testButton?.setOnClickListener {
            testUrl(testButton)
        }
        urlView?.text = urlPreference().getUrl()
        urlView?.hint = getString(
            when (prefs.sendScanType) {
                "0" -> R.string.url_hint_add_content
                else -> R.string.url_hint
            }
        )
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            urlPreference().setUrl(getUrl())
        }
    }

    private fun getUrl() = completeUrl(urlView?.text.toString())

    private fun urlPreference() = preference as UrlPreference

    private fun testUrl(textView: TextView?) {
        val url = getUrl()
        if (url.isEmpty()) {
            return
        }
        textView ?: return
        textView.text = "…"
        Scan(
			content = "test",
			raw = null,
			format = "none"
        ).sendAsync(url, prefs.sendScanType) { code, body ->
            textView.text = when {
                code != null -> "$code"
                body != null -> body
                else -> getString(R.string.background_request_failed)
            }
        }
    }

    companion object {
        fun newInstance(key: String): FragmentUrlDialog {
            val args = Bundle()
            args.putString(ARG_KEY, key)
            val fragment = FragmentUrlDialog()
            fragment.arguments = args
            return fragment
        }
    }
}

private fun completeUrl(template: String): String {
    var s = template.trim()
    if (s.isEmpty()) {
        return ""
    }
    if (!s.startsWith("http")) {
        s = "http://${s}"
    }
    if (prefs.sendScanType == "0" &&
        !s.matches(".*/[a-zA-Z._-]*\\?[a-zA-Z0-9&=_-]+=$".toRegex())
    ) {
        s = "${s}?content="
    }
    return s
}
