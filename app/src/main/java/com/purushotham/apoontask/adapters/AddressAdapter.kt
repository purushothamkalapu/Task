package com.purushotham.apoontask.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.purushotham.apoontask.R
import com.purushotham.apoontask.databinding.RowAddressItemBinding
import com.purushotham.apoontask.room.AddressEntity
import com.purushotham.apoontask.ui.home.HomeViewModel


class AddressAdapter(
    val addressEntityMutableList: MutableList<AddressEntity>,
    val context: Context,
    val homeViewModel: HomeViewModel,
    private val itemClickListener: ItemSelected
) :
    RecyclerView.Adapter<AddressAdapter.UserDetailViewHolder>() {
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDetailViewHolder {
        val binding =
            RowAddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserDetailViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return addressEntityMutableList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: UserDetailViewHolder, position: Int) {
        holder.bindData(addressEntityMutableList.get(position))

        holder.itemView.setOnClickListener {


        }
        holder.binding.ivDelete.setOnClickListener {

            addressEntityMutableList.get(position).Id?.let { it1 ->
                homeViewModel.deleteItem(context,
                    it1
                )
            }
            addressEntityMutableList.removeAt(position)
            notifyItemChanged(position)
            itemClickListener.onItemRemoveClick(position)

        }
        holder.binding.ivFav.setOnClickListener {
            if(addressEntityMutableList.get(position).isFav){
                addressEntityMutableList.get(position).Id?.let { it1 ->
                    homeViewModel.update(context,
                        it1,false)
                }
            }else{
                addressEntityMutableList.get(position).Id?.let { it1 ->
                    homeViewModel.update(context,
                        it1,true)
                }
            }
            addressEntityMutableList.forEach { addressEntity ->
                if(addressEntity.Id != addressEntityMutableList.get(position).Id){
                    addressEntity.Id?.let { it1 ->
                        homeViewModel.update(context,
                            it1,false)
                    }
                }
            }
            notifyItemChanged(position)
            itemClickListener.onItemRemoveClick(position)
        }






    }

    class UserDetailViewHolder(val binding: RowAddressItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(addressEntity: AddressEntity) {
            binding.tvAddress.text = addressEntity.address
            binding.tvLatitude.text = addressEntity.lat
            binding.tvLongitude.text = addressEntity.lng
            if(addressEntity.isFav){
                binding.ivFav.setImageResource(R.drawable.ic_fav_checked)
            }else{
                binding.ivFav.setImageResource(R.drawable.ic_fav_un_checked)
            }





        }


    }


}

