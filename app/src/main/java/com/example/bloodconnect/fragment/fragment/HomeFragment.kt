package com.example.bloodconnect.fragment.fragment

import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bloodconnect.R
import com.example.bloodconnect.databinding.FragmentHomeBinding
import com.example.bloodconnect.databinding.RvDesignBinding
import com.example.bloodconnect.fragment.activity.AuthenticationActivity
import com.example.bloodconnect.fragment.activity.DonateActivity
import com.example.bloodconnect.fragment.activity.HistoryActivity
import com.example.bloodconnect.fragment.activity.ProfileActivity
import com.example.bloodconnect.fragment.activity.RequestActivity
import com.example.bloodconnect.fragment.adapter.RequestAdapter
import com.example.bloodconnect.fragment.model.Request
import com.example.bloodconnect.fragment.model.User
import com.example.bloodconnect.fragment.utils.Utils
import com.example.bloodconnect.fragment.viewModel.MainViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.launch


class HomeFragment : Fragment() , NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding: FragmentHomeBinding
    private lateinit var request:ArrayList<Request>
    private val viewModel:MainViewModel by viewModels()
    private lateinit var adapter: RequestAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        binding= FragmentHomeBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.materialToolbar)
        setHasOptionsMenu(true)




        fetchUserDetail()
        fetchAllRequest()
        goToActivities()
        setDrawerLyt()

        return binding.root

    }

    private fun setDrawerLyt() {
        binding.navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, binding.materialToolbar, R.string.open_nav, R.string.close_nav)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setCheckedItem(R.id.home1)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                   binding. drawerLayout.closeDrawer(GravityCompat.START)
                }
                else{
                    activity?.finish()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)


    }

    private fun goToActivities() {
        binding.cardView3.setOnClickListener {
            startActivity(Intent(requireContext(),RequestActivity::class.java))
        }

        binding.cardview4.setOnClickListener {
            startActivity(Intent(requireContext(), DonateActivity::class.java))
        }

    }


    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu,menu)
        menu.getItem(0).setIconTintList(ColorStateList.valueOf(resources.getColor(R.color.white)));
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun fetchUserDetail(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(Utils.getUserUid()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user=snapshot.getValue<User>()



                    binding.userId.text=user!!.id.toString()
                    binding.bloodGrp.text=user.userBloodGroup.toString()
                    binding.materialToolbar.title="Welcome ${user.userFirstName}"

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private  fun fetchAllRequest() {

        FirebaseDatabase.getInstance().getReference().child("Requests").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                request.clear()
                val temperList=ArrayList<Request>()
                for (dataSnapshot in snapshot.children) {
                    val req: Request? = dataSnapshot.getValue(Request::class.java)
                    if (req?.uploaderUid!=Utils.getUserUid()) {
                        if (req != null) {
                            temperList.add(req)
                        }
                    }
                }
                request.addAll(temperList)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        request = ArrayList<Request>()
        adapter = RequestAdapter(request, ::onAcceptClicked)
        binding.homeRv.layoutManager= LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.homeRv.adapter=adapter


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home1 -> binding.drawerLayout.closeDrawer(GravityCompat.START)
            R.id.about -> Utils.showToast(requireContext(),"Donate Blood , Save Life")
            R.id.history ->startActivity(Intent(context, HistoryActivity::class.java))
            R.id.profile -> startActivity(Intent(context,ProfileActivity::class.java))
            R.id.logout ->  logOut()


        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(context,AuthenticationActivity::class.java))
        context?.let { Utils.showToast(it,"Logout successfully")
        activity?.finish()}
    }

    override fun onResume() {
        super.onResume()
        binding.navView.setCheckedItem(R.id.home1)
    }

    private fun onAcceptClicked(request: Request,binding: RvDesignBinding){

        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Alert")
        builder.setMessage("Do You want to accept the request !!!")
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->

            lifecycleScope.launch {
                viewModel.acceptRequest(request.requestId.toString(),request.uploaderUid,request)
                Utils.showToast(requireContext(),"Accepted")
                dialog.dismiss()
            }


        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
            dialog.dismiss()
        })
        val dialog: android.app.AlertDialog? = builder.create()
        dialog?.show()


    }





}