package lp1.eu.dpg

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var dpg: dpg = dpg()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mContext = this
        var clipboard: ClipboardManager  = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val editTextPassphrase = findViewById(R.id.editText_passphrase) as EditText
        val editTextWord = findViewById(R.id.editText_word) as EditText
        val displayPassButton = findViewById(R.id.button_display_password) as Button
        val textViewWarning = findViewById(R.id.textView_warning) as TextView
        val textViewPass = findViewById(R.id.textView_pass) as TextView
        val buttonClear = findViewById(R.id.button_clear) as Button
        var password = ""

        if (android.os.Build.VERSION.SDK_INT < 26) {
            textViewWarning.text = getString(R.string.warning_api_26)
        }

        buttonClear.setOnClickListener {
            editTextPassphrase.text.clear()
            editTextWord.text.clear()
            textViewPass.text = ""
        }

        displayPassButton.setOnClickListener {
            if (editTextPassphrase.length() > 0 && editTextWord.length() > 0) {
                password = dpg.getPassword(editTextPassphrase.text.toString(), editTextWord.text.toString())
                textViewPass.text = String.format("%s********************", password.substring(0, 4))
                var clipData = ClipData.newPlainText("dpg-password", password)
                clipboard.primaryClip = clipData
            }
        }

        textViewPass.setOnClickListener {
            Toast.makeText(mContext, getString(R.string.textview_password_hint), Toast.LENGTH_LONG).show()
        }

        textViewPass.setOnLongClickListener {
            textViewPass.text = password
            true
        }

    }
}
