package ws.tilda.anastasiia.timefighter

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    internal lateinit var tapMeButton: Button
    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView
    internal var score = 0
    internal var gameStarted = false
    internal lateinit var countDownTimer: CountDownTimer
    internal val initialCountDown: Long = 60000 // the length of the countdown in milliseconds
    internal val countDownInterval: Long = 1000 // the rate at which the countdown will increment in milliseconds
    internal val TAG = MainActivity::class.java.simpleName
    internal var timeLeftOntimer: Long = 60000

    companion object {
        private val SCORE_KEY = "SCORE KEY"
        private val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate called. Score is: $score")

        tapMeButton = findViewById(R.id.tap_me_button)
        gameScoreTextView = findViewById(R.id.game_score_text_view)
        timeLeftTextView = findViewById(R.id.time_left_text_view)

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOntimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }

        tapMeButton.setOnClickListener { view ->
            incrementScore()
        }
    }

    private fun restoreGame() {
        gameScoreTextView.text = getString(R.string.score_text_view_name, score.toString())
        val restoredTimeInSeconds = timeLeftOntimer / 1000
        timeLeftTextView.text = getString(R.string.time_left_text_view_name, restoredTimeInSeconds.toString())

        setCountDownTimer(timeLeftOntimer, countDownInterval)
        startGame()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOntimer)
        countDownTimer.cancel()
        Log.d(TAG, "onSavedInstanceState: Saving Score: $score & Time Left: $timeLeftOntimer")

    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy called.")
    }

    private fun resetGame() {
        score = 0
        gameScoreTextView.text = getString(R.string.score_text_view_name, score.toString())
        val initialTimeLeft = initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.time_left_text_view_name, initialTimeLeft.toString())

        setCountDownTimer(initialCountDown, countDownInterval)
        gameStarted = false
    }

    private fun setCountDownTimer(countDown: Long, interval: Long) {
        countDownTimer = object : CountDownTimer(countDown, interval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOntimer = millisUntilFinished
                val timeLeftInSeconds = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.time_left_text_view_name, timeLeftInSeconds.toString())
            }

            override fun onFinish() {
                endGame()
            }
        }
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this,
                getString(R.string.game_over_message, score.toString()),
                Toast.LENGTH_SHORT).show()
        resetGame()
    }

    private fun incrementScore() {
        if (!gameStarted) {
            startGame()
        }
        score += 1
        val newScore = getString(R.string.score_text_view_name, score.toString())
        gameScoreTextView.text = newScore
    }


}
