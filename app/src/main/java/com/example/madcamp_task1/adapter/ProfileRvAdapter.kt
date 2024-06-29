package com.example.madcamp_task1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_task1.data.Profile
import com.example.madcamp_task1.databinding.ItemProfileBinding



class ProfileRvAdapter(
    private val profilelist: ArrayList<Profile>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ProfileRvAdapter.ProfileViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(profile: Profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        return ProfileViewHolder(
            ItemProfileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(profilelist[position], itemClickListener)
    }

    override fun getItemCount(): Int = profilelist.size

    inner class ProfileViewHolder(private val binding : ItemProfileBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(profile: Profile, clickListener: OnItemClickListener){
            binding.profileNameTv.text = profile.name
            binding.profilePhoneNumTv.text = profile.phonenum
            binding.profileGroupNameTv.text = profile.groupname
            binding.root.setOnClickListener{
                clickListener.onItemClick(profile)
            }
        }
    }
}