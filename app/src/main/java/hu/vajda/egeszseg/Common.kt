package hu.vajda.egeszseg

import android.content.Context
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


//FireAuth
val mAuth = FirebaseAuth.getInstance()
var db: FirebaseFirestore? = null


//variables
var qNum = 0
var name = ""


//Data storage structures
data class Question(
        var question: String,
        var answer: String,
        var answers: MutableMap<String, String>,
        var id: Int = 0
)

var questions: MutableList<Question> = mutableListOf()

data class Answer(
        val question: Question,
        var answer: String,
        var prevAns: String = ""
){
    fun isCorrect(): Boolean = (this.question.answer == this.answer)
}

var answers: MutableList<Answer> = mutableListOf()


//Helper functions
fun View.vis(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    }
    else {
        this.visibility = View.GONE
    }
}

fun View.pad(pad: Int) {
    this.setPadding(pad, pad, pad, pad)
}

fun String.isName(): Boolean = (!(this.contains(Regex("[$&/]")))) && this.isNotEmpty()

fun Int.Dp2Px(context: Context): Int {
    val density = context.resources.displayMetrics.density
    return this * density.toInt()
}

fun getRandomList(size: Int, max: Int): List<Int> {
    val random = Random()
    val out = mutableListOf<Int>()
    while (out.size < size) {
        val rand = random.nextInt(max)
        if (!out.contains(rand)){
            out.add(rand)
        }
    }
    return out
}

fun inflateQuestions(force: Boolean = false) {
    if ((answers.size == 0) || force) {
        answers = mutableListOf()
        getRandomList(10, questions.size-1).forEach {
            answers.add(Answer(questions[it], ""))
        }
    }
}

fun saveAnswers(){
    var ans: MutableMap<String, Any> = mutableMapOf()
    answers.forEach {
        ans.put(it.question.id.toString(), it.answer)
    }
    db?.collection("users")!!.document(name)
            .set(ans)
}

fun saveQuestions() {
    db?.collection("quiz")?.get()!!.addOnCompleteListener { t ->
        var wait = t.result.size()
        t.result.forEach {
            it.reference.delete().addOnCompleteListener {
                wait--
                if (wait == 0) {
                    var i = 0
                    questions.forEach {
                        i++
                        val upload = mutableMapOf<String, Any>(
                                "answer" to it.answer,
                                "answers" to it.answers,
                                "question" to it.question
                        )
                        db?.collection("quiz")!!.document(it.id.toString()).set(upload)
                    }
                }
            }
        }
    }
    inflateQuestions(true)
}
