package com.example.guessthephrase

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.item.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var recView: RecyclerView
    lateinit var phraseTextView: TextView
    lateinit var letterTextView: TextView
    lateinit var guessedET: EditText
    var guessList = arrayListOf<String>()
    var guessLetters = arrayListOf<String>()
    var phrase = "ohood"
    var answer = ""
    var counter = 10
    var isPhrase = true
    var guessCorrectLetter = ""

    fun EditText.setMaxLength(maxLength: Int) {
        filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recView = findViewById(R.id.recyclerview)
        recView.adapter = RecyclerViewAdapter(guessList)
        recView.layoutManager = LinearLayoutManager(this)

        phraseTextView = findViewById(R.id.guessPTv)
        letterTextView = findViewById(R.id.guessLTv)

        guessPTv.text = "Phrase: " + encodePhrase(answer)
        val GuessBT = findViewById<Button>(R.id.guessButton)
        guessedET = findViewById(R.id.guessEditText)

        guessedET.setMaxLength(phrase.length)

        GuessBT.setOnClickListener {
            var guessText = guessedET.text.toString()
            if (isPhrase) {
                if (counter > 0) {
                    if (!checkIfEmpty(guessText)) {
                        checkPhrase(guessText)
                    }
                }
            } else {
                if (counter > 0) {
                    if (!checkIfEmpty(guessText)) {
                        guessedET.hint = "Guess the letters of the phrase"
                        checkLetter(guessText[0])
                    }
                } else {
                    showAlertDialog("Oops!,The phrase was $answer")
                    // replayGame()
                }
            }
            guessedET.text.clear()
        }

    }


    private fun showAlertDialog(title: String) {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage(title)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                this.recreate()
            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Game Over")
        // show alert dialog
        alert.show()
    }

    fun checkPhrase(guessText: String) {
        if (phrase == guessText) {
            showAlertDialog("Congratulations,You guessed it correctly!")
            //replayGame()

            } else {
            counter--
            guessedET.hint = "Guess the letters of the phrase"
            guessList.add("$guessText is a wrong guess.")
            guessedET.setMaxLength(1)
            isPhrase = false


            if (counter != 0) {
                guessList.add("$counter guesses remaining!")
            }
            recView.adapter!!.notifyDataSetChanged()

        }

    }


fun checkLetter(guessLetter: Char) {

    letterTextView.setVisibility(View.VISIBLE)


    if (phrase.contains(guessLetter)) {//the letter exist in the phrase
        guessLetters.add(guessLetter + "")
        counter--
        guessCorrectLetter = ""
        var countLetters = 0
        for (i in phrase) {
            if (guessLetters.contains(i + "")) {
                if (guessLetter == i)
                    countLetters++
                guessCorrectLetter += i
            } else if (i == ' ') {
                guessCorrectLetter += " "
            } else if (i != guessLetter && i != ' ' && i != '*') {
                guessCorrectLetter += "*"
            } else {
                guessCorrectLetter += i
            }
        }

        letterTextView.text = "Guessed letters: " + guessLetters.toString()
        phraseTextView.text = "Phrase:" + guessCorrectLetter

        guessList.add("Found $countLetters $guessLetter(s)")
        guessList.add("$counter guesses remaining")
        recView.adapter!!.notifyDataSetChanged()
    } else { //if the user entered a wrong letter
        counter--
        guessList.add("$guessLetter is a wrong guess")
        guessList.add("$counter guesses remaining")
        recView.adapter!!.notifyDataSetChanged()
    }
    if (phrase == guessCorrectLetter) {

        showAlertDialog("Congratulations,You guessed it correctly!")

        recView.adapter!!.notifyDataSetChanged()
        // replayGame()

    }

}

    fun encodePhrase(phrase: String): String {
        var enPhrase = ""
        for (i in phrase) {
            if (i == ' ') {
                enPhrase += " "
            } else {
                enPhrase += "*"
            }

        }
        return enPhrase
    }

    fun checkIfEmpty(str: String): Boolean {
        if (str.isEmpty()) {
            var myLayout = findViewById<ConstraintLayout>(R.id.consLayOut)

            Snackbar.make(myLayout, "Enter a phrase/letter", Snackbar.LENGTH_SHORT).show()
            return true
        }
        return false

    }

}
