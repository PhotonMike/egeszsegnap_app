package hu.vajda.egeszseg

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_overview.*
import org.jetbrains.anko.startActivity

class OverviewActivity : AppCompatActivity() {

    var open = true

    override fun onDestroy() {
        if (open) {
            startActivity<QuizActivity>()
        }
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        setSupportActionBar(toolbar_overview)
        title = getString(R.string.title_activity_overview)
        open = true

        val pad = 16.Dp2Px(this)
        val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        )

        val vert = LinearLayout(this)
        vert.orientation = LinearLayout.VERTICAL

        for (a in 0..(answers.size-1)) {
            val l = LinearLayout(this)
            l.pad(pad)
            l.layoutParams = param
            l.gravity = Gravity.CENTER
            val c = CardView(this)
            c.pad(pad)
            c.layoutParams = param
            if (answers[a].isCorrect()){
                c.setCardBackgroundColor(resources.getColor(R.color.correct))
            }
            else {
                c.setCardBackgroundColor(resources.getColor(R.color.incorrect))
            }
            /*c.setCardBackgroundColor(resources.getColor(R.color.overCard))
            c.setOnClickListener{
                qNum = a
                startActivity<QuizActivity>()
                open = false
                finish()
            }*/
            val t = TextView(this)
            t.gravity = Gravity.CENTER
            t.text = answers[a].question.question
            t.pad(pad)
            c.addView(t)
            l.addView(c)
            over_lin.addView(l)
        }
    }

}
