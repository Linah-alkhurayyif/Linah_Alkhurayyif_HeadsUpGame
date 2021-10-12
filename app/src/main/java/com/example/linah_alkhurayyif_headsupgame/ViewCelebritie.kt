package com.example.linah_alkhurayyif_headsupgame

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_upd_celebritie.*
import kotlinx.android.synthetic.main.activity_view_celebritie.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewCelebritie : AppCompatActivity() {
    val celebritiesInfo: ArrayList<CelebritiesInfo> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_celebritie)
        val progressDialog = ProgressDialog(this@ViewCelebritie)
        progressDialog.setMessage("Please wait")
        progressDialog.show()
        addNewCelebrity.setOnClickListener {
            intent = Intent(applicationContext, AddCelebrities::class.java)
            startActivity(intent)
        }
        vbabkbtn.setOnClickListener {
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val call: Call<List<CelebritiesDetails.Celebrities>>? = apiInterface!!.getListUser()
        call?.enqueue(object : Callback<List<CelebritiesDetails.Celebrities>> {
            override fun onResponse(
                call: Call<List<CelebritiesDetails.Celebrities>>,
                response: Response<List<CelebritiesDetails.Celebrities>>
            ) {
                progressDialog.dismiss()
                for (user in response.body()!!){
                    celebritiesInfo.add(CelebritiesInfo(user.name.toString(),user.taboo1.toString(),user.taboo2.toString(),user.taboo3.toString(),user.pk))
                }
                initializeRV()
            }

            override fun onFailure(call: Call<List<CelebritiesDetails.Celebrities>>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, ""+t.message, Toast.LENGTH_SHORT).show()
            }
        })


    }
    private fun initializeRV(){
        recyclerView.adapter = CelebritiesAdapter(celebritiesInfo)
        recyclerView.layoutManager = LinearLayoutManager(this)

    }
}