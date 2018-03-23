package hu.vajda.egeszseg

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {

    var clicks = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseFirestore.getInstance()

        startButton.setOnClickListener {
            val nameStr = nameTxt.text.toString()
            if (nameStr.isName()){
                inflateQuestions(true)
                name = nameStr
                startActivity<QuizActivity>()
                nameTxt.setText("")
            }
            else {
                snackbar(mainLinear, getString(R.string.badname))
            }
        }
        updateUI()
        getQuestions()
    }

    fun getQuestions() {
        qNum = 0
        if (mAuth.currentUser == null) {
            mAuth.signInAnonymously().addOnCompleteListener {
                getQuestions()
            }
        }
        else {
            db?.collection("quiz")
                    ?.get()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            questions = mutableListOf()
                            for (document in task.result) {
                                val q = document.data
                                questions.add(Question(
                                        q["question"] as String,
                                        q["answer"] as String,
                                        q["answers"] as MutableMap<String, String>,
                                        document.id.toInt()))
                            }
                            updateUI()
                        }
                    }
        }
    }

    fun updateUI() {
        if (questions.size > 0){
            toptext.text = String.format(getString(R.string.WelcTxt), questions.size)
            toptext.setOnClickListener {
                if (clicks > 10){
                    inflateQuestions(true)
                    startActivity<AdminActivity>()
                    clicks = 0
                }
                else {
                    clicks++
                }
            }
            toptext.vis(true)
            btmCard.vis(true)
        }
        else {
            btmCard.vis(false)
            toptext.vis(true)
            toptext.text = getString(R.string.loading)
        }
    }
}
