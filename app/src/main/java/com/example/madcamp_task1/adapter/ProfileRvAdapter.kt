package com.example.madcamp_task1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_task1.roomdb.Profile
import com.example.madcamp_task1.databinding.ItemProfileBinding

class ProfileRvAdapter(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<Profile, ProfileRvAdapter.ProfileViewHolder>(ProfileDiffCallback()) {


    interface OnItemClickListener {
        fun onItemClick(profile: Profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = getItem(position)
        holder.bind(profile, itemClickListener)
    }

    override fun onBindViewHolder(
        holder: ProfileViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val groupname = payloads[0] as String
            holder.updateGroupName(groupname)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    inner class ProfileViewHolder(private val binding: ItemProfileBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(profile: Profile, clickListener: OnItemClickListener) {
            binding.profileNameTv.text = profile.name
            binding.profilePhoneNumTv.text = profile.phonenum
            binding.profileGroupNameTv.text = profile.groupname
            binding.root.setOnClickListener {
                clickListener.onItemClick(profile)
            }
        }

        fun updateGroupName(groupname: String) {
            binding.profileGroupNameTv.text = groupname
        }
    }

    class ProfileDiffCallback : DiffUtil.ItemCallback<Profile>() {
        override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            return oldItem.phonenum == newItem.phonenum // Assuming phonenum as unique identifier
        }

        override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Profile, newItem: Profile): Any? {
            return if (oldItem.groupname != newItem.groupname) newItem.groupname else null
        }
    }
}