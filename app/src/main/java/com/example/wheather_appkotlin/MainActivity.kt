package com.example.wheather_appkotlin

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wheather_appkotlin.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    //ccfa2ce829f3923e63a2b2876596f3d2
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchWeatherData ("Faisalabad")
       SearchCity()



    }

    private fun SearchCity() {
        val SearchView = binding.Serachview
        SearchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               return true
            }

        })
    }

    private fun fetchWeatherData(CityName: String) {
      val retrofit = Retrofit.Builder()
          .addConverterFactory(GsonConverterFactory.create())
          .baseUrl("https://api.openweathermap.org/data/2.5/")
          .build().create(ApiInterface::class.java)
        val response = retrofit.getwheatherdata(CityName ,"ccfa2ce829f3923e63a2b2876596f3d2", "metric")
        response.enqueue(object :Callback<Weatherapp>{
            override fun onResponse(call: Call<Weatherapp>, response: Response<Weatherapp>) {
                val responceBody = response.body()
                if(response.isSuccessful && responceBody != null){
                    val temperature = responceBody.main.temp.toString()
                    val humidity = responceBody.main.humidity.toString()
                    val WindSpeed = responceBody.wind.speed.toString()
                    val Condition = responceBody.weather.firstOrNull()?.main?:"Unknoun"
                    val Sunrice = responceBody.sys.sunrise.toLong()
                    val Sunset = responceBody.sys.sunset.toLong()
                    val SeaLeval = responceBody.main.pressure.toString()
                    val Temp_max = responceBody.main.temp_max.toString()
                    val Temp_min = responceBody.main.temp_min.toString()
                   // Log.d("TAG", "onResponse: $temperature")
                    binding.Tvtemp.text ="$temperature °C"
                    binding.tvweather.text= Condition
                    binding.tvMax.text="$Temp_max  °C"
                    binding.tvMin.text="$Temp_min  °C"
                    binding.tvhumidity.text= "$humidity %"
                    binding.tvWspeed.text= "$WindSpeed m/s"
                    binding.tvcondition.text= Condition
                    binding.tvsunrice.text= "${time(Sunrice)}"
                    binding.tvsunset.text= "${time(Sunset)}"
                    binding.tvsea.text= "$SeaLeval hpa"
                    binding.tvlocation.text = CityName.replaceFirstChar { it.uppercase() }
                    binding.tvDay.text = Dayname(System.currentTimeMillis())
                    binding.tvdate.text= date()


                    chageimagebackgroundonweather(Condition)

                }
            }

            override fun onFailure(call: Call<Weatherapp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun chageimagebackgroundonweather(Condition : String) {
        when(Condition){
            "Clear sky", "Sunny", " Clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_screen)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
            "Partly Clouds", "Clouds", "OverCast","Mist", " Foggy" -> {
                binding.root.setBackgroundResource(R.drawable.sloud_screen)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Light Rain", " Drizzel", "Moderate Rain", "Showers", "Heavy Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain_screen)
                binding.lottieAnimationView.setAnimation(R.raw.rainanimation)
            }
            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzerd" -> {
                binding.root.setBackgroundResource(R.drawable.snow_screen)
                binding.lottieAnimationView.setAnimation(R.raw.snowanimation)
            }
            else ->{
                binding.root.setBackgroundResource(R.drawable.sunny_screen)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    fun Dayname(timestamp: Long): String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }
    private fun date() : String{
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }
    private fun time(timestamp: Long) : String{
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }
}


