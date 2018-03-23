package hu.vajda.egeszseg

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {

    var pad = 0
    val param = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1.0f
    )
    var vert: LinearLayout? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_admin, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.uploadQ -> {
            currentFocus?.clearFocus()
            saveQuestions()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        setSupportActionBar(toolbar_admin)
        title = getString(R.string.title_activity_overview)
        pad = 16.Dp2Px(this)

        vert = LinearLayout(this)
        vert!!.orientation = LinearLayout.VERTICAL
        admin_lin.addView(vert)
        updateUI()
    }

    fun updateUI(){
        vert?.removeAllViews()
        for (a in 0..(questions.size-1)) {
            val l = LinearLayout(this)
            l.orientation = LinearLayout.VERTICAL
            l.pad(pad)
            l.layoutParams = param
            l.gravity = Gravity.CENTER
            val c = CardView(this)
            c.pad(pad)
            c.layoutParams = param
            c.setCardBackgroundColor(resources.getColor(R.color.overCard))
            c.setOnClickListener{}
            val cardLin = LinearLayout(this)
            cardLin.orientation = LinearLayout.VERTICAL
            cardLin.pad(pad)
            cardLin.gravity = Gravity.CENTER
            val d = ImageButton(this)
            d.setImageResource(R.drawable.delete)
            d.pad(pad)
            d.setOnClickListener{
                it.requestFocus()
                questions.removeAt(a)
                updateUI()
            }
            val t = EditText(this)
            t.setOnFocusChangeListener { view, b ->
                if (!b) {
                    questions[a].question = t.text.toString()
                }
            }
            t.gravity = Gravity.CENTER
            t.setText(questions[a].question)
            t.pad(pad)
            val ansOpt = mutableListOf<LinearLayout>()
            questions[a].answers.forEach {
                val ti = it
                val out = LinearLayout(this)
                out.orientation = LinearLayout.HORIZONTAL
                out.pad(pad)
                out.layoutParams = param
                out.gravity = Gravity.LEFT
                val corr = ImageButton(this)
                corr.setImageResource(R.drawable.tick)
                corr.pad(pad)
                if (questions[a].answer == ti.key) {
                    corr.setBackgroundColor(Color.GREEN)
                }
                corr.setOnClickListener{
                    questions[a].answer = ti.key
                    it.requestFocus()
                    updateUI()
                }
                val ansT = EditText(this)
                ansT.setOnFocusChangeListener { view, b ->
                    if (!b) {
                        questions[a].answers[ti.key] = ansT.text.toString()
                    }
                }
                ansT.setText(it.value)
                ansT.pad(pad)
                out.addView(corr)
                out.addView(ansT)
                ansOpt.add(out)
            }
            val plusAns = ImageButton(this)
            plusAns.setImageResource(R.drawable.add)
            plusAns.pad(pad)
            val plusLin = LinearLayout(this)
            plusLin.addView(plusAns)
            plusAns.setOnClickListener{
                questions[a].answers.put(questions[a].answers.size.toString(), "válasz")
                it.requestFocus()
                updateUI()
            }
            ansOpt.add(plusLin)
            cardLin.addView(d)
            cardLin.addView(t)
            ansOpt.forEach {
                cardLin.addView(it)
            }

            c.addView(cardLin)
            l.addView(c)
            vert?.addView(l)
        }
        val add_lin = LinearLayout(this)
        add_lin.pad(pad)
        add_lin.layoutParams = param
        add_lin.gravity = Gravity.CENTER
        val add_card = CardView(this)
        add_card.pad(pad)
        add_card.setCardBackgroundColor(resources.getColor(R.color.overCard))
        add_card.setOnClickListener{
            questions.add(Question("", "", mutableMapOf(), questions.size))
            it.requestFocus()
            updateUI()
        }
        val add_icon = ImageView(this)
        add_icon.setImageResource(R.drawable.add)
        add_icon.pad(pad)
        add_card.addView(add_icon)
        add_lin.addView(add_card)
        vert?.addView(add_lin)
    }

}
