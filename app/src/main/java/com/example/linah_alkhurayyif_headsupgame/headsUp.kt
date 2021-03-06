package com.example.linah_alkhurayyif_headsupgame

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_heads_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class headsUp : AppCompatActivity() {
    var isGameStart = false
    var isFinish = false
    val start_time = 60000
    lateinit var mCountDownTimer: CountDownTimer
    private var mTimeLeftInMillis = start_time.toLong()
    private var mEndTime: Long = 0
    private var currentCelebrity = 0
    val celebritiesInfo: ArrayList<CelebritiesInfo> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heads_up)
        Startbtn.setOnClickListener {
            isGameStart = true
            isFinish = false
            mEndTime = 0
            mTimeLeftInMillis=start_time.toLong()
            currentCelebrity = 0
            GameStart.isVisible = false
            Timer.isVisible =true
            getCelebrity()
        }
        hbabkbtn.setOnClickListener {
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun countTimer(){
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
//                Time_tv.setText("Time: " )
                updateCountDownText()
            }

            override fun onFinish() {
                isGameStart = false
                isFinish = true
                Time_tv.setText("done!")
                GameStart.isVisible = true
                Rotate.isVisible= false
                Gamebegin.isVisible= false
            }
        }.start()
    }
    private fun updateCountDownText() {
        if(isFinish == true){
            Timer.isVisible = false
            GameStart.isVisible = true
            Rotate.isVisible= false
            Gamebegin.isVisible= false
        }else{
            Timer.isVisible = true
            val seconds = (mTimeLeftInMillis / 1000).toInt()
            Time_tv.setText("Time: " +seconds)
        }

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("millisLeft", mTimeLeftInMillis);
        outState.putLong("endTime", mEndTime);
        outState.putBoolean("GameStart", isGameStart)
        outState.putBoolean("Finish", isFinish)
        outState.putInt("currentCelebrity",currentCelebrity)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isGameStart = savedInstanceState.getBoolean("GameStart")
        isFinish = savedInstanceState.getBoolean("Finish")
        mTimeLeftInMillis = savedInstanceState.getLong("millisLeft")
        mEndTime = savedInstanceState.getLong("endTime")
        mTimeLeftInMillis = mEndTime - System.currentTimeMillis()
        currentCelebrity = savedInstanceState.getInt("currentCelebrity")
        updateCountDownText()
        if(isGameStart){
            GameStart.isVisible = false
            Timer.isVisible =true
            getCelebrity()
        }

    }
    fun checkDevicePosition(){
        if(isGameStart){
            val orientation = resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                updateCelebrity(currentCelebrity)
                currentCelebrity++
                Gamebegin.isVisible= true
            } else {
                Rotate.isVisible= true
            }
            countTimer()
        }else if(isFinish == true){
            Time_tv.setText("done!")
            GameStart.isVisible = true
            Rotate.isVisible= false
            Gamebegin.isVisible= false
        }
    }
    fun updateCelebrity(currentCelebritynum:Int){
        Log.d("hh","${celebritiesInfo.size}")
        Log.d("hh","${currentCelebritynum}")
        Log.d("hh",celebritiesInfo[currentCelebritynum].name)
        name.text = celebritiesInfo[currentCelebritynum].name
        Taboo1.text = celebritiesInfo[currentCelebritynum].taboo1
        Taboo2.text = celebritiesInfo[currentCelebritynum].taboo2
        Taboo3.text = celebritiesInfo[currentCelebritynum].taboo3
    }
    fun getCelebrity(){
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val call: Call<List<CelebritiesDetails.Celebrities>>? = apiInterface!!.getListUser()
        call?.enqueue(object : Callback<List<CelebritiesDetails.Celebrities>> {
            override fun onResponse(
                call: Call<List<CelebritiesDetails.Celebrities>>,
                response: Response<List<CelebritiesDetails.Celebrities>>
            ) {
                for (celebrities in response.body()!!){
                    celebritiesInfo.add(CelebritiesInfo(celebrities.name.toString(),celebrities.taboo1.toString(),celebrities.taboo2.toString(),celebrities.taboo3.toString(),celebrities.pk))
                }
                checkDevicePosition()
            }

            override fun onFailure(call: Call<List<CelebritiesDetails.Celebrities>>, t: Throwable) {
                Toast.makeText(applicationContext, ""+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}