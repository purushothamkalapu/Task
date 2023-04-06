package com.purushotham.apoontask.ui.city

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.purushotham.apoontask.R
import com.purushotham.apoontask.adapters.AddressAdapter
import com.purushotham.apoontask.adapters.ItemSelected
import com.purushotham.apoontask.data.ForcastData
import com.purushotham.apoontask.data.TempuratureData
import com.purushotham.apoontask.databinding.FragmentCityBinding
import com.purushotham.apoontask.ui.home.HomeViewModel
import com.purushotham.apoontask.util.API_KEY
import com.purushotham.apoontask.util.Progress
import com.purushotham.apoontask.util.UNITS
import com.purushotham.task.api.API
import com.purushotham.task.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CityFragment : Fragment() {

    private var _binding: FragmentCityBinding? = null
    private val cityViewModel: CityViewModel by activityViewModels()
    private var progress: Progress? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cityViewModel.getFavAddress(requireContext())?.observe(this.viewLifecycleOwner) {
            if(it != null){
                binding.tvDefault.visibility = View.GONE
                binding.nsvDataLayout.visibility = View.VISIBLE
                binding.tvAddress.text = it.address
                binding.tvLatitude.text = it.lat
                binding.tvLogitude.text = it.lng
                setProgressDialogVisibility(true)
                getWeatherReport(it.lat, it.lng)
                getForecastReport(it.lat, it.lng)
            }else{
                binding.tvDefault.visibility = View.VISIBLE
                binding.nsvDataLayout.visibility = View.GONE
            }

        }

    }

    fun getWeatherReport(lat: String, lng: String) {
        val retrofitService = ServiceBuilder.builderService(API::class.java)
        val retrofitCall = retrofitService.getTemp(API_KEY, lat, lng)
        retrofitCall.enqueue(object : Callback<TempuratureData> {
            override fun onResponse(
                call: Call<TempuratureData>,
                response: Response<TempuratureData>
            ) {

                if(response.isSuccessful) {
                    var tempDat = response.body()
                    binding.tvTemp.text = (tempDat?.main?.temp!! /9).toInt().toString()+" \u2103"+""
                    binding.tvMinTemp.text = (tempDat?.main?.temp_min!! /9).toInt().toString()+" \u2103"+""
                    binding.tvMaxTemp.text = (tempDat?.main?.temp_max!! /9).toInt().toString()+" \u2103"+""
                    binding.tvPressure.text = tempDat.main.pressure.toString()
                    binding.tvHumidity.text = tempDat.main.humidity.toString()
                    binding.tvCity.text = tempDat.name
                    setProgressDialogVisibility(false)

                }else{
                    setProgressDialogVisibility(false)
                }




            }

            override fun onFailure(call: Call<TempuratureData>, t: Throwable) {
                Log.v("Krithik", "" + t.message)
                setProgressDialogVisibility(false)
            }

        })
    }

    fun getForecastReport(lat: String, lng: String) {

        val retrofitService = ServiceBuilder.builderService(API::class.java)
        val retrofitCall = retrofitService.getForcast(API_KEY, lat, lng,UNITS)
        retrofitCall.enqueue(object : Callback<ForcastData> {
            override fun onResponse(
                call: Call<ForcastData>,
                response: Response<ForcastData>
            ) {

                if(response.isSuccessful) {
                    var forecastData = response.body()
                    Log.v("Krithik", "" + response.body().toString())
                    setProgressDialogVisibility(false)

                }




            }

            override fun onFailure(call: Call<ForcastData>, t: Throwable) {
                Log.v("Krithik", "" + t.message)
                setProgressDialogVisibility(false)
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
       // _binding = null
    }

    private fun setProgressDialogVisibility(visible: Boolean) {
        if (visible) progress = Progress(requireContext(), R.string.please_wait, cancelable = true)
        progress?.apply { if (visible) show() else dismiss() }
    }
}