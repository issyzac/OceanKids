package apps.issy.com.oceankids

import android.os.Bundle
import apps.issy.com.oceankids.Base.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProviders
import apps.issy.com.oceankids.viewmodels.KidViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity


/**
 *  Created by issy on 06/06/2018.
 *  @issyzac
 *  issyzac.iz@gmail.com
 *  On Project OceanKids
 */

class LoginActivity : BaseActivity(){

    private lateinit var kidViewModel : KidViewModel

    private val RC_SIGN_IN = 123 //the request code could be any Integer
    val auth = FirebaseAuth.getInstance()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        kidViewModel = ViewModelProviders.of(this).get(KidViewModel::class.java)

        if(auth.currentUser != null){ //If user is signed in
//                startActivity(Next Activity)
        }else {

        }

        login_button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                if (getInputs()){
                    progress_view.visibility = View.VISIBLE
                    login_button_text.visibility = View.GONE

                    loginUser(email.text.toString(), password.text.toString())
                }
            }
        })

    }

    fun getInputs() : Boolean{
        return !(email.text.isEmpty() || password.text.isEmpty())
    }

    fun showSnackbar(id : Int){
        Snackbar.make(findViewById(R.id.sign_in_container), resources.getString(id), Snackbar.LENGTH_LONG).show()
    }

    fun loginUser(email : String, password : String){

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {

                override fun onComplete(task: Task<AuthResult>) {

                    progress_view.visibility = View.GONE
                    login_button_text.visibility = View.VISIBLE

                    if (task.isSuccessful()) {
                        //This will be suspended by a coroutine function called
                        loadAllKids()

                    } else {
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                        updateUI(null)
                    }

                    // ...
                }
            })
    }

    fun loadAllKids() {
        GlobalScope.launch (Dispatchers.IO) {
            kidViewModel.loadAllKids()
            val user = auth.getCurrentUser()
            updateUI(user)
        }

    }

    fun updateUI(user: FirebaseUser?){
        if(user != null){
            this@LoginActivity.startActivity<MainActivity>()
            finish()
        }
    }

}