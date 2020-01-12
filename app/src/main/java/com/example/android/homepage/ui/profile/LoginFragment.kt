package com.example.android.homepage.ui.profile

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.SupportActionModeWrapper
import androidx.navigation.findNavController

import com.example.android.homepage.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_login.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoginFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: ProfileFragment.OnFragmentInteractionListener? = null
    private lateinit var mAuth: FirebaseAuth
//    var visibleButton: Button? = null
//    var buttonRegister: Button? = null
//    var buttonLogin: Button? = null
//    var buttonForgotPassword: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser: FirebaseUser? = mAuth!!.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            if(currentUser.isEmailVerified){
                //call fragment instead of activity
                //var i = Intent(context, ProfileActivity::class.java)
                //i.putExtra("currentUser",currentUser)
                //startActivity(i)

                //call fragment here
                view?.findNavController()?.navigate(R.id.action_loginFragment_to_navigation_profile)
                return
            }
            else{
                Toast.makeText(context, "Please verify your email", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun login() {

        //START OF VALIDATION FOR REGISTRATION
        if (editTextEmail.text.toString().isEmpty()) {
            editTextEmail.error = "Email Address cannot be empty!"
            editTextEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.text.toString()).matches()) {

            editTextEmail.error = "Please enter a valid Email Address!"
            editTextEmail.requestFocus()
            return
        }

        if (editTextPassword.text.toString().isEmpty()) {
            editTextPassword.error = "Password cannot be empty!"
            editTextPassword.requestFocus()
            return
        }
        //END OF VALIDATION FOR REGISTRATION

        mAuth.signInWithEmailAndPassword(
            editTextEmail.text.toString(),
            editTextPassword.text.toString()
        )
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, "Wrong Password Entered", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }



    private fun forgotPassword(email : EditText){
        if(email.text.toString().isEmpty()){
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return

        }

        mAuth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Password has been reset, please check your email", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)//initialize Firebase authentication
        mAuth = FirebaseAuth.getInstance()


        visibleButton.setOnClickListener {

            if (visibleButton.text.toString().equals("-")) {

                editTextPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                visibleButton.setBackgroundResource(R.drawable.visible_30dp)
                visibleButton.text = ""
            } else {

                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                visibleButton.setBackgroundResource(R.drawable.not_visible_30dp)
                visibleButton.text = "-"
            }

        }

        buttonRegister.setOnClickListener {

            var i = Intent(it.context, RegistrationActivity::class.java)
            startActivity(i)
        }

        buttonLogin.setOnClickListener {

            login()

        }

        buttonForgotPassword.setOnClickListener{

            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("Enter registered email to reset password")
            val view = layoutInflater.inflate(R.layout.forgot_password_dialog,null)
            val email = view.findViewById<EditText>(R.id.editTextResetPasswordEmail)
            builder.setView(view)

            builder.setPositiveButton("RESET", DialogInterface.OnClickListener { _, _ ->
                forgotPassword(email)
            })
            builder.setNegativeButton("CANCEL", DialogInterface.OnClickListener { _, _ ->  })
            builder.show()



        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProfileFragment.OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
