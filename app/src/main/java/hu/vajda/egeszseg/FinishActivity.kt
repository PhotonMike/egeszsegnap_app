package hu.vajda.egeszseg

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_finish.*

class FinishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        var correctNum = 0
        answers.forEach {
            if (it.answer == it.question.answer) {
                correctNum++
            }
        }
        val percentage = correctNum.toDouble() / answers.size.toDouble() * 100.00
        textViewCorrect.text = String.format(getString(R.string.correctAns), answers.size, correctNum, percentage)
        button2.setOnClickListener{
            finish()
        }
    }
}
