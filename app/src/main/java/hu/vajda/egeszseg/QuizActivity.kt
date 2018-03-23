package hu.vajda.egeszseg

import android.content.Context
import org.jetbrains.anko.startActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_quiz.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.doAsync

class QuizActivity : AppCompatActivity() {

    var content: LinearLayout? = null
    val param = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1.0f
    )
    var con: Context? = null
    var pad = 0
    var backPressed = System.currentTimeMillis()
    var active = true


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_left -> {
                move(-1)
            }
            R.id.navigation_back -> {
                saveAnswers()
                startActivity<OverviewActivity>()
                finish()
            }
            R.id.navigation_right -> {
                move(1)
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        navigation.selectedItemId = R.id.navigation_back
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        con = this
        pad = 16.Dp2Px(this)
        content = LinearLayout(this)
        content!!.orientation = LinearLayout.HORIZONTAL
        updateUI()
        quiz_lin.addView(content)
    }

    override fun onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed()
        }
        else {
            snackbar(container, getString(R.string.doubleBack))
        }
        backPressed = System.currentTimeMillis()
    }

    fun updateUI(){
        content?.removeAllViews()
        for (a in answers[qNum].question.answers){
            val l = LinearLayout(this)
            l.pad(pad)
            l.layoutParams = param
            l.gravity = Gravity.CENTER
            val t = TextView(this)
            t.layoutParams = param
            t.text = a.value
            t.gravity = Gravity.CENTER
            t.pad(pad)
            val c = CardView(this)
            c.layoutParams = param
            c.pad(pad)
            c.addView(t)
            c.setCardBackgroundColor(resources.getColor(R.color.ansDeselected))
            c.setOnClickListener {
                if (active) {
                    answers[qNum].answer = a.key
                    saveAnswers()
                    when (a.key) {
                        answers[qNum].question.answer -> {
                            it.setBackgroundColor(resources.getColor(R.color.correct))
                        }
                        else -> {
                            it.setBackgroundColor(resources.getColor(R.color.incorrect))
                        }
                    }
                    active = false
                    doAsync {
                        Thread.sleep(1000)
                        runOnUiThread {
                            move(1)
                        }
                    }
                }
            }
            l.addView(c)
            content?.addView(l)
        }
        title = (qNum+1).toString() + ". " + getString(R.string.title_activity_question)
        question.text = answers[qNum].question.question
    }

    fun move(num: Int){
        if ((qNum + num) > (answers.size-1)){
            qNum = 0
            startActivity<FinishActivity>()
            finish()
        }
        else{
            qNum+=num
            updateUI()
        }
        active = true
    }
}
